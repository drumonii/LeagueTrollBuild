package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.stream.Collectors;

import static com.drumonii.loltrollbuild.model.SummonerSpell.GameMode.CLASSIC;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SummonerSpellsRestControllerTest extends BaseSpringTestRunner {

	private static final int DEFAULT_PAGE_SIZE = 20;

	@Value("${spring.data.rest.base-path}")
	private String apiPath;

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	private SummonerSpellsResponse summonerSpellsResponseSlice;

	@Before
	public void before() {
		super.before();

		summonerSpellsResponseSlice = new SummonerSpellsResponse();
		summonerSpellsResponseSlice.setType(championsResponse.getType());
		summonerSpellsResponseSlice.setVersion(championsResponse.getVersion());
		summonerSpellsResponseSlice.setSummonerSpells(RandomizeUtil.getRandoms(
				summonerSpellsResponse.getSummonerSpells().values(), DEFAULT_PAGE_SIZE).stream()
				.collect(Collectors.toMap(spell -> String.valueOf(spell.getId()), spell -> spell)));
		summonerSpellsRepository.save(summonerSpellsResponseSlice.getSummonerSpells().values());
	}

	@After
	public void after() {
		summonerSpellsRepository.deleteAll();
	}

	@Test
	public void getSummonerSpells() throws Exception {
		SummonerSpell summonerSpell = RandomizeUtil.getRandom(summonerSpellsResponseSlice.getSummonerSpells().values());

		// qbe
		mockMvc.perform(get(apiPath + "/summoner-spells"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.summonerSpells",
						hasSize(summonerSpellsResponseSlice.getSummonerSpells().values().size())))
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(20)))
				.andExpect(jsonPath("$.page.totalElements",
						is(summonerSpellsResponseSlice.getSummonerSpells().values().size())))
				.andExpect(jsonPath("$.page.totalPages", is(
						(int) Math.ceil((double) summonerSpellsResponseSlice.getSummonerSpells().values().size()
								/ (double) 20))))
				.andExpect(jsonPath("$.page.number", is(0)));

		// qbe with name
		mockMvc.perform(get(apiPath + "/summoner-spells")
				.param("name", summonerSpell.getName().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.summonerSpells", hasSize(1)))
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(20)))
				.andExpect(jsonPath("$.page.totalElements", is(1)))
				.andExpect(jsonPath("$.page.totalPages", is(1)))
				.andExpect(jsonPath("$.page.number", is(0)));

		// qbe with no results
		mockMvc.perform(get(apiPath + "/summoner-spells")
				.param("name", "abcd1234"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded").doesNotExist())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page.size", is(20)))
				.andExpect(jsonPath("$.page.totalElements", is(0)))
				.andExpect(jsonPath("$.page.totalPages", is(0)))
				.andExpect(jsonPath("$.page.number", is(0)));
	}

	@Test
	public void getForTrollBuild() throws Exception {
		mockMvc.perform(get(apiPath + "/summoner-spells/for-troll-build")
				.param("mode", CLASSIC.name()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.summonerSpells").exists())
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists());
	}

}