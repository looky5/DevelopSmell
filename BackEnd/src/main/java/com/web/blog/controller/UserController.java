package com.web.blog.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.blog.model.BasicResponse;
import com.web.blog.model.user.User;
import com.web.blog.model.user.UserRank;
import com.web.blog.service.IUserService;
import com.web.blog.util.SHA256Util;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
		@ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
		@ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
		@ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	IUserService userService;

	@ApiOperation(value = "회원의 정보를 수정한다.")
	   @PutMapping("")
	   public ResponseEntity<?> update(@RequestPart("profile") MultipartFile profile, @RequestParam("user") String u) throws Exception {

	      String salt = SHA256Util.generateSalt();
	      User user = new ObjectMapper().readValue(u, User.class);
	      String npw = SHA256Util.getEncrypt(user.getPassword(), salt);

	      user.setSalt(salt);
	      user.setPassword(npw);
	      
	      
	      SimpleDateFormat format1 = new SimpleDateFormat("yyMMddHHmmss");
	      String time1 = format1.format(new Date());

	      String filename = "http://i3b208.p.ssafy.io:8080/blog/images/profiles/" + user.getUserno() + time1 + profile.getOriginalFilename();
	      user.setProfilename(filename);

	      String fileUrl = "/home/ubuntu/images/profiles/" + user.getUserno() + time1
	            + profile.getOriginalFilename();
//	      String fileUrl = "/C://images/" + user.getUserno() + time1
//	            + profile.getOriginalFilename();
	      File dest = new File(fileUrl);
	      profile.transferTo(dest);

	      ResponseEntity response = null;

	      int updateRes = userService.updateUser(user);

	      if (updateRes == 1) {
	         final BasicResponse result = new BasicResponse();
	         result.status = true;
	         result.data = "success";
	         response = new ResponseEntity<>(result, HttpStatus.OK);
	      } else {
	         response = new ResponseEntity<>(null, HttpStatus.OK);

	      }
	      return response;
	   }

	@ApiOperation(value = "유저 정보를 반환한다.")
	@GetMapping("/{userno}")
	public ResponseEntity<?> info(@PathVariable("userno") int userno) throws Exception {

		ResponseEntity response = null;
		User u = userService.infoUser(userno);

		final BasicResponse result = new BasicResponse();
		result.object = u;
		result.status = true;
		result.data = "success";
		response = new ResponseEntity<>(result, HttpStatus.OK);

		return response;
	}

	@ApiOperation(value = "유저를 삭제한다.")
	@DeleteMapping("/{userno}")
	public ResponseEntity<?> deleteUser(@PathVariable("userno") int userno) throws Exception {

		ResponseEntity response = null;
		int deleteRes = userService.deleteUser(userno);

		if (deleteRes == 1) {
			final BasicResponse result = new BasicResponse();
			result.status = true;
			result.data = "success";
			response = new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(null, HttpStatus.OK);
		}
		return response;
	}
	
	@ApiOperation(value = "유저 정보를 반환한다.")
	@GetMapping("/bloguser/{bloguser}")
	public ResponseEntity<?> blogUserInfo(@PathVariable("bloguser") String bloguser) throws Exception {

		ResponseEntity response = null;
		User u = userService.getUserbyUid(bloguser);

		final BasicResponse result = new BasicResponse();
		result.object = u;
		result.status = true;
		result.data = "success";
		response = new ResponseEntity<>(result, HttpStatus.OK);

		return response;
	}

	@ApiOperation(value = "프로필이미지를 업로드한다.")
	@PostMapping("/profile")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> upload(@RequestPart("file") MultipartFile file, @RequestParam("userno") int userno)
			throws Exception {
		ResponseEntity response = null;
		
		SimpleDateFormat format1 = new SimpleDateFormat("yyMMddHHmmss");
		String time1 = format1.format(new Date());

		String filename = "http://i3b208.p.ssafy.io:8080/blog/images/profiles/" + userno + time1 + file.getOriginalFilename();
		String fileUrl = "/home/ubuntu/images/profiles/" + userno + time1 + file.getOriginalFilename();
		File dest = new File(fileUrl);
		file.transferTo(dest);
		// local
		int insFileUrlRes = userService.insProfileUrl(userno, filename);

		if (insFileUrlRes != 0) {
			final BasicResponse result = new BasicResponse();
			result.status = true;
			result.data = "success";
			response = new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}

		return response;
	}

	@ApiOperation(value = "전체 유저의 랭크를 조회한다.")
	@GetMapping("/rank/{limit}")
	public ResponseEntity<?> getUsersRank(@PathVariable("limit") int limit) throws Exception {
		ResponseEntity response = null;
		List<UserRank> u = userService.getUsersRank(limit);
		final BasicResponse result = new BasicResponse();
		if (u != null) {
			result.object = u;
			result.status = true;
			result.data = "success";
			response = new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}

		return response;
	}

	/*
	 * @ApiOperation(value = "프로필이미지를 업로드한다.")
	 * 
	 * @PostMapping("/profile")
	 * 
	 * @ResponseStatus(HttpStatus.CREATED) public ResponseEntity<?>
	 * upload(@RequestPart("file") MultipartFile file, @RequestBody User user)
	 * throws Exception { ResponseEntity response = null; System.out.println(user);
	 * 
	 * String filename =
	 * "http://localhost:8082/blog/"+userno+file.getOriginalFilename(); String
	 * fileUrl =
	 * "C:\\SSAFY\\spring-workspace\\backend\\src\\main\\resources\\profiles/" +
	 * filename; File dest = new File(fileUrl); file.transferTo(dest); //local int
	 * insFileUrlRes = userService.insProfileUrl(userno, filename);
	 * 
	 * if(insFileUrlRes != 0) { final BasicResponse result = new BasicResponse();
	 * result.status = true; result.data = "success"; response = new
	 * ResponseEntity<>(result, HttpStatus.OK); }else { response = new
	 * ResponseEntity<>(null, HttpStatus.NOT_FOUND); }
	 * 
	 * 
	 * return response; }
	 */

}