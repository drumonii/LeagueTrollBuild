package com.drumonii.loltrollbuild;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static java.nio.charset.StandardCharsets.UTF_8;
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

	protected MockMvc mockMvc;

	protected MediaType APPLICATION_JSON_UTF8 = new MediaType("application", "json", UTF_8);
	protected MediaType PLAN_TEXT_UTF8 = new MediaType("text", "plain", UTF_8);

	@Before
	public void before() {
		mockMvc = webAppContextSetup(wac)
				.build();
	}

}
