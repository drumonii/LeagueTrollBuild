package com.drumonii.loltrollbuild.config;

import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties.Ddragon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@ExtendWith({SpringExtension.class})
@Import(RiotApiConfig.class)
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
@EnableConfigurationProperties(RiotApiProperties.class)
@ImportAutoConfiguration({ RestTemplateAutoConfiguration.class, JacksonAutoConfiguration.class })
@ActiveProfiles({ TESTING, DDRAGON })
class RiotApiPropertiesDdragonConfigTest {

	@Autowired
	private RiotApiProperties riotProperties;

	@Autowired
	private ApplicationContext applicationContext;

	private Ddragon ddragon;

	private String patch;
	private String locale;

	@BeforeEach
	void beforeEach() {
		ddragon = riotProperties.getDdragon();
		patch = "7.17.2";
		locale = ddragon.getLocale();
	}

	/*
	 * RestTemplate Tests
	 */

	@Test
	void restTemplate() {
		try {
			RestTemplate restTemplate = applicationContext.getBean("restTemplate", RestTemplate.class);
			assertThat(restTemplate.getInterceptors()).hasSize(1);
			assertThat(restTemplate.getMessageConverters()).flatExtracting(HttpMessageConverter::getSupportedMediaTypes)
					.contains(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM,
							MediaType.parseMediaType("binary/octet-stream"), MediaType.parseMediaType("text/json;charset=UTF-8"));
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
	void summonerSpellsUri() {
		UriComponents summonerSpellsUri = summonerSpellsUriBuilder.buildAndExpand(patch, locale);
		assertThat(summonerSpellsUri.toString()).as("Summoner Spells URI")
				.isEqualTo(ddragon.getBaseUrl() + ddragon.getSummonerSpells().replace("{version}", patch).replace("{locale}", locale));
	}

	@Autowired
	@Qualifier("summonerSpellsImg")
	private UriComponentsBuilder summonerSpellImgBuilder;

	@Test
	void summonerSpellImgUri() {
		String imgFull = "SummonerBoost.png";
		UriComponents summonerSpellImgUri = summonerSpellImgBuilder.buildAndExpand(patch, imgFull);
		assertThat(summonerSpellImgUri.toString()).as("Summoner Spells Image URI")
				.isEqualTo(ddragon.getBaseUrl() + ddragon.getSummonerSpellsImg().replace("{version}", patch)
						.replace("{summonerSpellImgFull}", imgFull));
	}

	/*
	 * Items Uri Components Tests
	 */

	@Autowired
	@Qualifier("items")
	private UriComponentsBuilder itemsUriBuilder;

	@Test
	void itemsUri() {
		UriComponents itemsUri = itemsUriBuilder.buildAndExpand(patch, locale);
		assertThat(itemsUri.toString()).as("Items URI")
				.isEqualTo(ddragon.getBaseUrl() + ddragon.getItems().replace("{version}", patch).replace("{locale}", locale));
	}

	@Autowired
	@Qualifier("itemsImg")
	private UriComponentsBuilder itemsImgBuilder;

	@Test
	void itemsImgUri() {
		String imgFull = "1.png";
		UriComponents itemsImgUri = itemsImgBuilder.buildAndExpand(patch, imgFull);
		assertThat(itemsImgUri.toString()).as("Items Image URI")
				.isEqualTo(ddragon.getBaseUrl() + ddragon.getItemsImg().replace("{version}", patch).replace("{itemImgFull}", imgFull));
	}

	/*
	 * Champions Uri Components Tests
	 */

	@Autowired
	@Qualifier("champions")
	private UriComponentsBuilder championsUriBuilder;

	@Test
	void championsUri() {
		UriComponents championsUri = championsUriBuilder.buildAndExpand(patch, locale);
		assertThat(championsUri.toString()).as("Champions URI")
				.isEqualTo(ddragon.getBaseUrl() + ddragon.getChampions().replace("{version}", patch).replace("{locale}", locale));
	}

	@Autowired
	@Qualifier("champion")
	private UriComponentsBuilder championUriBuilder;

	@Test
	void championUri() {
		int id = 1;
		UriComponents championUri = championUriBuilder.buildAndExpand(patch, locale, id);
		assertThat(championUri.toString()).as("Champion URI")
				.isEqualTo(ddragon.getBaseUrl() + ddragon.getChampion().replace("{version}", patch)
						.replace("{locale}", locale).replace("{id}", String.valueOf(id)));
	}

	@Autowired
	@Qualifier("championsImg")
	private UriComponentsBuilder championsImgBuilder;

	@Test
	void championsImgUri() {
		String imgFull = "Champion.png";
		UriComponents championsImgUri = championsImgBuilder.buildAndExpand(patch, imgFull);
		assertThat(championsImgUri.toString()).as("Champions Image URI")
				.isEqualTo(ddragon.getBaseUrl() + ddragon.getChampionsImg().replace("{version}", patch).replace("{championImgFull}", imgFull));
	}

	@Autowired
	@Qualifier("championsSpellImg")
	private UriComponentsBuilder championsSpellImgBuilder;

	@Test
	void championsSpellImgUri() {
		String spellImgFull = "ChampionSpell.png";
		UriComponents championsSpellImgUri = championsSpellImgBuilder.buildAndExpand(patch, spellImgFull);
		assertThat(championsSpellImgUri.toString()).as("Champion Spell Image URI")
				.isEqualTo(ddragon.getBaseUrl() + ddragon.getChampionsSpellImg().replace("{version}", patch)
						.replace("{championSpellImgFull}", spellImgFull));
	}

	@Autowired
	@Qualifier("championsPassiveImg")
	private UriComponentsBuilder championsPassiveImgBuilder;

	@Test
	void championsPassiveImgUri() {
		String spellImgFull = "ChampionPassive.png";
		UriComponents championsPassiveImgUri = championsPassiveImgBuilder.buildAndExpand(patch, spellImgFull);
		assertThat(championsPassiveImgUri.toString()).as("Champion Passive Image URI")
				.isEqualTo(ddragon.getBaseUrl() + ddragon.getChampionsPassiveImg().replace("{version}", patch)
						.replace("{championPassiveImgFull}", spellImgFull));
	}

	/*
	 * Maps Uri Components Tests
	 */

	@Autowired
	@Qualifier("maps")
	private UriComponentsBuilder mapsUriBuilder;

	@Test
	void mapsUri() {
		UriComponents mapsUri = mapsUriBuilder.buildAndExpand(patch, locale);
		assertThat(mapsUri.toString()).as("Maps URI")
				.isEqualTo(ddragon.getBaseUrl() + ddragon.getMaps().replace("{version}", patch).replace("{locale}", locale));
	}

	@Autowired
	@Qualifier("mapsImg")
	private UriComponentsBuilder mapsImgBuilder;

	@Test
	void mapsImgUri() {
		String imgFull = "Map.png";
		UriComponents mapsImgUri = mapsImgBuilder.buildAndExpand(patch, imgFull);
		assertThat(mapsImgUri.toString()).as("Maps Image URI")
				.isEqualTo(ddragon.getBaseUrl() + ddragon.getMapsImg().replace("{version}", patch).replace("{mapImgFull}", imgFull));
	}

	/*
	 * Versions Uri Components Tests
	 */

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	@Test
	void versionsUri() {
		assertThat(versionsUri.toString()).as("Versions URI").isEqualTo(ddragon.getBaseUrl() + ddragon.getVersions());
	}

}
