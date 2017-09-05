package com.drumonii.loltrollbuild.admin;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class ActuatorControllerTest extends BaseSpringTestRunner {

	@Before
	public void before() {
		super.before();

		versionsRepository.save(versions.get(0));
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