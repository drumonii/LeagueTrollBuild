package com.drumonii.loltrollbuild;

import com.drumonii.loltrollbuild.model.*;
import com.drumonii.loltrollbuild.repository.*;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

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
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	private MapsRepository mapsRepository;

	@Autowired
	private BuildsRepository buildsRepository;

	@Autowired
	private VersionsRepository versionsRepository;

	@After
	public void after() {
		summonerSpellsRepository.deleteAll();
		itemsRepository.deleteAll();
		championsRepository.deleteAll();
		mapsRepository.deleteAll();
		versionsRepository.deleteAll();
		buildsRepository.deleteAll();
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
						linkWithRel("builds").description("<<resources-builds, Builds resource>>"),
						linkWithRel("profile").description("Profile resource"))));
	}

	/*
	 * Summoner Spells doc
	 */

	@Test
	public void getSummonerSpells() throws Exception {
		summonerSpellsRepository.save(summonerSpellsResponse.getSummonerSpells().get("SummonerExhaust"));

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

		summonerSpellsRepository.save(summonerSpellsResponse.getSummonerSpells().get("SummonerPoroThrow"));

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
		SummonerSpell teleport = summonerSpellsRepository.save(summonerSpellsResponse.getSummonerSpells()
				.get("SummonerTeleport"));

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
		summonerSpellsRepository.save(summonerSpellsResponse.getSummonerSpells().get("SummonerSmite"));

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
		itemsRepository.save(itemsResponse.getItems().get("3512"));

		mockMvc.perform(get(apiPath + "/items"))
				.andExpect(status().isOk())
				.andDo(document("getItems", responseFields(
						fieldWithPath("_embedded.items")
								.description("An array of Items"),
						fieldWithPath("_embedded.items[*].name")
								.description("The name of the Item"),
						fieldWithPath("_embedded.items[*].group")
								.description("The group of the Item"),
						fieldWithPath("_embedded.items[*].consumed")
								.description("Whether the Item is consumable"),
						fieldWithPath("_embedded.items[*].description")
								.description("The group of the Item"),
						fieldWithPath("_embedded.items[*].from")
								.description("An array of Item IDs that the Item is built from as part of the Item's recipe"),
						fieldWithPath("_embedded.items[*].into")
								.description("An array of Item IDs that the Item builds into as part of the Item's recipe"),
						fieldWithPath("_embedded.items[*].requiredChampion")
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

		itemsRepository.save(itemsResponse.getItems().get("3069"));

		mockMvc.perform(get(apiPath + "/items?name={name}", "talisman"))
				.andExpect(status().isOk())
				.andDo(document("itemsFindBy", responseFields(
						fieldWithPath("_embedded.items")
								.description("An array of Items"),
						fieldWithPath("_embedded.items[*].name")
								.description("The name of the Item"),
						fieldWithPath("_embedded.items[*].group")
								.description("The group of the Item"),
						fieldWithPath("_embedded.items[*].consumed")
								.description("Whether the Item is consumable"),
						fieldWithPath("_embedded.items[*].description")
								.description("The group of the Item"),
						fieldWithPath("_embedded.items[*].from")
								.description("An array of Item IDs that the Item is built from as part of the Item's recipe"),
						fieldWithPath("_embedded.items[*].into")
								.description("An array of Item IDs that the Item builds into as part of the Item's recipe"),
						fieldWithPath("_embedded.items[*].requiredChampion")
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
		Item warmogs = itemsRepository.save(itemsResponse.getItems().get("3083"));

		mockMvc.perform(get(apiPath + "/items/{id}", warmogs.getId()))
				.andExpect(status().isOk())
				.andDo(document("getItem", responseFields(
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
		itemsRepository.save(itemsResponse.getItems().get("3158"));

		mockMvc.perform(get(apiPath + "/items/boots?mapId={mapId}", "11"))
				.andExpect(status().isOk())
				.andDo(document("bootsItems", responseFields(
						fieldWithPath("_embedded.items")
								.description("An array of Items"),
						fieldWithPath("_embedded.items[*].name")
								.description("The name of the Item"),
						fieldWithPath("_embedded.items[*].group")
								.description("The group of the Item"),
						fieldWithPath("_embedded.items[*].consumed")
								.description("Whether the Item is consumable"),
						fieldWithPath("_embedded.items[*].description")
								.description("The group of the Item"),
						fieldWithPath("_embedded.items[*].from")
								.description("An array of Item IDs that the Item is built from as part of the Item's recipe"),
						fieldWithPath("_embedded.items[*].into")
								.description("An array of Item IDs that the Item builds into as part of the Item's recipe"),
						fieldWithPath("_embedded.items[*].requiredChampion")
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
		itemsRepository.save(itemsResponse.getItems().get("3341"));

		mockMvc.perform(get(apiPath + "/items/trinkets?mapId={mapId}", "11"))
				.andExpect(status().isOk())
				.andDo(document("trinketItems", responseFields(
						fieldWithPath("_embedded.items")
								.description("An array of Items"),
						fieldWithPath("_embedded.items[*].name")
								.description("The name of the Item"),
						fieldWithPath("_embedded.items[*].group")
								.description("The group of the Item"),
						fieldWithPath("_embedded.items[*].consumed")
								.description("Whether the Item is consumable"),
						fieldWithPath("_embedded.items[*].description")
								.description("The group of the Item"),
						fieldWithPath("_embedded.items[*].from")
								.description("An array of Item IDs that the Item is built from as part of the Item's recipe"),
						fieldWithPath("_embedded.items[*].into")
								.description("An array of Item IDs that the Item builds into as part of the Item's recipe"),
						fieldWithPath("_embedded.items[*].requiredChampion")
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
		itemsRepository.save(itemsResponse.getItems().get("3200"));

		mockMvc.perform(get(apiPath + "/items/viktor-only"))
				.andExpect(status().isOk())
				.andDo(document("viktorOnlyItems", responseFields(
						fieldWithPath("_embedded.items")
								.description("An array of Items"),
						fieldWithPath("_embedded.items[*].name")
								.description("The name of the Item"),
						fieldWithPath("_embedded.items[*].group")
								.description("The group of the Item"),
						fieldWithPath("_embedded.items[*].consumed")
								.description("Whether the Item is consumable"),
						fieldWithPath("_embedded.items[*].description")
								.description("The group of the Item"),
						fieldWithPath("_embedded.items[*].from")
								.description("An array of Item IDs that the Item is built from as part of the Item's recipe"),
						fieldWithPath("_embedded.items[*].into")
								.description("An array of Item IDs that the Item builds into as part of the Item's recipe"),
						fieldWithPath("_embedded.items[*].requiredChampion")
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
		itemsRepository.save(itemsResponse.getItems().get("3056"));

		mockMvc.perform(get(apiPath + "/items/for-troll-build?mapId={mapId}", "11"))
				.andExpect(status().isOk())
				.andDo(document("itemsForTrollBuild", responseFields(
						fieldWithPath("_embedded.items")
								.description("An array of Items"),
						fieldWithPath("_embedded.items[*].name")
								.description("The name of the Item"),
						fieldWithPath("_embedded.items[*].group")
								.description("The group of the Item"),
						fieldWithPath("_embedded.items[*].consumed")
								.description("Whether the Item is consumable"),
						fieldWithPath("_embedded.items[*].description")
								.description("The group of the Item"),
						fieldWithPath("_embedded.items[*].from")
								.description("An array of Item IDs that the Item is built from as part of the Item's recipe"),
						fieldWithPath("_embedded.items[*].into")
								.description("An array of Item IDs that the Item builds into as part of the Item's recipe"),
						fieldWithPath("_embedded.items[*].requiredChampion")
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
		championsRepository.save(championsResponse.getChampions().get("Warwick"));

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
						fieldWithPath("_embedded.champions[*].partype")
								.description("The ability resource of the Champion"),
						fieldWithPath("_embedded.champions[*].passive")
								.description("The passive ability of the Champion"),
						fieldWithPath("_embedded.champions[*].info")
								.description("Statistical information of the Champion"),
						fieldWithPath("_embedded.champions[*].spells")
								.description("Spell abilities of the Champion"),
						fieldWithPath("_embedded.champions[*].image")
								.description("The image of the Champion"),
						fieldWithPath("_embedded.champions[*].tags")
								.description("An array of tags of the Champion"),
						fieldWithPath("_links")
								.description("Links to resources related to Champions"),
						fieldWithPath("page")
								.description("Current Page settings of the pagination"))));

		championsRepository.save(championsResponse.getChampions().get("Blitzcrank"));

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
						fieldWithPath("_embedded.champions[*].info")
								.description("Statistical information of the Champion"),
						fieldWithPath("_embedded.champions[*].spells")
								.description("Spell abilities of the Champion"),
						fieldWithPath("_embedded.champions[*].passive")
								.description("The passive ability of the Champion"),
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
		Champion talon = championsRepository.save(championsResponse.getChampions().get("Talon"));

		mockMvc.perform(get(apiPath + "/champions/{id}", talon.getId()))
				.andExpect(status().isOk())
				.andDo(document("getChampion", responseFields(
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
						fieldWithPath("image")
								.description("The image of the Champion"),
						fieldWithPath("tags")
								.description("An array of tags of the Champion"),
						fieldWithPath("_links")
								.description("Link to the self Champion"))));
	}

	/*
	 * Maps doc
	 */

	@Test
	public void getMaps() throws Exception {
		mapsRepository.save(mapsResponse.getMaps().get(String.valueOf(SUMMONERS_RIFT)));

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

		mapsRepository.save(mapsResponse.getMaps().get(String.valueOf(TWISTED_TREELINE)));

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
		GameMap summonersRift = mapsResponse.getMaps().get(String.valueOf(HOWLING_ABYSS));
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
		versionsRepository.save(versions.get(0));

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
								.description("Links to resources related to Version"),
						fieldWithPath("page")
								.description("Current Page settings of the pagination"))));
	}

	@Test
	public void getVersion() throws Exception {
		Version version = versionsRepository.save(versions.get(0));

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
		build.setSummonerSpell1Id(summonerSpellsResponse.getSummonerSpells().get("SummonerSmite").getId());
		build.setSummonerSpell2Id(summonerSpellsResponse.getSummonerSpells().get("SummonerTeleport").getId());
		build.setTrinketId(itemsResponse.getItems().get("3341").getId());
		build.setMapId(mapsResponse.getMaps().get(SUMMONERS_RIFT).getMapId());
		buildsRepository.save(build);

		mockMvc.perform(get(apiPath + "/builds"))
				.andExpect(status().isOk())
				.andDo(document("getBuilds", responseFields(
						fieldWithPath("_embedded.builds")
								.description("An array of Builds"),
						fieldWithPath("_embedded.builds[*].createdDate")
								.description("The date the Build was created"),
						fieldWithPath("_embedded.builds[*].champion")
								.description("The Champion Id of the Build"),
						fieldWithPath("_embedded.builds[*].item1")
								.description("The Item 1 Id (boots) of the Build"),
						fieldWithPath("_embedded.builds[*].item2")
								.description("The Item 2 Id of the Build"),
						fieldWithPath("_embedded.builds[*].item3")
								.description("The Item 3 Id of the Build"),
						fieldWithPath("_embedded.builds[*].item4")
								.description("The Item 4 Id of the Build"),
						fieldWithPath("_embedded.builds[*].item5")
								.description("The Item 5 Id of the Build"),
						fieldWithPath("_embedded.builds[*].item6")
								.description("The Item 6 Id of the Build"),
						fieldWithPath("_embedded.builds[*].summonerSpell1")
								.description("The Summoner Spell 1 Id of the Build"),
						fieldWithPath("_embedded.builds[*].summonerSpell2")
								.description("The Summoner Spell 2 Id of the Build"),
						fieldWithPath("_embedded.builds[*].trinket")
								.description("The Trinket Id of the Build"),
						fieldWithPath("_embedded.builds[*].map")
								.description("The Map Id of the Build"),
						fieldWithPath("_links")
								.description("Links to resources related to Builds"),
						fieldWithPath("page")
								.description("Current Page settings of the pagination"))));
	}

	@Test
	public void getBuild() throws Exception {
		Build build = new Build();
		build.setChampionId(championsResponse.getChampions().get("Pantheon").getId());
		build.setItem1Id(itemsResponse.getItems().get("3091").getId());
		build.setItem2Id(itemsResponse.getItems().get("3009").getId());
		build.setItem3Id(itemsResponse.getItems().get("3027").getId());
		build.setItem4Id(itemsResponse.getItems().get("3050").getId());
		build.setItem5Id(itemsResponse.getItems().get("3110").getId());
		build.setItem6Id(itemsResponse.getItems().get("3078").getId());
		build.setSummonerSpell1Id(summonerSpellsResponse.getSummonerSpells().get("SummonerHaste").getId());
		build.setSummonerSpell2Id(summonerSpellsResponse.getSummonerSpells().get("SummonerHeal").getId());
		build.setTrinketId(itemsResponse.getItems().get("3341").getId());
		build.setMapId(mapsResponse.getMaps().get(SUMMONERS_RIFT).getMapId());
		build = buildsRepository.save(build);

		mockMvc.perform(get(apiPath + "/builds/{id}", build.getId()))
				.andExpect(status().isOk())
				.andDo(document("getBuild", responseFields(
						fieldWithPath("createdDate")
								.description("The date the Build was created"),
						fieldWithPath("champion")
								.description("The Champion Id of the Build"),
						fieldWithPath("item1")
								.description("The Item 1 Id (boots) of the Build"),
						fieldWithPath("item2")
								.description("The Item 2 Id of the Build"),
						fieldWithPath("item3")
								.description("The Item 3 Id of the Build"),
						fieldWithPath("item4")
								.description("The Item 4 Id of the Build"),
						fieldWithPath("item5")
								.description("The Item 5 Id of the Build"),
						fieldWithPath("item6")
								.description("The Item 6 Id of the Build"),
						fieldWithPath("summonerSpell1")
								.description("The Summoner Spell 1 Id of the Build"),
						fieldWithPath("summonerSpell2")
								.description("The Summoner Spell 2 Id of the Build"),
						fieldWithPath("trinket")
								.description("The Trinket Id of the Build"),
						fieldWithPath("map")
								.description("The Map Id of the Build"),
						fieldWithPath("_links")
								.description("Links to resources related to Builds"))));
	}

}
