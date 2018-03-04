package com.drumonii.loltrollbuild.controller;

import com.drumonii.loltrollbuild.config.Profiles.Dev;
import com.drumonii.loltrollbuild.config.Profiles.Testing;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A "bad" Controller that throws various exceptions which map to a single {@link HttpStatus}. It is intended for only
 * {@link Dev} and {@link Testing} profiles so that the styling of error specific pages can be easily and visually
 * tested. These mapping endpoints will not be available in other profiles.
 */
@Controller
@Dev @Testing
public class BadController {

	@GetMapping(path = "/400")
	public String throw400() {
		throw new BadRequestException();
	}

	@GetMapping(path = "/403")
	public String throw403() {
		throw new AccessDeniedException();
	}

	@GetMapping(path = "/404")
	public String throw404() {
		throw new NotFoundException();
	}

	@GetMapping(path = "/500")
	public String throw500() {
		throw new InternalServerErrorException();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	class BadRequestException extends RuntimeException {
		BadRequestException() {
			super("Bad Request!", null);
		}
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	class AccessDeniedException extends RuntimeException {
		AccessDeniedException() {
			super("Access Denied!", null);
		}
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	class NotFoundException extends RuntimeException {
		NotFoundException() {
			super("Not Found!", null);
		}
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	class InternalServerErrorException extends RuntimeException {
		InternalServerErrorException() {
			super("Internal Server Error!", null);
		}
	}

}
