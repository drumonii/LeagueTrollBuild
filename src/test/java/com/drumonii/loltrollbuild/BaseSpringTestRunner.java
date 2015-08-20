package com.drumonii.loltrollbuild;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Base {@link SpringJUnit4ClassRunner} for Spring JUnit 4 tests that runs on a random unused port to avoid conflicts
 * from the main application (potentially) already running on port 8080.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LeagueTrollBuildApplication.class)
@WebIntegrationTest(randomPort = true)
public abstract class BaseSpringTestRunner {

	@Autowired
	private WebApplicationContext wac;

	protected MockMvc mockMvc;

	@Before
	public void before() {
		mockMvc = webAppContextSetup(wac)
				.build();
	}

}
