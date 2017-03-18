package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChampionsRestControllerTest extends BaseSpringTestRunner {

	private ChampionsResponse championsResponseSlice;

	@Before
	public void before() {
		super.before();

		championsResponseSlice = new ChampionsResponse();
		championsResponseSlice.setType(championsResponse.getType());
		championsResponseSlice.setVersion(championsResponse.getVersion());
		championsResponseSlice.setChampions(RandomizeUtil.getRandoms(
				championsResponse.getChampions().values(), DEFAULT_PAGE_SIZE).stream()
				.collect(Collectors.toMap(champion -> String.valueOf(champion.getId()), champion -> champion)));
		championsRepository.save(championsResponseSlice.getChampions().values());
	}

	@Test
	public void getChampions() throws Exception {
		Champion champion = RandomizeUtil.getRandom(championsResponseSlice.getChampions().values());

		// qbe
		mockMvc.perform(get(apiPath + "/champions"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.champions",
						hasSize(championsResponseSlice.getChampions().values().size())))
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(20)))
				.andExpect(jsonPath("$.page.totalElements", is(championsResponseSlice.getChampions().values().size())))
				.andExpect(jsonPath("$.page.totalPages", is(
						(int) Math.ceil((double) championsResponseSlice.getChampions().values().size() / (double) 20))))
				.andExpect(jsonPath("$.page.number", is(0)));

		// qbe with name
		mockMvc.perform(get(apiPath + "/champions")
				.param("name", champion.getName().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.champions", hasSize(1)))
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(20)))
				.andExpect(jsonPath("$.page.totalElements", is(1)))
				.andExpect(jsonPath("$.page.totalPages", is(1)))
				.andExpect(jsonPath("$.page.number", is(0)));

		// qbe with title
		mockMvc.perform(get(apiPath + "/champions")
				.param("title", champion.getTitle().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.champions", hasSize(1)))
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(20)))
				.andExpect(jsonPath("$.page.totalElements", is(1)))
				.andExpect(jsonPath("$.page.totalPages", is(1)))
				.andExpect(jsonPath("$.page.number", is(0)));

		// qbe with partype
		mockMvc.perform(get(apiPath + "/champions")
				.param("partype", champion.getPartype().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.champions", hasSize(
						(int) championsResponseSlice.getChampions().values().stream()
								.filter(c -> c.getPartype().equals(champion.getPartype()))
								.count())))
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(20)))
				.andExpect(jsonPath("$.page.totalElements", is(
						(int) championsResponseSlice.getChampions().values().stream()
								.filter(c -> c.getPartype().equals(champion.getPartype()))
								.count())))
				.andExpect(jsonPath("$.page.totalPages", is(
						(int) Math.ceil((double) championsResponseSlice.getChampions().values().stream()
								.filter(c -> c.getPartype().equals(champion.getPartype()))
								.count() / (double) 20))))
				.andExpect(jsonPath("$.page.number", is(0)));

		// qbe with no results
		mockMvc.perform(get(apiPath + "/champions")
				.param("name", "abcd1234"))
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