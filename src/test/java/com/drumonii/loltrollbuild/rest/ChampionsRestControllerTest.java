package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.rest.processor.ChampionResourceProcessor;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.test.rest.WebMvcRestTest;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.drumonii.loltrollbuild.rest.ChampionsRestController.PAGE_SIZE;
import static com.drumonii.loltrollbuild.util.GameMapUtil.HOWLING_ABYSS_ID;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcRestTest(controllers = ChampionsRestController.class,
		includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ChampionResourceProcessor.class))
public abstract class ChampionsRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	protected ChampionsRepository championsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	@Value("${spring.data.rest.base-path}")
	private String apiPath;

	protected ChampionsResponse championsResponse;

	public abstract void before();

	@Test
	public void getChampions() throws Exception {
		Champion champion = RandomizeUtil.getRandom(championsResponse.getChampions().values());

		// qbe
		mockMvc.perform(get("{apiPath}/champions", apiPath))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.champions").exists())
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(PAGE_SIZE)))
				.andExpect(jsonPath("$.page.totalElements").exists())
				.andExpect(jsonPath("$.page.totalPages").exists())
				.andExpect(jsonPath("$.page.number").exists());

		// qbe with name
		mockMvc.perform(get("{apiPath}/champions", apiPath)
				.param("name", champion.getName().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.champions").exists())
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(PAGE_SIZE)))
				.andExpect(jsonPath("$.page.totalElements").exists())
				.andExpect(jsonPath("$.page.totalPages").exists())
				.andExpect(jsonPath("$.page.number").exists());

		// qbe with title
		mockMvc.perform(get("{apiPath}/champions", apiPath)
				.param("title", champion.getTitle().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.champions").exists())
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(PAGE_SIZE)))
				.andExpect(jsonPath("$.page.totalElements").exists())
				.andExpect(jsonPath("$.page.totalPages").exists())
				.andExpect(jsonPath("$.page.number").exists());

		// qbe with partype
		mockMvc.perform(get("{apiPath}/champions", apiPath)
				.param("partype", champion.getPartype().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.champions").exists())
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(PAGE_SIZE)))
				.andExpect(jsonPath("$.page.totalElements").exists())
				.andExpect(jsonPath("$.page.totalPages").exists())
				.andExpect(jsonPath("$.page.number").exists());

		// qbe with tags
		mockMvc.perform(get("/api/champions")
				.param("tags", champion.getTags().iterator().next()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.champions").exists())
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(PAGE_SIZE)))
				.andExpect(jsonPath("$.page.totalElements").exists())
				.andExpect(jsonPath("$.page.totalPages").exists())
				.andExpect(jsonPath("$.page.number").exists());

		// qbe with no results
		mockMvc.perform(get("{apiPath}/champions", apiPath)
				.param("name", "abcd1234"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded").doesNotExist())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page.size", is(PAGE_SIZE)))
				.andExpect(jsonPath("$.page.totalElements").exists())
				.andExpect(jsonPath("$.page.totalPages").exists())
				.andExpect(jsonPath("$.page.number").exists());
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