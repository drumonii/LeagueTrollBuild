package com.drumonii.loltrollbuild.config;

import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties.Ddragon;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties.StaticData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebClientAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;

import static com.drumonii.loltrollbuild.config.Profiles.STATIC_DATA;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.data.MapEntry.entry;

@RunWith(SpringRunner.class)
@Import(RiotApiConfig.class)
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
@EnableConfigurationProperties(RiotApiProperties.class)
@ImportAutoConfiguration(WebClientAutoConfiguration.class)
@TestPropertySource(properties = "riot.static-data.apiKey=API_KEY")
@ActiveProfiles({ TESTING, STATIC_DATA })
public class RiotApiPropertiesStaticDataConfigTest {

	@Autowired
	private RiotApiProperties riotProperties;

	@Autowired
	private ApplicationContext applicationContext;

	private StaticData staticData;
	private Ddragon ddragon;

	private String host;
	private String patch;
	private String apiKey;

	@Before
	public void before() {
		staticData = riotProperties.getStaticData();
		ddragon = riotProperties.getDdragon();
		host = staticData.getBaseUrl().replace("{region}", staticData.getRegion());
		patch = "7.17.2";
		apiKey = staticData.getApiKey();
	}

	/*
	 * RestTemplate Tests
	 */

	@Test
	public void restTemplate() {
		try {
			applicationContext.getBean("restTemplate", RestTemplate.class);
		} catch (BeansException e) {
			fail("Caught BeansException", e);
		}
	}

	/*
	 * Summoner Spells Uri Components Tests
	 */

	@Autowired
	@Qualifier("summonerSpells")
	private UriComponents summonerSpellsUri;

	@Test
	public void summonerSpellsUri() {
		assertThat(summonerSpellsUri.getScheme()).isEqualTo("https");
		assertThat(summonerSpellsUri.getHost()).isEqualTo(host);
		assertThat(summonerSpellsUri.getPath()).isEqualTo(staticData.getSummonerSpells());
		assertThat(summonerSpellsUri.getQueryParams()).contains(
				entry("tags", Arrays.asList("cooldown", "image", "key", "modes")),
				entry("api_key", Collections.singletonList(apiKey)));
	}

	@Autowired
	@Qualifier("summonerSpell")
	private UriComponentsBuilder summonerSpellUri;

	@Test
	public void summonerSpellUri() {
		int id = 1;
		UriComponents uriComponents = summonerSpellUri.buildAndExpand(staticData.getRegion(), id);
		assertThat(uriComponents.getScheme()).isEqualTo("https");
		assertThat(uriComponents.getHost()).isEqualTo(host);
		assertThat(uriComponents.getPath()).isEqualTo(staticData.getSummonerSpell().replace("{id}",
				String.valueOf(id)));
		assertThat(uriComponents.getQueryParams()).contains(
				entry("tags", Arrays.asList("cooldown", "image", "key", "modes")),
				entry("api_key", Collections.singletonList(apiKey)));
	}

	@Autowired
	@Qualifier("summonerSpellsImg")
	private UriComponentsBuilder summonerSpellImgBuilder;

	@Test
	public void summonerSpellImgUri() {
		String imgFull = "SummonerBoost.png";
		UriComponents summonerSpellImgUri = summonerSpellImgBuilder.buildAndExpand(patch, imgFull);
		assertThat(summonerSpellImgUri.getHost()).isEqualTo(ddragon.getBaseUrl());
		assertThat(summonerSpellImgUri.getPath()).isEqualTo(ddragon.getSummonerSpellsImg().replace("{version}", patch)
				.replace("{summonerSpellImgFull}", imgFull));
		assertThat(summonerSpellImgUri.getScheme()).isEqualTo("https");
	}

	/*
	 * Items Uri Components Tests
	 */

	@Autowired
	@Qualifier("items")
	private UriComponents itemsUri;

	@Test
	public void itemsUri() {
		assertThat(itemsUri.getScheme()).isEqualTo("https");
		assertThat(itemsUri.getHost()).isEqualTo(host);
		assertThat(itemsUri.getPath()).isEqualTo(staticData.getItems());
		assertThat(itemsUri.getQueryParams()).contains(
				entry("tags", Arrays.asList("colloq", "consumed", "from", "gold", "image", "into", "maps", "requiredChampion")),
				entry("api_key", Collections.singletonList(apiKey)));
	}

	@Autowired
	@Qualifier("item")
	private UriComponentsBuilder itemUri;

	@Test
	public void itemUri() {
		int id = 1;
		UriComponents uriComponents = itemUri.buildAndExpand(staticData.getRegion(), id);
		assertThat(uriComponents.getScheme()).isEqualTo("https");
		assertThat(uriComponents.getHost()).isEqualTo(host);
		assertThat(uriComponents.getPath()).isEqualTo(staticData.getItem().replace("{id}", String.valueOf(id)));
		assertThat(uriComponents.getQueryParams()).contains(
				entry("tags", Arrays.asList("colloq", "consumed", "from", "gold", "image", "into", "maps", "requiredChampion")),
				entry("api_key", Collections.singletonList(apiKey)));
	}

	@Autowired
	@Qualifier("itemsImg")
	private UriComponentsBuilder itemsImgBuilder;

