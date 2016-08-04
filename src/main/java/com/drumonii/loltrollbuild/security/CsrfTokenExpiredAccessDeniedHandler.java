package com.drumonii.loltrollbuild.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A {@link AccessDeniedHandler} which handles when a session has expired and a {@link MissingCsrfTokenException} is
 * thrown. If the exception is caught, it will redirect to the previous request's URI instead of sending the request as
 * a 403 forbidden.
 */
public class CsrfTokenExpiredAccessDeniedHandler extends AccessDeniedHandlerImpl {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		if (accessDeniedException instanceof MissingCsrfTokenException) {
			response.sendRedirect(request.getRequestURI());
		} else {
			super.handle(request, response, accessDeniedException);
		}
	}

}
