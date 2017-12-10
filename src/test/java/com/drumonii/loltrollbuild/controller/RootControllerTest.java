package com.drumonii.loltrollbuild.controller;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = RootController.class, excludeFilters = @Filter(ControllerAdvice.class))
@Import(WebSecurityConfig.class)
@ActiveProfiles({ TESTING })
public class RootControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@WithAnonymousUser
	@Test
	public void redirectToChampions() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/champions"));
	}

}