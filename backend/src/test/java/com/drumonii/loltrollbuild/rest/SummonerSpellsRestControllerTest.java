package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.drumonii.loltrollbuild.test.rest.WebMvcRestTest;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Collectors;

import static com.drumonii.loltrollbuild.model.SummonerSpell.GameMode.CLASSIC;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcRestTest(SummonerSpellsRestController.class)
public abstract class SummonerSpellsRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	protected SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	@Value("${api.base-path}")
	private String apiPath;

	protected SummonerSpellsResponse summonerSpellsResponse;

	public abstract void before();

	@Test
	public void getSummonerSpells() throws Exception {
		SummonerSpell summonerSpell = RandomizeUtil.getRandom(summonerSpellsResponse.getSummonerSpells().values().stream()
				.filter(spell -> !spell.getModes().isEmpty())
				.collect(Collectors.toSet()));

		// qbe
		mockMvc.perform(get("{apiPath}/summoner-spells", apiPath))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with name
		mockMvc.perform(get("{apiPath}/summoner-spells", apiPath)
				.param("name", summonerSpell.getName().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with modes
		mockMvc.perform(get("{apiPath}/summoner-spells", apiPath)
				.param("modes", summonerSpell.getModes().iterator().next().name()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with no results
		mockMvc.perform(get("{apiPath}/summoner-spells", apiPath)
				.param("name", "abcd1234"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(content().json("[]"));
	}

	@Test
	public void getSummonerSpell() throws Exception {
		// find with non existing summoner spell Id
		mockMvc.perform(get("{apiPath}/summoner-spells/{id}", apiPath, 0))
				.andExpect(status().isNotFound());

		SummonerSpell snowball = summonerSpellsResponse.getSummonerSpells().get("SummonerSnowball");

		// find with existing summoner spell Id
		mockMvc.perform(get("{apiPath}/summoner-spells/{id}", apiPath, snowball.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());
	}

	@Test
	public void getForTrollBuild() throws Exception {
		mockMvc.perform(get("{apiPath}/summoner-spells/for-troll-build", apiPath)
				.param("mode", CLASSIC.name()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());
	}

}