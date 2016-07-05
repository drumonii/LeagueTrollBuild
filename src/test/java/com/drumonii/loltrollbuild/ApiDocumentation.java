package com.drumonii.loltrollbuild;

import com.drumonii.loltrollbuild.model.*;
import com.drumonii.loltrollbuild.repository.*;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiDocumentation extends BaseSpringTestRunner {

	@Value("${spring.data.rest.base-path}")
	private String apiPath;

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

	@Autowired
	private VersionsRepository versionsRepository;

	@Before
	public void before() {
		super.before();
		summonerSpellsRepository.deleteAll();
		itemsRepository.deleteAll();
		championsRepository.deleteAll();
		mapsRepository.deleteAll();
		versionsRepository.deleteAll();
	}

	@Test
	public void index() throws Exception {
		mockMvc.perform(get(apiPath))
				.andExpect(status().isOk())
				.andDo(document("index", links(
						linkWithRel("summonerSpells").description("<<resources-summoner-spells, Summoner Spells resource>>"),
						linkWithRel("items").description("<<resources-items, Items resource>>"),
						linkWithRel("champions").description("<<resources-champions, Champions resource>>"),
						linkWithRel("maps").description("<<resources-maps, Maps resource>>"),
						linkWithRel("versions").description("<<resources-versions, Versions resource>>"),
						linkWithRel("profile").description("Profile resource"))));
	}

	/*
	 * Summoner Spells doc
	 */

	@Test
	public void getSummonerSpells() throws Exception {
		String responseBody = "{\"type\":\"summoner\",\"version\":\"6.8.1\",\"data\":{\"SummonerExhaust\":{\"name\":" +
				"\"Exhaust\",\"description\":\"Exhausts target enemy champion, reducing their Movement Speed and " +
				"Attack Speed by 30%, their Armor and Magic Resist by 10, and their damage dealt by 40% for 2.5 " +
				"seconds.\",\"image\":{\"full\":\"SummonerExhaust.png\",\"sprite\":\"spell0.png\",\"group\":" +
				"\"spell\",\"x\":192,\"y\":0,\"w\":48,\"h\":48},\"cooldown\":[210],\"id\":3,\"key\":" +
				"\"SummonerExhaust\",\"modes\":[\"CLASSIC\",\"ODIN\",\"TUTORIAL\",\"ARAM\",\"ASCENSION\"]}}}";
		summonerSpellsRepository.save(objectMapper.readValue(responseBody, SummonerSpellsResponse.class)
				.getSummonerSpells().get("SummonerExhaust"));

		mockMvc.perform(get(apiPath + "/summoner-spells"))
				.andExpect(status().isOk())
				.andDo(document("getSummonerSpells", responseFields(
						fieldWithPath("_embedded.summonerSpells")
								.description("An array of Summoner Spells"),
						fieldWithPath("_embedded.summonerSpells[*].name")
								.description("The name of the Summoner Spell"),
						fieldWithPath("_embedded.summonerSpells[*].description")
								.description("The description of the Summoner Spell"),
						fieldWithPath("_embedded.summonerSpells[*].image")
								.description("The image of the Summoner Spell"),
						fieldWithPath("_embedded.summonerSpells[*].cooldown")
								.description("An array of cooldown values of the Summoner Spell"),
						fieldWithPath("_embedded.summonerSpells[*].modes")
								.description("An array of game modes eligible with the Summoner Spell"),
						fieldWithPath("_links")
								.description("Links to resources related to Summoner Spells"),
						fieldWithPath("page")
								.description("Current Page settings of the pagination"))));

		responseBody = "{\"type\":\"summoner\",\"version\":\"6.8.1\",\"data\":{\"SummonerPoroThrow\":{\"name\":" +
				"\"Poro Toss\",\"description\":\"Toss a Poro at your enemies. If it hits, you can quickly travel to " +
				"your target as a follow up.\",\"image\":{\"full\":\"SummonerPoroThrow.png\",\"sprite\":" +
				"\"spell0.png\",\"group\":\"spell\",\"x\":48,\"y\":48,\"w\":48,\"h\":48},\"cooldown\":[20],\"id\":31," +
				"\"key\":\"SummonerPoroThrow\",\"modes\":[\"KINGPORO\"]}}}";
		summonerSpellsRepository.save(objectMapper.readValue(responseBody, SummonerSpellsResponse.class)
				.getSummonerSpells().get("SummonerPoroThrow"));

		mockMvc.perform(get(apiPath + "/summoner-spells?name={name}", "poro"))
				.andExpect(status().isOk())
				.andDo(document("summonerSpellsFindBy", responseFields(
						fieldWithPath("_embedded.summonerSpells")
								.description("An array of Summoner Spells"),
						fieldWithPath("_embedded.summonerSpells[*].name")
								.description("The name of the Summoner Spell"),
						fieldWithPath("_embedded.summonerSpells[*].description")
								.description("The description of the Summoner Spell"),
						fieldWithPath("_embedded.summonerSpells[*].image")
								.description("The image of the Summoner Spell"),
						fieldWithPath("_embedded.summonerSpells[*].cooldown")
								.description("An array of cooldown values of the Summoner Spell"),
						fieldWithPath("_embedded.summonerSpells[*].modes")
								.description("An array of game modes eligible with the Summoner Spell"),
						fieldWithPath("_links")
								.description("Link to the self Summoner Spell"),
						fieldWithPath("page")
								.description("Current Page settings of the pagination"))));
	}

	@Test
	public void getSummonerSpell() throws Exception {
		String responseBody = "{\"type\":\"summoner\",\"version\":\"6.8.1\",\"data\":{\"SummonerTeleport\":{\"name\":" +
				"\"Teleport\",\"description\":\"After channeling for 3.5 seconds, teleports your champion to target " +
				"allied structure, minion, or ward.\",\"image\":{\"full\":\"SummonerTeleport.png\",\"sprite\":" +
				"\"spell0.png\",\"group\":\"spell\",\"x\":144,\"y\":48,\"w\":48,\"h\":48},\"cooldown\":[300],\"id\":" +
				"12,\"key\":\"SummonerTeleport\",\"modes\":[\"CLASSIC\",\"TUTORIAL\"]}}}";
		SummonerSpell teleport = summonerSpellsRepository.save(objectMapper.readValue(responseBody,
				SummonerSpellsResponse.class).getSummonerSpells().get("SummonerTeleport"));

		mockMvc.perform(get(apiPath + "/summoner-spells/{id}", teleport.getId()))
				.andExpect(status().isOk())
				.andDo(document("getSummonerSpell", responseFields(
						fieldWithPath("name")
								.description("The name of the Summoner Spell"),
						fieldWithPath("description")
								.description("The description of the Summoner Spell"),
						fieldWithPath("image")
								.description("The image of the Summoner Spell"),
						fieldWithPath("cooldown")
								.description("An array of cooldown values of the Summoner Spell"),
						fieldWithPath("modes")
								.description("An array of game modes eligible with the Summoner Spell"),
						fieldWithPath("_links")
								.description("Link to the self Summoner Spell"))));

	}

	@Test
	public void summonerSpellsForTrollBuild() throws Exception {
		String responseBody = "{\"type\":\"summoner\",\"version\":\"6.8.1\",\"data\":{\"SummonerSmite\":{\"name\":" +
				"\"Smite\",\"description\":\"Deals 390-1000 true damage (depending on champion level) to target epic " +
				"or large monster or enemy minion.\",\"image\":{\"full\":\"SummonerSmite.png\",\"sprite\":" +
				"\"spell0.png\",\"group\":\"spell\",\"x\":96,\"y\":48,\"w\":48,\"h\":48},\"cooldown\":[90],\"id\":11," +
				"\"key\":\"SummonerSmite\",\"modes\":[\"CLASSIC\",\"TUTORIAL\"]}}}";
		summonerSpellsRepository.save(objectMapper.readValue(responseBody, SummonerSpellsResponse.class)
				.getSummonerSpells().get("SummonerSmite"));

		mockMvc.perform(get(apiPath + "/summoner-spells/for-troll-build?mode={mode}", "CLASSIC"))
				.andExpect(status().isOk())
				.andDo(document("summonerSpellsForTrollBuild", responseFields(
						fieldWithPath("_embedded.summonerSpells")
								.description("An array of Summoner Spells"),
						fieldWithPath("_embedded.summonerSpells[*].name")
								.description("The name of the Summoner Spell"),
						fieldWithPath("_embedded.summonerSpells[*].description")
								.description("The description of the Summoner Spell"),
						fieldWithPath("_embedded.summonerSpells[*].image")
								.description("The image of the Summoner Spell"),
						fieldWithPath("_embedded.summonerSpells[*].cooldown")
								.description("An array of cooldown values of the Summoner Spell"),
						fieldWithPath("_embedded.summonerSpells[*].modes")
								.description("An array of game modes eligible with the Summoner Spell"),
						fieldWithPath("_links")
								.description("Links to resources related to Summoner Spells"))));
	}

	/*
	 * Items doc
	 */

	@Test
	public void getItems() throws Exception {
		String responseBody = "{\"type\":\"item\",\"version\":\"6.8.1\",\"data\":{\"3512\":{\"id\":3512,\"name\":" +
				"\"Zz'RotPortal\",\"description\":\"<stats>+55 Armor<br>+55 Magic Resist<br>+125% Base Health Regen " +
				"<br></stats><br><unique>UNIQUE Passive - Point Runner:</unique> Builds up to +20% Movement Speed " +
				"over 2 seconds while near turrets, fallen turrets and Void Gates.<br><active>UNIQUE Active:</active>" +
				" Spawns a <a href='VoidGate'>Void Gate</a> for 150 seconds (150 second cooldown).<br><br>Every 4 " +
				"seconds the gate makes a <a href='Voidspawn'>Voidspawn</a>. The first and every fourth Voidspawn " +
				"gains 15% of maximum Health as damage.\",\"from\":[\"2053\",\"1057\"],\"maps\":{\"1\":false,\"8\":" +
				"false,\"10\":false,\"11\":true,\"12\":false,\"14\":false},\"image\":{\"full\":\"3512.png\"," +
				"\"sprite\":\"item2.png\",\"group\":\"item\",\"x\":96,\"y\":192,\"w\":48,\"h\":48},\"gold\":" +
				"{\"base\":780,\"total\":2700,\"sell\":1890,\"purchasable\":true}}}}";
		itemsRepository.save(objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3512"));

		mockMvc.perform(get(apiPath + "/items"))
				.andExpect(status().isOk())
				.andDo(document("getItems", responseFields(
						fieldWithPath("_embedded.items")
								.description("An array of Items"),
						fieldWithPath("_embedded.items[*].name")
								.description("The name of the Item"),
						fieldWithPath("_embedded.items[*].group")
								.type(JsonFieldType.STRING)
								.description("The group of the Item"),
						fieldWithPath("_embedded.items[*].consumed")
								.type(JsonFieldType.BOOLEAN)
								.description("Whether the Item is consumable"),
						fieldWithPath("_embedded.items[*].description")
								.description("The group of the Item"),
						fieldWithPath("_embedded.items[*].from")
								.description("An array of Item IDs that the Item is built from as part of the Item's recipe"),
						fieldWithPath("_embedded.items[*].into")
								.description("An array of Item IDs that the Item builds into as part of the Item's recipe"),
						fieldWithPath("_embedded.items[*].requiredChampion")
								.type(JsonFieldType.STRING)
								.description("The required champion of the Item"),
						fieldWithPath("_embedded.items[*].maps")
								.description("A map of Map IDs keys and boolean values whether the Item can be purchased in the Map"),
						fieldWithPath("_embedded.items[*].image")
								.description("The image of the Item"),
						fieldWithPath("_embedded.items[*].gold")
								.description("The gold of the Item"),
						fieldWithPath("_links")
								.description("Links to resources related to Items"),
						fieldWithPath("page")
								.description("Current Page settings of the pagination"))));

		responseBody = "{\"type\":\"item\",\"version\":\"6.8.1\",\"data\":{\"3069\":{\"id\":3069,\"name\":" +
				"\"Talisman of Ascension\",\"group\":\"GoldBase\",\"description\":\"<stats>+100% Base Health Regen " +
				"<br><mana>+100% Base Mana Regen <br></mana>+10% Cooldown Reduction</stats><br><br><unique>UNIQUE " +
				"Passive - Favor:</unique> Being near a minion's death without dealing the killing blow grants 6 Gold" +
				" and 10 Health.<br><active>UNIQUE Active:</active> Grants nearby allies +40% Movement Speed for 3" +
				" seconds (60 second cooldown).<br><br><groupLimit>Limited to 1 Gold Income Item.</groupLimit><br>" +
				"<br><rules><font color='#447777'>''Praise the sun.'' - Historian Shurelya, 22 September, 25 CLE" +
				"</font></rules>\",\"from\":[\"3096\",\"3114\"],\"maps\":{\"1\":\"false\",\"8\":\"true\",\"10\":" +
				"\"true\",\"11\":\"true\",\"12\":\"true\",\"14\":\"false\"},\"image\":{\"full\":\"3069.png\"," +
				"\"sprite\":\"item0.png\",\"group\":\"item\",\"x\":144,\"y\":384,\"w\":48,\"h\":48},\"gold\":" +
				"{\"base\":800,\"total\":2200,\"sell\":880,\"purchasable\":\"true\"}}}}";
		itemsRepository.save(objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3069"));

		mockMvc.perform(get(apiPath + "/items?name={name}", "talisman"))
				.andExpect(status().isOk())
				.andDo(document("itemsFindBy", responseFields(
						fieldWithPath("_embedded.items")
								.description("An array of Items"),
						fieldWithPath("_embedded.items[*].name")
								.description("The name of the Item"),
						fieldWithPath("_embedded.items[*].group")
								.type(JsonFieldType.STRING)
								.description("The group of the Item"),
						fieldWithPath("_embedded.items[*].consumed")
								.type(JsonFieldType.BOOLEAN)
								.description("Whether the Item is consumable"),
						fieldWithPath("_embedded.items[*].description")
								.description("The group of the Item"),
						fieldWithPath("_embedded.items[*].from")
								.description("An array of Item IDs that the Item is built from as part of the Item's recipe"),
						fieldWithPath("_embedded.items[*].into")
								.description("An array of Item IDs that the Item builds into as part of the Item's recipe"),
						fieldWithPath("_embedded.items[*].requiredChampion")
								.type(JsonFieldType.STRING)
								.description("The required champion of the Item"),
						fieldWithPath("_embedded.items[*].maps")
								.description("A map of Map IDs keys and boolean values whether the Item can be purchased in the Map"),
						fieldWithPath("_embedded.items[*].image")
								.description("The image of the Item"),
						fieldWithPath("_embedded.items[*].gold")
								.description("The gold of the Item"),
						fieldWithPath("_links")
								.description("Links to resources related to Items"),
						fieldWithPath("page")
								.description("Current Page settings of the pagination"))));
	}

	@Test
	public void getItem() throws Exception {
		String responseBody = "{\"type\":\"item\",\"version\":\"6.8.1\",\"data\":{\"3083\":{\"id\":3083,\"name\":" +
				"\"Warmog's Armor\",\"description\":\"<stats>+850 Health<br>+200% Base Health Regen </stats><br><br>" +
				"<unique>UNIQUE Passive:</unique> Grants <unlockedPassive>Warmog's Heart</unlockedPassive> if you " +
				"have at least 3000 maximum Health.<br><br><unlockedPassive>Warmog's Heart:</unlockedPassive> " +
				"Restores 15% of maximum Health every 5 seconds if damage hasn't been taken within 8 seconds. \"," +
				"\"from\":[\"3801\",\"1011\",\"3801\"],\"maps\":{\"1\":\"false\",\"8\":\"false\",\"10\":\"false\"," +
				"\"11\":\"true\",\"12\":\"true\",\"14\":\"false\"},\"image\":{\"full\":\"3083.png\",\"sprite\":" +
				"\"item0.png\",\"group\":\"item\",\"x\":144,\"y\":432,\"w\":48,\"h\":48},\"gold\":{\"base\":550," +
				"\"total\":2850,\"sell\":1995,\"purchasable\":\"true\"}}}}";
		Item warmogs = itemsRepository.save(objectMapper.readValue(responseBody, ItemsResponse.class).getItems()
				.get("3083"));

		mockMvc.perform(get(apiPath + "/items/{id}", warmogs.getId()))
				.andExpect(status().isOk())
				.andDo(document("getItem", responseFields(
						fieldWithPath("name")
								.description("The name of the Item"),
						fieldWithPath("group")
								.type(JsonFieldType.STRING)
								.description("The group of the Item"),
						fieldWithPath("consumed")
								.type(JsonFieldType.BOOLEAN)
								.description("Whether the Item is consumable"),
						fieldWithPath("description")
								.description("The group of the Item"),
						fieldWithPath("from")
								.description("An array of Item IDs that the Item is built from as part of the Item's recipe"),
						fieldWithPath("into")
								.description("An array of Item IDs that the Item builds into as part of the Item's recipe"),
						fieldWithPath("requiredChampion")
								.type(JsonFieldType.STRING)
								.description("The required champion of the Item"),
						fieldWithPath("maps")
								.description("A map of Map IDs keys and boolean values whether the Item can be purchased in the Map"),
						fieldWithPath("image")
								.description("The image of the Item"),
						fieldWithPath("gold")
								.description("The gold of the Item"),
						fieldWithPath("_links")
								.description("Links to resources related to Items"))));

	}

	@Test
	public void bootsItems() throws Exception {
		String responseBody = "{\"type\":\"item\",\"version\":\"6.8.1\",\"data\":{\"3158\":{\"id\":3158,\"name\":" +
				"\"Ionian Boots of Lucidity\",\"group\":\"BootsUpgrades\",\"description\":\"<unique>UNIQUE Passive:" +
				"</unique> +10% Cooldown Reduction<br><unique>UNIQUE Passive - Enhanced Movement:</unique> +45 " +
				"Movement Speed<br><unique>UNIQUE Passive:</unique> Reduces Summoner Spell cooldowns by 10%<br><br>" +
				"<br><rules><font color='#FDD017'>''This item is dedicated in honor of Ionia's victory over Noxus in " +
				"the Rematch for the Southern Provinces on 10 December, 20 CLE.''</font></rules>\",\"from\":" +
				"[\"1001\"],\"into\":[\"1331\",\"1333\",\"1330\",\"1332\"],\"maps\":{\"1\":\"false\",\"8\":\"true\"," +
				"\"10\":\"true\",\"11\":\"true\",\"12\":\"true\",\"14\":\"false\"},\"image\":{\"full\":\"3158.png\"," +
				"\"sprite\":\"item1.png\",\"group\":\"item\",\"x\":96,\"y\":192,\"w\":48,\"h\":48},\"gold\":" +
				"{\"base\":600,\"total\":900,\"sell\":630,\"purchasable\":\"true\"}}}}";
		itemsRepository.save(objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3158"));

		mockMvc.perform(get(apiPath + "/items/boots?mapId={mapId}", "11"))
				.andExpect(status().isOk())
				.andDo(document("bootsItems", responseFields(
						fieldWithPath("_embedded.items")
								.description("An array of Items"),
						fieldWithPath("_embedded.items[*].name")
								.description("The name of the Item"),
						fieldWithPath("_embedded.items[*].group")
								.type(JsonFieldType.STRING)
								.description("The group of the Item"),
						fieldWithPath("_embedded.items[*].consumed")
								.type(JsonFieldType.BOOLEAN)
								.description("Whether the Item is consumable"),
						fieldWithPath("_embedded.items[*].description")
								.description("The group of the Item"),
						fieldWithPath("_embedded.items[*].from")
								.description("An array of Item IDs that the Item is built from as part of the Item's recipe"),
						fieldWithPath("_embedded.items[*].into")
								.description("An array of Item IDs that the Item builds into as part of the Item's recipe"),
						fieldWithPath("_embedded.items[*].requiredChampion")
								.type(JsonFieldType.STRING)
								.description("The required champion of the Item"),
						fieldWithPath("_embedded.items[*].maps")
								.description("A map of Map IDs keys and boolean values whether the Item can be purchased in the Map"),
						fieldWithPath("_embedded.items[*].image")
								.description("The image of the Item"),
						fieldWithPath("_embedded.items[*].gold")
								.description("The gold of the Item"),
						fieldWithPath("_links")
								.description("Links to resources related to Items"))));
	}

	@Test
	public void trinketItems() throws Exception {
		String responseBody = "{\"type\":\"item\",\"version\":\"6.8.1\",\"data\":{\"3341\":{\"id\":3341,\"name\":" +
				"\"Sweeping Lens (Trinket)\",\"group\":\"RelicBase\",\"description\":\"<groupLimit>Limited to 1 " +
				"Trinket.</groupLimit><br><br><active>Active:</active> Scans an area for 6 seconds, warning against " +
				"hidden hostile units and revealing / disabling invisible traps and wards (90 to 60 second cooldown)." +
				"<br><br>Cast range and sweep radius gradually improve with level.<br><br><rules>(Switching to a " +
				"<font color='#BBFFFF'>Totem</font>-type trinket will disable <font color='#BBFFFF'>Trinket</font> " +
				"use for 120 seconds.)</rules>\",\"maps\":{\"1\":\"false\",\"8\":\"false\",\"10\":\"false\",\"11\":" +
				"\"true\",\"12\":\"true\",\"14\":\"false\"},\"image\":{\"full\":\"3341.png\",\"sprite\":" +
				"\"item2.png\",\"group\":\"item\",\"x\":336,\"y\":0,\"w\":48,\"h\":48},\"gold\":{\"base\":0," +
				"\"total\":0,\"sell\":0,\"purchasable\":\"true\"}}}}";
		itemsRepository.save(objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3341"));

		mockMvc.perform(get(apiPath + "/items/trinkets?mapId={mapId}", "11"))
				.andExpect(status().isOk())
				.andDo(document("trinketItems", responseFields(
						fieldWithPath("_embedded.items")
								.description("An array of Items"),
						fieldWithPath("_embedded.items[*].name")
								.description("The name of the Item"),
						fieldWithPath("_embedded.items[*].group")
								.type(JsonFieldType.STRING)
								.description("The group of the Item"),
						fieldWithPath("_embedded.items[*].consumed")
								.type(JsonFieldType.BOOLEAN)
								.description("Whether the Item is consumable"),
						fieldWithPath("_embedded.items[*].description")
								.description("The group of the Item"),
						fieldWithPath("_embedded.items[*].from")
								.description("An array of Item IDs that the Item is built from as part of the Item's recipe"),
						fieldWithPath("_embedded.items[*].into")
								.description("An array of Item IDs that the Item builds into as part of the Item's recipe"),
						fieldWithPath("_embedded.items[*].requiredChampion")
								.type(JsonFieldType.STRING)
								.description("The required champion of the Item"),
						fieldWithPath("_embedded.items[*].maps")
								.description("A map of Map IDs keys and boolean values whether the Item can be purchased in the Map"),
						fieldWithPath("_embedded.items[*].image")
								.description("The image of the Item"),
						fieldWithPath("_embedded.items[*].gold")
								.description("The gold of the Item"),
						fieldWithPath("_links")
								.description("Links to resources related to Items"))));
	}

	@Test
	public void viktorOnlyItems() throws Exception {
		String responseBody = "{\"type\":\"item\",\"version\":\"6.8.1\",\"data\":{\"3200\":{\"id\":3200,\"name\":" +
				"\"Prototype Hex Core\",\"description\":\"<stats>+1 Ability Power per level<br>+10 Mana per level" +
				"</stats><br><br><unique>UNIQUE Passive - Progress:</unique> This item can be upgraded three times " +
				"to enhance Viktor's basic abilities.\",\"into\":[\"3196\"],\"requiredChampion\":\"Viktor\",\"maps\":" +
				"{\"1\":\"false\",\"8\":\"true\",\"10\":\"true\",\"11\":\"true\",\"12\":\"true\",\"14\":\"false\"}," +
				"\"image\":{\"full\":\"3200.png\",\"sprite\":\"item1.png\",\"group\":\"item\",\"x\":192,\"y\":288," +
				"\"w\":48,\"h\":48},\"gold\":{\"base\":0,\"total\":0,\"sell\":0,\"purchasable\":\"false\"}}}}";
		itemsRepository.save(objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3200"));

		mockMvc.perform(get(apiPath + "/items/viktor-only"))
				.andExpect(status().isOk())
				.andDo(document("viktorOnlyItems", responseFields(
						fieldWithPath("_embedded.items")
								.description("An array of Items"),
						fieldWithPath("_embedded.items[*].name")
								.description("The name of the Item"),
						fieldWithPath("_embedded.items[*].group")
								.type(JsonFieldType.STRING)
								.description("The group of the Item"),
						fieldWithPath("_embedded.items[*].consumed")
								.type(JsonFieldType.BOOLEAN)
								.description("Whether the Item is consumable"),
						fieldWithPath("_embedded.items[*].description")
								.description("The group of the Item"),
						fieldWithPath("_embedded.items[*].from")
								.description("An array of Item IDs that the Item is built from as part of the Item's recipe"),
						fieldWithPath("_embedded.items[*].into")
								.description("An array of Item IDs that the Item builds into as part of the Item's recipe"),
						fieldWithPath("_embedded.items[*].requiredChampion")
								.type(JsonFieldType.STRING)
								.description("The required champion of the Item"),
						fieldWithPath("_embedded.items[*].maps")
								.description("A map of Map IDs keys and boolean values whether the Item can be purchased in the Map"),
						fieldWithPath("_embedded.items[*].image")
								.description("The image of the Item"),
						fieldWithPath("_embedded.items[*].gold")
								.description("The gold of the Item"),
						fieldWithPath("_links")
								.description("Links to resources related to Items"))));
	}

	@Test
	public void itemsForTrollBuild() throws Exception {
		String responseBody = "{\"type\":\"item\",\"version\":\"6.8.1\",\"data\":{\"3056\":{\"id\":3056,\"name\":" +
				"\"Ohmwrecker\",\"description\":\"<stats>+300 Health<br>+50 Armor<br>+150% Base Health Regen <br>+10%" +
				" Cooldown Reduction</stats><br><br><active>UNIQUE Active:</active> Prevents nearby enemy turrets " +
				"from attacking for 3 seconds (120 second cooldown). This effect cannot be used against the same " +
				"turret more than once every 8 seconds.<br><br><unique>UNIQUE Passive - Point Runner:</unique> Builds" +
				" up to +20% Movement Speed over 2 seconds while near turrets (including fallen turrets).\"," +
				"\"from\":[\"2053\",\"3067\"],\"maps\":{\"1\":\"false\",\"8\":\"false\",\"10\":\"false\",\"11\":" +
				"\"true\",\"12\":\"false\",\"14\":\"false\"},\"image\":{\"full\":\"3056.png\",\"sprite\":" +
				"\"item0.png\",\"group\":\"item\",\"x\":336,\"y\":336,\"w\":48,\"h\":48},\"gold\":{\"base\":650," +
				"\"total\":2650,\"sell\":1855,\"purchasable\":\"true\"}}}}";
		itemsRepository.save(objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3056"));

		mockMvc.perform(get(apiPath + "/items/for-troll-build?mapId={mapId}", "11"))
				.andExpect(status().isOk())
				.andDo(document("itemsForTrollBuild", responseFields(
						fieldWithPath("_embedded.items")
								.description("An array of Items"),
						fieldWithPath("_embedded.items[*].name")
								.description("The name of the Item"),
						fieldWithPath("_embedded.items[*].group")
								.type(JsonFieldType.STRING)
								.description("The group of the Item"),
						fieldWithPath("_embedded.items[*].consumed")
								.type(JsonFieldType.BOOLEAN)
								.description("Whether the Item is consumable"),
						fieldWithPath("_embedded.items[*].description")
								.description("The group of the Item"),
						fieldWithPath("_embedded.items[*].from")
								.description("An array of Item IDs that the Item is built from as part of the Item's recipe"),
						fieldWithPath("_embedded.items[*].into")
								.description("An array of Item IDs that the Item builds into as part of the Item's recipe"),
						fieldWithPath("_embedded.items[*].requiredChampion")
								.type(JsonFieldType.STRING)
								.description("The required champion of the Item"),
						fieldWithPath("_embedded.items[*].maps")
								.description("A map of Map IDs keys and boolean values whether the Item can be purchased in the Map"),
						fieldWithPath("_embedded.items[*].image")
								.description("The image of the Item"),
						fieldWithPath("_embedded.items[*].gold")
								.description("The gold of the Item"),
						fieldWithPath("_links")
								.description("Links to resources related to Items"))));
	}

	/*
	 * Champions doc
	 */

	@Test
	public void getChampions() throws Exception {
		String responseBody = "{\"type\":\"champion\",\"version\":\"6.8.1\",\"data\":{\"Warwick\":{\"id\":19,\"key\":" +
				"\"Warwick\",\"name\":\"Warwick\",\"title\":\"the Blood Hunter\",\"image\":{\"full\":\"Warwick.png\"," +
				"\"sprite\":\"champion3.png\",\"group\":\"champion\",\"x\":144,\"y\":96,\"w\":48,\"h\":48},\"tags\":" +
				"[\"Fighter\",\"Tank\"],\"partype\":\"MP\"}}}";
		championsRepository.save(objectMapper.readValue(responseBody, ChampionsResponse.class).getChampions()
				.get("Warwick"));

		mockMvc.perform(get(apiPath + "/champions"))
				.andExpect(status().isOk())
				.andDo(document("getChampions", responseFields(
						fieldWithPath("_embedded.champions")
								.description("An array of Champions"),
						fieldWithPath("_embedded.champions[*].key")
								.description("The key of the Champion. Usually the same as the name"),
						fieldWithPath("_embedded.champions[*].name")
								.description("The name of the Champion"),
						fieldWithPath("_embedded.champions[*].title")
								.description("The title of the Champion"),
						fieldWithPath("_embedded.champions[*].image")
								.description("The image of the Champion"),
						fieldWithPath("_embedded.champions[*].tags")
								.description("An array of tags of the Champion"),
						fieldWithPath("_embedded.champions[*].partype")
								.description("The ability resource of the Champion"),
						fieldWithPath("_links")
								.description("Links to resources related to Champions"),
						fieldWithPath("page")
								.description("Current Page settings of the pagination"))));

		responseBody = "{\"type\":\"champion\",\"version\":\"6.8.1\",\"data\":{\"Blitzcrank\":{\"id\":53,\"key\":" +
				"\"Blitzcrank\",\"name\":\"Blitzcrank\",\"title\":\"the Great Steam Golem\",\"image\":{\"full\":" +
				"\"Blitzcrank.png\",\"sprite\":\"champion0.png\",\"group\":\"champion\",\"x\":432,\"y\":0,\"w\":48," +
				"\"h\":48},\"tags\":[\"Tank\",\"Fighter\"],\"partype\":\"MP\"}}}";
		championsRepository.save(objectMapper.readValue(responseBody, ChampionsResponse.class).getChampions()
				.get("Blitzcrank"));

		mockMvc.perform(get(apiPath + "/champions?name={name}", "blitz"))
				.andExpect(status().isOk())
				.andDo(document("championsFindBy", responseFields(
						fieldWithPath("_embedded.champions")
								.description("An array of Champions"),
						fieldWithPath("_embedded.champions[*].key")
								.description("The key of the Champion. Usually the same as the name"),
						fieldWithPath("_embedded.champions[*].name")
								.description("The name of the Champion"),
						fieldWithPath("_embedded.champions[*].title")
								.description("The title of the Champion"),
						fieldWithPath("_embedded.champions[*].image")
								.description("The image of the Champion"),
						fieldWithPath("_embedded.champions[*].tags")
								.description("An array of tags of the Champion"),
						fieldWithPath("_embedded.champions[*].partype")
								.description("The ability resource of the Champion"),
						fieldWithPath("_links")
								.description("Links to resources related to Champions"),
						fieldWithPath("page")
								.description("Current Page settings of the pagination"))));
	}

	@Test
	public void getChampion() throws Exception {
		String responseBody = "{\"type\":\"champion\",\"version\":\"6.8.1\",\"data\":{\"Talon\":{\"id\":91,\"key\":" +
				"\"Talon\",\"name\":\"Talon\",\"title\":\"the Blade's Shadow\",\"image\":{\"full\":\"Talon.png\"," +
				"\"sprite\":\"champion3.png\",\"group\":\"champion\",\"x\":192,\"y\":0,\"w\":48,\"h\":48},\"tags\":" +
				"[\"Assassin\",\"Fighter\"],\"partype\":\"MP\"}}}";

		Champion talon = championsRepository.save(objectMapper.readValue(responseBody, ChampionsResponse.class)
				.getChampions().get("Talon"));

		mockMvc.perform(get(apiPath + "/champions/{id}", talon.getId()))
				.andExpect(status().isOk())
				.andDo(document("getChampion", responseFields(
						fieldWithPath("key")
								.description("The key of the Champion. Usually the same as the name"),
						fieldWithPath("name")
								.description("The name of the Champion"),
						fieldWithPath("title")
								.description("The title of the Champion"),
						fieldWithPath("image")
								.description("The image of the Champion"),
						fieldWithPath("tags")
								.description("An array of tags of the Champion"),
						fieldWithPath("partype")
								.description("The ability resource of the Champion"),
						fieldWithPath("_links")
								.description("Link to the self Champion"))));
	}

	/*
	 * Maps doc
	 */

	@Test
	public void getMaps() throws Exception {
		String responseBody = "{\"type\":\"map\",\"version\":\"6.8.1\",\"data\":{\"11\":{\"mapName\":" +
				"\"SummonersRiftNew\",\"mapId\":11,\"image\":{\"full\":\"map11.png\",\"sprite\":\"map0.png\"," +
				"\"group\":\"map\",\"x\":144,\"y\":0,\"w\":48,\"h\":48}}}}";
		mapsRepository.save(objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("11"));

		mockMvc.perform(get(apiPath + "/maps"))
				.andExpect(status().isOk())
				.andDo(document("getMaps", responseFields(
						fieldWithPath("_embedded.maps")
								.description("An array of Maps"),
						fieldWithPath("_embedded.maps[*].mapName")
								.description("The name of the Map"),
						fieldWithPath("_embedded.maps[*].image")
								.description("The image of the Map"),
						fieldWithPath("_links")
								.description("Links to resources related to Maps"),
						fieldWithPath("page")
								.description("Current Page settings of the pagination"))));

		responseBody = "{\"type\":\"map\",\"version\":\"6.8.1\",\"data\":{\"10\":{\"mapName\":\"NewTwistedTreeline\"," +
				"\"mapId\":10,\"image\":{\"full\":\"map10.png\",\"sprite\":\"map0.png\",\"group\":\"map\",\"x\":0," +
				"\"y\":0,\"w\":48,\"h\":48}}}}";
		mapsRepository.save(objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("10"));

		mockMvc.perform(get(apiPath + "/maps?mapName={mapName}", "treeline"))
				.andExpect(status().isOk())
				.andDo(document("mapsFindBy", responseFields(
						fieldWithPath("_embedded.maps")
								.description("An array of Maps"),
						fieldWithPath("_embedded.maps[*].mapName")
								.description("The name of the Map"),
						fieldWithPath("_embedded.maps[*].image")
								.description("The image of the Map"),
						fieldWithPath("_links")
								.description("Links to resources related to Maps"),
						fieldWithPath("page")
								.description("Current Page settings of the pagination"))));
	}

	@Test
	public void getMap() throws Exception {
		String responseBody = "{\"type\":\"map\",\"version\":\"6.8.1\",\"data\":{\"12\":{\"mapName\":" +
				"\"ProvingGroundsNew\",\"mapId\":12,\"image\":{\"full\":\"map12.png\",\"sprite\":\"map0.png\"," +
				"\"group\":\"map\",\"x\":48,\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap summonersRift = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("12");
		mapsRepository.save(summonersRift);

		mockMvc.perform(get(apiPath + "/maps/{id}", summonersRift.getMapId()))
				.andExpect(status().isOk())
				.andDo(document("getMap", responseFields(
						fieldWithPath("mapName")
								.description("The name of the Map"),
						fieldWithPath("image")
								.description("The image of the Map"),
						fieldWithPath("_links")
								.description("Links to resources related to Maps"))));
	}

	/*
	 * Versions doc
	 */

	@Test
	public void getVersions() throws Exception {
		String responseBody = "\"6.9.1\"";
		versionsRepository.save(objectMapper.readValue(responseBody, Version.class));

		mockMvc.perform(get(apiPath + "/versions"))
				.andExpect(status().isOk())
				.andDo(document("getVersions", responseFields(
						fieldWithPath("_embedded.versions")
								.description("An array of Versions"),
						fieldWithPath("_embedded.versions[*].major")
								.description("The major version number of the Version"),
						fieldWithPath("_embedded.versions[*].minor")
								.description("The minor version number of the Map"),
						fieldWithPath("_embedded.versions[*].revision")
								.description("The revision version number of the Version"),
						fieldWithPath("_links")
								.description("Links to resources related to Version"))));
	}

	@Test
	public void getVersion() throws Exception {
		String responseBody = "\"6.8.1\"";
		Version version = versionsRepository.save(objectMapper.readValue(responseBody, Version.class));

		mockMvc.perform(get(apiPath + "/versions/{version}", version.getPatch()))
				.andExpect(status().isOk())
				.andDo(document("getVersion", responseFields(
						fieldWithPath("major")
								.description("The major version number of the Version"),
						fieldWithPath("minor")
								.description("The minor version number of the Map"),
						fieldWithPath("revision")
								.description("The revision version number of the Version"),
						fieldWithPath("_links")
								.description("Link to the self Version"))));
	}

}
