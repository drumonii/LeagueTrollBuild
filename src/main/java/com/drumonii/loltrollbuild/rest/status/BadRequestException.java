package com.drumonii.loltrollbuild.rest.status;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * Indicates a 400 status, bad request.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

	public BadRequestException(List<ObjectError> message) {
		super(message.toString());
	}

}