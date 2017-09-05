package com.drumonii.loltrollbuild.security;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static com.drumonii.loltrollbuild.config.WebSecurityConfig.WebDevTestingSecurityConfig.IN_MEM_PASSWORD;
import static com.drumonii.loltrollbuild.config.WebSecurityConfig.WebDevTestingSecurityConfig.IN_MEM_USERNAME;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class CsrfTokenExpiredAccessDeniedHandlerTest extends BaseSpringTestRunner {

	@Test
	public void expiredCsrfTokenRedirectsToPreviousRequest() throws Exception {
		mockMvc.perform(post("/admin/login")
				.param("username", IN_MEM_USERNAME)
				.param("password", IN_MEM_PASSWORD))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin/login"));
	}

	@WithMockUser
	@Test
	public void csrfNotExpiredSendsForbiddenErrorCode() throws Exception {
		mockMvc.perform(post("/admin").with(csrf())
				.param("username", "bad_username")
				.param("password", "bad_password"))
				.andExpect(status().isForbidden());
	}

}