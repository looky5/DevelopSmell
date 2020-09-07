package com.web.blog.jwt;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.web.blog.service.IJwtService;
import com.web.blog.util.UnauthorizedException;

@Component
public class JwtInterceptor implements HandlerInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);
	private static final String HEADER_AUTH = "Authorization";

	@Autowired
	private IJwtService jwtService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String uri = request.getRequestURI();
		logger.info("In Interceptor ({})", uri);
		
		final String token = request.getHeader(HEADER_AUTH);
		logger.info("token : {}", token);
		Enumeration headers = request.getHeaderNames();

		while (headers.hasMoreElements()) {
			String headerName = (String) headers.nextElement();
			String value = request.getHeader(headerName);
			logger.info("headerName:" + headerName + "," + value);
		}

		if (token != null && jwtService.isUsable(token)) {
			return true;
		} else {
			throw new UnauthorizedException();
		}
	}
}
