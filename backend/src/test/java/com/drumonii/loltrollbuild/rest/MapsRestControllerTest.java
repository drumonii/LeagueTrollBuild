package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcRestTest(MapsRestController.class)
public abstract class MapsRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	protected MapsRepository mapsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	@Value("${api.base-path}")
	private String apiPath;

	protected MapsResponse mapsResponse;

	public abstract void before();

	@Test
	public void getGameMaps() throws Exception {
		GameMap map = RandomizeUtil.getRandom(mapsResponse.getMaps().values());

		// qbe
		mockMvc.perform(get("{apiPath}/maps", apiPath))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with name
		mockMvc.perform(get("{apiPath}/maps", apiPath)
				.param("mapName", map.getMapName().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with no results
		mockMvc.perform(get("{apiPath}/maps", apiPath)
				.param("mapName", "abcd1234"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(content().json("[]"));
	}

	@Test
	public void getGameMap() throws Exception {
		// find with non existing map Id
		mockMvc.perform(get("{apiPath}/maps/{id}", apiPath, 0))
				.andExpect(status().isNotFound());

		GameMap howlingAbyss = mapsResponse.getMaps().get("12");

		// find with existing map Id
		mockMvc.perform(get("{apiPath}/maps/{id}", apiPath, howlingAbyss.getMapId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());
	}

	@Test
	public void getForTrollBuild() throws Exception {
		mockMvc.perform(get("{apiPath}/maps/for-troll-build", apiPath))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());
	}

}