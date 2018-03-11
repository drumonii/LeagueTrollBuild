package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.test.rest.WebMvcRestTest;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.drumonii.loltrollbuild.util.GameMapUtil.HOWLING_ABYSS_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcRestTest(controllers = ChampionsRestController.class)
public abstract class ChampionsRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	protected ChampionsRepository championsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	@Value("${api.base-path}")
	private String apiPath;

	protected ChampionsResponse championsResponse;

	public abstract void before();

	@Test
	public void getChampions() throws Exception {
		Champion champion = RandomizeUtil.getRandom(championsResponse.getChampions().values());

		// qbe
		mockMvc.perform(get("{apiPath}/champions", apiPath))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with name
		mockMvc.perform(get("{apiPath}/champions", apiPath)
				.param("name", champion.getName().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with title
		mockMvc.perform(get("{apiPath}/champions", apiPath)
				.param("title", champion.getTitle().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with partype
		mockMvc.perform(get("{apiPath}/champions", apiPath)
				.param("partype", champion.getPartype().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with tags
		mockMvc.perform(get("/api/champions")
				.param("tags", champion.getTags().iterator().next()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with no results
		mockMvc.perform(get("{apiPath}/champions", apiPath)
				.param("name", "abcd1234"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(content().json("[]"));
	}

	@Test
	public void getChampion() throws Exception {
		// find with non existing champion Id
		mockMvc.perform(get("{apiPath}/champions/{id}", apiPath, 0))
				.andExpect(status().isNotFound());

		Champion poppy = championsResponse.getChampions().get("Poppy");

		// find with existing champion Id
		mockMvc.perform(get("{apiPath}/champions/{id}", apiPath, poppy.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());
	}

	@Test
	public void getTags() throws Exception {
		mockMvc.perform(get("{apiPath}/champions/tags", apiPath))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}

	@Test
	public void trollBuildWithChampionThatDoesNotExist() throws Exception {
		mockMvc.perform(get("{apiPath}/champions/{id}/troll-build", apiPath, 0))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("{}"));
	}

	@Test
	public void trollBuild() throws Exception {
		Champion azir = championsResponse.getChampions().get("Azir");

		mockMvc.perform(get("{apiPath}/champions/{id}/troll-build", apiPath, azir.getId())
				.param("mapId", String.valueOf(HOWLING_ABYSS_ID)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.items").exists())
				.andExpect(jsonPath("$.summoner-spells").exists())
				.andExpect(jsonPath("$.trinket").exists());
	}

	@Test
	public void trollBuildForViktor() throws Exception {
		Champion viktor = championsResponse.getChampions().get("Viktor");

		mockMvc.perform(get("{apiPath}/champions/{id}/troll-build", apiPath, viktor.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.items").exists())
				.andExpect(jsonPath("$.summoner-spells").exists())
				.andExpect(jsonPath("$.trinket").exists());
	}

}