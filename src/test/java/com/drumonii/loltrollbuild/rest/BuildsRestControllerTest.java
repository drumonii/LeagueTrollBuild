package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.*;
import com.drumonii.loltrollbuild.repository.BuildsRepository;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.drumonii.loltrollbuild.test.rest.WebMvcRestTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.drumonii.loltrollbuild.util.GameMapUtil.SUMMONERS_RIFT_SID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcRestTest(controllers = BuildsRestController.class)
public abstract class BuildsRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	protected BuildsRepository buildsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	@Value("${api.base-path}")
	private String apiPath;

	protected ChampionsResponse championsResponse;
	protected ItemsResponse itemsResponse;
	protected MapsResponse mapsResponse;
	protected SummonerSpellsResponse summonerSpellsResponse;

	public abstract void before();

	@Test
	public void getBuilds() throws Exception {
		// qbe
		mockMvc.perform(get("{apiPath}/builds", apiPath))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());
	}

	@Test
	public void getBuild() throws Exception {
		// find with non existing build Id
		mockMvc.perform(get("{apiPath}/builds/{id}", apiPath, 0))
				.andExpect(status().isNotFound());

		Build build = new Build();
		build.setChampionId(championsResponse.getChampions().get("Zed").getId());
		build.setItem1Id(itemsResponse.getItems().get("3117").getId());
		build.setItem2Id(itemsResponse.getItems().get("3194").getId());
		build.setItem3Id(itemsResponse.getItems().get("3193").getId());
		build.setItem4Id(itemsResponse.getItems().get("3036").getId());
		build.setItem5Id(itemsResponse.getItems().get("3812").getId());
		build.setItem6Id(itemsResponse.getItems().get("3092").getId());
		build.setSummonerSpell1Id(summonerSpellsResponse.getSummonerSpells().get("SummonerSmite").getId());
		build.setSummonerSpell2Id(summonerSpellsResponse.getSummonerSpells().get("SummonerMana").getId());
		build.setTrinketId(itemsResponse.getItems().get("3364").getId());
		build.setMapId(mapsResponse.getMaps().get(SUMMONERS_RIFT_SID).getMapId());
		build = buildsRepository.save(build);

		// find with existing build Id
		mockMvc.perform(get("{apiPath}/builds/{id}", apiPath, build.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());
	}

	@Test
	public void saveBuild() throws Exception {
		Build build = new Build();

		// Save with missing attributes
		mockMvc.perform(post("{apiPath}/builds", apiPath).with(csrf())
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(build)))
				.andExpect(status().isBadRequest());

		Champion tahmKench = championsResponse.getChampions().get("TahmKench");
		Item item1 = itemsResponse.getItems().get("3009");
		Item item2 = itemsResponse.getItems().get("3001");
		Item item3 = itemsResponse.getItems().get("3056");
		Item item4 = itemsResponse.getItems().get("3009");
		Item item5 = itemsResponse.getItems().get("3083");
		Item item6 = itemsResponse.getItems().get("3092");
		SummonerSpell summonerSpell1 = summonerSpellsResponse.getSummonerSpells().get("SummonerTeleport");
		SummonerSpell summonerSpell2 = summonerSpellsResponse.getSummonerSpells().get("SummonerHaste");
		Item trinket = itemsResponse.getItems().get("3340");
		GameMap map = mapsResponse.getMaps().get(SUMMONERS_RIFT_SID);

		build.setChampionId(tahmKench.getId());
		build.setItem1Id(item1.getId());
		build.setItem2Id(item2.getId());
		build.setItem3Id(item3.getId());
		build.setItem4Id(item4.getId());
		build.setItem5Id(item5.getId());
		build.setItem6Id(item6.getId());
		build.setSummonerSpell1Id(summonerSpell1.getId());
		build.setSummonerSpell2Id(summonerSpellsResponse.getSummonerSpells().get("SummonerHaste").getId());
		build.setTrinketId(itemsResponse.getItems().get("3340").getId());
		build.setMapId(map.getMapId());

		// Save full build
		mockMvc.perform(post("{apiPath}/builds", apiPath).with(csrf())
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(build)))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		assertThat(buildsRepository.findAll())
				.filteredOn(savedBuild -> savedBuild.getChampionId() == tahmKench.getId() &&
						savedBuild.getItem1Id() == item1.getId() &&
						savedBuild.getItem2Id() == item2.getId() &&
						savedBuild.getItem3Id() == item3.getId() &&
						savedBuild.getItem4Id() == item4.getId() &&
						savedBuild.getItem5Id() == item5.getId() &&
						savedBuild.getItem6Id() == item6.getId() &&
						savedBuild.getSummonerSpell1Id() == summonerSpell1.getId() &&
						savedBuild.getSummonerSpell2Id() == summonerSpell2.getId() &&
						savedBuild.getTrinketId() == trinket.getId() &&
						savedBuild.getMapId() == map.getMapId())
				.isNotEmpty();
	}

}