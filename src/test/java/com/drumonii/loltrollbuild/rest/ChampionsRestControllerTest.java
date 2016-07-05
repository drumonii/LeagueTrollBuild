package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.MediaTypes;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ChampionsRestControllerTest extends BaseSpringTestRunner {

	@Value("${spring.data.rest.base-path}")
	private String apiPath;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ChampionsRepository championsRepository;

	@After
	public void after() {
		championsRepository.deleteAll();
	}

	@Test
	public void getChampions() throws Exception {
		String responseBody = "{\"type\":\"champion\",\"version\":\"6.13.1\",\"data\":{\"Tristana\":{\"id\":18," +
				"\"key\":\"Tristana\",\"name\":\"Tristana\",\"title\":\"the Yordle Gunner\",\"image\":{\"full\":" +
				"\"Tristana.png\",\"sprite\":\"champion3.png\",\"group\":\"champion\",\"x\":288,\"y\":48,\"w\":48," +
				"\"h\":48},\"tags\":[\"Marksman\",\"Assassin\"],\"partype\":\"MP\"}}}";
		Champion tristana = objectMapper.readValue(responseBody, ChampionsResponse.class).getChampions().get("Tristana");
		championsRepository.save(tristana);

		// qbe with name
		mockMvc.perform(get(apiPath + "/champions")
				.param("name", "trist"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON))
				.andExpect(jsonPath("$._embedded.champions", hasSize(1)))
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$.page").exists());

		// qbe with title
		mockMvc.perform(get(apiPath + "/champions")
				.param("title", "gunner"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON))
				.andExpect(jsonPath("$._embedded.champions", hasSize(1)))
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$.page").exists());

		// qbe with partype
		mockMvc.perform(get(apiPath + "/champions")
				.param("partype", "mp"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON))
				.andExpect(jsonPath("$._embedded.champions", hasSize(1)))
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$.page").exists());

		// qbe with no results
		mockMvc.perform(get(apiPath + "/champions")
				.param("name", "notfound"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON))
				.andExpect(jsonPath("$._embedded").doesNotExist())
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$.page").exists());
	}

}