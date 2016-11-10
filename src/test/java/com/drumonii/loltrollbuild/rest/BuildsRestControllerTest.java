package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Build;
import com.drumonii.loltrollbuild.repository.BuildsRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BuildsRestControllerTest extends BaseSpringTestRunner {

	@Value("${spring.data.rest.base-path}")
	private String apiPath;

	@Autowired
	private BuildsRepository buildsRepository;

	@Before
	public void before() {
		super.before();

		Build build = new Build();
		build.setChampionId(championsResponse.getChampions().get("Nasus").getId());
		build.setItem1Id(itemsResponse.getItems().get("3006").getId());
		build.setItem2Id(itemsResponse.getItems().get("3285").getId());
		build.setItem3Id(itemsResponse.getItems().get("3102").getId());
		build.setItem4Id(itemsResponse.getItems().get("3083").getId());
		build.setItem5Id(itemsResponse.getItems().get("3001").getId());
		build.setItem6Id(itemsResponse.getItems().get("3157").getId());
		build.setSummonerSpell1Id(summonerSpellsResponse.getSummonerSpells().get("SummonerTeleport").getId());
		build.setSummonerSpell2Id(summonerSpellsResponse.getSummonerSpells().get("SummonerHaste").getId());
		build.setTrinketId(itemsResponse.getItems().get("3341").getId());
		build.setMapId(mapsResponse.getMaps().get(SUMMONERS_RIFT).getMapId());
		buildsRepository.save(build);
	}

	@After
	public void after() {
		buildsRepository.deleteAll();
	}

	@Test
	public void getBuilds() throws Exception {
		// qbe
		mockMvc.perform(get(apiPath + "/builds"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.builds", hasSize(1)))
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(20)))
				.andExpect(jsonPath("$.page.totalElements", is(1)))
				.andExpect(jsonPath("$.page.totalPages", is((int) Math.ceil((double) 1 / (double) 20))))
				.andExpect(jsonPath("$.page.number", is(0)));
	}


	@Test
	public void saveBuild() throws Exception {
		Build build = new Build();

		// Save with missing attributes
		mockMvc.perform(post(apiPath + "/builds").with(csrf())
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(build)))
				.andExpect(status().isBadRequest());

		build.setChampionId(championsResponse.getChampions().get("Nasus").getId());
		build.setItem1Id(itemsResponse.getItems().get("3009").getId());
		build.setItem2Id(itemsResponse.getItems().get("3001").getId());
		build.setItem3Id(itemsResponse.getItems().get("3089").getId());
		build.setItem4Id(itemsResponse.getItems().get("3056").getId());
		build.setItem5Id(itemsResponse.getItems().get("3083").getId());
		build.setItem6Id(itemsResponse.getItems().get("3092").getId());
		build.setSummonerSpell1Id(summonerSpellsResponse.getSummonerSpells().get("SummonerTeleport").getId());
		build.setSummonerSpell2Id(summonerSpellsResponse.getSummonerSpells().get("SummonerHaste").getId());
		build.setTrinketId(itemsResponse.getItems().get("3340").getId());
		build.setMapId(mapsResponse.getMaps().get(SUMMONERS_RIFT).getMapId());

		// Save full build
		mockMvc.perform(post(apiPath + "/builds").with(csrf())
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(build)))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(HAL_JSON_UTF8));
	}

}