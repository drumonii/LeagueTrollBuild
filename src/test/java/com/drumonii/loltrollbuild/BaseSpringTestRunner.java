package com.drumonii.loltrollbuild;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static com.drumonii.loltrollbuild.config.WebSecurityConfig.ADMIN_ROLE;
import static com.drumonii.loltrollbuild.config.WebSecurityConfig.WebDevTestingSecurityConfig.IN_MEM_PASSWORD;
import static com.drumonii.loltrollbuild.config.WebSecurityConfig.WebDevTestingSecurityConfig.IN_MEM_USERNAME;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
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

	protected MediaType APPLICATION_JSON_UTF8 = MediaType.parseMediaType("application/json;charset=UTF-8");
	protected MediaType PLAN_TEXT_UTF8 = MediaType.parseMediaType("text/plain;charset=UTF-8");

	protected static final String TESTING_USERNAME = IN_MEM_USERNAME;
	protected static final String TESTING_PASSWORD = IN_MEM_PASSWORD;
	protected static final String TESTING_USER_ROLE = ADMIN_ROLE;

	@Before
	public void before() {
		mockMvc = webAppContextSetup(wac)
				.addFilters(springSecurityFilterChain)
				.apply(springSecurity())
				.build();
	}

	protected static RequestPostProcessor testUser() {
		return user(TESTING_USERNAME).password(TESTING_PASSWORD).roles(TESTING_USER_ROLE);
	}

}
