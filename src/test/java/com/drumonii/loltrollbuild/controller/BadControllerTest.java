package com.drumonii.loltrollbuild.controller;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BadControllerTest extends BaseSpringTestRunner {

	@Test
	public void throws400() throws Exception {
		mockMvc.perform(get("/400"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void throws403() throws Exception {
		mockMvc.perform(get("/403"))
				.andExpect(status().isForbidden());
	}

	@Test
	public void throws404() throws Exception {
		mockMvc.perform(get("/404"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void throws500() throws Exception {
		mockMvc.perform(get("/500"))
				.andExpect(status().isInternalServerError());
	}

}