package com.drumonii.loltrollbuild.doc;

import com.drumonii.loltrollbuild.api.*;
import com.drumonii.loltrollbuild.model.*;
import com.drumonii.loltrollbuild.repository.*;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.drumonii.loltrollbuild.test.api.WebMvcRestTest;
import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static com.drumonii.loltrollbuild.util.GameMapUtil.HOWLING_ABYSS_SID;
import static com.drumonii.loltrollbuild.util.GameMapUtil.SUMMONERS_RIFT_SID;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcRestTest({ BuildsRestController.class, ChampionsRestController.class, ItemsRestController.class,
		MapsRestController.class, SummonerSpellsRestController.class, VersionsRestController.class })
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "loltrollbuild.com", uriPort = 443)
@ActiveProfiles({ TESTING, DDRAGON })
@Tag("api-doc")
class ApiDocumentation {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private BuildsRepository buildsRepository;

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private MapsRepository mapsRepository;

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	private VersionsRepository versionsRepository;

	private ChampionsResponse championsResponse;
	private ItemsResponse itemsResponse;
	private MapsResponse mapsResponse;
	private SummonerSpellsResponse summonerSpellsResponse;
	private List<Version> versions;

	@BeforeEach
	void beforeEach() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);

		championsResponse = jsonTestFilesUtil.getFullChampionsResponse();
		itemsResponse = jsonTestFilesUtil.getItemsResponse();
		mapsResponse = jsonTestFilesUtil.getMapsResponse();
		summonerSpellsResponse = jsonTestFilesUtil.getSummonerSpellsResponse();
		versions = jsonTestFilesUtil.getVersions();
	}

	/*
	 * Summoner Spells doc
	 */

	@Test
	void getSummonerSpells() throws Exception {
		summonerSpellsRepository.save(summonerSpellsResponse.getSummonerSpells().get("SummonerExhaust"));

		mockMvc.perform(get("/api/summoner-spells"))
				.andExpect(status().isOk())
				.andDo(document("getSummonerSpells", relaxedResponseFields(
						fieldWithPath("[*].id")
								.description("The Id of the Summoner Spell"),
						fieldWithPath("[*].name")
								.description("The name of the Summoner Spell"),
						fieldWithPath("[*].description")
								.description("The description of the Summoner Spell"),
						fieldWithPath("[*].cooldown")
								.description("An array of cooldown values of the Summoner Spell"),
						fieldWithPath("[*].key")
								.description("The key of the Summoner Spell"),
						fieldWithPath("[*].modes")
								.description("An array of game modes eligible with the Summoner Spell"))));
	}

	@Test
	void getSummonerSpell() throws Exception {
		SummonerSpell teleport = summonerSpellsRepository.save(summonerSpellsResponse.getSummonerSpells()
				.get("SummonerTeleport"));

		mockMvc.perform(RestDocumentationRequestBuilders.get("/api/summoner-spells/{id}", teleport.getId()))
				.andExpect(status().isOk())
				.andDo(document("getSummonerSpell", pathParameters(
						parameterWithName("id")
								.description("The Id of the Summoner Spell")), relaxedResponseFields(
						fieldWithPath("id")
								.description("The Id of the Summoner Spell"),
						fieldWithPath("name")
								.description("The name of the Summoner Spell"),
						fieldWithPath("description")
								.description("The description of the Summoner Spell"),
						fieldWithPath("cooldown")
								.description("An array of cooldown values of the Summoner Spell"),
						fieldWithPath("key")
								.description("The key of the Summoner Spell"),
						fieldWithPath("modes")
								.description("An array of game modes eligible with the Summoner Spell"))));
	}

	@Test
	void summonerSpellsForTrollBuild() throws Exception {
		summonerSpellsRepository.save(summonerSpellsResponse.getSummonerSpells().get("SummonerSmite"));

		mockMvc.perform(get("/api/summoner-spells/for-troll-build")
				.param("mode", "CLASSIC"))
				.andExpect(status().isOk())
				.andDo(document("summonerSpellsForTrollBuild", relaxedResponseFields(
						fieldWithPath("[*].id")
								.description("The Id of the Summoner Spell"),
						fieldWithPath("[*].name")
								.description("The name of the Summoner Spell"),
						fieldWithPath("[*].description")
								.description("The description of the Summoner Spell"),
						fieldWithPath("[*].cooldown")
								.description("An array of cooldown values of the Summoner Spell"),
						fieldWithPath("[*].key")
								.description("The key of the Summoner Spell"),
						fieldWithPath("[*].modes")
								.description("An array of game modes eligible with the Summoner Spell"))));
	}

	/*
	 * Items doc
	 */

	@Test
	void getItems() throws Exception {
		itemsRepository.save(itemsResponse.getItems().get("3140"));

		mockMvc.perform(get("/api/items"))
				.andExpect(status().isOk())
				.andDo(document("getItems", relaxedResponseFields(
						fieldWithPath("[*].id")
								.description("The Id of the Item"),
						fieldWithPath("[*].name")
								.description("The name of the Item"),
						fieldWithPath("[*].group")
								.description("The group of the Item"),
						fieldWithPath("[*].consumed")
								.description("Whether the Item is consumable"),
						fieldWithPath("[*].description")
								.description("The group of the Item"),
						fieldWithPath("[*].from")
								.description("An array of Item IDs that the Item is built from as part of the Item's recipe"),
						fieldWithPath("[*].into")
								.description("An array of Item IDs that the Item builds into as part of the Item's recipe"),
						fieldWithPath("[*].requiredChampion")
								.description("The required champion of the Item"),
						fieldWithPath("[*].requiredAlly")
								.description("The required ally champion of the Item"),
						fieldWithPath("[*].maps")
								.description("A map of Map IDs keys and boolean values whether the Item can be purchased in the Map"),
						fieldWithPath("[*].gold")
								.description("The gold of the Item"))));
	}

	@Test
	void getItem() throws Exception {
		Item warmogs = itemsRepository.save(itemsResponse.getItems().get("3083"));

		mockMvc.perform(RestDocumentationRequestBuilders.get("/api/items/{id}", warmogs.getId()))
				.andExpect(status().isOk())
				.andDo(document("getItem", pathParameters(
						parameterWithName("id")
								.description("The Id of the Item")), relaxedResponseFields(
						fieldWithPath("id")
								.description("The Id of the Item"),
						fieldWithPath("name")
								.description("The name of the Item"),
						fieldWithPath("group")
								.description("The group of the Item"),
						fieldWithPath("consumed")
								.description("Whether the Item is consumable"),
						fieldWithPath("description")
								.description("The group of the Item"),
						fieldWithPath("from")
								.description("An array of Item IDs that the Item is built from as part of the Item's recipe"),
						fieldWithPath("into")
								.description("An array of Item IDs that the Item builds into as part of the Item's recipe"),
						fieldWithPath("requiredChampion")
								.description("The required champion of the Item"),
						fieldWithPath("requiredAlly")
								.description("The required ally champion of the Item"),
						fieldWithPath("maps")
								.description("A map of Map IDs keys and boolean values whether the Item can be purchased in the Map"),
						fieldWithPath("gold")
								.description("The gold of the Item"))));

	}

	@Test
	void bootsItems() throws Exception {
		itemsRepository.save(itemsResponse.getItems().get("3158"));

		mockMvc.perform(get("/api/items/boots")
				.param("mapId", SUMMONERS_RIFT_SID))
				.andExpect(status().isOk())
				.andDo(document("bootsItems", relaxedResponseFields(
						fieldWithPath("[*].id")
								.description("The Id of the Item"),
						fieldWithPath("[*].name")
								.description("The name of the Item"),
						fieldWithPath("[*].group")
								.description("The group of the Item"),
						fieldWithPath("[*].consumed")
								.description("Whether the Item is consumable"),
						fieldWithPath("[*].description")
								.description("The group of the Item"),
						fieldWithPath("[*].from")
								.description("An array of Item IDs that the Item is built from as part of the Item's recipe"),
						fieldWithPath("[*].into")
								.description("An array of Item IDs that the Item builds into as part of the Item's recipe"),
						fieldWithPath("[*].requiredChampion")
								.description("The required champion of the Item"),
						fieldWithPath("[*].requiredAlly")
								.description("The required ally champion of the Item"),
						fieldWithPath("[*].maps")
								.description("A map of Map IDs keys and boolean values whether the Item can be purchased in the Map"),
						fieldWithPath("[*].gold")
								.description("The gold of the Item"))));
	}

	@Test
	void trinketItems() throws Exception {
		itemsRepository.save(itemsResponse.getItems().get("3364"));

		mockMvc.perform(get("/api/items/trinkets")
				.param("mapId", SUMMONERS_RIFT_SID))
				.andExpect(status().isOk())
				.andDo(document("trinketItems", relaxedResponseFields(
						fieldWithPath("[*].id")
								.description("The ID of the Item"),
						fieldWithPath("[*].name")
								.description("The name of the Item"),
						fieldWithPath("[*].group")
								.description("The group of the Item"),
						fieldWithPath("[*].consumed")
								.description("Whether the Item is consumable"),
						fieldWithPath("[*].description")
								.description("The group of the Item"),
						fieldWithPath("[*].from")
								.description("An array of Item IDs that the Item is built from as part of the Item's recipe"),
						fieldWithPath("[*].into")
								.description("An array of Item IDs that the Item builds into as part of the Item's recipe"),
						fieldWithPath("[*].requiredChampion")
								.description("The required champion of the Item"),
						fieldWithPath("[*].requiredAlly")
								.description("The required ally champion of the Item"),
						fieldWithPath("[*].maps")
								.description("A map of Map IDs keys and boolean values whether the Item can be purchased in the Map"),
						fieldWithPath("[*].gold")
								.description("The gold of the Item"))));
	}

	@Test
	void itemsForTrollBuild() throws Exception {
		itemsRepository.save(itemsResponse.getItems().get("3065"));

		mockMvc.perform(get("/api/items/for-troll-build")
				.param("mapId", SUMMONERS_RIFT_SID))
				.andExpect(status().isOk())
				.andDo(document("itemsForTrollBuild", relaxedResponseFields(
						fieldWithPath("[*].id")
								.description("The Id of the Item"),
						fieldWithPath("[*].name")
								.description("The name of the Item"),
						fieldWithPath("[*].group")
								.description("The group of the Item"),
						fieldWithPath("[*].consumed")
								.description("Whether the Item is consumable"),
						fieldWithPath("[*].description")
								.description("The group of the Item"),
						fieldWithPath("[*].from")
								.description("An array of Item IDs that the Item is built from as part of the Item's recipe"),
						fieldWithPath("[*].into")
								.description("An array of Item IDs that the Item builds into as part of the Item's recipe"),
						fieldWithPath("[*].requiredChampion")
								.description("The required champion of the Item"),
						fieldWithPath("[*].requiredAlly")
								.description("The required ally champion of the Item"),
						fieldWithPath("[*].maps")
								.description("A map of Map IDs keys and boolean values whether the Item can be purchased in the Map"),
						fieldWithPath("[*].gold")
								.description("The gold of the Item"))));
	}

	/*
	 * Champions doc
	 */

	@Test
	void getChampions() throws Exception {
		championsRepository.save(championsResponse.getChampions().get("Warwick"));

		mockMvc.perform(get("/api/champions"))
				.andExpect(status().isOk())
				.andDo(document("getChampions", relaxedResponseFields(
						fieldWithPath("[*].id")
								.description("The Id of the Champion"),
						fieldWithPath("[*].key")
								.description("The key of the Champion. Usually the same as the name"),
						fieldWithPath("[*].name")
								.description("The name of the Champion"),
						fieldWithPath("[*].title")
								.description("The title of the Champion"),
						fieldWithPath("[*].partype")
								.description("The ability resource of the Champion"),
						fieldWithPath("[*].tags")
								.description("An array of tags of the Champion"))));
	}

	@Test
	void getChampion() throws Exception {
		Champion talon = championsRepository.save(championsResponse.getChampions().get("Talon"));

		mockMvc.perform(RestDocumentationRequestBuilders.get("/api/champions/{id}", talon.getId()))
				.andExpect(status().isOk())
				.andDo(document("getChampion", pathParameters(
						parameterWithName("id")
								.description("The Id of the Champion")), relaxedResponseFields(
						fieldWithPath("id")
								.description("The Id of the Champion"),
						fieldWithPath("key")
								.description("The key of the Champion. Usually the same as the name"),
						fieldWithPath("name")
								.description("The name of the Champion"),
						fieldWithPath("title")
								.description("The title of the Champion"),
						fieldWithPath("partype")
								.description("The ability resource of the Champion"),
						fieldWithPath("tags")
								.description("An array of tags of the Champion"))));
	}

	@Test
	void getTags() throws Exception {
		championsRepository.save(championsResponse.getChampions().get("Nocturne"));

		mockMvc.perform(get("/api/champions/tags"))
				.andExpect(status().isOk())
				.andDo(document("getChampionTags"));
	}

	@Test
	void getTrollBuildForChampion() throws Exception {
		Champion jayce = championsRepository.save(championsResponse.getChampions().get("Jayce"));

		itemsRepository.save(itemsResponse.getItems().get("3340")); // Warding Totem (Trinket)

		summonerSpellsRepository.save(summonerSpellsResponse.getSummonerSpells().get("SummonerFlash"));
		summonerSpellsRepository.save(summonerSpellsResponse.getSummonerSpells().get("SummonerMana"));

		itemsRepository.save(itemsResponse.getItems().get("3020")); // Sorcerer's Shoes
		itemsRepository.save(itemsResponse.getItems().get("3004")); // Manamune
		itemsRepository.save(itemsResponse.getItems().get("3742")); // Dead Man's Plate
		itemsRepository.save(itemsResponse.getItems().get("3074")); // Ravenous Hydra
		itemsRepository.save(itemsResponse.getItems().get("3116")); // Rylai's Crystal Scepter
		itemsRepository.save(itemsResponse.getItems().get("3046")); // Phantom Dancer

		mockMvc.perform(RestDocumentationRequestBuilders.get("/api/champions/{id}/troll-build", jayce.getId()))
				.andExpect(status().isOk())
				.andDo(document("getTrollBuildForChampion", pathParameters(
						parameterWithName("id")
								.description("The Id of the Champion or the Champion name")), requestParameters(
						parameterWithName("mapId")
								.description("The Map Id. Defaults to Summoner's Rift if unspecified. See <<game-maps-table, Game Maps Table>> for Map IDs")
								.optional()), relaxedResponseFields(
						fieldWithPath("champion")
								.description("The Champion of the Troll Build"),
						fieldWithPath("trinket")
								.description("The trinket of the Troll Build"),
						fieldWithPath("summonerSpells")
								.description("The Summoner Spells of the Troll Build"),
						fieldWithPath("items")
								.description("The Items of the Troll Build, with the first always a boots Item"),
						fieldWithPath("totalGold")
								.description("The total gold amount of the Troll Build"))));
	}

	/*
	 * Maps doc
	 */

	@Test
	void getMaps() throws Exception {
		mapsRepository.save(mapsResponse.getMaps().get(SUMMONERS_RIFT_SID));

		mockMvc.perform(get("/api/maps"))
				.andExpect(status().isOk())
				.andDo(document("getMaps", relaxedResponseFields(
						fieldWithPath("[*].mapId")
								.description("The Id of the Map"),
						fieldWithPath("[*].mapName")
								.description("The name of the Map"))));
	}

	@Test
	void getMap() throws Exception {
		GameMap summonersRift = mapsResponse.getMaps().get(HOWLING_ABYSS_SID);
		mapsRepository.save(summonersRift);

		mockMvc.perform(RestDocumentationRequestBuilders.get("/api/maps/{id}", summonersRift.getMapId()))
				.andExpect(status().isOk())
				.andDo(document("getMap", pathParameters(
						parameterWithName("id")
								.description("The Id of the Map")), relaxedResponseFields(
						fieldWithPath("mapId")
								.description("The Id of the Map"),
						fieldWithPath("mapName")
								.description("The name of the Map"))));
	}

	/*
	 * Versions doc
	 */

	@Test
	void getVersions() throws Exception {
		versionsRepository.save(versions.get(0));

		mockMvc.perform(get("/api/versions"))
				.andExpect(status().isOk())
				.andDo(document("getVersions", relaxedResponseFields(
						fieldWithPath("[*].patch")
								.description("The patch number"),
						fieldWithPath("[*].major")
								.description("The major version number of the Version"),
						fieldWithPath("[*].minor")
								.description("The minor version number of the Map"),
						fieldWithPath("[*].revision")
								.description("The revision version number of the Version"))));
	}

	@Test
	void getVersion() throws Exception {
		Version version = versionsRepository.save(versions.get(0));

		mockMvc.perform(RestDocumentationRequestBuilders.get("/api/versions/{version}", version.getPatch()))
				.andExpect(status().isOk())
				.andDo(document("getVersion", pathParameters(
						parameterWithName("version")
								.description("The full patch version")), relaxedResponseFields(
						fieldWithPath("patch")
								.description("The patch number"),
						fieldWithPath("major")
								.description("The major version number of the Version"),
						fieldWithPath("minor")
								.description("The minor version number of the Map"),
						fieldWithPath("revision")
								.description("The revision version number of the Version"))));
	}

	/*
	 * Builds doc
	 */

	@Disabled
	@Test
	void getBuilds() throws Exception {
		Build build = new Build();
		build.setChampionId(championsResponse.getChampions().get("Karthus").getId());
		build.setItem1Id(itemsResponse.getItems().get("3117").getId());
		build.setItem2Id(itemsResponse.getItems().get("3512").getId());
		build.setItem3Id(itemsResponse.getItems().get("3135").getId());
		build.setItem4Id(itemsResponse.getItems().get("3508").getId());
		build.setItem5Id(itemsResponse.getItems().get("3075").getId());
		build.setItem6Id(itemsResponse.getItems().get("3046").getId());
		build.setSummonerSpell1Id(summonerSpellsResponse.getSummonerSpells().get("SummonerDot").getId());
		build.setSummonerSpell2Id(summonerSpellsResponse.getSummonerSpells().get("SummonerTeleport").getId());
		build.setTrinketId(itemsResponse.getItems().get("3340").getId());
		build.setMapId(mapsResponse.getMaps().get(SUMMONERS_RIFT_SID).getMapId());
		buildsRepository.save(build);

		mockMvc.perform(get("/api/builds"))
				.andExpect(status().isOk())
				.andDo(document("getBuilds", relaxedResponseFields(
						fieldWithPath("content.[*].id")
								.description("The Id of the Build"),
						fieldWithPath("content.[*].createdDate")
								.description("The date the Build was created"),
						fieldWithPath("content.[*].championId")
								.description("The Champion Id of the Build"),
						fieldWithPath("content.[*].item1Id")
								.description("The Item 1 Id (boots) of the Build"),
						fieldWithPath("content.[*].item2Id")
								.description("The Item 2 Id of the Build"),
						fieldWithPath("content.[*].item3Id")
								.description("The Item 3 Id of the Build"),
						fieldWithPath("content.[*].item4Id")
								.description("The Item 4 Id of the Build"),
						fieldWithPath("content.[*].item5Id")
								.description("The Item 5 Id of the Build"),
						fieldWithPath("content.[*].item6Id")
								.description("The Item 6 Id of the Build"),
						fieldWithPath("content.[*].summonerSpell1Id")
								.description("The Summoner Spell 1 Id of the Build"),
						fieldWithPath("content.[*].summonerSpell2Id")
								.description("The Summoner Spell 2 Id of the Build"),
						fieldWithPath("content.[*].trinketId")
								.description("The Trinket Id of the Build"),
						fieldWithPath("content.[*].mapId")
								.description("The Map Id of the Build"))));
	}

	@Disabled
	@Test
	void getBuild() throws Exception {
		Champion pantheon = championsRepository.save(championsResponse.getChampions().get("Pantheon"));
		Item item1 = itemsRepository.save(itemsResponse.getItems().get("3091"));
		Item item2 = itemsRepository.save(itemsResponse.getItems().get("3009"));
		Item item3 = itemsRepository.save(itemsResponse.getItems().get("3027"));
		Item item4 = itemsRepository.save(itemsResponse.getItems().get("3050"));
		Item item5 = itemsRepository.save(itemsResponse.getItems().get("3110"));
		Item item6 = itemsRepository.save(itemsResponse.getItems().get("3078"));
		SummonerSpell summonerSpell1 = summonerSpellsRepository.save(summonerSpellsResponse.getSummonerSpells().get("SummonerHaste"));
		SummonerSpell summonerSpell2 = summonerSpellsRepository.save(summonerSpellsResponse.getSummonerSpells().get("SummonerHeal"));
		Item trinket = itemsRepository.save(itemsRepository.save(itemsResponse.getItems().get("3340")));
		GameMap map = mapsRepository.save(mapsResponse.getMaps().get(SUMMONERS_RIFT_SID));

		Build build = new Build();
		build.setChampionId(pantheon.getId());
		build.setItem1Id(item1.getId());
		build.setItem2Id(item2.getId());
		build.setItem3Id(item3.getId());
		build.setItem4Id(item4.getId());
		build.setItem5Id(item5.getId());
		build.setItem6Id(item6.getId());
		build.setSummonerSpell1Id(summonerSpell1.getId());
		build.setSummonerSpell2Id(summonerSpell2.getId());
		build.setTrinketId(trinket.getId());
		build.setMapId(map.getMapId());
		build = buildsRepository.save(build);

		mockMvc.perform(get("/api/builds/{id}", build.getId()))
				.andExpect(status().isOk())
				.andDo(document("getBuild", pathParameters(
						parameterWithName("id")
								.description("The Id of the Build")), relaxedResponseFields(
						fieldWithPath("id")
								.description("The Id of the Build"),
						fieldWithPath("createdDate")
								.description("The date the Build was created"),
						fieldWithPath("championId")
								.description("The Champion Id of the Build"),
						fieldWithPath("item1Id")
								.description("The Item 1 Id (boots) of the Build"),
						fieldWithPath("item2Id")
								.description("The Item 2 Id of the Build"),
						fieldWithPath("item3Id")
								.description("The Item 3 Id of the Build"),
						fieldWithPath("item4Id")
								.description("The Item 4 Id of the Build"),
						fieldWithPath("item5Id")
								.description("The Item 5 Id of the Build"),
						fieldWithPath("item6Id")
								.description("The Item 6 Id of the Build"),
						fieldWithPath("summonerSpell1Id")
								.description("The Summoner Spell 1 Id of the Build"),
						fieldWithPath("summonerSpell2Id")
								.description("The Summoner Spell 2 Id of the Build"),
						fieldWithPath("trinketId")
								.description("The Trinket Id of the Build"),
						fieldWithPath("mapId")
								.description("The Map Id of the Build"))));
	}

}
