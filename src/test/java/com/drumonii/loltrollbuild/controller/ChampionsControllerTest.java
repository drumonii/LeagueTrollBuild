package com.drumonii.loltrollbuild.controller;

import com.drumonii.loltrollbuild.config.WebSecurityConfig;
import com.drumonii.loltrollbuild.model.*;
import com.drumonii.loltrollbuild.model.SummonerSpell.GameMode;
import com.drumonii.loltrollbuild.model.builder.*;
import com.drumonii.loltrollbuild.model.image.ChampionImage;
import com.drumonii.loltrollbuild.model.image.GameMapImage;
import com.drumonii.loltrollbuild.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static com.drumonii.loltrollbuild.util.GameMapUtil.HOWLING_ABYSS_ID;
import static com.drumonii.loltrollbuild.util.GameMapUtil.SUMMONERS_RIFT_ID;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ChampionsController.class)
@Import(WebSecurityConfig.class)
@ActiveProfiles({ TESTING })
public class ChampionsControllerTest {

	@Autowired
	private MockMvc mockMvc;

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
	public void champions() throws Exception {
		ChampionImage nasusImage = new ChampionImageBuilder()
				.withFull("Nasus.png")
				.withSprite("champion2.png")
				.withGroup("champion")
				.withX(240)
				.withY(48)
				.withW(48)
				.withH(48)
				.build();

		ChampionInfo nasusInfo = new ChampionInfoBuilder()
				.withAttack(7)
				.withDefense(5)
				.withMagic(6)
				.withDifficulty(6)
				.build();

		ChampionPassive nasusPassive = new ChampionPassiveBuilder()
				.withName("Soul Eater")
				.withDescription("Nasus drains his foe's spiritual energy, giving him bonus Life Steal.")
				.withImage(new ChampionPassiveImageBuilder()
						.withFull("Nasus_Passive.png")
						.withSprite("passive2.png")
						.withGroup("passive")
						.withX(240)
						.withY(48)
						.withX(48)
						.withH(48)
						.build())
				.build();

		ChampionSpell nasusSpell1 = new ChampionSpellBuilder()
				.withName("Siphoning Strike")
				.withDescription("Nasus strikes his foe, dealing damage and increasing the power of his future Siphoning Strikes if he slays his target.")
				.withTooltip("Nasus's next basic attack will deal {{ e1 }} <span class=\\\"colorFF8C00\\\">(+{{ a2 }})</span> <span class=\\\"color5555FF\\\">(+{{ f1 }})</span> physical damage.<br /><br />Siphoning Strike permanently gains <span class=\\\"color5555FF\\\">{{ e2 }}</span> damage if it kills an enemy. This bonus is doubled against Champions, large minions and large monsters.")
				.withKey("NasusQ")
				.withImage(new ChampionSpellImageBuilder()
						.withFull("NasusQ.png")
						.withSprite("spell7.png")
						.withGroup("spell")
						.withX(336)
						.withY(144)
						.withW(48)
						.withH(48)
						.build())
				.build();

		Champion nasus = new ChampionBuilder()
				.withId(75)
				.withKey("Nasus")
				.withName("Nasus")
				.withTitle("the Curator of the Sands")
				.withTags("Fighter", "Tank")
				.withImage(nasusImage)
				.withInfo(nasusInfo)
				.withPassive(nasusPassive)
				.withSpells(nasusSpell1)
				.build();

		given(championsRepository.findAll(any(Sort.class))).willReturn(Arrays.asList(nasus));

		List<String> tags = Arrays.asList("Tank", "Support", "Mage", "Marksman");
		given(championsRepository.getTags()).willReturn(tags);

		mockMvc.perform(get("/champions"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("champions", "latestSavedPatch"))
				.andExpect(model().attribute("champions", containsInAnyOrder(nasus)))
				.andExpect(model().attributeExists("tags"))
				.andExpect(model().attribute("tags", containsInAnyOrder(tags.toArray())))
				.andExpect(view().name("champions/champions"));

		verify(championsRepository, times(1)).findAll(eq(new Sort(ASC, "name")));
		verify(championsRepository, times(1)).getTags();
	}

	@WithAnonymousUser
	@Test
	public void championById() throws Exception {
		ChampionImage xinImage = new ChampionImageBuilder()
				.withFull("XinZhao.png")
				.withSprite("champion4.png")
				.withGroup("champion")
				.withX(0)
				.withY(48)
				.withW(48)
				.withH(48)
				.build();

		ChampionInfo xinInfo = new ChampionInfoBuilder()
				.withAttack(8)
				.withDefense(6)
				.withMagic(3)
				.withDifficulty(2)
				.build();

		ChampionPassive xinPassive = new ChampionPassiveBuilder()
				.withName("Determination")
				.withDescription("Every third attack deals bonus damage and heals Xin Zhao.")
				.withImage(new ChampionPassiveImageBuilder()
						.withFull("XinZhaoP.png")
						.withSprite("passive4.png")
						.withGroup("passive")
						.withX(0)
						.withY(48)
						.withX(48)
						.withH(48)
						.build())
				.build();

		ChampionSpell xinSpell1 = new ChampionSpellBuilder()
				.withName("Three Talon Strike")
				.withDescription("Xin Zhao's next 3 standard attacks deal increased damage with the third attack knocking an opponent into the air.")
				.withTooltip("Xin Zhao's next 3 basic attacks deal {{ e1 }} <span class=\\\"colorFF8C00\\\">(+{{ f1 }})</span> extra physical damage and reduce his other cooldowns by 1 second. The final strike also knocks the target into the air for {{ e2 }} seconds.")
				.withKey("XinZhaoQ")
				.withImage(new ChampionSpellImageBuilder()
						.withFull("XinZhaoQ.png")
						.withSprite("spell13.png")
						.withGroup("spell")
						.withX(336)
						.withY(48)
						.withW(48)
						.withH(48)
						.build())
				.build();

		Champion xin = new ChampionBuilder()
				.withId(5)
				.withKey("XinZhao")
				.withName("Xin Zhao")
				.withTitle("the Seneschal of Demacia")
				.withTags("Fighter", "Assassin")
				.withImage(xinImage)
				.withInfo(xinInfo)
				.withPassive(xinPassive)
				.withSpells(xinSpell1)
				.build();

		given(championsRepository.findOne(anyInt())).willReturn(xin);

		GameMapImage crystalScarImage = new GameMapImageBuilder()
				.withFull("map8.png")
				.withSprite("map0.png")
				.withGroup("map")
				.withX(0)
				.withY(0)
				.withW(48)
				.withH(48)
				.build();

		GameMap crystalScar = new GameMapBuilder()
				.withMapId(8)
				.withMapName("The Crystal Scar")
				.withImage(crystalScarImage)
				.build();

		GameMapImage summonersRiftImage = new GameMapImageBuilder()
				.withFull("map11.png")
				.withSprite("map0.png")
				.withGroup("map")
				.withX(96)
				.withY(0)
				.withW(48)
				.withH(48)
				.build();

		GameMap summonersRift = new GameMapBuilder()
				.withMapId(11)
				.withMapName("Summoner's Rift")
				.withImage(summonersRiftImage)
				.build();

		given(mapsRepository.findAll()).willReturn(Arrays.asList(crystalScar, summonersRift));

		mockMvc.perform(get("/champions/{id}", xin.getId()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("champion", "latestSavedPatch"))
				.andExpect(model().attribute("champion", is(xin)))
				.andExpect(model().attribute("maps", containsInAnyOrder(summonersRift)))
				.andExpect(view().name("champions/champion"));

		verify(championsRepository, times(1)).findOne(eq(xin.getId()));
	}

	@WithAnonymousUser
	@Test
	public void championByName() throws Exception {
		ChampionImage brandImage = new ChampionImageBuilder()
				.withFull("Brand.png")
				.withSprite("champion0.png")
				.withGroup("champion")
				.withX(96)
				.withY(48)
				.withW(48)
				.withH(48)
				.build();

		ChampionInfo brandInfo = new ChampionInfoBuilder()
				.withAttack(2)
				.withDefense(2)
				.withMagic(9)
				.withDifficulty(4)
				.build();

		ChampionPassive brandPassive = new ChampionPassiveBuilder()
				.withName("Soul Eater")
				.withDescription("Nasus drains his foe's spiritual energy, giving him bonus Life Steal.")
				.withImage(new ChampionPassiveImageBuilder()
						.withFull("Nasus_Passive.png")
						.withSprite("passive2.png")
						.withGroup("passive")
						.withX(240)
						.withY(48)
						.withX(48)
						.withH(48)
						.build())
				.build();

		ChampionSpell brandSpell1 = new ChampionSpellBuilder()
				.withName("Blaze")
				.withDescription("Brand's spells light his targets ablaze, dealing 2% of their maximum Health in magic damage over 4 seconds, stacking up to 3 times. If Brand kills an enemy while it is ablaze he regains mana. When Blaze reaches max stacks on a Champion or monster, it becomes unstable. It detonates in 2 seconds, applying spell effects and dealing massive damage in an area around the victim.")
				.withTooltip("Brand's spells light his targets ablaze, dealing 2% of their maximum Health in magic damage over 4 seconds, stacking up to 3 times. If Brand kills an enemy while it is ablaze he regains mana. When Blaze reaches max stacks on a Champion or monster, it becomes unstable. It detonates in 2 seconds, applying spell effects and dealing massive damage in an area around the victim.")
				.withKey("BrandQ")
				.withImage(new ChampionSpellImageBuilder()
						.withFull("BrandP.png")
						.withSprite("passive0.png")
						.withGroup("spell")
						.withX(96)
						.withY(48)
						.withW(48)
						.withH(48)
						.build())
				.build();

		Champion brand = new ChampionBuilder()
				.withId(63)
				.withKey("Brand")
				.withName("Brand")
				.withTitle("the Burning Vengeance")
				.withTags("Mage")
				.withImage(brandImage)
				.withInfo(brandInfo)
				.withPassive(brandPassive)
				.withSpells(brandSpell1)
				.build();

		given(championsRepository.findByName(anyString())).willReturn(brand);

		GameMapImage butchersBridgeImage = new GameMapImageBuilder()
				.withFull("map14.png")
				.withSprite("map0.png")
				.withGroup("map")
				.withX(192)
				.withY(0)
				.withW(48)
				.withH(48)
				.build();

		GameMap butchersBridge = new GameMapBuilder()
				.withMapId(14)
				.withMapName("Butcher's Bridge")
				.withImage(butchersBridgeImage)
				.build();

		GameMapImage twistedTreeLineImage = new GameMapImageBuilder()
				.withFull("map10.png")
				.withSprite("map0.png")
				.withGroup("map")
				.withX(48)
				.withY(0)
				.withW(48)
				.withH(48)
				.build();

		GameMap twistedTreeLine = new GameMapBuilder()
				.withMapId(10)
				.withMapName("The Twisted Treeline")
				.withImage(twistedTreeLineImage)
				.build();

		given(mapsRepository.findAll()).willReturn(Arrays.asList(butchersBridge, twistedTreeLine));

		mockMvc.perform(get("/champions/{name}", brand.getName()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("champion", "latestSavedPatch"))
				.andExpect(model().attribute("champion", is(brand)))
				.andExpect(model().attribute("maps", containsInAnyOrder(twistedTreeLine)))
				.andExpect(view().name("champions/champion"));

		verify(championsRepository, times(1)).findByName(eq(brand.getName()));
	}

	@WithAnonymousUser
	@Test
	public void championDoesNotExist() throws Exception {
		given(championsRepository.findOne(anyInt())).willReturn(null);

		mockMvc.perform(get("/champions/{id}", 0))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/champions"));

		verify(championsRepository, times(1)).findOne(eq(0));

		given(championsRepository.findByName(anyString())).willReturn(null);

		mockMvc.perform(get("/champions/{id}", "not exist"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/champions"));

		verify(championsRepository, times(1)).findByName(eq("not exist"));
	}

	@WithAnonymousUser
	@Test
	public void trollBuildWithChampionThatDoesNotExist() throws Exception {
		given(championsRepository.findOne(anyInt())).willReturn(null);

		mockMvc.perform(get("/champions/{id}/troll-build", 0))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("{}"));
	}

	@WithAnonymousUser
	@Test
	public void trollBuild() throws Exception {
		Champion azir = new ChampionBuilder()
				.withId(268)
				.build();
		given(championsRepository.findOne(anyInt())).willReturn(azir);

		given(itemsRepository.boots(anyInt())).willReturn(new ArrayList<>());
		given(itemsRepository.forTrollBuild(anyInt())).willReturn(new ArrayList<>());
		given(summonerSpellsRepository.forTrollBuild(any(GameMode.class))).willReturn(new ArrayList<>());
		given(mapsRepository.findOne(anyInt())).willReturn(new GameMapBuilder().withMapName("Howling Abyss").build());
		given(itemsRepository.trinkets(anyInt())).willReturn(new ArrayList<>());

		mockMvc.perform(get("/champions/{id}/troll-build", azir.getId())
				.param("mapId", String.valueOf(HOWLING_ABYSS_ID)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.items").exists())
				.andExpect(jsonPath("$.summoner-spells").exists())
				.andExpect(jsonPath("$.trinket").exists());

		verify(itemsRepository, times(1)).boots(eq(HOWLING_ABYSS_ID));
		verify(itemsRepository, never()).viktorOnly();
		verify(itemsRepository, times(1)).forTrollBuild(eq(HOWLING_ABYSS_ID));
		verify(summonerSpellsRepository, times(1)).forTrollBuild(eq(GameMode.ARAM));
		verify(itemsRepository, times(1)).forTrollBuild(eq(HOWLING_ABYSS_ID));
	}

	@WithAnonymousUser
	@Test
	public void trollBuildForViktor() throws Exception {
		Champion viktor = new ChampionBuilder()
				.withId(112)
				.withName("Viktor")
				.build();
		given(championsRepository.findOne(anyInt())).willReturn(viktor);

		given(itemsRepository.boots(anyInt())).willReturn(new ArrayList<>());
		given(itemsRepository.viktorOnly()).willReturn(new ArrayList<>());
		given(itemsRepository.forTrollBuild(anyInt())).willReturn(new ArrayList<>());
		given(summonerSpellsRepository.forTrollBuild(any(GameMode.class))).willReturn(new ArrayList<>());
		given(mapsRepository.findOne(anyInt())).willReturn(new GameMapBuilder().withMapName("Summoner's Rift").build());
		given(itemsRepository.trinkets(anyInt())).willReturn(new ArrayList<>());

		mockMvc.perform(get("/champions/{id}/troll-build", viktor.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.items").exists())
				.andExpect(jsonPath("$.summoner-spells").exists())
				.andExpect(jsonPath("$.trinket").exists());

		verify(itemsRepository, times(1)).boots(eq(SUMMONERS_RIFT_ID));
		verify(itemsRepository, times(1)).viktorOnly();
		verify(itemsRepository, times(1)).forTrollBuild(eq(SUMMONERS_RIFT_ID));
		verify(summonerSpellsRepository, times(1)).forTrollBuild(eq(GameMode.CLASSIC));
		verify(itemsRepository, times(1)).forTrollBuild(eq(SUMMONERS_RIFT_ID));
	}

}