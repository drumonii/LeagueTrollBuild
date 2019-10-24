package com.drumonii.loltrollbuild.api;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.drumonii.loltrollbuild.test.api.WebMvcRestTest;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcRestTest(MapsRestController.class)
abstract class MapsRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	protected MapsRepository mapsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	@Value("${api.base-path}")
	private String apiPath;

	protected MapsResponse mapsResponse;

	protected abstract void beforeEach();

	@Test
	void getGameMaps() throws Exception {
		GameMap map = RandomizeUtil.getRandom(mapsResponse.getMaps().values());

		// qbe
		mockMvc.perform(get("{apiPath}/maps", apiPath))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with name
		mockMvc.perform(get("{apiPath}/maps", apiPath)
				.param("mapName", map.getMapName().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with no results
		mockMvc.perform(get("{apiPath}/maps", apiPath)
				.param("mapName", "abcd1234"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(content().json("[]"));
	}

	@Test
	void getGameMap() throws Exception {
		// find with non existing map Id
		mockMvc.perform(get("{apiPath}/maps/{id}", apiPath, 0))
				.andExpect(status().isNotFound());

		GameMap howlingAbyss = mapsResponse.getMaps().get("12");

		// find with existing map Id
		mockMvc.perform(get("{apiPath}/maps/{id}", apiPath, howlingAbyss.getMapId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());
	}

	@Test
	void getForTrollBuild() throws Exception {
		mockMvc.perform(get("{apiPath}/maps/for-troll-build", apiPath))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());
	}

}