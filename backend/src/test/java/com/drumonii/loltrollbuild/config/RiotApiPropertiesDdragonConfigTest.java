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

import java.util.Locale;
import java.util.function.Consumer;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@ExtendWith({ SpringExtension.class })
@Import(RiotApiConfig.class)
@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
@EnableConfigurationProperties(RiotApiProperties.class)
@ImportAutoConfiguration({ RestTemplateAutoConfiguration.class, JacksonAutoConfiguration.class })
@ActiveProfiles({ TESTING })
class RiotApiPropertiesDdragonConfigTest {

	@Autowired
	private RiotApiProperties riotProperties;

	private Ddragon ddragon;
	private Locale locale;

	@BeforeEach
	void beforeEach() {
		ddragon = riotProperties.getDdragon();
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
			assertThat(summonerSpellsUriBuilder.build()).as("Summoner Spells URI")
					.satisfies(new RiotApiJsonUriComponents(ddragon.getSummonerSpells()));
		}

		@Test
		void summonerSpellImgUri(@Qualifier("summonerSpellsImg") UriComponentsBuilder summonerSpellImgBuilder) {
			assertThat(summonerSpellImgBuilder.build()).as("Summoner Spells Image URI")
					.satisfies(new RiotApiImgUriComponents(ddragon.getSummonerSpellsImg()));
		}

	}

	@Nested
	@DisplayName("Items Uri Components Tests")
	class ItemsUriComponentsTests {

		@Test
		void itemsUri(@Qualifier("items") UriComponentsBuilder itemsUriBuilder) {
			assertThat(itemsUriBuilder.build()).as("Items URI")
					.satisfies(new RiotApiJsonUriComponents(ddragon.getItems()));
		}

		@Test
		void itemsImgUri(@Qualifier("itemsImg") UriComponentsBuilder itemsImgBuilder) {
			assertThat(itemsImgBuilder.build()).as("Items Image URI")
					.satisfies(new RiotApiImgUriComponents(ddragon.getItemsImg()));
		}

	}

	@Nested
	@DisplayName("Champions Uri Components Tests")
	class ChampionsUriComponentsTests {

		@Test
		void championsUri(@Qualifier("champions") UriComponentsBuilder championsUriBuilder) {
			assertThat(championsUriBuilder.build()).as("Champions URI")
					.satisfies(new RiotApiJsonUriComponents(ddragon.getChampions()));
		}

		@Test
		void championUri(@Qualifier("champion") UriComponentsBuilder championUriBuilder) {
			assertThat(championUriBuilder.build()).as("Champion URI")
					.satisfies(new RiotApiJsonUriComponents(ddragon.getChampion()));
		}

		@Test
		void championsImgUri(@Qualifier("championsImg") UriComponentsBuilder championsImgBuilder) {
			assertThat(championsImgBuilder.build()).as("Champions Image URI")
					.satisfies(new RiotApiImgUriComponents(ddragon.getChampionsImg()));
		}

		@Test
		void championsSpellImgUri(@Qualifier("championsSpellImg") UriComponentsBuilder championsSpellImgBuilder) {
			assertThat(championsSpellImgBuilder.build()).as("Champion Spell Image URI")
					.satisfies(new RiotApiImgUriComponents(ddragon.getChampionsSpellImg()));
		}

		@Test
		void championsPassiveImgUri(@Qualifier("championsPassiveImg") UriComponentsBuilder championsPassiveImgBuilder) {
			assertThat(championsPassiveImgBuilder.build()).as("Champion Passive Image URI")
					.satisfies(new RiotApiImgUriComponents(ddragon.getChampionsPassiveImg()));
		}

	}

	@Nested
	@DisplayName("Maps Uri Components Tests")
	class MapsUriComponentsTests {

		@Test
		void mapsUri(@Qualifier("maps") UriComponentsBuilder mapsUriBuilder) {
			assertThat(mapsUriBuilder.build()).as("Maps URI")
					.satisfies(new RiotApiJsonUriComponents(ddragon.getMaps()));
		}

		@Test
		void mapsImgUri(@Qualifier("mapsImg") UriComponentsBuilder mapsImgBuilder) {
			assertThat(mapsImgBuilder.build()).as("Maps Image URI")
					.satisfies(new RiotApiImgUriComponents(ddragon.getMapsImg()));
		}

	}

	@Nested
	@DisplayName("Versions Uri Components Tests")
	class VersionsUriComponentsTests {

		@Test
		void versionsUri(@Qualifier("versions") UriComponents versionsUri) {
			assertThat(versionsUri).as("Versions URI")
					.satisfies(new RiotApiJsonUriComponents(ddragon.getVersions()));
		}

	}

	public class RiotApiUriComponents implements Consumer<UriComponents> {

		private String path;

		public RiotApiUriComponents(String path) {
			this.path = path;
		}

		@Override
		public void accept(UriComponents uriComponents) {
			assertThat(uriComponents.getScheme()).as("Scheme").isEqualTo("https");
			assertThat(uriComponents.getHost()).as("Host")
					.isEqualTo(ddragon.getBaseUrl().replace("https://", ""));
			assertThat(uriComponents.getPath()).as("Path").isEqualTo(path);
			assertThat(uriComponents.getQueryParams()).as("Query Params").isEmpty();
		}

	}

	public class RiotApiImgUriComponents extends RiotApiUriComponents {

		public RiotApiImgUriComponents(String path) {
			super(path);
		}

	}

	public class RiotApiJsonUriComponents extends RiotApiUriComponents {

		public RiotApiJsonUriComponents(String path) {
			super(path.replace("{locale}", locale.toString()));
		}

	}

}
