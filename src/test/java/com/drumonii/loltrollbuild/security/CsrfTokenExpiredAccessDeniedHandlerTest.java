package com.drumonii.loltrollbuild.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class CsrfTokenExpiredAccessDeniedHandlerTest {

	private CsrfTokenExpiredAccessDeniedHandler handler = new CsrfTokenExpiredAccessDeniedHandler();

	@Test
	public void expiredCsrfTokenRedirectsToPreviousRequest() throws Exception {
		String requestUri = "http://localhost/request-uri";

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI(requestUri);

		MockHttpServletResponse response = new MockHttpServletResponse();

		MissingCsrfTokenException missingCsrfTokenException = new MissingCsrfTokenException(UUID.randomUUID().toString());

		handler.handle(request, response, missingCsrfTokenException);

		assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_MOVED_TEMPORARILY);
		assertThat(response.getRedirectedUrl()).isEqualTo(requestUri);
	}

	@Test
	public void csrfNotExpiredSendsForbiddenErrorCode() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();

		MockHttpServletResponse response = new MockHttpServletResponse();

		CsrfToken csrfToken = new DefaultCsrfToken("HEADER_NAME", "PARAMETER_NAME", UUID.randomUUID().toString());
		InvalidCsrfTokenException invalidCsrfTokenException = new InvalidCsrfTokenException(csrfToken, null);

		handler.handle(request, response, invalidCsrfTokenException);

		assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_FORBIDDEN);
	}

}