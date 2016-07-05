package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.MediaTypes;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MapsRestControllerTest extends BaseSpringTestRunner {

	@Value("${spring.data.rest.base-path}")
	private String apiPath;

	@Autowired
	private MapsRepository mapsRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@After
	public void after() {
		mapsRepository.deleteAll();
	}

	@Test
	public void getGameMaps() throws Exception {
		String responseBody = "{\"type\":\"map\",\"version\":\"6.3.1\",\"data\":{\"8\":{\"mapName\":\"CrystalScar\"," +
				"\"mapId\":8,\"image\":{\"full\":\"map8.png\",\"sprite\":\"map0.png\",\"group\":\"map\",\"x\":192," +
				"\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap crystalScar = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("8");
		mapsRepository.save(crystalScar);

		// qbe with name
		mockMvc.perform(get(apiPath + "/maps")
				.param("mapName", "crystal"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON))
				.andExpect(jsonPath("$._embedded.maps", hasSize(1)))
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$.page").exists());

		// qbe with no results
		mockMvc.perform(get(apiPath + "/maps")
				.param("mapName", "notfound"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON))
				.andExpect(jsonPath("$._embedded").doesNotExist())
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$.page").exists());
	}

}