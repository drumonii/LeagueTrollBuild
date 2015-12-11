package com.drumonii.loltrollbuild.controller;

import com.drumonii.loltrollbuild.config.Profiles.Dev;
import com.drumonii.loltrollbuild.config.Profiles.Testing;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A "bad" Controller that throws various exceptions which map to a single {@link HttpStatus}. It is intended for only
 * {@link Dev} and {@link Testing} profiles so that the styling of error specific pages can be easily and visually
 * tested. These mapping endpoints will not be available in other profiles.
 */
@Controller
@Dev @Testing
public class BadController {

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String throw403() {
		throw new AccessDeniedException();
	}

	@RequestMapping(value = "/404", method = RequestMethod.GET)
	public String throw404() {
		throw new NotFoundException();
	}

	@RequestMapping(value = "/500", method = RequestMethod.GET)
	public String throw500() {
		throw new InternalServerErrorException();
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public class InternalServerErrorException extends RuntimeException {
		public InternalServerErrorException() {
			super("Internal Server Error!", null);
		}
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	public class NotFoundException extends RuntimeException {
		public NotFoundException() {
			super("Not Found!", null);
		}
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	public class AccessDeniedException extends RuntimeException {
		public AccessDeniedException() {
			super("Access Denied!", null);
		}
	}

}
