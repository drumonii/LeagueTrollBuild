package com.drumonii.loltrollbuild.admin;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import org.junit.Test;

import static com.drumonii.loltrollbuild.config.WebSecurityConfig.ADMIN_ROLE;
import static com.drumonii.loltrollbuild.config.WebSecurityConfig.WebDevTestingSecurityConfig.IN_MEM_PASSWORD;
import static com.drumonii.loltrollbuild.config.WebSecurityConfig.WebDevTestingSecurityConfig.IN_MEM_USERNAME;
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
		mockMvc.perform(formLogin("/admin/login").user(IN_MEM_USERNAME).password(IN_MEM_PASSWORD))
				.andExpect(authenticated().withRoles(ADMIN_ROLE))
				.andExpect(redirectedUrl("/admin"));
		mockMvc.perform(logout("/admin/logout"))
				.andExpect(unauthenticated())
				.andExpect(redirectedUrl("/admin/login?logout"));
	}

	@Test
	public void getLoginAsNotLoggedIn() throws Exception {
		mockMvc.perform(get("/admin/login"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/login"));
	}

	@WithMockAdminUser
	@Test
	public void getLoginAsLoggedIn() throws Exception {
		mockMvc.perform(get("/admin/login"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin"));
	}

}