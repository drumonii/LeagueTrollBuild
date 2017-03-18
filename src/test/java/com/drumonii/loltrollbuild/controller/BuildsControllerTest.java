package com.drumonii.loltrollbuild.controller;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Build;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class BuildsControllerTest extends BaseSpringTestRunner {

	@Before
	public void before() {
		super.before();

		versionsRepository.save(versions.get(0));
	}

	@Test
	public void builds() throws Exception {
		mockMvc.perform(get("/builds"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/builds/1"));
	}

	@Test
	public void build() throws Exception {
		// With missing build
		mockMvc.perform(get("/builds/{id}", -1))
				.andExpect(status().isOk())
				.andExpect(view().name("builds/notFound"));

		Build build = new Build();

		build.setChampionId(-1); // Bad Champion Id

		build.setItem1(itemsRepository.save(itemsResponse.getItems().get("3111")));
		build.setItem1Id(build.getItem1().getId());
		build.setItem2(itemsRepository.save(itemsResponse.getItems().get("3022")));
		build.setItem2Id(build.getItem2().getId());
		build.setItem3(itemsRepository.save(itemsResponse.getItems().get("3006")));
		build.setItem3Id(build.getItem3().getId());
		build.setItem4(itemsRepository.save(itemsResponse.getItems().get("3116")));
		build.setItem4Id(build.getItem4().getId());
		build.setItem5(itemsRepository.save(itemsResponse.getItems().get("3190")));
		build.setItem5Id(build.getItem5().getId());
		build.setItem6(itemsRepository.save(itemsResponse.getItems().get("3031")));
		build.setItem6Id(build.getItem6().getId());

		build.setSummonerSpell1(summonerSpellsRepository.save(summonerSpellsResponse.getSummonerSpells()
				.get("SummonerDot")));
		build.setSummonerSpell1Id(build.getSummonerSpell1().getId());
		build.setSummonerSpell2(summonerSpellsRepository.save(summonerSpellsResponse.getSummonerSpells()
				.get("SummonerExhaust")));
		build.setSummonerSpell2Id(build.getSummonerSpell2().getId());

		build.setTrinket(itemsRepository.save(itemsResponse.getItems().get("3340")));
		build.setTrinketId(build.getTrinket().getId());

		build.setMap(mapsRepository.save(mapsResponse.getMaps().get(SUMMONERS_RIFT)));
		build.setMapId(build.getMap().getMapId());
		build = buildsRepository.save(build);

		// With missing build attributes
		mockMvc.perform(get("/builds/{id}", build.getId()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("latestSavedPatch"))
				.andExpect(view().name("builds/invalidAttributes"));

		build.setChampion(championsRepository.save(championsResponse.getChampions().get("Teemo")));
		build.setChampionId(build.getChampion().getId());
		build = buildsRepository.save(build);

		// With full build attributes
		mockMvc.perform(get("/builds/{id}", build.getId()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("latestSavedPatch"))
				.andExpect(model().attribute("build", is(build)))
				.andExpect(view().name("builds/build"));
	}

}