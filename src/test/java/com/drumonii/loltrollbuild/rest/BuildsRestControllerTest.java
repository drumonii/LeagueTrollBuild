package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.*;
import com.drumonii.loltrollbuild.repository.*;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.drumonii.loltrollbuild.util.GameMapUtil.SUMMONERS_RIFT_SID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcRestTest(controllers = BuildsRestController.class)
public abstract class BuildsRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	protected BuildsRepository buildsRepository;

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	private MapsRepository mapsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	@Value("${api.base-path}")
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
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.content").exists())
				.andExpect(jsonPath("$.pageable").exists())
				.andExpect(jsonPath("$.totalPages").exists())
				.andExpect(jsonPath("$.totalElements").exists())
				.andExpect(jsonPath("$.last").exists())
				.andExpect(jsonPath("$.size", is(BuildsRestController.PAGE_SIZE)))
				.andExpect(jsonPath("$.number").exists())
				.andExpect(jsonPath("$.sort").exists())
				.andExpect(jsonPath("$.numberOfElements").exists())
				.andExpect(jsonPath("$.first").exists());
	}

	@Test
	public void getBuild() throws Exception {
		// find with non existing build Id
		mockMvc.perform(get("{apiPath}/builds/{id}", apiPath, 0))
				.andExpect(status().isNotFound());

		Champion zed = championsResponse.getChampions().get("Zed");
		Item item1 = itemsResponse.getItems().get("3117");
		Item item2 = itemsResponse.getItems().get("3194");
		Item item3 = itemsResponse.getItems().get("3193");
		Item item4 = itemsResponse.getItems().get("3036");
		Item item5 = itemsResponse.getItems().get("3812");
		Item item6 = itemsResponse.getItems().get("3092");
		SummonerSpell summonerSpell1 = summonerSpellsResponse.getSummonerSpells().get("SummonerSmite");
		SummonerSpell summonerSpell2 = summonerSpellsResponse.getSummonerSpells().get("SummonerMana");
		Item trinket = itemsResponse.getItems().get("3364");
		GameMap map = mapsResponse.getMaps().get(SUMMONERS_RIFT_SID);

		Build build = new Build();
		build.setChampionId(zed.getId());
		build.setItem1Id(item1.getId());
		build.setItem2Id(item2.getId());
		build.setItem3Id(item3.getId());
		build.setItem4Id(item4.getId());
		build.setItem5Id(item5.getId());
		build.setItem6Id(item6.getId());
		build.setSummonerSpell1Id(summonerSpell1.getId());
		build.setSummonerSpell2Id(summonerSpell2.getId());
		build.setTrinketId(trinket.getId());
		build.setMapId(map.getMapId());
		build = buildsRepository.save(build);

		// find with missing build attributes
		mockMvc.perform(get("{apiPath}/builds/{id}", apiPath, build.getId()))
				.andExpect(status().isBadRequest());

		championsRepository.save(zed);
		itemsRepository.save(item1);
		itemsRepository.save(item2);
		itemsRepository.save(item3);
		itemsRepository.save(item4);
		itemsRepository.save(item5);
		itemsRepository.save(item6);
		summonerSpellsRepository.save(summonerSpell1);
		summonerSpellsRepository.save(summonerSpell2);
		itemsRepository.save(trinket);
		mapsRepository.save(map);

		// find with existing build Id with attributes
		mockMvc.perform(get("{apiPath}/builds/{id}", apiPath, build.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.champion").exists())
				.andExpect(jsonPath("$.item1").exists())
				.andExpect(jsonPath("$.item2").exists())
				.andExpect(jsonPath("$.item3").exists())
				.andExpect(jsonPath("$.item4").exists())
				.andExpect(jsonPath("$.item5").exists())
				.andExpect(jsonPath("$.item6").exists())
				.andExpect(jsonPath("$.summonerSpell1").exists())
				.andExpect(jsonPath("$.summonerSpell2").exists())
				.andExpect(jsonPath("$.trinket").exists())
				.andExpect(jsonPath("$.map").exists());
	}

	@Test
	public void saveBuild() throws Exception {
		Build build = new Build();

		// Save with missing attributes
		mockMvc.perform(post("{apiPath}/builds", apiPath).with(csrf())
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(build)))
				.andExpect(status().isBadRequest());

		Champion tahmKench = championsResponse.getChampions().get("TahmKench");
		Item item1 = itemsResponse.getItems().get("3009");
		Item item2 = itemsResponse.getItems().get("3001");
		Item item3 = itemsResponse.getItems().get("3056");
		Item item4 = itemsResponse.getItems().get("3009");
		Item item5 = itemsResponse.getItems().get("3083");
		Item item6 = itemsResponse.getItems().get("3092");
		SummonerSpell summonerSpell1 = summonerSpellsResponse.getSummonerSpells().get("SummonerTeleport");
		SummonerSpell summonerSpell2 = summonerSpellsResponse.getSummonerSpells().get("SummonerHaste");
		Item trinket = itemsResponse.getItems().get("3340");
		GameMap map = mapsResponse.getMaps().get(SUMMONERS_RIFT_SID);

		build.setChampionId(tahmKench.getId());
		build.setItem1Id(item1.getId());
		build.setItem2Id(item2.getId());
		build.setItem3Id(item3.getId());
		build.setItem4Id(item4.getId());
		build.setItem5Id(item5.getId());
		build.setItem6Id(item6.getId());
		build.setSummonerSpell1Id(summonerSpell1.getId());
		build.setSummonerSpell2Id(summonerSpell2.getId());
		build.setTrinketId(trinket.getId());
		build.setMapId(map.getMapId());

		// Save full build
		mockMvc.perform(post("{apiPath}/builds", apiPath).with(csrf())
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(build)))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.championId", is(build.getChampionId())))
				.andExpect(jsonPath("$.item1Id", is(build.getItem1Id())))
				.andExpect(jsonPath("$.item2Id", is(build.getItem2Id())))
				.andExpect(jsonPath("$.item3Id", is(build.getItem3Id())))
				.andExpect(jsonPath("$.item4Id", is(build.getItem4Id())))
				.andExpect(jsonPath("$.item5Id", is(build.getItem5Id())))
				.andExpect(jsonPath("$.item6Id", is(build.getItem6Id())))
				.andExpect(jsonPath("$.summonerSpell1Id", is(build.getSummonerSpell1Id())))
				.andExpect(jsonPath("$.summonerSpell2Id", is(build.getSummonerSpell2Id())))
				.andExpect(jsonPath("$.trinketId", is(build.getTrinketId())))
				.andExpect(jsonPath("$.mapId", is(build.getMapId())));

		assertThat(buildsRepository.findAll())
				.filteredOn(savedBuild -> savedBuild.getChampionId() == tahmKench.getId() &&
						savedBuild.getItem1Id() == item1.getId() &&
						savedBuild.getItem2Id() == item2.getId() &&
						savedBuild.getItem3Id() == item3.getId() &&
						savedBuild.getItem4Id() == item4.getId() &&
						savedBuild.getItem5Id() == item5.getId() &&
						savedBuild.getItem6Id() == item6.getId() &&
						savedBuild.getSummonerSpell1Id() == summonerSpell1.getId() &&
						savedBuild.getSummonerSpell2Id() == summonerSpell2.getId() &&
						savedBuild.getTrinketId() == trinket.getId() &&
						savedBuild.getMapId() == map.getMapId())
				.isNotEmpty();
	}

}