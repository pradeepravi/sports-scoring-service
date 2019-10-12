package com.example.sports.error;

import com.example.sports.service.exception.BadRequestException;
import com.example.sports.service.exception.MatchAlreadyConcludedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = "Malformed JSON request";
		return buildResponseEntity(new Error(HttpStatus.BAD_REQUEST, error, ex));
	}

	private ResponseEntity<Object> buildResponseEntity(Error apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

	@ExceptionHandler(EntityNotFoundException.class)
	protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
		Error apiError = new Error(HttpStatus.NOT_FOUND);
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler({BadRequestException.class})
	protected ResponseEntity<Object> handleBadRequestFromClient(BadRequestException ex) {
		Error apiError = new Error(HttpStatus.BAD_REQUEST);
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler({MatchAlreadyConcludedException.class})
	protected ResponseEntity<Object> handleMatchCompletedException(BadRequestException ex) {
		Error apiError = new Error(HttpStatus.BAD_REQUEST);
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}


}
