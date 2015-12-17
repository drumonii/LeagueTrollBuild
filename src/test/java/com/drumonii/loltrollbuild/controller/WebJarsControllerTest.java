package com.drumonii.loltrollbuild.controller;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WebJarsControllerTest extends BaseSpringTestRunner {

	@Test
	public void locateWebjar() throws Exception {
		mockMvc.perform(get("/webjars/{webjar}", "jquery/jquery.min.js"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/webjars/{webjar}", "semantic/dist/semantic.min.css"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/webjars/{webjar}", "semantic/dist/semantic.min.js"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/webjars/{webjar}", "hideseek/jquery.hideseek.min.js"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/webjars/{webjar}", "notfound/notfound.min.js"))
				.andExpect(status().isNotFound());
		mockMvc.perform(get("/webjars/{webjar}", "malformed"))
				.andExpect(status().isNotFound());
		mockMvc.perform(get("/webjars/{webjar}", "jquery/jquery.js"))
				.andExpect(status().isNotFound());
	}

}