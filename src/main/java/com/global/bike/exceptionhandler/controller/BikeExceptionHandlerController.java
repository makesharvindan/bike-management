package com.global.bike.exceptionhandler.controller;

import com.global.bike.exception.InternalServerErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.global.bike.exception.RecordNotFoundException;

@ControllerAdvice
public class BikeExceptionHandlerController {

	@ExceptionHandler
	public ResponseEntity<String> handleException(RecordNotFoundException e){
		return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler
	public ResponseEntity<String> handleException(InternalServerErrorException e){
		return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
