package com.drumonii.loltrollbuild.api;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.test.api.WebMvcRestTest;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.drumonii.loltrollbuild.util.GameMapUtil.HOWLING_ABYSS_ID;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcRestTest(ChampionsRestController.class)
abstract class ChampionsRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	protected ChampionsRepository championsRepository;

	@Autowired
	protected ItemsRepository itemsRepository;

	@Autowired
	protected SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	@Value("${api.base-path}")
	private String apiPath;

	protected ChampionsResponse championsResponse;

	protected abstract void beforeEach();

	@Test
	void getChampions() throws Exception {
		Champion champion = RandomizeUtil.getRandom(championsResponse.getChampions().values());

		// qbe
		mockMvc.perform(get("{apiPath}/champions", apiPath))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with name
		mockMvc.perform(get("{apiPath}/champions", apiPath)
				.param("name", champion.getName().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with title
		mockMvc.perform(get("{apiPath}/champions", apiPath)
				.param("title", champion.getTitle().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with partype
		mockMvc.perform(get("{apiPath}/champions", apiPath)
				.param("partype", champion.getPartype().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with tags
		mockMvc.perform(get("/api/champions")
				.param("tags", champion.getTags().iterator().next()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with no results
		mockMvc.perform(get("{apiPath}/champions", apiPath)
				.param("name", "abcd1234"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(content().json("[]"));
	}

	@Test
	void getChampion() throws Exception {
		// find with non existing champion Id
		mockMvc.perform(get("{apiPath}/champions/{id}", apiPath, 0))
				.andExpect(status().isNotFound());

		Champion poppy = championsResponse.getChampions().get("Poppy");

		// find with existing champion Id
		mockMvc.perform(get("{apiPath}/champions/{id}", apiPath, poppy.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// find with existing champion name
		mockMvc.perform(get("/api/champions/{id}", poppy.getName()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());
	}

	@Test
	void getTags() throws Exception {
		mockMvc.perform(get("{apiPath}/champions/tags", apiPath))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	void trollBuildWithChampionThatDoesNotExist() throws Exception {
		mockMvc.perform(get("{apiPath}/champions/{id}/troll-build", apiPath, 0))
				.andExpect(status().isNotFound());
	}

	@Test
	void trollBuild() throws Exception {
		Champion azir = championsResponse.getChampions().get("Azir");

		// get with champion Id and map specified
		mockMvc.perform(get("{apiPath}/champions/{id}/troll-build", apiPath, azir.getId())
				.param("mapId", String.valueOf(HOWLING_ABYSS_ID)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.items").exists())
				.andExpect(jsonPath("$.totalGold").exists())
				.andExpect(jsonPath("$.items.length()", is(6)))
				.andExpect(jsonPath("$.summonerSpells").exists())
				.andExpect(jsonPath("$.summonerSpells.length()", is(2)))
				.andExpect(jsonPath("$.trinket").exists());

		// get with champion name and map specified
		mockMvc.perform(get("/api/champions/{id}/troll-build", azir.getName())
				.param("mapId", String.valueOf(HOWLING_ABYSS_ID)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.items").exists())
				.andExpect(jsonPath("$.totalGold").exists())
				.andExpect(jsonPath("$.items.length()", is(6)))
				.andExpect(jsonPath("$.summonerSpells").exists())
				.andExpect(jsonPath("$.summonerSpells.length()", is(2)))
				.andExpect(jsonPath("$.trinket").exists());

		// get with champion Id and no map specified
		mockMvc.perform(get("{apiPath}/champions/{id}/troll-build", apiPath, azir.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.items").exists())
				.andExpect(jsonPath("$.items.length()", is(6)))
				.andExpect(jsonPath("$.summonerSpells").exists())
				.andExpect(jsonPath("$.summonerSpells.length()", is(2)))
				.andExpect(jsonPath("$.trinket").exists());

		// get with champion name and no map specified
		mockMvc.perform(get("/api/champions/{id}/troll-build", azir.getName()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.items").exists())
				.andExpect(jsonPath("$.totalGold").exists())
				.andExpect(jsonPath("$.items.length()", is(6)))
				.andExpect(jsonPath("$.summonerSpells").exists())
				.andExpect(jsonPath("$.summonerSpells.length()", is(2)))
				.andExpect(jsonPath("$.trinket").exists());
	}

	@Test
	void trollBuildForViktor() throws Exception {
		Champion viktor = championsResponse.getChampions().get("Viktor");

		// get with Viktor Id and map specified
		mockMvc.perform(get("{apiPath}/champions/{id}/troll-build", apiPath, viktor.getId())
				.param("mapId", String.valueOf(HOWLING_ABYSS_ID)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.items").exists())
				.andExpect(jsonPath("$.totalGold").exists())
				.andExpect(jsonPath("$.items.length()", is(6)))
				.andExpect(jsonPath("$.summonerSpells").exists())
				.andExpect(jsonPath("$.summonerSpells.length()", is(2)))
				.andExpect(jsonPath("$.trinket").exists());

		// get with Viktor name and map specified
		mockMvc.perform(get("{apiPath}/champions/{id}/troll-build", apiPath, viktor.getName())
				.param("mapId", String.valueOf(HOWLING_ABYSS_ID)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.items").exists())
				.andExpect(jsonPath("$.totalGold").exists())
				.andExpect(jsonPath("$.items.length()", is(6)))
				.andExpect(jsonPath("$.summonerSpells").exists())
				.andExpect(jsonPath("$.summonerSpells.length()", is(2)))
				.andExpect(jsonPath("$.trinket").exists());

		// get with Viktor Id and no map specified
		mockMvc.perform(get("{apiPath}/champions/{id}/troll-build", apiPath, viktor.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.items").exists())
				.andExpect(jsonPath("$.totalGold").exists())
				.andExpect(jsonPath("$.items.length()", is(6)))
				.andExpect(jsonPath("$.summonerSpells").exists())
				.andExpect(jsonPath("$.summonerSpells.length()", is(2)))
				.andExpect(jsonPath("$.trinket").exists());

		// get with Viktor name and no map specified
		mockMvc.perform(get("{apiPath}/champions/{id}/troll-build", apiPath, viktor.getName()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.items").exists())
				.andExpect(jsonPath("$.totalGold").exists())
				.andExpect(jsonPath("$.items.length()", is(6)))
				.andExpect(jsonPath("$.summonerSpells").exists())
				.andExpect(jsonPath("$.summonerSpells.length()", is(2)))
				.andExpect(jsonPath("$.trinket").exists());
	}

	@Test
	void trollBuildWithNoItems() throws Exception {
		itemsRepository.deleteAll();
		summonerSpellsRepository.deleteAll();

		Champion orianna = championsResponse.getChampions().get("Orianna");

		// get with champion Id and map specified
		mockMvc.perform(get("{apiPath}/champions/{id}/troll-build", apiPath, orianna.getId())
				.param("mapId", String.valueOf(HOWLING_ABYSS_ID)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.items").exists())
				.andExpect(jsonPath("$.totalGold").exists())
				.andExpect(jsonPath("$.items.length()", is(0)))
				.andExpect(jsonPath("$.summonerSpells").exists())
				.andExpect(jsonPath("$.summonerSpells.length()", is(0)))
				.andExpect(jsonPath("$.trinket", nullValue()));

		// get with champion name and map specified
		mockMvc.perform(get("/api/champions/{id}/troll-build", orianna.getName())
				.param("mapId", String.valueOf(HOWLING_ABYSS_ID)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.items").exists())
				.andExpect(jsonPath("$.totalGold").exists())
				.andExpect(jsonPath("$.items.length()", is(0)))
				.andExpect(jsonPath("$.summonerSpells").exists())
				.andExpect(jsonPath("$.summonerSpells.length()", is(0)))
				.andExpect(jsonPath("$.trinket", nullValue()));

		// get with champion Id and no map specified
		mockMvc.perform(get("{apiPath}/champions/{id}/troll-build", apiPath, orianna.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.items").exists())
				.andExpect(jsonPath("$.totalGold").exists())
				.andExpect(jsonPath("$.items.length()", is(0)))
				.andExpect(jsonPath("$.summonerSpells").exists())
				.andExpect(jsonPath("$.summonerSpells.length()", is(0)))
				.andExpect(jsonPath("$.trinket", nullValue()));

		// get with champion name and no map specified
		mockMvc.perform(get("/api/champions/{id}/troll-build", orianna.getName()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.items").exists())
				.andExpect(jsonPath("$.totalGold").exists())
				.andExpect(jsonPath("$.items.length()", is(0)))
				.andExpect(jsonPath("$.summonerSpells").exists())
				.andExpect(jsonPath("$.summonerSpells.length()", is(0)))
				.andExpect(jsonPath("$.trinket", nullValue()));
	}

}