package com.drumonii.loltrollbuild.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ControllerAdvice;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = BadController.class, excludeFilters = @Filter(ControllerAdvice.class), secure = false)
@ActiveProfiles({ TESTING })
public class BadControllerTest {

	@Autowired
	private MockMvc mockMvc;

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