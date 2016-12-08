package com.drumonii.loltrollbuild.admin;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ActuatorControllerTest extends BaseSpringTestRunner {

	@Autowired
	private VersionsRepository versionsRepository;

	@Before
	public void before() {
		super.before();

		versionsRepository.save(versions.get(0));
	}

	@After
	public void after() {
		versionsRepository.deleteAll();
	}

	@Test
	public void env() throws Exception {
		mockMvc.perform(get("/admin/env").with(adminUser()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("activeTab", "accordion", "latestSavedPatch"))
				.andExpect(model().attribute("activeTab", is("actuator")))
				.andExpect(model().attribute("accordion", is("env")))
				.andExpect(view().name("admin/actuator/env"));
	}

	@Test
	public void flyway() throws Exception {
		mockMvc.perform(get("/admin/flyway").with(adminUser()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("activeTab", "accordion", "latestSavedPatch"))
				.andExpect(model().attribute("activeTab", is("actuator")))
				.andExpect(model().attribute("accordion", is("flyway")))
				.andExpect(view().name("admin/actuator/flyway"));
	}

	@Test
	public void health() throws Exception {
		mockMvc.perform(get("/admin/health").with(adminUser()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("activeTab", "accordion", "latestSavedPatch"))
				.andExpect(model().attribute("activeTab", is("actuator")))
				.andExpect(model().attribute("accordion", is("health")))
				.andExpect(view().name("admin/actuator/health"));
	}

	@Test
	public void metrics() throws Exception {
		mockMvc.perform(get("/admin/metrics").with(adminUser()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("activeTab", "accordion", "latestSavedPatch"))
				.andExpect(model().attribute("activeTab", is("actuator")))
				.andExpect(model().attribute("accordion", is("metrics")))
				.andExpect(view().name("admin/actuator/metrics"));
	}

}