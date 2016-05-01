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
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ImagesControllerTest extends BaseSpringTestRunner {
	
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	private MapsRepository mapsRepository;

	@After
	public void after() {
		summonerSpellsRepository.deleteAll();
		itemsRepository.deleteAll();
		championsRepository.deleteAll();
		mapsRepository.deleteAll();
	}

	@Test
	public void summonerSpellImg() throws Exception {
		String responseBody = "{\"type\":\"summoner\",\"version\":\"6.8.1\",\"data\":{\"SummonerSmite\":{\"name\":" +
				"\"Smite\",\"description\":\"Deals 390-1000 true damage (depending on champion level) to target epic " +
				"or large monster or enemy minion.\",\"image\":{\"full\":\"SummonerSmite.png\",\"sprite\":" +
				"\"spell0.png\",\"group\":\"spell\",\"x\":96,\"y\":48,\"w\":48,\"h\":48},\"cooldown\":[90],\"id\":11," +
				"\"key\":\"SummonerSmite\",\"modes\":[\"CLASSIC\",\"TUTORIAL\"]}}}";
		SummonerSpell smite = objectMapper.readValue(responseBody, SummonerSpellsResponse.class)
				.getSummonerSpells().get("SummonerSmite");
		smite = summonerSpellsRepository.save(smite);

		String fileExt = FilenameUtils.getExtension(smite.getImage().getFull());

		mockMvc.perform(get("/img/summoner-spells/{img}", smite.getId() + "." + fileExt))
				.andExpect(status().isOk())
				.andExpect(content().contentType("image/" + fileExt));
	}

	@Test
	public void itemImg() throws Exception {
		String responseBody = "{\"type\":\"item\",\"version\":\"6.8.1\",\"data\":{\"3075\":{\"id\":3075,\"name\":" +
				"\"Thornmail\",\"description\":\"<stats>+100 Armor </stats><br><br><unique>UNIQUE Passive:</unique> " +
				"Upon being hit by a basic attack, reflects magic damage back to the attacker equal to 25% of your " +
				"bonus Armor plus 15% of the incoming damage.<br><br><rules>(Bonus Armor is Armor from items, buffs, " +
				"runes and masteries.)</rules><br><rules>(Reflect damage is calculated based on damage taken before " +
				"being reduced by Armor.)</rules>\",\"plaintext\":\"Returns damage taken from basic attacks as magic " +
				"damage\",\"from\":[\"1029\",\"1031\"],\"maps\":{\"1\":\"false\",\"8\":\"true\",\"10\":\"false\"," +
				"\"11\":\"true\",\"12\":\"true\",\"14\":\"false\"},\"image\":{\"full\":\"3075.png\",\"sprite\":" +
				"\"item0.png\",\"group\":\"item\",\"x\":432,\"y\":384,\"w\":48,\"h\":48},\"gold\":{\"base\":1250," +
				"\"total\":2350,\"sell\":1645,\"purchasable\":\"true\"}}}}";
		Item thornmail = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3075");
		thornmail = itemsRepository.save(thornmail);

		String fileExt = FilenameUtils.getExtension(thornmail.getImage().getFull());

		mockMvc.perform(get("/img/items/{img}", thornmail.getId() + "." + fileExt))
				.andExpect(status().isOk())
				.andExpect(content().contentType("image/" + fileExt));
	}

	@Test
	public void championImg() throws Exception {
		String responseBody = "{\"type\":\"champion\",\"version\":\"6.8.1\",\"data\":{\"Shen\":{\"id\":98,\"key\":" +
				"\"Shen\",\"name\":\"Shen\",\"title\":\"the Eye of Twilight\",\"image\":{\"full\":\"Shen.png\"," +
				"\"sprite\":\"champion2.png\",\"group\":\"champion\",\"x\":192,\"y\":96,\"w\":48,\"h\":48},\"tags\":" +
				"[\"Tank\",\"Melee\"],\"partype\":\"Energy\"}}}";
		Champion shen = objectMapper.readValue(responseBody, ChampionsResponse.class).getChampions().get("Shen");
		shen = championsRepository.save(shen);

		String fileExt = FilenameUtils.getExtension(shen.getImage().getFull());

		mockMvc.perform(get("/img/champions/{img}", shen.getId() + "." + fileExt))
				.andExpect(status().isOk())
				.andExpect(content().contentType("image/" + fileExt));
	}

	@Test
	public void mapImg() throws Exception {
		String responseBody = "{\"type\":\"map\",\"version\":\"6.8.1\",\"data\":{\"12\":{\"mapName\":" +
				"\"ProvingGroundsNew\",\"mapId\":12,\"image\":{\"full\":\"map12.png\",\"sprite\":\"map0.png\"," +
				"\"group\":\"map\",\"x\":48,\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap summonersRift = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("12");
		summonersRift = mapsRepository.save(summonersRift);

		String fileExt = FilenameUtils.getExtension(summonersRift.getImage().getFull());

		mockMvc.perform(get("/img/maps/map{img}", summonersRift.getMapId() + "." + fileExt))
				.andExpect(status().isOk())
				.andExpect(content().contentType("image/" + fileExt));
	}

}