package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.riot.RiotApiProperties.Ddragon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Locale;
import java.util.function.Consumer;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ SpringExtension.class })
@Import(RiotApiConfig.class)
@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
@EnableConfigurationProperties(RiotApiProperties.class)
@ImportAutoConfiguration({ WebClientAutoConfiguration.class, JacksonAutoConfiguration.class })
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
	@DisplayName("Summoner Spells Uri Components Tests")
	class SummonerSpellsUriComponentsTests {


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
		void itemsImgUri(@Qualifier("itemsImg") UriComponentsBuilder itemsImgBuilder) {
			assertThat(itemsImgBuilder.build()).as("Items Image URI")
					.satisfies(new RiotApiImgUriComponents(ddragon.getItemsImg()));
		}

	}

	@Nested
	@DisplayName("Champions Uri Components Tests")
	class ChampionsUriComponentsTests {

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
		void mapsImgUri(@Qualifier("mapsImg") UriComponentsBuilder mapsImgBuilder) {
			assertThat(mapsImgBuilder.build()).as("Maps Image URI")
					.satisfies(new RiotApiImgUriComponents(ddragon.getMapsImg()));
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
