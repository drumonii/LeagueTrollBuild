package com.drumonii.loltrollbuild.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

@RunWith(SpringRunner.class)
@WebMvcTest(ChampionsController.class)
@ActiveProfiles({ TESTING })
public class ChampionsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@WithAnonymousUser
	@Test
	public void champions() throws Exception {
		mockMvc.perform(get("/champions"))
				.andExpect(forwardedUrl("/index.html"));
	}

	@WithAnonymousUser
	@Test
	public void championById() throws Exception {
		mockMvc.perform(get("/champions/{id}", 1))
				.andExpect(forwardedUrl("/index.html"));
	}

	@WithAnonymousUser
	@Test
	public void championByName() throws Exception {
		mockMvc.perform(get("/champions/{name}", "name"))
				.andExpect(forwardedUrl("/index.html"));
	}

}