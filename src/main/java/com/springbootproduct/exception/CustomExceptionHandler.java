package com.springbootproduct.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	public static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

	@ExceptionHandler(ProductException.class)
	public final ResponseEntity<CustomErrorResponse> handleProductException(ProductException ex, WebRequest request) {
		CustomErrorResponse errorDetails = new CustomErrorResponse(ex.getMessage());
		return new ResponseEntity<>(errorDetails, ex.getErrorStatus());
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<CustomErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
		logger.error(ex.getMessage());
		CustomErrorResponse errorResponse = new CustomErrorResponse("Internal error, please contact support.");
		return new ResponseEntity<CustomErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
