package com.drumonii.loltrollbuild.rest.status;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * Indicates a 400 status, bad request.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

	public BadRequestException(String message) {
		super(message);
	}

	public BadRequestException(List<FieldError> fieldErrors) {
		super(getFieldErrors(fieldErrors));
	}

	private static String getFieldErrors(List<FieldError> fieldErrors) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < fieldErrors.size(); i++) {
			FieldError fieldError = fieldErrors.get(i);
			if (i == fieldErrors.size() - 1) {
				builder.append(fieldError.getField()).append(": ").append(fieldError.getDefaultMessage());
			} else {
				builder.append(fieldError.getField()).append(": ").append(fieldError.getDefaultMessage()).append(", ");
			}
		}
		return builder.toString();
	}

}