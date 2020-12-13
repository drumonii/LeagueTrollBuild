package com.drumonii.loltrollbuild.config;

import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties.Ddragon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
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
import static org.assertj.core.api.Assertions.assertThatCode;

@ExtendWith({ SpringExtension.class })
@Import(RiotApiConfig.class)
@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
@EnableConfigurationProperties(RiotApiProperties.class)
@ImportAutoConfiguration({ RestTemplateAutoConfiguration.class, JacksonAutoConfiguration.class })
@ActiveProfiles({ TESTING, DDRAGON })
class RiotApiPropertiesDdragonConfigTest {

	@Autowired
	private RiotApiProperties riotProperties;

	private Ddragon ddragon;

	private String patch;
	private String locale;

	@BeforeEach
	void beforeEach() {
		ddragon = riotProperties.getDdragon();
		patch = "7.17.2";
		locale = ddragon.getLocale();
	}

	@Nested
	@DisplayName("RestTemplate Tests")
	class RestTemplateTests {

		@Test
		void restTemplate(ApplicationContext applicationContext) {
			assertThatCode(() -> {
				RestTemplate restTemplate = applicationContext.getBean("restTemplate", RestTemplate.class);
				assertThat(restTemplate.getInterceptors()).hasSize(1);
				assertThat(restTemplate.getMessageConverters()).flatExtracting(HttpMessageConverter::getSupportedMediaTypes)
						.contains(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM,
								MediaType.parseMediaType("binary/octet-stream"), MediaType.parseMediaType("text/json;charset=UTF-8"));
			}).doesNotThrowAnyException();
		}

	}

	@Nested
	@DisplayName("Summoner Spells Uri Components Tests")
	class SummonerSpellsUriComponentsTests {

		@Test
		void summonerSpellsUri(@Qualifier("summonerSpells") UriComponentsBuilder summonerSpellsUriBuilder) {
			UriComponents summonerSpellsUri = summonerSpellsUriBuilder.buildAndExpand(patch, locale);
			assertThat(summonerSpellsUri.toString()).as("Summoner Spells URI")
					.isEqualTo(ddragon.getBaseUrl() + ddragon.getSummonerSpells().replace("{version}", patch).replace("{locale}", locale));
		}

		@Test
		void summonerSpellImgUri(@Qualifier("summonerSpellsImg") UriComponentsBuilder summonerSpellImgBuilder) {
			String imgFull = "SummonerBoost.png";
			UriComponents summonerSpellImgUri = summonerSpellImgBuilder.buildAndExpand(patch, imgFull);
			assertThat(summonerSpellImgUri.toString()).as("Summoner Spells Image URI")
					.isEqualTo(ddragon.getBaseUrl() + ddragon.getSummonerSpellsImg().replace("{version}", patch)
							.replace("{summonerSpellImgFull}", imgFull));
		}

	}

	@Nested
	@DisplayName("Items Uri Components Tests")
	class ItemsUriComponentsTests {

		@Test
		void itemsUri(@Qualifier("items") UriComponentsBuilder itemsUriBuilder) {
			UriComponents itemsUri = itemsUriBuilder.buildAndExpand(patch, locale);
			assertThat(itemsUri.toString()).as("Items URI")
					.isEqualTo(ddragon.getBaseUrl() + ddragon.getItems().replace("{version}", patch).replace("{locale}", locale));
		}

		@Test
		void itemsImgUri(@Qualifier("itemsImg") UriComponentsBuilder itemsImgBuilder) {
			String imgFull = "1.png";
			UriComponents itemsImgUri = itemsImgBuilder.buildAndExpand(patch, imgFull);
			assertThat(itemsImgUri.toString()).as("Items Image URI")
					.isEqualTo(ddragon.getBaseUrl() + ddragon.getItemsImg().replace("{version}", patch).replace("{itemImgFull}", imgFull));
		}

	}

	@Nested
	@DisplayName("Champions Uri Components Tests")
	class ChampionsUriComponentsTests {

