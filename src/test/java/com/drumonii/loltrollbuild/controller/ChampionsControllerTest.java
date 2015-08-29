package com.drumonii.loltrollbuild.controller;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ChampionsControllerTest extends BaseSpringTestRunner {

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void champion() throws Exception {
		mockMvc.perform(get("/champions"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("champions"))
				.andExpect(view().name("champions/champions"));
	}

	@Test
	public void champions() throws Exception {
		mockMvc.perform(get("/champions/{id}", 1))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/champions"));

		String responseBody = "{\"type\":\"champion\",\"version\":\"5.16.1\",\"data\":{\"Test\":{\"id\":10001,\"" +
				"key\":\"Test\",\"name\":\"Test\",\"title\":\"Much Test Champion\",\"image\":{\"full\":" +
				"\"Test.png\",\"sprite\":\"champion0.png\",\"group\":\"champion\",\"x\":336,\"y\":0,\"w\":48," +
				"\"h\":48},\"tags\":[\"Testing1\",\"Testing2\"],\"partype\":\"TestParType\"}}}";
		ChampionsResponse championsResponse = objectMapper.readValue(responseBody, ChampionsResponse.class);
		Champion unmarshalChampion = championsResponse.getChampions().get("Test");
		championsRepository.save(unmarshalChampion);

		mockMvc.perform(get("/champions/{id}", 10001))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("champion"))
				.andExpect(model().attribute("champion", is(unmarshalChampion)))
				.andExpect(view().name("champions/champion"));
		championsRepository.delete(10001);
	}

}