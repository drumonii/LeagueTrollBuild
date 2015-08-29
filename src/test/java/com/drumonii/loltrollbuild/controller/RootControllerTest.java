package com.drumonii.loltrollbuild.controller;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RootControllerTest extends BaseSpringTestRunner {

	@Test
	public void redirectToChampions() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/champions"));
	}

}