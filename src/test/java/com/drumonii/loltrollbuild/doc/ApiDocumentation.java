package com.drumonii.loltrollbuild.doc;

import com.drumonii.loltrollbuild.model.*;
import com.drumonii.loltrollbuild.repository.*;
import com.drumonii.loltrollbuild.rest.*;
import com.drumonii.loltrollbuild.rest.processor.*;
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
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.drumonii.loltrollbuild.config.Profiles.STATIC_DATA;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static com.drumonii.loltrollbuild.util.GameMapUtil.HOWLING_ABYSS_SID;
import static com.drumonii.loltrollbuild.util.GameMapUtil.SUMMONERS_RIFT_SID;
import static com.drumonii.loltrollbuild.util.GameMapUtil.TWISTED_TREELINE_SID;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.relaxedLinks;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcRestTest( // below is a bit hard on the eyes...i apologize
		controllers = { BuildsRestController.class, ChampionsRestController.class, ItemsRestController.class,
				MapsRestController.class, SummonerSpellsRestController.class, VersionsRestController.class },
		includeFilters = { @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = BuildResourceProcessor.class),
				@Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ChampionResourceProcessor.class),
				@Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ItemResourceProcessor.class),
				@Filter(type = FilterType.ASSIGNABLE_TYPE, classes = MapResourceProcessor.class),
				@Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SummonerSpellResourceProcessor.class),
				@Filter(type = FilterType.ASSIGNABLE_TYPE, classes = VersionResourceProcessor.class) })
@AutoConfigureRestDocs(outputDir = "build/generated-snippets", uriScheme = "https", uriHost = "loltrollbuild.com",
		uriPort = 443)
@TestPropertySource(properties = "riot.static-data.apiKey=API_KEY")
@ActiveProfiles({ TESTING, STATIC_DATA })
public class ApiDocumentation {

	private final FieldDescriptor pageField =
			fieldWithPath("page").description("Current page settings of the paginated results");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${spring.data.rest.base-path}")
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
		ClassPathResource championsJsonResource = new ClassPathResource("champions_static_data.json");
		try {
			championsResponse = objectMapper.readValue(championsJsonResource.getFile(), ChampionsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Champions response.", e);
		}
		ClassPathResource itemsJsonResource = new ClassPathResource("items_static_data.json");
		try {
			itemsResponse = objectMapper.readValue(itemsJsonResource.getFile(), ItemsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Items response.", e);
		}
		ClassPathResource mapsJsonResource = new ClassPathResource("maps_static_data.json");
		try {
			mapsResponse = objectMapper.readValue(mapsJsonResource.getFile(), MapsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Maps response.", e);
		}
		ClassPathResource summonerSpellsJson = new ClassPathResource("summoners_static_data.json");
		try {
			summonerSpellsResponse = objectMapper.readValue(summonerSpellsJson.getFile(), SummonerSpellsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Summoner Spells response.", e);
		}
		ClassPathResource versionsJson = new ClassPathResource("versions_static_data.json");
		try {
			versions = objectMapper.readValue(versionsJson.getFile(), new TypeReference<List<Version>>() {});
			versions.sort(Collections.reverseOrder());
		} catch (IOException e) {
			fail("Unable to unmarshal the Versions response.", e);
		}
	}

	@Test
	public void index() throws Exception {
		mockMvc.perform(get(apiPath))
				.andExpect(status().isOk())
				.andDo(document("index", relaxedLinks(
						linkWithRel("summonerSpells").description("<<resources-summoner-spells, Summoner Spells resource>>"),
						linkWithRel("items").description("<<resources-items, Items resource>>"),
						linkWithRel("champions").description("<<resources-champions, Champions resource>>"),
						linkWithRel("maps").description("<<resources-maps, Maps resource>>"),
						linkWithRel("versions").description("<<resources-versions, Versions resource>>"),
						linkWithRel("builds").description("<<resources-builds, Builds resource>>"))));
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
						pageField)));

		summonerSpellsRepository.save(summonerSpellsResponse.getSummonerSpells().get("SummonerPoroThrow"));

		mockMvc.perform(get("{apiPath}/summoner-spells?name={name}", apiPath, "poro"))
				.andExpect(status().isOk())
				.andDo(document("summonerSpellsFindBy", relaxedResponseFields(
						fieldWithPath("_embedded.summonerSpells")
								.description("An array of Summoner Spells").attributes(),
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
						pageField)));
	}

