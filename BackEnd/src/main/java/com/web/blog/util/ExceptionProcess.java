package com.web.blog.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.web.blog.model.BasicResponse;

@RestControllerAdvice
public class ExceptionProcess {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionProcess.class);

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<?> unauthorization() {
		logger.info("unauthorization");
		BasicResponse result = new BasicResponse();
		result.status = false;
		result.data = "Unauthorization";
		ResponseEntity response = new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);

		return response;
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> normalError(Exception e) {
		e.printStackTrace();
		BasicResponse result = new BasicResponse();
		result.status = false;
		result.data = "normalError";
		ResponseEntity response = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);

		return response;
	}
}