		@Test
		void championsUri(@Qualifier("champions") UriComponentsBuilder championsUriBuilder) {
			UriComponents championsUri = championsUriBuilder.buildAndExpand(patch, locale);
			assertThat(championsUri.toString()).as("Champions URI")
					.isEqualTo(ddragon.getBaseUrl() + ddragon.getChampions().replace("{version}", patch).replace("{locale}", locale));
		}

		@Test
		void championUri(@Qualifier("champion") UriComponentsBuilder championUriBuilder) {
			int id = 1;
			UriComponents championUri = championUriBuilder.buildAndExpand(patch, locale, id);
			assertThat(championUri.toString()).as("Champion URI")
					.isEqualTo(ddragon.getBaseUrl() + ddragon.getChampion().replace("{version}", patch)
							.replace("{locale}", locale).replace("{id}", String.valueOf(id)));
		}

		@Test
		void championsImgUri(@Qualifier("championsImg") UriComponentsBuilder championsImgBuilder) {
			String imgFull = "Champion.png";
			UriComponents championsImgUri = championsImgBuilder.buildAndExpand(patch, imgFull);
			assertThat(championsImgUri.toString()).as("Champions Image URI")
					.isEqualTo(ddragon.getBaseUrl() + ddragon.getChampionsImg().replace("{version}", patch).replace("{championImgFull}", imgFull));
		}

		@Test
		void championsSpellImgUri(@Qualifier("championsSpellImg") UriComponentsBuilder championsSpellImgBuilder) {
			String spellImgFull = "ChampionSpell.png";
			UriComponents championsSpellImgUri = championsSpellImgBuilder.buildAndExpand(patch, spellImgFull);
			assertThat(championsSpellImgUri.toString()).as("Champion Spell Image URI")
					.isEqualTo(ddragon.getBaseUrl() + ddragon.getChampionsSpellImg().replace("{version}", patch)
							.replace("{championSpellImgFull}", spellImgFull));
		}

		@Test
		void championsPassiveImgUri(@Qualifier("championsPassiveImg") UriComponentsBuilder championsPassiveImgBuilder) {
			String spellImgFull = "ChampionPassive.png";
			UriComponents championsPassiveImgUri = championsPassiveImgBuilder.buildAndExpand(patch, spellImgFull);
			assertThat(championsPassiveImgUri.toString()).as("Champion Passive Image URI")
					.isEqualTo(ddragon.getBaseUrl() + ddragon.getChampionsPassiveImg().replace("{version}", patch)
							.replace("{championPassiveImgFull}", spellImgFull));
		}

	}

	@Nested
	@DisplayName("Maps Uri Components Tests")
	class MapsUriComponentsTests {

		@Test
		void mapsUri(@Qualifier("maps") UriComponentsBuilder mapsUriBuilder) {
			UriComponents mapsUri = mapsUriBuilder.buildAndExpand(patch, locale);
			assertThat(mapsUri.toString()).as("Maps URI")
					.isEqualTo(ddragon.getBaseUrl() + ddragon.getMaps().replace("{version}", patch).replace("{locale}", locale));
		}

		@Test
		void mapsImgUri(@Qualifier("mapsImg") UriComponentsBuilder mapsImgBuilder) {
			String imgFull = "Map.png";
			UriComponents mapsImgUri = mapsImgBuilder.buildAndExpand(patch, imgFull);
			assertThat(mapsImgUri.toString()).as("Maps Image URI")
					.isEqualTo(ddragon.getBaseUrl() + ddragon.getMapsImg().replace("{version}", patch).replace("{mapImgFull}", imgFull));
		}

	}

	@Nested
	@DisplayName("Versions Uri Components Tests")
	class VersionsUriComponentsTests {

		@Test
		void versionsUri(@Qualifier("versions") UriComponents versionsUri) {
			assertThat(versionsUri.toString()).as("Versions URI").isEqualTo(ddragon.getBaseUrl() + ddragon.getVersions());
		}

	}

}
