package com.drumonii.loltrollbuild.riot.api;

import com.drumonii.loltrollbuild.constraint.ValidRiotApiProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for Riot's API found in config/application.yml of the resources folder.
 */
@Validated
@ValidRiotApiProperties
@ConfigurationProperties(prefix = "riot")
public class RiotApiProperties {

	@NestedConfigurationProperty
	@Getter @Setter private StaticData staticData;

	@NestedConfigurationProperty
	@Getter @Setter private Ddragon ddragon;

	/**
	 * Query configuration for Riot's {@code lol-static-data-v3} API part of Riot's full API.
	 */
	public static class StaticData {

		@Getter @Setter private String apiKey;

		@Getter @Setter private String baseUrl;

		@Getter @Setter private String keyParam;

		@Getter @Setter private String localeParam;

		@Getter @Setter private String locale;

		@Getter @Setter private String region;

		@Getter @Setter private String champions;

		@Getter @Setter private String champion;

		@Getter @Setter private String items;

		@Getter @Setter private String item;

		@Getter @Setter private String maps;

		@Getter @Setter private String summonerSpells;

		@Getter @Setter private String summonerSpell;

		@Getter @Setter private String versions;

	}

	/**
	 * Configuration for Riot's {@code Data Dragon}, a web service that is meant to centralize League of Legends game
	 * data.
	 */
	public static class Ddragon {

		@Getter @Setter private String baseUrl;

		@Getter @Setter private String locale;

		@Getter @Setter private String champions;

		@Getter @Setter private String champion;

		@Getter @Setter private String championsImg;

		@Getter @Setter private String championsSpellImg;

		@Getter @Setter private String championsPassiveImg;

		@Getter @Setter private String items;

		@Getter @Setter private String itemsImg;

		@Getter @Setter private String maps;

		@Getter @Setter private String mapsImg;

		@Getter @Setter private String summonerSpells;

		@Getter @Setter private String summonerSpellsImg;

		@Getter @Setter private String versions;

	}

}
