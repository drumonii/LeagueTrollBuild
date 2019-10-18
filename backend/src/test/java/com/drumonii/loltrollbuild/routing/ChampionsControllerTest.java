package com.drumonii.loltrollbuild.routing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

@WebMvcTest(ChampionsController.class)
@ActiveProfiles({ TESTING })
class ChampionsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@WithAnonymousUser
	@Test
	void champions() throws Exception {
		mockMvc.perform(get("/champions"))
				.andExpect(forwardedUrl("/troll-build/index.html"));
	}

	@WithAnonymousUser
	@Test
	void championById() throws Exception {
		mockMvc.perform(get("/champions/{id}", 1))
				.andExpect(forwardedUrl("/troll-build/index.html"));
	}

	@WithAnonymousUser
	@Test
	void championByName() throws Exception {
		mockMvc.perform(get("/champions/{name}", "name"))
				.andExpect(forwardedUrl("/troll-build/index.html"));
	}

}
