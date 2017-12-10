package com.drumonii.loltrollbuild.admin;

import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import com.drumonii.loltrollbuild.config.WebSecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ControllerAdvice;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
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

@RunWith(SpringRunner.class)
@WebMvcTest(value = AdminLoginController.class, excludeFilters = @Filter(ControllerAdvice.class))
@Import(WebSecurityConfig.class)
@ActiveProfiles({ TESTING })
public class AdminLoginControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@WithAnonymousUser
	@Test
	public void adminCanLogin() throws Exception {
		mockMvc.perform(formLogin("/admin/login").user(IN_MEM_USERNAME).password(IN_MEM_PASSWORD))
				.andExpect(authenticated().withRoles(ADMIN_ROLE))
				.andExpect(redirectedUrl("/admin"));
	}

	@WithMockAdminUser
	@Test
	public void adminCanLogout() throws Exception {
		mockMvc.perform(logout("/admin/logout"))
				.andExpect(unauthenticated())
				.andExpect(redirectedUrl("/admin/login?logout"));
	}

	@WithAnonymousUser
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