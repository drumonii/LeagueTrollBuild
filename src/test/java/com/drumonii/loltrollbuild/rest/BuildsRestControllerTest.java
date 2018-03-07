package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.Build;
import com.drumonii.loltrollbuild.repository.BuildsRepository;
import com.drumonii.loltrollbuild.rest.processor.BuildResourceProcessor;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.drumonii.loltrollbuild.test.rest.WebMvcRestTest;
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

import static com.drumonii.loltrollbuild.rest.BuildsRestController.PAGE_SIZE;
import static com.drumonii.loltrollbuild.util.GameMapUtil.SUMMONERS_RIFT_SID;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcRestTest(controllers = BuildsRestController.class,
		includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = BuildResourceProcessor.class))
public abstract class BuildsRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	protected BuildsRepository buildsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	@Value("${spring.data.rest.base-path}")
	private String apiPath;

	protected ChampionsResponse championsResponse;
	protected ItemsResponse itemsResponse;
	protected MapsResponse mapsResponse;
	protected SummonerSpellsResponse summonerSpellsResponse;

	public abstract void before();

	@Test
	public void getBuilds() throws Exception {
		// qbe
		mockMvc.perform(get("{apiPath}/builds", apiPath))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.builds").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(PAGE_SIZE)))
				.andExpect(jsonPath("$.page.totalElements").exists())
				.andExpect(jsonPath("$.page.totalPages").exists())
				.andExpect(jsonPath("$.page.number").exists());
	}

	@Test
	public void saveBuild() throws Exception {
		Build build = new Build();

		// Save with missing attributes
		mockMvc.perform(post("{apiPath}/builds", apiPath).with(csrf())
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(build)))
				.andExpect(status().isBadRequest());

		build.setChampionId(championsResponse.getChampions().get("Nasus").getId());
		build.setItem1Id(itemsResponse.getItems().get("3009").getId());
		build.setItem2Id(itemsResponse.getItems().get("3001").getId());
		build.setItem3Id(itemsResponse.getItems().get("3089").getId());
		build.setItem4Id(itemsResponse.getItems().get("3056").getId());
		build.setItem5Id(itemsResponse.getItems().get("3083").getId());
		build.setItem6Id(itemsResponse.getItems().get("3092").getId());
		build.setSummonerSpell1Id(summonerSpellsResponse.getSummonerSpells().get("SummonerTeleport").getId());
		build.setSummonerSpell2Id(summonerSpellsResponse.getSummonerSpells().get("SummonerHaste").getId());
		build.setTrinketId(itemsResponse.getItems().get("3340").getId());
		build.setMapId(mapsResponse.getMaps().get(SUMMONERS_RIFT_SID).getMapId());

		// Save full build
		mockMvc.perform(post("{apiPath}/builds", apiPath).with(csrf())
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(build)))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8));
	}

}