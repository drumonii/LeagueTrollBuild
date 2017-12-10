package com.drumonii.loltrollbuild.admin;

import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import com.drumonii.loltrollbuild.config.WebSecurityConfig;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(ActuatorController.class)
@Import(WebSecurityConfig.class)
@ActiveProfiles({ TESTING })
public class ActuatorControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private VersionsRepository versionsRepository;

	@Before
	public void before() {
		given(versionsRepository.latestVersion()).willReturn(new Version("7.17.2"));
	}

	@WithMockAdminUser
	@Test
	public void env() throws Exception {
		mockMvc.perform(get("/admin/env"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("activeTab", "accordion", "latestSavedPatch"))
				.andExpect(model().attribute("activeTab", is("actuator")))
				.andExpect(model().attribute("accordion", is("env")))
				.andExpect(view().name("admin/actuator/env"));
	}

	@WithMockAdminUser
	@Test
	public void flyway() throws Exception {
		mockMvc.perform(get("/admin/flyway"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("activeTab", "accordion", "latestSavedPatch"))
				.andExpect(model().attribute("activeTab", is("actuator")))
				.andExpect(model().attribute("accordion", is("flyway")))
				.andExpect(view().name("admin/actuator/flyway"));
	}

	@WithMockAdminUser
	@Test
	public void health() throws Exception {
		mockMvc.perform(get("/admin/health"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("activeTab", "accordion", "latestSavedPatch"))
				.andExpect(model().attribute("activeTab", is("actuator")))
				.andExpect(model().attribute("accordion", is("health")))
				.andExpect(view().name("admin/actuator/health"));
	}

	@WithMockAdminUser
	@Test
	public void metrics() throws Exception {
		mockMvc.perform(get("/admin/metrics"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("activeTab", "accordion", "latestSavedPatch"))
				.andExpect(model().attribute("activeTab", is("actuator")))
				.andExpect(model().attribute("accordion", is("metrics")))
				.andExpect(view().name("admin/actuator/metrics"));
	}

}