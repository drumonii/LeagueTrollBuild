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

@WebMvcTest(BuildsController.class)
@ActiveProfiles({ TESTING })
class BuildsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@WithAnonymousUser
	@Test
	void builds() throws Exception {
		mockMvc.perform(get("/builds"))
				.andExpect(forwardedUrl("/troll-build/index.html"));
	}

	@WithAnonymousUser
	@Test
	void build() throws Exception {
		mockMvc.perform(get("/builds/{id}", 1))
				.andExpect(forwardedUrl("/troll-build/index.html"));
	}

}
