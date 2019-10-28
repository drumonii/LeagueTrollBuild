package com.drumonii.loltrollbuild.api;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.drumonii.loltrollbuild.test.api.WebMvcRestTest;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Collectors;

import static com.drumonii.loltrollbuild.model.SummonerSpell.GameMode.CLASSIC;
import static java.util.function.Predicate.not;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcRestTest(SummonerSpellsRestController.class)
abstract class SummonerSpellsRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	protected SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	@Value("${api.base-path}")
	private String apiPath;

	protected SummonerSpellsResponse summonerSpellsResponse;

	protected abstract void beforeEach();

	@Test
	void getSummonerSpells() throws Exception {
		SummonerSpell summonerSpell = RandomizeUtil.getRandom(summonerSpellsResponse.getSummonerSpells().values().stream()
				.filter(not(spell -> spell.getModes().isEmpty()))
				.collect(Collectors.toSet()));

		// qbe
		mockMvc.perform(get("{apiPath}/summoner-spells", apiPath))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with name
		mockMvc.perform(get("{apiPath}/summoner-spells", apiPath)
				.param("name", summonerSpell.getName().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$..name", everyItem(is(summonerSpell.getName()))));

		// qbe with modes
		mockMvc.perform(get("{apiPath}/summoner-spells", apiPath)
				.param("modes", summonerSpell.getModes().iterator().next().name()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$..modes", everyItem(hasItem(summonerSpell.getModes().iterator().next().name()))));

		// qbe with no results
		mockMvc.perform(get("{apiPath}/summoner-spells", apiPath)
				.param("name", "abcd1234"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(content().json("[]"));
	}

	@Test
	void getSummonerSpell() throws Exception {
		// find with non existing summoner spell Id
		mockMvc.perform(get("{apiPath}/summoner-spells/{id}", apiPath, 0))
				.andExpect(status().isNotFound());

		SummonerSpell snowball = summonerSpellsResponse.getSummonerSpells().get("SummonerSnowball");

		// find with existing summoner spell Id
		mockMvc.perform(get("{apiPath}/summoner-spells/{id}", apiPath, snowball.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());
	}

	@Test
	void getForTrollBuild() throws Exception {
		mockMvc.perform(get("{apiPath}/summoner-spells/for-troll-build", apiPath)
				.param("mode", CLASSIC.name()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());
	}

}
