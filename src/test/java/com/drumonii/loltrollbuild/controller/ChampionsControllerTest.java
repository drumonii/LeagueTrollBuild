package com.drumonii.loltrollbuild.controller;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.SummonerSpell.GameMode;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Repeat;

import java.util.List;

import static com.drumonii.loltrollbuild.model.SummonerSpell.GameMode.ARAM;
import static com.drumonii.loltrollbuild.model.SummonerSpell.GameMode.CLASSIC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ChampionsControllerTest extends BaseSpringTestRunner {

	private static final int SUMMONERS_RIFT = 11;

	@Autowired
	private ChampionsController championsController;

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	private MapsRepository mapsRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@After
	public void after() {
		itemsRepository.deleteAll();
		summonerSpellsRepository.deleteAll();
		championsRepository.deleteAll();
		mapsRepository.deleteAll();
	}

	@Test
	public void champions() throws Exception {
		mockMvc.perform(get("/champions"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("champions"))
				.andExpect(view().name("champions/champions"));
	}

	@Test
	public void champion() throws Exception {
		mockMvc.perform(get("/champions/{id}", 0))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/champions"));

		String responseBody = "{\"type\":\"champion\",\"version\":\"5.22.3\",\"data\":{\"TestTest\":{\"id\":10001,\"" +
				"key\":\"TestTest\",\"name\":\"Test'Test\",\"title\":\"much TestTest Champion\",\"image\":{\"full\":" +
				"\"Test.png\",\"sprite\":\"champion0.png\",\"group\":\"champion\",\"x\":336,\"y\":0,\"w\":48," +
				"\"h\":48},\"tags\":[\"Testing1\",\"Testing2\"],\"partype\":\"TestParType\"}}}";
		ChampionsResponse championsResponse = objectMapper.readValue(responseBody, ChampionsResponse.class);
		Champion unmarshalledChampion = championsResponse.getChampions().get("TestTest");
		championsRepository.save(unmarshalledChampion);

		mockMvc.perform(get("/champions/{id}", 10001))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("champion"))
				.andExpect(model().attribute("champion", is(unmarshalledChampion)))
				.andExpect(view().name("champions/champion"));

		mockMvc.perform(get("/champions/{name}", "Test'Test"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("champion"))
				.andExpect(model().attribute("champion", is(unmarshalledChampion)))
				.andExpect(view().name("champions/champion"));

		mockMvc.perform(get("/champions/{name}", "tEstTeST"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("champion"))
				.andExpect(model().attribute("champion", is(unmarshalledChampion)))
				.andExpect(view().name("champions/champion"));
	}

	@Repeat(50)
	@Test
	public void trollBuild() throws Exception {
		// Items
		String responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"3047\":{\"id\":3047,\"name\":" +
				"\"Ninja Tabi\",\"description\":\"<stats>+30 Armor</stats><br><br><unique>UNIQUE Passive:</unique> " +
				"Blocks 10% of the damage from basic attacks.<br><unique>UNIQUE Passive - Enhanced Movement:</unique>" +
				" +45 Movement Speed\",\"plaintext\":\"Enhances Movement Speed and reduces incoming basic attack " +
				"damage\",\"from\":[\"1001\",\"1029\"],\"into\":[\"1316\",\"1318\",\"1315\",\"1317\",\"1319\"]," +
				"\"maps\":{\"1\":false,\"8\":true,\"10\":true,\"11\":true,\"12\":true,\"14\":false},\"image\":" +
				"{\"full\":\"3047.png\",\"sprite\":\"item0.png\",\"group\":\"item\",\"x\":192,\"y\":336,\"w\":48," +
				"\"h\":48},\"gold\":{\"base\":500,\"total\":1100,\"sell\":770,\"purchasable\":true}}}}";
		Item ninjaTabi = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3047");
		itemsRepository.save(ninjaTabi); // 1

		responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"3072\":{\"id\":3072,\"name\":" +
				"\"The Bloodthirster\",\"description\":\"<stats>+80 Attack Damage</stats><br><br><passive>UNIQUE " +
				"Passive:</passive> +20% Life Steal<br><passive>UNIQUE Passive:</passive> Your basic attacks can now " +
				"overheal you. Excess life is stored as a shield that can block 50-350 damage, based on champion " +
				"level.<br><br>This shield decays slowly if you haven't dealt or taken damage in the last 25 seconds." +
				"\",\"plaintext\":\"Grants Attack Damage, Life Steal and Life Steal now overheals\",\"from\":" +
				"[\"1053\",\"1038\"],\"maps\":{\"1\":false,\"8\":false,\"10\":false,\"11\":true,\"12\":true,\"14\":" +
				"false},\"image\":{\"full\":\"3072.png\",\"sprite\":\"item0.png\",\"group\":\"item\",\"x\":288," +
				"\"y\":384,\"w\":48,\"h\":48},\"gold\":{\"base\":1150,\"total\":3500,\"sell\":2450,\"purchasable\":" +
				"true}}}}";
		Item bloodThirster = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3072");
		itemsRepository.save(bloodThirster); // 2

		responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"3143\":{\"id\":3143,\"name\":" +
				"\"Randuin's Omen\",\"description\":\"<stats>+450 Health<br>+60 Armor<br>-10% Damage taken from " +
				"Critical Strikes</stats><br><br><unique>UNIQUE Passive - Cold Steel:</unique> When hit by basic " +
				"attacks, reduces the attacker's Attack Speed by 15%.<br><active>UNIQUE Active:</active> Slows the " +
				"Movement Speed of nearby enemy units by 35% for 4 seconds (60 second cooldown).\",\"plaintext\":" +
				"\"Greatly increases defenses, activate to slow nearby enemies\",\"from\":[\"3082\",\"1011\"]," +
				"\"maps\":{\"1\":false,\"8\":true,\"10\":true,\"11\":true,\"12\":true,\"14\":false},\"image\":" +
				"{\"full\":\"3143.png\",\"sprite\":\"item1.png\",\"group\":\"item\",\"x\":48,\"y\":144,\"w\":48," +
				"\"h\":48},\"gold\":{\"base\":900,\"total\":3000,\"sell\":2100,\"purchasable\":true}}}}";
		Item randuinsOmen = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3143");
		itemsRepository.save(randuinsOmen); // 3

		responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"3022\":{\"id\":3022,\"name\":" +
				"\"Frozen Mallet\",\"description\":\"<stats>+650 Health<br>+40 Attack Damage</stats><br><br><unique>" +
				"UNIQUE Passive - Icy:</unique> Basic attacks slow the target's Movement Speed for 1.5 seconds on hit" +
				" (40% slow for melee attacks, 30% slow for ranged attacks).\",\"plaintext\":\"Basic attacks slow " +
				"enemies\",\"from\":[\"3052\",\"1037\",\"1028\"],\"maps\":{\"1\":false,\"8\":true,\"10\":true,\"11\":" +
				"true,\"12\":true,\"14\":false},\"image\":{\"full\":\"3022.png\",\"sprite\":\"item0.png\",\"group\":" +
				"\"item\",\"x\":384,\"y\":240,\"w\":48,\"h\":48},\"gold\":{\"base\":625,\"total\":3100,\"sell\":2170," +
				"\"purchasable\":true}}}}";
		Item frozenMallet = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3022");
		itemsRepository.save(frozenMallet); // 4

		responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"3142\":{\"id\":3142,\"name\":" +
				"\"Youmuu's Ghostblade\",\"description\":\"<stats>+65 Attack Damage<br>+10% Cooldown Reduction" +
				"</stats><br><br><unique>UNIQUE Passive:</unique> +20 Armor Penetration<br><active>UNIQUE Active:" +
				"</active> Grants +20% Movement Speed and +40% Attack Speed for 6 seconds (45 second cooldown).<br>" +
				"<br><rules>(Armor Penetration: Physical damage is increased by ignoring an amount of the target's " +
				"Armor equal to Armor Penetration.)</rules>\",\"plaintext\":\"Activate to greatly increase Movement " +
				"Speed and Attack Speed\",\"from\":[\"3133\",\"3134\"],\"maps\":{\"1\":false,\"8\":true,\"10\":true," +
				"\"11\":true,\"12\":true,\"14\":false},\"image\":{\"full\":\"3142.png\",\"sprite\":\"item1.png\"," +
				"\"group\":\"item\",\"x\":0,\"y\":144,\"w\":48,\"h\":48},\"gold\":{\"base\":1000,\"total\":3200," +
				"\"sell\":2240,\"purchasable\":true}}}}";
		Item ghostblade = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3142");
		itemsRepository.save(ghostblade); // 5

		responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"3027\":{\"id\":3027,\"name\":" +
				"\"Rod of Ages\",\"description\":\"<stats>+300 Health<br><mana>+400 Mana</mana><br>+80 Ability Power" +
				"</stats><br><br><passive>Passive:</passive> Grants +20 Health, +40 Mana, and +4 Ability Power per " +
				"stack (max +200 Health, +400 Mana, and +40 Ability Power). Grants 1 stack per minute (max 10 stacks)" +
				".<br><unique>UNIQUE Passive - Valor's Reward:</unique> Upon leveling up, restores 150 Health and 200" +
				" Mana over 8 seconds.\",\"plaintext\":\"Greatly increases Health, Mana, and Ability Power\"," +
				"\"from\":[\"3010\",\"1026\"],\"maps\":{\"1\":false,\"8\":false,\"10\":true,\"11\":true,\"12\":true," +
				"\"14\":false},\"image\":{\"full\":\"3027.png\",\"sprite\":\"item0.png\",\"group\":\"item\",\"x\":" +
				"144,\"y\":288,\"w\":48,\"h\":48},\"gold\":{\"base\":950,\"total\":3000,\"sell\":2100,\"purchasable\":" +
				"true}}}}";
		Item rodOfAges = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3027");
		itemsRepository.save(rodOfAges); // 6

		// Summoner Spells
		responseBody = "{\"type\":\"summoner\",\"version\":\"5.22.3\",\"data\":{\"SummonerHeal\":{\"name\":\"Heal\"," +
				"\"description\":\"Restores 90-345 Health (depending on champion level) and grants 30% Movement Speed" +
				" for 1 second to you and target allied champion. This healing is halved for units recently affected " +
				"by Summoner Heal.\",\"image\":{\"full\":\"SummonerHeal.png\",\"sprite\":\"spell0.png\",\"group\":" +
				"\"spell\",\"x\":336,\"y\":0,\"w\":48,\"h\":48},\"cooldown\":[240],\"summonerLevel\":1,\"id\":7," +
				"\"key\":\"SummonerHeal\",\"modes\":[\"CLASSIC\",\"ODIN\",\"TUTORIAL\",\"ARAM\",\"ASCENSION\"]}}}";
		SummonerSpell heal = objectMapper.readValue(responseBody, SummonerSpellsResponse.class)
				.getSummonerSpells().get("SummonerHeal");
		summonerSpellsRepository.save(heal); // 1

		responseBody = "{\"type\":\"summoner\",\"version\":\"5.22.3\",\"data\":{\"SummonerDot\":{\"name\":\"Ignite\"," +
				"\"description\":\"Ignites target enemy champion, dealing 70-410 true damage (depending on champion " +
				"level) over 5 seconds, grants you vision of the target, and reduces healing effects on them for the " +
				"duration.\",\"image\":{\"full\":\"SummonerDot.png\",\"sprite\":\"spell0.png\",\"group\":\"spell\"," +
				"\"x\":144,\"y\":0,\"w\":48,\"h\":48},\"cooldown\":[180],\"summonerLevel\":10,\"id\":14,\"key\":" +
				"\"SummonerDot\",\"modes\":[\"CLASSIC\",\"ODIN\",\"TUTORIAL\",\"ARAM\",\"ASCENSION\"]}}}";
		SummonerSpell ignite = objectMapper.readValue(responseBody, SummonerSpellsResponse.class)
				.getSummonerSpells().get("SummonerDot");
		summonerSpellsRepository.save(ignite); // 2

		// Trinket
		responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"3341\":{\"id\":3341,\"name\":" +
				"\"Sweeping Lens (Trinket)\",\"group\":\"RelicBase\",\"description\":\"<groupLimit>Limited to 1 " +
				"Trinket.</groupLimit><br><br><active>Active:</active> Scans an area for 6 seconds, warning against " +
				"hidden hostile units and revealing and disabling invisible traps and wards. (90 to 60 second " +
				"cooldown)<br><br>Cast range and sweep radius gradually improve with level.<br><br><rules>(Switching " +
				"to a <font color='#BBFFFF'>Totem</font>-type trinket will disable <font color='#BBFFFF'>Trinket" +
				"</font> use for 120 seconds).</rules>\",\"plaintext\":\"Detects and disables nearby invisible wards " +
				"and traps\",\"maps\":{\"1\":false,\"8\":false,\"10\":false,\"11\":true,\"12\":true,\"14\":false}," +
				"\"image\":{\"full\":\"3341.png\",\"sprite\":\"item2.png\",\"group\":\"item\",\"x\":336,\"y\":0," +
				"\"w\":48,\"h\":48},\"gold\":{\"base\":0,\"total\":0,\"sell\":0,\"purchasable\":true}}}}";
		Item scryingOrb = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3341");
		itemsRepository.save(scryingOrb); // 1

		// Map
		responseBody = "{\"type\":\"map\",\"version\":\"6.3.1\",\"data\":{\"11\":{\"mapName\":\"SummonersRiftNew\"," +
				"\"mapId\":11,\"image\":{\"full\":\"map11.png\",\"sprite\":\"map0.png\",\"group\":\"map\",\"x\":144," +
				"\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap summonersRift = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("11");
		mapsRepository.save(summonersRift);

		responseBody = "{\"type\":\"champion\",\"version\":\"5.22.3\",\"data\":{\"Annie\":{\"id\":1,\"key\":" +
				"\"Annie\",\"name\":\"Annie\",\"title\":\"the Dark Child\",\"image\":{\"full\":\"Annie.png\"," +
				"\"sprite\":\"champion0.png\",\"group\":\"champion\",\"x\":288,\"y\":0,\"w\":48,\"h\":48},\"tags\":" +
				"[\"Mage\"],\"partype\":\"Mana\"}}}";
		Champion annie = objectMapper.readValue(responseBody, ChampionsResponse.class).getChampions().get("Annie");
		championsRepository.save(annie);

		mockMvc.perform(get("/champions/{id}/troll-build?mapId={mapId}", annie.getId(), SUMMONERS_RIFT))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.items").exists())
				.andExpect(jsonPath("$.items", hasSize(6)))
				.andExpect(jsonPath("$.summoner-spells").exists())
				.andExpect(jsonPath("$.summoner-spells", hasSize(2)))
				.andExpect(jsonPath("$.trinket", hasSize(1)));
	}

	@Test
	public void eligibleMaps() throws Exception {
		String responseBody = "{\"type\":\"map\",\"version\":\"6.3.1\",\"data\":{\"8\":{\"mapName\":\"CrystalScar\"," +
				"\"mapId\":8,\"image\":{\"full\":\"map8.png\",\"sprite\":\"map0.png\",\"group\":\"map\",\"x\":192," +
				"\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap crystalScar = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("8");
		mapsRepository.save(crystalScar);

		responseBody = "{\"type\":\"map\",\"version\":\"6.3.1\",\"data\":{\"11\":{\"mapName\":\"SummonersRiftNew\"," +
				"\"mapId\":11,\"image\":{\"full\":\"map11.png\",\"sprite\":\"map0.png\",\"group\":\"map\",\"x\":144," +
				"\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap summonersRift = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("11");
		mapsRepository.save(summonersRift);

		responseBody = "{\"type\":\"map\",\"version\":\"6.3.1\",\"data\":{\"12\":{\"mapName\":\"ProvingGroundsNew\"," +
				"\"mapId\":12,\"image\":{\"full\":\"map12.png\",\"sprite\":\"map0.png\",\"group\":\"map\",\"x\":48," +
				"\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap provingGrounds = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("12");
		mapsRepository.save(provingGrounds);

		responseBody = "{\"type\":\"map\",\"version\":\"6.3.1\",\"data\":{\"10\":{\"mapName\":" +
				"\"NewTwistedTreeline\",\"mapId\":10,\"image\":{\"full\":\"map10.png\",\"sprite\":\"map0.png\"," +
				"\"group\":\"map\",\"x\":0,\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap twistedTreeline = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("10");
		mapsRepository.save(twistedTreeline);

		List<GameMap> eligibleMaps = championsController.eligibleMaps();
		assertThat(eligibleMaps).isNotEmpty();
		assertThat(eligibleMaps).extracting("mapId")
								.containsExactly(provingGrounds.getMapId(), summonersRift.getMapId(),
										twistedTreeline.getMapId());
	}

	@Test
	public void getModeFromMap() throws Exception {
		String responseBody = "{\"type\":\"map\",\"version\":\"6.3.1\",\"data\":{\"8\":{\"mapName\":\"CrystalScar\"," +
				"\"mapId\":8,\"image\":{\"full\":\"map8.png\",\"sprite\":\"map0.png\",\"group\":\"map\",\"x\":192," +
				"\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap crystalScar = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("8");
		mapsRepository.save(crystalScar);

		GameMode gameMode = championsController.getModeFromMap(String.valueOf(crystalScar.getMapId()));
		assertThat(gameMode).isEqualTo(CLASSIC);

		responseBody = "{\"type\":\"map\",\"version\":\"6.3.1\",\"data\":{\"11\":{\"mapName\":\"SummonersRiftNew\"," +
				"\"mapId\":11,\"image\":{\"full\":\"map11.png\",\"sprite\":\"map0.png\",\"group\":\"map\",\"x\":144," +
				"\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap summonersRift = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("11");
		mapsRepository.save(summonersRift);

		gameMode = championsController.getModeFromMap(String.valueOf(summonersRift.getMapId()));
		assertThat(gameMode).isEqualTo(CLASSIC);

		responseBody = "{\"type\":\"map\",\"version\":\"6.3.1\",\"data\":{\"12\":{\"mapName\":\"ProvingGroundsNew\"," +
				"\"mapId\":12,\"image\":{\"full\":\"map12.png\",\"sprite\":\"map0.png\",\"group\":\"map\",\"x\":48," +
				"\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap provingGrounds = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("12");
		mapsRepository.save(provingGrounds);

		gameMode = championsController.getModeFromMap(String.valueOf(provingGrounds.getMapId()));
		assertThat(gameMode).isEqualTo(ARAM);
	}

}