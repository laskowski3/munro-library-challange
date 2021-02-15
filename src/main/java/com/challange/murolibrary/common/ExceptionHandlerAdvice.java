package com.challange.murolibrary.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

	@ExceptionHandler(ParamException.class)
	public ResponseEntity<String> handleException(ParamException e) {
		// log exception
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
}