	@Test
	public void itemsImgUri() {
		String imgFull = "1.png";
		UriComponents itemsImgUri = itemsImgBuilder.buildAndExpand(patch, imgFull);
		assertThat(itemsImgUri.getHost()).isEqualTo(ddragon.getBaseUrl());
		assertThat(itemsImgUri.getPath()).isEqualTo(ddragon.getItemsImg().replace("{version}", patch)
				.replace("{itemImgFull}", imgFull));
		assertThat(itemsImgUri.getScheme()).isEqualTo("https");
	}

	/*
	 * Champions Uri Components Tests
	 */

	@Autowired
	@Qualifier("champions")
	private UriComponents championsUri;

	@Test
	public void championsUri() {
		assertThat(championsUri.getScheme()).isEqualTo("https");
		assertThat(championsUri.getHost()).isEqualTo(host);
		assertThat(championsUri.getPath()).isEqualTo(staticData.getChampions());
		assertThat(championsUri.getQueryParams()).contains(
				entry("tags", Arrays.asList("image", "info", "passive", "partype", "spells", "tags")),
				entry("api_key", Collections.singletonList(apiKey)));
	}

	@Autowired
	@Qualifier("champion")
	private UriComponentsBuilder championUri;

	@Test
	public void championUri() {
		int id = 1;
		UriComponents uriComponents = championUri.buildAndExpand(staticData.getRegion(), id);
		assertThat(uriComponents.getScheme()).isEqualTo("https");
		assertThat(uriComponents.getHost()).isEqualTo(host);
		assertThat(uriComponents.getPath()).isEqualTo(staticData.getChampion().replace("{id}", String.valueOf(id)));
		assertThat(uriComponents.getQueryParams()).contains(
				entry("tags", Arrays.asList("image", "info", "passive", "partype", "spells", "tags")),
				entry("api_key", Collections.singletonList(apiKey)));
	}

	@Autowired
	@Qualifier("championsImg")
	private UriComponentsBuilder championsImgBuilder;

	@Test
	public void championsImgUri() {
		String imgFull = "Champion.png";
		UriComponents championsImgUri = championsImgBuilder.buildAndExpand(patch, imgFull);
		assertThat(championsImgUri.getHost()).isEqualTo(ddragon.getBaseUrl());
		assertThat(championsImgUri.getPath()).isEqualTo(ddragon.getChampionsImg().replace("{version}", patch)
				.replace("{championImgFull}", imgFull));
		assertThat(championsImgUri.getScheme()).isEqualTo("https");
	}

	@Autowired
	@Qualifier("championsSpellImg")
	private UriComponentsBuilder championsSpellImgBuilder;

	@Test
	public void championsSpellImgUri() {
		String spellImgFull = "ChampionSpell.png";
		UriComponents championsImgUri = championsSpellImgBuilder.buildAndExpand(patch, spellImgFull);
		assertThat(championsImgUri.getHost()).isEqualTo(ddragon.getBaseUrl());
		assertThat(championsImgUri.getPath()).isEqualTo(ddragon.getChampionsSpellImg().replace("{version}", patch)
				.replace("{championSpellImgFull}", spellImgFull));
		assertThat(championsImgUri.getScheme()).isEqualTo("https");
	}

	@Autowired
	@Qualifier("championsPassiveImg")
	private UriComponentsBuilder championsPassiveImgBuilder;

	@Test
	public void championsPassiveImgUri() {
		String spellImgFull = "ChampionPassive.png";
		UriComponents championsImgUri = championsPassiveImgBuilder.buildAndExpand(patch, spellImgFull);
		assertThat(championsImgUri.getHost()).isEqualTo(ddragon.getBaseUrl());
		assertThat(championsImgUri.getPath()).isEqualTo(ddragon.getChampionsPassiveImg().replace("{version}", patch)
				.replace("{championPassiveImgFull}", spellImgFull));
		assertThat(championsImgUri.getScheme()).isEqualTo("https");
	}

	/*
	 * Maps Uri Components Tests
	 */

	@Autowired
	@Qualifier("maps")
	private UriComponents mapsUri;

	@Test
	public void mapsUri() {
		assertThat(mapsUri.getScheme()).isEqualTo("https");
		assertThat(mapsUri.getHost()).isEqualTo(host);
		assertThat(mapsUri.getPath()).isEqualTo(staticData.getMaps());
		assertThat(mapsUri.getQueryParams()).contains(entry("api_key", Collections.singletonList(apiKey)));
	}

	@Autowired
	@Qualifier("mapsImg")
	private UriComponentsBuilder mapsImgBuilder;

	@Test
	public void mapsImgUri() {
		String imgFull = "Map.png";
		UriComponents mapsImgUri = mapsImgBuilder.buildAndExpand(patch, imgFull);
		assertThat(mapsImgUri.getHost()).isEqualTo(ddragon.getBaseUrl());
		assertThat(mapsImgUri.getPath()).isEqualTo(ddragon.getMapsImg().replace("{version}", patch)
				.replace("{mapImgFull}", imgFull));
		assertThat(mapsImgUri.getScheme()).isEqualTo("https");
	}

	/*
	 * Versions Uri Components Tests
	 */

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	@Test
	public void versionsUri() {
		assertThat(versionsUri.getScheme()).isEqualTo("https");
		assertThat(versionsUri.getHost()).isEqualTo(host);
		assertThat(versionsUri.getPath()).isEqualTo(staticData.getVersions());
		assertThat(versionsUri.getQueryParams()).contains(entry("api_key", Collections.singletonList(apiKey)));
	}

}
