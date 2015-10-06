package com.drumonii.loltrollbuild.admin;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class AdminControllerTest extends BaseSpringTestRunner {

	@Test
	public void admin() throws Exception {
		mockMvc.perform(get("/admin").with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/admin"));
	}

	@Test
	public void summonerSpells() throws Exception {
		mockMvc.perform(get("/admin/summoner-spells").with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("spells"))
				.andExpect(view().name("admin/summonerSpells/summonerSpells"));
	}

	@Test
	public void items() throws Exception {
		mockMvc.perform(get("/admin/items").with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("items"))
				.andExpect(view().name("admin/items/items"));
	}

	@Test
	public void champions() throws Exception {
		mockMvc.perform(get("/admin/champions").with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("champions"))
				.andExpect(view().name("admin/champions/champions"));
	}

}