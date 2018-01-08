package com.drumonii.loltrollbuild.controller;

import com.drumonii.loltrollbuild.config.WebSecurityConfig;
import com.drumonii.loltrollbuild.model.*;
import com.drumonii.loltrollbuild.model.builder.*;
import com.drumonii.loltrollbuild.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static com.drumonii.loltrollbuild.util.GameMapUtil.SUMMONERS_RIFT_ID;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(BuildsController.class)
@Import(WebSecurityConfig.class)
@ActiveProfiles({ TESTING })
public class BuildsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BuildsRepository buildsRepository;

	@MockBean
	private ChampionsRepository championsRepository;

	@MockBean
	private ItemsRepository itemsRepository;

	@MockBean
	private SummonerSpellsRepository summonerSpellsRepository;

	@MockBean
	private MapsRepository mapsRepository;

	@MockBean
	private VersionsRepository versionsRepository;

	@Before
	public void before() {
		given(versionsRepository.latestVersion()).willReturn(new Version("7.17.2"));
	}

	@WithAnonymousUser
	@Test
	public void emptyBuilds() throws Exception {
		given(buildsRepository.count()).willReturn(0L);

		mockMvc.perform(get("/builds"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/builds/1"));

		verify(buildsRepository, times(1)).count();
	}

	@WithAnonymousUser
	@Test
	public void builds() throws Exception {
		given(buildsRepository.count()).willReturn(1L);

		mockMvc.perform(get("/builds"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/builds/1"));

		verify(buildsRepository, times(1)).count();
	}

	@WithAnonymousUser
	@Test
	public void missingBuild() throws Exception {
		given(buildsRepository.findOne(anyInt())).willReturn(null);

		mockMvc.perform(get("/builds/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(view().name("builds/notFound"));

		verify(buildsRepository, times(1)).findOne(eq(1));
	}

	@WithAnonymousUser
	@Test
	public void missingBuildAttributes() throws Exception {
		Build build = new Build();
		build.setChampionId(1);
		build.setItem1Id(2);
		build.setItem2Id(3);
		build.setItem3Id(4);
		build.setItem4Id(5);
		build.setItem5Id(6);
		build.setItem6Id(7);
		build.setSummonerSpell1Id(8);
		build.setSummonerSpell2Id(9);
		build.setTrinketId(10);
		build.setMapId(11);

		given(championsRepository.findOne(anyInt())).willReturn(null);
		given(itemsRepository.findOne(anyInt())).willReturn(null);
		given(itemsRepository.findOne(anyInt())).willReturn(null);
		given(itemsRepository.findOne(anyInt())).willReturn(null);
		given(itemsRepository.findOne(anyInt())).willReturn(null);
		given(itemsRepository.findOne(anyInt())).willReturn(null);
		given(itemsRepository.findOne(anyInt())).willReturn(null);
		given(summonerSpellsRepository.findOne(anyInt())).willReturn(null);
		given(summonerSpellsRepository.findOne(anyInt())).willReturn(null);
		given(itemsRepository.findOne(anyInt())).willReturn(null);
		given(mapsRepository.findOne(anyInt())).willReturn(null);

		given(buildsRepository.findOne(anyInt())).willReturn(build);

		mockMvc.perform(get("/builds/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("latestSavedPatch"))
				.andExpect(view().name("builds/invalidAttributes"));

		verify(championsRepository, times(1)).findOne(eq(1));
		verify(itemsRepository, times(1)).findOne(eq(2));
		verify(itemsRepository, times(1)).findOne(eq(3));
		verify(itemsRepository, times(1)).findOne(eq(4));
		verify(itemsRepository, times(1)).findOne(eq(5));
		verify(itemsRepository, times(1)).findOne(eq(6));
		verify(itemsRepository, times(1)).findOne(eq(7));
		verify(summonerSpellsRepository, times(1)).findOne(eq(8));
		verify(summonerSpellsRepository, times(1)).findOne(eq(9));
		verify(itemsRepository, times(1)).findOne(eq(10));
		verify(mapsRepository, times(1)).findOne(eq(11));
	}

	@WithAnonymousUser
	@Test
	public void build() throws Exception {
		Build build = new Build();
		build.setId(1);

		Champion champion = new ChampionBuilder()
				.withId(17)
				.withName("Teemo")
				.withImage(new ChampionImageBuilder()
						.withFull("image_full.png")
						.build())
				.withPassive(new ChampionPassiveBuilder()
						.withImage(new ChampionPassiveImageBuilder()
								.withFull("image_full.png")
								.build())
						.build())
				.build();

		Item boots = new ItemBuilder()
				.withId(3111)
				.withName("Mercury's Treads")
				.withImage(new ItemImageBuilder()
						.withFull("image_full.png")
						.build())
				.build();
		Item item2 = new ItemBuilder()
				.withId(3022)
				.withName("Frozen Mallet")
				.withImage(new ItemImageBuilder()
						.withFull("image_full.png")
						.build())
				.build();
		Item item3 = new ItemBuilder()
				.withId(3026)
				.withName("Guardian Angel")
				.withImage(new ItemImageBuilder()
						.withFull("image_full.png")
						.build())
				.build();
		Item item4 = new ItemBuilder()
				.withId(3116)
				.withName("Rylai's Crystal Scepter")
				.withImage(new ItemImageBuilder()
						.withFull("image_full.png")
						.build())
				.build();
		Item item5 = new ItemBuilder()
				.withId(3190)
				.withName("Locket of the Iron Solari")
				.withImage(new ItemImageBuilder()
						.withFull("image_full.png")
						.build())
				.build();
		Item item6 = new ItemBuilder()
				.withId(3031)
				.withName("Infinity Edge")
				.withImage(new ItemImageBuilder()
						.withFull("image_full.png")
						.build())
				.build();

		SummonerSpell summonerSpell1 = new SummonerSpellBuilder()
				.withId(14)
				.withName("Ignite")
				.withImage(new SummonerSpellImageBuilder()
						.withFull("image_full.png")
						.build())
				.build();

		SummonerSpell summonerSpell2 = new SummonerSpellBuilder()
				.withId(3)
				.withName("Exhaust")
				.withImage(new SummonerSpellImageBuilder()
						.withFull("image_full.png")
						.build())
				.build();

		Item trinket = new ItemBuilder()
				.withId(3340)
				.withName("Warding Totem (Trinket)")
				.withImage(new ItemImageBuilder()
						.withFull("image_full.png")
						.build())
				.build();

		GameMap map = new GameMapBuilder()
				.withMapId(SUMMONERS_RIFT_ID)
				.withMapName("Summoner's Rift")
				.build();

		build.setChampionId(champion.getId());
		build.setItem1Id(boots.getId());
		build.setItem2Id(item2.getId());
		build.setItem3Id(item3.getId());
		build.setItem4Id(item4.getId());
		build.setItem5Id(item5.getId());
		build.setItem6Id(item6.getId());
		build.setSummonerSpell1Id(summonerSpell1.getId());
		build.setSummonerSpell2Id(summonerSpell2.getId());
		build.setTrinketId(trinket.getId());
		build.setMapId(map.getMapId());

		given(championsRepository.findOne(anyInt())).willReturn(champion);
		given(itemsRepository.findOne(anyInt())).willReturn(boots);
		given(itemsRepository.findOne(anyInt())).willReturn(item2);
		given(itemsRepository.findOne(anyInt())).willReturn(item3);
		given(itemsRepository.findOne(anyInt())).willReturn(item4);
		given(itemsRepository.findOne(anyInt())).willReturn(item5);
		given(itemsRepository.findOne(anyInt())).willReturn(item6);
		given(summonerSpellsRepository.findOne(anyInt())).willReturn(summonerSpell1);
		given(summonerSpellsRepository.findOne(anyInt())).willReturn(summonerSpell2);
		given(itemsRepository.findOne(anyInt())).willReturn(trinket);
		given(mapsRepository.findOne(anyInt())).willReturn(map);

		given(buildsRepository.findOne(anyInt())).willReturn(build);

		mockMvc.perform(get("/builds/{id}", build.getId()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("latestSavedPatch"))
				.andExpect(model().attribute("build", is(build)))
				.andExpect(view().name("builds/build"));

		verify(championsRepository, times(1)).findOne(eq(champion.getId()));
		verify(itemsRepository, times(1)).findOne(eq(boots.getId()));
		verify(itemsRepository, times(1)).findOne(eq(item2.getId()));
		verify(itemsRepository, times(1)).findOne(eq(item3.getId()));
		verify(itemsRepository, times(1)).findOne(eq(item4.getId()));
		verify(itemsRepository, times(1)).findOne(eq(item5.getId()));
		verify(itemsRepository, times(1)).findOne(eq(item6.getId()));
		verify(summonerSpellsRepository, times(1)).findOne(eq(summonerSpell1.getId()));
		verify(summonerSpellsRepository, times(1)).findOne(eq(summonerSpell2.getId()));
		verify(itemsRepository, times(1)).findOne(eq(trinket.getId()));
		verify(mapsRepository, times(1)).findOne(eq(map.getMapId()));
	}

}