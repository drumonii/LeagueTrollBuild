package com.drumonii.loltrollbuild.admin;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ActuatorControllerTest extends BaseSpringTestRunner {

	@Test
	public void env() throws Exception {
		mockMvc.perform(get("/admin/env").with(adminUser()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("activeTab", "accordion"))
				.andExpect(model().attribute("activeTab", is("actuator")))
				.andExpect(model().attribute("accordion", is("env")))
				.andExpect(view().name("admin/actuator/env"));
	}

	@Test
	public void flyway() throws Exception {
		mockMvc.perform(get("/admin/flyway").with(adminUser()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("activeTab", "accordion"))
				.andExpect(model().attribute("activeTab", is("actuator")))
				.andExpect(model().attribute("accordion", is("flyway")))
				.andExpect(view().name("admin/actuator/flyway"));
	}

}