package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.MediaTypes;

import static com.drumonii.loltrollbuild.model.SummonerSpell.GameMode.CLASSIC;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SummonerSpellsRestControllerTest extends BaseSpringTestRunner {

	@Value("${spring.data.rest.base-path}")
	private String apiPath;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@After
	public void after() {
		summonerSpellsRepository.deleteAll();
	}

	@Test
	public void getSummonerSpells() throws Exception {
		String responseBody = "{\"type\":\"summoner\",\"version\":\"6.13.1\",\"data\":{\"SummonerHaste\":{\"name\":" +
				"\"Ghost\",\"description\":\"Your champion can move through units and has 28-45% (depending on " +
				"champion level) increased Movement Speed for 10 seconds.\",\"image\":{\"full\":\"SummonerHaste.png\"," +
				"\"sprite\":\"spell0.png\",\"group\":\"spell\",\"x\":288,\"y\":0,\"w\":48,\"h\":48},\"cooldown\":" +
				"[210],\"summonerLevel\":1,\"id\":6,\"key\":\"SummonerHaste\",\"modes\":[\"CLASSIC\",\"ODIN\"," +
				"\"TUTORIAL\",\"ARAM\",\"ASCENSION\",\"FIRSTBLOOD\"]}}}";
		SummonerSpell ghost = objectMapper.readValue(responseBody, SummonerSpellsResponse.class).getSummonerSpells()
				.get("SummonerHaste");
		summonerSpellsRepository.save(ghost);

		// qbe with name
		mockMvc.perform(get(apiPath + "/summoner-spells")
				.param("name", "ghost"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON))
				.andExpect(jsonPath("$._embedded.summonerSpells", hasSize(1)))
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$.page").exists());

		// qbe with no results
		mockMvc.perform(get(apiPath + "/summoner-spells")
				.param("name", "notfound"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON))
				.andExpect(jsonPath("$._embedded").doesNotExist())
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$.page").exists());
	}

	@Test
	public void getForTrollBuild() throws Exception {
		mockMvc.perform(get(apiPath + "/summoner-spells/for-troll-build")
				.param("mode", CLASSIC.name()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON))
				.andExpect(jsonPath("$._links").exists());
	}

}