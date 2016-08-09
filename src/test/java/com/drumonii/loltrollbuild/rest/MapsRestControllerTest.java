package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MapsRestControllerTest extends BaseSpringTestRunner {

	private static final int DEFAULT_PAGE_SIZE = 20;

	@Value("${spring.data.rest.base-path}")
	private String apiPath;

	@Autowired
	private MapsRepository mapsRepository;

	private MapsResponse mapsResponseSlice;

	@Before
	public void before() {
		super.before();

		mapsResponseSlice = new MapsResponse();
		mapsResponseSlice.setType(championsResponse.getType());
		mapsResponseSlice.setVersion(championsResponse.getVersion());
		mapsResponseSlice.setMaps(RandomizeUtil.getRandoms(
				mapsResponse.getMaps().values(), DEFAULT_PAGE_SIZE).stream()
				.collect(Collectors.toMap(map -> String.valueOf(map.getMapId()), map -> map)));
		mapsRepository.save(mapsResponseSlice.getMaps().values());
	}

	@After
	public void after() {
		mapsRepository.deleteAll();
	}

	@Test
	public void getGameMaps() throws Exception {
		GameMap map = RandomizeUtil.getRandom(mapsResponseSlice.getMaps().values());

		// qbe
		mockMvc.perform(get(apiPath + "/maps"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.maps",
						hasSize(mapsResponseSlice.getMaps().values().size())))
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(20)))
				.andExpect(jsonPath("$.page.totalElements", is(mapsResponseSlice.getMaps().values().size())))
				.andExpect(jsonPath("$.page.totalPages", is(
						(int) Math.ceil((double) mapsResponseSlice.getMaps().values().size() / (double) 20))))
				.andExpect(jsonPath("$.page.number", is(0)));

		// qbe with name
		mockMvc.perform(get(apiPath + "/maps")
				.param("mapName", map.getMapName().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.maps", hasSize(
						(int) mapsResponseSlice.getMaps().values().stream()
								.filter(m -> m.getMapName().contains(map.getMapName()))
								.count())))
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(20)))
				.andExpect(jsonPath("$.page.totalElements", is(
						(int) mapsResponseSlice.getMaps().values().stream()
								.filter(m -> m.getMapName().contains(map.getMapName()))
								.count())))
				.andExpect(jsonPath("$.page.totalPages", is(
						(int) Math.ceil((double) mapsResponseSlice.getMaps().values().stream()
								.filter(m -> m.getMapName().contains(map.getMapName()))
								.count() / (double) 20))))
				.andExpect(jsonPath("$.page.number", is(0)));

		// qbe with no results
		mockMvc.perform(get(apiPath + "/maps")
				.param("mapName", "abcd1234"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded").doesNotExist())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page.size", is(20)))
				.andExpect(jsonPath("$.page.totalElements", is(0)))
				.andExpect(jsonPath("$.page.totalPages", is(0)))
				.andExpect(jsonPath("$.page.number", is(0)));
	}

}