	@Test
	public void getSummonerSpell() throws Exception {
		SummonerSpell teleport = summonerSpellsRepository.save(summonerSpellsResponse.getSummonerSpells()
				.get("SummonerTeleport"));

		mockMvc.perform(get("{apiPath}/summoner-spells/{id}", apiPath, teleport.getId()))
				.andExpect(status().isOk())
				.andDo(document("getSummonerSpell", relaxedResponseFields(
						fieldWithPath("name")
								.description("The name of the Summoner Spell"),
						fieldWithPath("description")
								.description("The description of the Summoner Spell"),
						fieldWithPath("image")
								.description("The image of the Summoner Spell"),
						fieldWithPath("cooldown")
								.description("An array of cooldown values of the Summoner Spell"),
						fieldWithPath("key")
								.description("The key of the Summoner Spell"),
						fieldWithPath("modes")
								.description("An array of game modes eligible with the Summoner Spell"),
						fieldWithPath("_links")
								.description("Link to the self Summoner Spell"))));

	}

	@Test
	public void summonerSpellsForTrollBuild() throws Exception {
		summonerSpellsRepository.save(summonerSpellsResponse.getSummonerSpells().get("SummonerSmite"));

		mockMvc.perform(get("{apiPath}/summoner-spells/for-troll-build?mode={mode}", apiPath, "CLASSIC"))
				.andExpect(status().isOk())
				.andDo(document("summonerSpellsForTrollBuild", relaxedResponseFields(
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
						fieldWithPath("_embedded.summonerSpells[*].key")
								.description("The key of the Summoner Spell"),
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

		mockMvc.perform(get("{apiPath}/items", apiPath))
				.andExpect(status().isOk())
				.andDo(document("getItems", relaxedResponseFields(
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
						pageField)));

		itemsRepository.save(itemsResponse.getItems().get("3069"));

		mockMvc.perform(get("{apiPath}/items?name={name}", apiPath, "remnant"))
				.andExpect(status().isOk())
				.andDo(document("itemsFindBy", relaxedResponseFields(
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
						pageField)));
	}

	@Test
	public void getItem() throws Exception {
		Item warmogs = itemsRepository.save(itemsResponse.getItems().get("3083"));

		mockMvc.perform(get("{apiPath}/items/{id}", apiPath, warmogs.getId()))
				.andExpect(status().isOk())
				.andDo(document("getItem", relaxedResponseFields(
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

		mockMvc.perform(get("{apiPath}/items/boots?mapId={mapId}", apiPath, "11"))
				.andExpect(status().isOk())
				.andDo(document("bootsItems", relaxedResponseFields(
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

		mockMvc.perform(get("{apiPath}/items/trinkets?mapId={mapId}", apiPath, "11"))
				.andExpect(status().isOk())
				.andDo(document("trinketItems", relaxedResponseFields(
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

		mockMvc.perform(get("{apiPath}/items/viktor-only", apiPath))
				.andExpect(status().isOk())
				.andDo(document("viktorOnlyItems", relaxedResponseFields(
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
		itemsRepository.save(itemsResponse.getItems().get("3065"));

		mockMvc.perform(get("{apiPath}/items/for-troll-build?mapId={mapId}", apiPath, "11"))
				.andExpect(status().isOk())
				.andDo(document("itemsForTrollBuild", relaxedResponseFields(
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

		mockMvc.perform(get("{apiPath}/champions", apiPath))
				.andExpect(status().isOk())
				.andDo(document("getChampions", relaxedResponseFields(
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
						pageField)));

		championsRepository.save(championsResponse.getChampions().get("Blitzcrank"));

		mockMvc.perform(get("{apiPath}/champions?name={name}", apiPath, "blitz"))
				.andExpect(status().isOk())
				.andDo(document("championsFindBy", relaxedResponseFields(
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
						pageField)));
	}

	@Test
	public void getChampion() throws Exception {
		Champion talon = championsRepository.save(championsResponse.getChampions().get("Talon"));

		mockMvc.perform(get("{apiPath}/champions/{id}", apiPath, talon.getId()))
				.andExpect(status().isOk())
				.andDo(document("getChampion", relaxedResponseFields(
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

	@Test
	public void getTrollBuildForChampion() throws Exception {
		Champion jayce = championsRepository.save(championsResponse.getChampions().get("Jayce"));

		itemsRepository.save(itemsResponse.getItems().get("3341")); // Sweeping Lens (Trinket)

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
						fieldWithPath("summoner-spells")
								.description("The Summoner Spells of the Troll Build"),
						fieldWithPath("items")
								.description("The Items of the Troll Build, with the first always a boots Item"))));
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
						fieldWithPath("_embedded.maps")
								.description("An array of Maps"),
						fieldWithPath("_embedded.maps[*].mapName")
								.description("The name of the Map"),
						fieldWithPath("_embedded.maps[*].image")
								.description("The image of the Map"),
						fieldWithPath("_links")
								.description("Links to resources related to Maps"),
						pageField)));

		mapsRepository.save(mapsResponse.getMaps().get(TWISTED_TREELINE_SID));

		mockMvc.perform(get("{apiPath}/maps?mapName={mapName}", apiPath, "treeline"))
				.andExpect(status().isOk())
				.andDo(document("mapsFindBy", relaxedResponseFields(
						fieldWithPath("_embedded.maps")
								.description("An array of Maps"),
						fieldWithPath("_embedded.maps[*].mapName")
								.description("The name of the Map"),
						fieldWithPath("_embedded.maps[*].image")
								.description("The image of the Map"),
						fieldWithPath("_links")
								.description("Links to resources related to Maps"),
						pageField)));
	}

	@Test
	public void getMap() throws Exception {
		GameMap summonersRift = mapsResponse.getMaps().get(HOWLING_ABYSS_SID);
		mapsRepository.save(summonersRift);

		mockMvc.perform(get("{apiPath}/maps/{id}", apiPath, summonersRift.getMapId()))
				.andExpect(status().isOk())
				.andDo(document("getMap", relaxedResponseFields(
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

		mockMvc.perform(get("{apiPath}/versions", apiPath))
				.andExpect(status().isOk())
				.andDo(document("getVersions", relaxedResponseFields(
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
						pageField)));
	}

	@Test
	public void getVersion() throws Exception {
		Version version = versionsRepository.save(versions.get(0));

		mockMvc.perform(get("{apiPath}/versions/{version}", apiPath, version.getPatch()))
				.andExpect(status().isOk())
				.andDo(document("getVersion", relaxedResponseFields(
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
		build.setSummonerSpell1Id(summonerSpellsResponse.getSummonerSpells().get("SummonerDot").getId());
		build.setSummonerSpell2Id(summonerSpellsResponse.getSummonerSpells().get("SummonerTeleport").getId());
		build.setTrinketId(itemsResponse.getItems().get("3341").getId());
		build.setMapId(mapsResponse.getMaps().get(SUMMONERS_RIFT_SID).getMapId());
		buildsRepository.save(build);

		mockMvc.perform(get("{apiPath}/builds", apiPath))
				.andExpect(status().isOk())
				.andDo(document("getBuilds", relaxedResponseFields(
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
						pageField)));
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
		build.setMapId(mapsResponse.getMaps().get(SUMMONERS_RIFT_SID).getMapId());
		build = buildsRepository.save(build);

		mockMvc.perform(get("{apiPath}/builds/{id}", apiPath, build.getId()))
				.andExpect(status().isOk())
				.andDo(document("getBuild", relaxedResponseFields(
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
