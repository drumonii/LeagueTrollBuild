package com.drumonii.loltrollbuild.config;

import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties.Ddragon;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(SpringRunner.class)
@Import(RiotApiConfig.class)
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
@EnableConfigurationProperties(RiotApiProperties.class)
@ImportAutoConfiguration({ RestTemplateAutoConfiguration.class, JacksonAutoConfiguration.class })
@ActiveProfiles({ TESTING, DDRAGON })
public class RiotApiPropertiesDdragonConfigTest {

	@Autowired
	private RiotApiProperties riotProperties;

	@Autowired
	private ApplicationContext applicationContext;

	private Ddragon ddragon;

	private String host;
	private String patch;
	private String locale;

	@Before
	public void before() {
		ddragon = riotProperties.getDdragon();
		host = ddragon.getBaseUrl();
		patch = "7.17.2";
		locale = ddragon.getLocale();
	}

	/*
	 * RestTemplate Tests
	 */

	@Test
	public void restTemplate() {
		try {
			RestTemplate restTemplate = applicationContext.getBean("restTemplate", RestTemplate.class);
			assertThat(restTemplate.getInterceptors()).hasSize(1);
			assertThat(restTemplate.getMessageConverters()).flatExtracting(HttpMessageConverter::getSupportedMediaTypes)
					.contains(MediaType.parseMediaType("text/json;charset=UTF-8"));
		} catch (BeansException e) {
			fail("Caught BeansException", e);
		}
	}

	/*
	 * Summoner Spells Uri Components Tests
	 */

	@Autowired
	@Qualifier("summonerSpells")
	private UriComponentsBuilder summonerSpellsUriBuilder;

	@Test
	public void summonerSpellsUri() {
		UriComponents summonerSpellsUri = summonerSpellsUriBuilder.buildAndExpand(patch, locale);
		assertThat(summonerSpellsUri.getScheme()).isEqualTo("https");
		assertThat(summonerSpellsUri.getHost()).isEqualTo(host);
		assertThat(summonerSpellsUri.getPath()).isEqualTo(ddragon.getSummonerSpells().replace("{version}", patch)
				.replace("{locale}", locale));
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
	private UriComponentsBuilder itemsUriBuilder;

	@Test
	public void itemsUri() {
		UriComponents itemsUri = itemsUriBuilder.buildAndExpand(patch, locale);
		assertThat(itemsUri.getScheme()).isEqualTo("https");
		assertThat(itemsUri.getHost()).isEqualTo(host);
		assertThat(itemsUri.getPath()).isEqualTo(ddragon.getItems().replace("{version}", patch)
				.replace("{locale}", locale));
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
	private UriComponentsBuilder championsUriBuilder;

	@Test
	public void championsUri() {
		UriComponents championsUri = championsUriBuilder.buildAndExpand(patch, locale);
		assertThat(championsUri.getHost()).isEqualTo(ddragon.getBaseUrl());
		assertThat(championsUri.getPath()).isEqualTo(ddragon.getChampions().replace("{version}", patch)
				.replace("{locale}", locale));
		assertThat(championsUri.getScheme()).isEqualTo("https");
	}

	@Autowired
	@Qualifier("champion")
	private UriComponentsBuilder championUriBuilder;

	@Test
	public void championUri() {
		int id = 1;
		UriComponents championUri = championUriBuilder.buildAndExpand(patch, locale, id);
		assertThat(championUri.getHost()).isEqualTo(ddragon.getBaseUrl());
		assertThat(championUri.getPath()).isEqualTo(ddragon.getChampion().replace("{version}", patch)
				.replace("{locale}", locale).replace("{id}", String.valueOf(id)));
		assertThat(championUri.getScheme()).isEqualTo("https");
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
	private UriComponentsBuilder mapsUriBuilder;

	@Test
	public void mapsUri() {
		UriComponents mapsUri = mapsUriBuilder.buildAndExpand(patch, locale);
		assertThat(mapsUri.getScheme()).isEqualTo("https");
		assertThat(mapsUri.getHost()).isEqualTo(host);
		assertThat(mapsUri.getPath()).isEqualTo(ddragon.getMaps().replace("{version}", patch)
				.replace("{locale}", locale));
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
		assertThat(versionsUri.getHost()).isEqualTo(ddragon.getBaseUrl());
		assertThat(versionsUri.getPath()).isEqualTo(ddragon.getVersions());
	}

}
