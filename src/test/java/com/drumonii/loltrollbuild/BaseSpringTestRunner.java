package com.drumonii.loltrollbuild;

import lombok.AllArgsConstructor;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Base {@link SpringJUnit4ClassRunner} for Spring JUnit 4 tests that runs on a random unused port to avoid conflicts
 * from the main application (potentially) already running on port 8080.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LeagueTrollBuildApplication.class)
@WebIntegrationTest(randomPort = true)
@ActiveProfiles(TESTING)
@TestPropertySource(properties = "riot.api.key=API_KEY")
public abstract class BaseSpringTestRunner {

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private Filter springSecurityFilterChain;

	@Autowired
	private UserDetailsService userDetailsService;

	protected MockMvc mockMvc;

	protected MediaType APPLICATION_JSON_UTF8 = new MediaType("application", "json", UTF_8);
	protected MediaType PLAN_TEXT_UTF8 = new MediaType("text", "plain", UTF_8);

	@Before
	public void before() {
		mockMvc = webAppContextSetup(wac)
				.addFilters(springSecurityFilterChain)
				.build();
	}

	protected static RequestPostProcessor csrf() {
		CsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();
		return request -> {
			CsrfToken token = csrfTokenRepository.generateToken(request);
			csrfTokenRepository.saveToken(token, request, new MockHttpServletResponse());
			request.setParameter(token.getParameterName(), token.getToken());
			return request;
		};
	}

	protected MockHttpSession mockHttpSession(String username) {
		MockHttpSession mockHttpSession = new MockHttpSession();
		mockHttpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new MockSecurityContext(getPrincipal(username)));
		return mockHttpSession;
	}

	private UsernamePasswordAuthenticationToken getPrincipal(String username) {
		UserDetails user = userDetailsService.loadUserByUsername(username);
		return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
	}

	@AllArgsConstructor
	private class MockSecurityContext implements SecurityContext {
		private Authentication authentication;
		@Override
		public Authentication getAuthentication() { return authentication; }
		@Override
		public void setAuthentication(Authentication authentication) { this.authentication = authentication; }
	}

}
