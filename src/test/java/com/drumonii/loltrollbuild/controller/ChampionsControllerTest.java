package com.drumonii.loltrollbuild.controller;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ChampionsControllerTest extends BaseSpringTestRunner {

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	private MapsRepository mapsRepository;

	@After
	public void after() {
		itemsRepository.deleteAll();
		summonerSpellsRepository.deleteAll();
		championsRepository.deleteAll();
		mapsRepository.deleteAll();
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
				.andExpect(model().attributeExists("champions"))
				.andExpect(model().attribute("champions", hasItems(champions.toArray(new Champion[champions.size()]))))
				.andExpect(model().attributeExists("tags"))
				.andExpect(model().attribute("tags", hasItems(tags.toArray(new String[tags.size()]))))
				.andExpect(view().name("champions/champions"));
	}

	@Test
	public void champion() throws Exception {
		mockMvc.perform(get("/champions/{id}", 0))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/champions"));

		Champion xin = championsResponse.getChampions().get("XinZhao");
		championsRepository.save(xin);

		mockMvc.perform(get("/champions/{id}", xin.getId()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("champion"))
				.andExpect(model().attribute("champion", is(xin)))
				.andExpect(view().name("champions/champion"));

		mockMvc.perform(get("/champions/{name}", "xinzhao"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("champion"))
				.andExpect(model().attribute("champion", is(xin)))
				.andExpect(view().name("champions/champion"));

		mockMvc.perform(get("/champions/{name}", "XinZhao"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("champion"))
				.andExpect(model().attribute("champion", is(xin)))
				.andExpect(view().name("champions/champion"));
	}

	@Test
	public void trollBuild() throws Exception {
		// Items
		Item ninjaTabi = itemsResponse.getItems().get("3047");
		itemsRepository.save(ninjaTabi); // 1

		Item perfectHexCore = itemsResponse.getItems().get("3198");
		itemsRepository.save(perfectHexCore); // 2

		Item randuinsOmen = itemsResponse.getItems().get("3143");
		itemsRepository.save(randuinsOmen); // 3

		Item frozenMallet = itemsResponse.getItems().get("3022");
		itemsRepository.save(frozenMallet); // 4

		Item ghostblade = itemsResponse.getItems().get("3142");
		itemsRepository.save(ghostblade); // 5

		Item rodOfAges = itemsResponse.getItems().get("3027");
		itemsRepository.save(rodOfAges); // 6

		// Summoner Spells
		SummonerSpell heal = summonerSpellsResponse.getSummonerSpells().get("SummonerHeal");
		summonerSpellsRepository.save(heal); // 1

		SummonerSpell ignite = summonerSpellsResponse.getSummonerSpells().get("SummonerDot");
		summonerSpellsRepository.save(ignite); // 2

		// Trinket
		Item scryingOrb = itemsResponse.getItems().get("3341");
		itemsRepository.save(scryingOrb); // 1

		// Map
		GameMap summonersRift = mapsResponse.getMaps().get("11");
		mapsRepository.save(summonersRift);

		Champion viktor = championsResponse.getChampions().get("Viktor");
		championsRepository.save(viktor);

		mockMvc.perform(get("/champions/{id}/troll-build?mapId={mapId}", viktor.getId(), SUMMONERS_RIFT))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.items", hasSize(6)))
				.andExpect(jsonPath("$.items[0].name").value(is(ninjaTabi.getName())))
				.andExpect(jsonPath("$.summoner-spells", hasSize(2)))
				.andExpect(jsonPath("$.trinket", hasSize(1)));
	}

}