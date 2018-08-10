package com.drumonii.loltrollbuild.doc;

import com.drumonii.loltrollbuild.model.*;
import com.drumonii.loltrollbuild.repository.*;
import com.drumonii.loltrollbuild.rest.*;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.drumonii.loltrollbuild.test.rest.WebMvcRestTest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static com.drumonii.loltrollbuild.util.GameMapUtil.HOWLING_ABYSS_SID;
import static com.drumonii.loltrollbuild.util.GameMapUtil.SUMMONERS_RIFT_SID;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcRestTest({ BuildsRestController.class, ChampionsRestController.class, ItemsRestController.class,
		MapsRestController.class, SummonerSpellsRestController.class, VersionsRestController.class })
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "loltrollbuild.com", uriPort = 443)
@ActiveProfiles({ TESTING, DDRAGON })
public class ApiDocumentation {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${api.base-path}")
	private String apiPath;

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

	@Before
	public void before() {
		ClassPathResource championsJsonResource = new ClassPathResource("champions_data_dragon.json");
		try {
			championsResponse = objectMapper.readValue(championsJsonResource.getFile(), ChampionsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Champions response.", e);
		}
		ClassPathResource itemsJsonResource = new ClassPathResource("items_data_dragon.json");
		try {
			itemsResponse = objectMapper.readValue(itemsJsonResource.getFile(), ItemsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Items response.", e);
		}
		ClassPathResource mapsJsonResource = new ClassPathResource("maps_data_dragon.json");
		try {
			mapsResponse = objectMapper.readValue(mapsJsonResource.getFile(), MapsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Maps response.", e);
		}
		ClassPathResource summonerSpellsJson = new ClassPathResource("summoners_data_dragon.json");
		try {
			summonerSpellsResponse = objectMapper.readValue(summonerSpellsJson.getFile(), SummonerSpellsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Summoner Spells response.", e);
		}
		ClassPathResource versionsJson = new ClassPathResource("versions_data_dragon.json");
		try {
			versions = objectMapper.readValue(versionsJson.getFile(), new TypeReference<List<Version>>() {});
			versions.sort(Collections.reverseOrder());
		} catch (IOException e) {
			fail("Unable to unmarshal the Versions response.", e);
		}
	}

	/*
	 * Summoner Spells doc
	 */

	@Test
	public void getSummonerSpells() throws Exception {
		summonerSpellsRepository.save(summonerSpellsResponse.getSummonerSpells().get("SummonerExhaust"));

		mockMvc.perform(get("{apiPath}/summoner-spells", apiPath))
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
	public void getSummonerSpell() throws Exception {
		SummonerSpell teleport = summonerSpellsRepository.save(summonerSpellsResponse.getSummonerSpells()
				.get("SummonerTeleport"));

		mockMvc.perform(get("{apiPath}/summoner-spells/{id}", apiPath, teleport.getId()))
				.andExpect(status().isOk())
				.andDo(document("getSummonerSpell", relaxedResponseFields(
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
	public void summonerSpellsForTrollBuild() throws Exception {
		summonerSpellsRepository.save(summonerSpellsResponse.getSummonerSpells().get("SummonerSmite"));

		mockMvc.perform(get("{apiPath}/summoner-spells/for-troll-build", apiPath)
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
	public void getItems() throws Exception {
		itemsRepository.save(itemsResponse.getItems().get("3512"));

		mockMvc.perform(get("{apiPath}/items", apiPath))
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
	public void getItem() throws Exception {
		Item warmogs = itemsRepository.save(itemsResponse.getItems().get("3083"));

		mockMvc.perform(get("{apiPath}/items/{id}", apiPath, warmogs.getId()))
				.andExpect(status().isOk())
				.andDo(document("getItem", relaxedResponseFields(
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
	public void bootsItems() throws Exception {
		itemsRepository.save(itemsResponse.getItems().get("3158"));

		mockMvc.perform(get("{apiPath}/items/boots", apiPath)
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
	public void trinketItems() throws Exception {
		itemsRepository.save(itemsResponse.getItems().get("3364"));

		mockMvc.perform(get("{apiPath}/items/trinkets", apiPath)
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
	public void viktorOnlyItems() throws Exception {
		itemsRepository.save(itemsResponse.getItems().get("3200"));

		mockMvc.perform(get("{apiPath}/items/viktor-only", apiPath))
				.andExpect(status().isOk())
				.andDo(document("viktorOnlyItems", relaxedResponseFields(
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
	public void itemsForTrollBuild() throws Exception {
		itemsRepository.save(itemsResponse.getItems().get("3065"));

		mockMvc.perform(get("{apiPath}/items/for-troll-build", apiPath)
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
	public void getChampions() throws Exception {
		championsRepository.save(championsResponse.getChampions().get("Warwick"));

		mockMvc.perform(get("{apiPath}/champions", apiPath))
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
						fieldWithPath("[*].passive")
								.description("The passive ability of the Champion"),
						fieldWithPath("[*].info")
								.description("Statistical information of the Champion"),
						fieldWithPath("[*].spells")
								.description("Spell abilities of the Champion"),
						fieldWithPath("[*].tags")
								.description("An array of tags of the Champion"))));
	}

	@Test
	public void getChampion() throws Exception {
		Champion talon = championsRepository.save(championsResponse.getChampions().get("Talon"));

		mockMvc.perform(get("{apiPath}/champions/{id}", apiPath, talon.getId()))
				.andExpect(status().isOk())
				.andDo(document("getChampion", relaxedResponseFields(
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
						fieldWithPath("info")
								.description("Statistical information of the Champion"),
						fieldWithPath("spells")
								.description("Spell abilities of the Champion"),
						fieldWithPath("passive")
								.description("The passive ability of the Champion"),
						fieldWithPath("tags")
								.description("An array of tags of the Champion"))));
	}

	@Test
	public void getTags() throws Exception {
		championsRepository.save(championsResponse.getChampions().get("Nocturne"));

		mockMvc.perform(get("{apiPath}/champions/tags", apiPath))
				.andExpect(status().isOk())
				.andDo(document("getChampionTags"));
	}

	@Test
	public void getTrollBuildForChampion() throws Exception {
		Champion jayce = championsRepository.save(championsResponse.getChampions().get("Jayce"));

		itemsRepository.save(itemsResponse.getItems().get("3340")); // Warding Totem (Trinket)

		summonerSpellsRepository.save(summonerSpellsResponse.getSummonerSpells().get("SummonerFlash"));
		summonerSpellsRepository.save(summonerSpellsResponse.getSummonerSpells().get("SummonerMana"));

		itemsRepository.save(itemsResponse.getItems().get("3020")); // Sorcerer's Shoes
		itemsRepository.save(itemsResponse.getItems().get("3004")); // Manamune
		itemsRepository.save(itemsResponse.getItems().get("3742")); // Dead Man's Plate
		itemsRepository.save(itemsResponse.getItems().get("3074")); // Ravenous Hydra
		itemsRepository.save(itemsResponse.getItems().get("3116")); // Rylai's Crystal Scepter
		itemsRepository.save(itemsResponse.getItems().get("3401")); // Face of the Mountain

		mockMvc.perform(get("{apiPath}/champions/{id}/troll-build", apiPath, jayce.getId()))
				.andExpect(status().isOk())
				.andDo(document("getTrollBuildForChampion", requestParameters(
						parameterWithName("mapId")
								.description("The Map Id. Defaults to Summoner's Rift if unspecified. See <<game-maps-table, Game Maps Table>> for Map IDs")
								.optional()), relaxedResponseFields(
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
	public void getMaps() throws Exception {
		mapsRepository.save(mapsResponse.getMaps().get(SUMMONERS_RIFT_SID));

		mockMvc.perform(get("{apiPath}/maps", apiPath))
				.andExpect(status().isOk())
				.andDo(document("getMaps", relaxedResponseFields(
						fieldWithPath("[*].mapId")
								.description("The Id of the Map"),
						fieldWithPath("[*].mapName")
								.description("The name of the Map"))));
	}

	@Test
	public void getMap() throws Exception {
		GameMap summonersRift = mapsResponse.getMaps().get(HOWLING_ABYSS_SID);
		mapsRepository.save(summonersRift);

		mockMvc.perform(get("{apiPath}/maps/{id}", apiPath, summonersRift.getMapId()))
				.andExpect(status().isOk())
				.andDo(document("getMap", relaxedResponseFields(
						fieldWithPath("mapId")
								.description("The Id of the Map"),
						fieldWithPath("mapName")
								.description("The name of the Map"))));
	}

	/*
	 * Versions doc
	 */

	@Test
	public void getVersions() throws Exception {
		versionsRepository.save(versions.get(0));

		mockMvc.perform(get("{apiPath}/versions", apiPath))
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
	public void getVersion() throws Exception {
		Version version = versionsRepository.save(versions.get(0));

		mockMvc.perform(get("{apiPath}/versions/{version}/", apiPath, version.getPatch()))
				.andExpect(status().isOk())
				.andDo(document("getVersion", relaxedResponseFields(
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

	@Test
	public void getBuilds() throws Exception {
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

		mockMvc.perform(get("{apiPath}/builds", apiPath))
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

	@Test
	public void getBuild() throws Exception {
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

		mockMvc.perform(get("{apiPath}/builds/{id}", apiPath, build.getId()))
				.andExpect(status().isOk())
				.andDo(document("getBuild", relaxedResponseFields(
						fieldWithPath(".id")
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
