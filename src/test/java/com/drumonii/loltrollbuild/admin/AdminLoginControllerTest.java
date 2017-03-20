package com.drumonii.loltrollbuild.admin;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import org.junit.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class AdminLoginControllerTest extends BaseSpringTestRunner {

	@Test
	public void adminCanLoginAndLogout() throws Exception {
		mockMvc.perform(formLogin("/admin/login").user(TESTING_USERNAME).password(TESTING_PASSWORD))
				.andExpect(authenticated().withRoles(TESTING_USER_ROLE))
				.andExpect(redirectedUrl("/admin"));
		mockMvc.perform(logout("/admin/logout"))
				.andExpect(unauthenticated())
				.andExpect(redirectedUrl("/admin/login?logout"));
	}

	@Test
	public void getLogin() throws Exception {
		mockMvc.perform(get("/admin/login"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/login"));

		mockMvc.perform(get("/admin/login").with(adminUser()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin"));
	}

}