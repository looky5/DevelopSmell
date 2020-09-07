package com.web.blog.service;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.web.blog.model.BasicResponse;
import com.web.blog.util.UnauthorizedException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

@Service
public class JwtServiceImpl implements IJwtService {
	
	private static final byte[] KEY = "DevelopSmell".getBytes();

	@Override
	public String createToken(int userno) throws Exception {
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put("typ", "JWT");
		headers.put("alg", "HS256");
		
		Map<String, Object> payloads = new HashMap<>();
		Long expiredTime = 1000 * 60 * 60 * 12L; // 만료기간
		Date expireTime = new Date();
		expireTime.setTime(expireTime.getTime() + expiredTime);
		payloads.put("userno", userno);
		
		String token = Jwts.builder()
				.setHeader(headers)
				.setClaims(payloads)
				.setExpiration(expireTime)
				.signWith(SignatureAlgorithm.HS256, KEY)
				.compact();
		
		return token;
	}

	@Override
	public boolean isUsable(String token) {
		BasicResponse response = new BasicResponse();
		try{
			Jws<Claims> claims = Jwts.parser()
					  .setSigningKey(KEY)
					  .parseClaimsJws(token);
			return true;
		}catch (Exception e) {
			throw new UnauthorizedException();
		}
	}
	
	public Map<String, Object> get(String key) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		String jwt = request.getHeader("Authorization");
		Jws<Claims> claims = null;
		try {
			claims = Jwts.parser()
						 .setSigningKey(KEY)
						 .parseClaimsJws(jwt);
		} catch (Exception e) {
			throw new UnauthorizedException();
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> value = (LinkedHashMap<String, Object>)claims.getBody().get(key);
		
		return value;
	}
	
}
