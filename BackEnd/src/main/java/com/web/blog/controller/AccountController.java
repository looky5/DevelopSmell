package com.web.blog.controller;

import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.blog.model.BasicResponse;
import com.web.blog.model.email.Mail;
import com.web.blog.model.user.SignupRequest;
import com.web.blog.model.user.User;
import com.web.blog.service.EmailServiceImpl;
import com.web.blog.service.IJwtService;
import com.web.blog.service.IUserService;
import com.web.blog.service.KakaoAPI;
import com.web.blog.util.SHA256Util;
import com.web.blog.util.UnauthorizedException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
		@ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
		@ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
		@ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/account")
public class AccountController {

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private IJwtService jwtService;

	@Autowired
	IUserService userService;

	@Autowired
	EmailServiceImpl emailService;

	@Autowired
	KakaoAPI kakao;

	@ApiOperation(value = "로그인을 한다.")
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User user) throws Exception {
		ResponseEntity res = null;
		int userno;
		try {
			userno = userService.loginUser(user);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		final BasicResponse result = new BasicResponse();
		if (userno != -1) {
			result.object = userno;
			result.status = true;
			result.data = "success";
			res = new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			res = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}

		return res;
	}

	@ApiOperation(value = "로그인 여부를 확인한다.")
	@GetMapping("/get/{userno}")
	public ResponseEntity<?> getUserbyUserNo(@PathVariable("userno") int userno) throws Exception {
		ResponseEntity response = null;
		User u = userService.getUserbyUserNo(userno);
		final BasicResponse result = new BasicResponse();
		if (u != null) {
			String token = jwtService.createToken(u.getUserno());
			u.setToken(token);
			result.object = u;
			result.status = true;
			result.data = "success";
			response = new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}

		return response;
	}

	@PostMapping("/signup")
	@ApiOperation(value = "가입하기")
	public ResponseEntity<?> join(@Valid @RequestBody SignupRequest request) throws Exception {
		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();
		// 이메일로 전송된 코드 == 사용자가 입력한 코드랑 같으면
		String code = userService.getAuthcode(request.getEmail());
		if (request.getCode().equals(code)) {
			// 이메일 중복처리 필수
			String email = request.getEmail();
			User userCheck;
			try {
				userCheck = userService.getUserByEmail(email);

				if (userCheck == null) { // 중복되지 않음
					String salt = SHA256Util.generateSalt();
					String npw = SHA256Util.getEncrypt(request.getPassword(), salt);
					request.setSalt(salt);
					request.setPassword(npw);

					userService.joinUser(request);
					result.status = true;
					result.data = "success";
					result.object = request.getEmail();

					response = new ResponseEntity<>(result, HttpStatus.OK);
				} else { // 중복된 아이디

					result.status = false;
					result.data = "fail";
					response = new ResponseEntity<>(result, HttpStatus.NOT_FOUND); // 이게 맞냐? 물어봐야됨 not found
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				userService.deleteAuthCode(email);
			}

		} else {
			userService.deleteAuthCode(request.getEmail());
			result.status = false;
			result.data = "fail";
			response = new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
		}

		return response;
	}

	@PostMapping("/certification/{flag}")
	@ApiOperation(value = "메일 인증하기")
	public ResponseEntity<?> certificate(@RequestBody User user, @PathVariable("flag") String flag) throws Exception {

		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();
		String to = user.getEmail();
		User u = userService.getUserByEmail(to);

		if (flag.equals("0") && u == null) {
				logger.info("비번찾기일때 해당 이메일을 가진 유저가 없을 경우");
				result.status = true;
				result.data = "noEmail";
				response = new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			logger.info("회원가입을 하는 경우, 비번찾기일 때 해당 이메일을 가진 유저가 있는 경우");
			Mail mail = new Mail(to, "회원가입 인증 메일", null);

			String code = emailService.mailSend(mail);
			if (code != null) {
				userService.createAuthCode(to, code);
				result.status = true;
				result.data = "success";
				response = new ResponseEntity<>(result, HttpStatus.OK);
			} else {
				result.status = false;
				result.data = "fail";
				response = new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
			}
		}
		
		return response;
	}

	@PutMapping("/changepw")
	@ApiOperation(value = "비밀번호 변경")
	public ResponseEntity<?> findpassword(@Valid @RequestBody SignupRequest request)
			throws Exception, SQLSyntaxErrorException {

		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();

		String code = userService.getAuthcode(request.getEmail());
		if (request.getCode().equals(code)) {
			String email = request.getEmail();
			User userCheck;
			try {
				userCheck = userService.getUserByEmail(email);

				if (userCheck != null) { // 해당 유저가 있으면 비밀번호 변경
					String salt = SHA256Util.generateSalt();
					String npw = SHA256Util.getEncrypt(request.getPassword(), salt);
					request.setSalt(salt);
					request.setPassword(npw);

					userService.UserPwChange(request);
					result.status = true;
					result.data = "success";
					response = new ResponseEntity<>(result, HttpStatus.OK);

				} else { // 해당 유저가 없다면 false
					result.status = true;
					result.data = "there is not user";
					response = new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
				}
			} catch (Exception e) {
				;
				throw new Exception();
			} finally {
				userService.deleteAuthCode(email);
			}

		} else {
			userService.deleteAuthCode(request.getEmail());
			result.status = false;
			result.data = "fail";
			response = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}

	@ApiOperation(value = "카카오소셜로그인.")
	@GetMapping("/kakaologin")
	public ResponseEntity<?> kakaologin(@RequestParam("access_token") String access_token) throws Exception {
		ResponseEntity response = null;

		HashMap<String, Object> userInfo = kakao.getUserInfo(access_token);
		System.out.println("login Controller : " + userInfo);
		final BasicResponse result = new BasicResponse();
		// 클라이언트의 이메일이 존재할 때 세션에 해당 이메일과 토큰 등록
		User u = userService.getUserByEmail((String) userInfo.get("email"));
		if (u != null) { // 이메일이 존재하면
			String token = jwtService.createToken(u.getUserno());
			u.setToken(token);
			u.setPassword(null);
			u.setSalt(null);
			result.object = u;
			result.status = true;
			result.data = "success";
			response = new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			SignupRequest user = new SignupRequest();
			String email = (String) userInfo.get("email");
			System.out.println("email : " + email);
			String uid = "kakao_" + email.split("@")[0];
			String pwd = access_token;
			String salt = SHA256Util.generateSalt();
			user.setEmail(email);
			user.setUid(uid);
			user.setPassword(pwd);
			user.setSalt(salt);

			userService.joinUser(user); // 회원가입 완료

			u = userService.getUserByEmail(email);
			// 로그인 시작
			String token = jwtService.createToken(u.getUserno());
			u = new User();
			u.setToken(token);
			u.setPassword(null);
			u.setSalt(null);
			result.object = u;
			result.status = true;
			result.data = "success";
			response = new ResponseEntity<>(result, HttpStatus.OK);
		}

		return response;
	}

}