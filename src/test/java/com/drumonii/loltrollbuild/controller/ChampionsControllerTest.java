package com.drumonii.loltrollbuild.controller;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Repeat;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.drumonii.loltrollbuild.util.GameMapUtil.SUMMONERS_RIFT_ID;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.core.IsNot.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ChampionsControllerTest extends BaseSpringTestRunner {

	@Before
	public void before() {
		super.before();

		versionsRepository.save(versions.get(0));
	}

	@Test
	public void champions() throws Exception {
		ChampionsResponse championsResponseSlice = new ChampionsResponse();
		championsResponseSlice.setType(championsResponse.getType());
		championsResponseSlice.setVersion(championsResponse.getVersion());
		championsResponseSlice.setChampions(RandomizeUtil.getRandoms(
				championsResponse.getChampions().values(), 10).stream()
				.collect(Collectors.toMap(champion -> String.valueOf(champion.getId()), champion -> champion)));
		List<Champion> champions = championsRepository.save(championsResponseSlice.getChampions().values());

		SortedSet<String> tags = new TreeSet<>();
		champions.forEach(champion -> tags.addAll(champion.getTags()));

		mockMvc.perform(get("/champions"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("champions", "latestSavedPatch"))
				.andExpect(model().attribute("champions", containsInAnyOrder(champions.toArray())))
				.andExpect(model().attributeExists("tags"))
				.andExpect(model().attribute("tags", containsInAnyOrder(tags.toArray())))
				.andExpect(view().name("champions/champions"));
	}

	@Test
	public void champion() throws Exception {
		mockMvc.perform(get("/champions/{id}", 0))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/champions"));

		Champion xin = championsResponse.getChampions().get("XinZhao");
		xin = championsRepository.save(xin);

		mockMvc.perform(get("/champions/{id}", xin.getId()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("champion", "latestSavedPatch"))
				.andExpect(model().attribute("champion", is(xin)))
				.andExpect(view().name("champions/champion"));

		mockMvc.perform(get("/champions/{name}", "xinzhao"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("champion", "latestSavedPatch"))
				.andExpect(model().attribute("champion", is(xin)))
				.andExpect(view().name("champions/champion"));

		mockMvc.perform(get("/champions/{name}", "XinZhao"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("champion", "latestSavedPatch"))
				.andExpect(model().attribute("champion", is(xin)))
				.andExpect(view().name("champions/champion"));
	}

	@Repeat(15)
	@Test
	public void trollBuild() throws Exception {
		itemsRepository.save(itemsResponse.getItems().values());
		championsRepository.save(championsResponse.getChampions().values());
		summonerSpellsRepository.save(summonerSpellsResponse.getSummonerSpells().values());
		mapsRepository.save(mapsResponse.getMaps().values());

		List<String> boots = itemsRepository.boots(SUMMONERS_RIFT_ID).stream()
				.map(Item::getName)
				.collect(Collectors.toList());

		Champion viktor = championsResponse.getChampions().get("Viktor");

		mockMvc.perform(get("/champions/{id}/troll-build?mapId={mapId}", viktor.getId(), SUMMONERS_RIFT))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.items", hasSize(6)))
				.andExpect(jsonPath("$.items[0].name").value(isIn(boots)))
				.andExpect(jsonPath("$.items[1:5].name").value(not(isIn(boots))))
				.andExpect(jsonPath("$.summoner-spells", hasSize(2)))
				.andExpect(jsonPath("$.trinket", hasSize(1)));
	}

}