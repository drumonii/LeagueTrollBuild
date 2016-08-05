package com.drumonii.loltrollbuild.security;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CsrfTokenExpiredAccessDeniedHandlerTest extends BaseSpringTestRunner {

	@Test
	public void expiredCsrfTokenRedirectsToPreviousRequest() throws Exception {
		mockMvc.perform(post("/admin/login")
				.param("username", TESTING_USERNAME)
				.param("password", TESTING_PASSWORD))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin/login"));
	}

	@Test
	@WithMockUser(value = TESTING_USERNAME, roles = "USER")
	public void csrfNotExpiredSendsForbiddenErrorCode() throws Exception {
		mockMvc.perform(get("/admin"))
				.andExpect(status().isForbidden());
	}

}