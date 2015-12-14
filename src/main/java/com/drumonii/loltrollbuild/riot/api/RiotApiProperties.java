package com.drumonii.loltrollbuild.riot.api;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Configuration properties for Riot's API found in config/application.yml of the resources folder.
 */
@Component
@ConfigurationProperties(prefix = "riot")
public class RiotApiProperties {

	@NotNull
	@Valid
	@Getter @Setter private Api api;

	public static class Api {

		@NotEmpty
		@Getter @Setter private String key;

		@NotNull
		@Valid
		@Getter @Setter private StaticData staticData;

		@NotNull
		@Valid
		@Getter @Setter private Ddragon ddragon;

	}

	/**
	 * Query configuration for Riot's {@code lol-static-data-v1.2} API part of Riot's full API.
	 */
	public static class StaticData {

		@NotEmpty
		@Getter @Setter private String baseUrl;

		@NotEmpty
		@Getter @Setter private String param;

		@NotEmpty
		@Getter @Setter private String region;

		@NotEmpty
		@Getter @Setter private String scheme;

		@NotEmpty
		@Getter @Setter private String champions;

		@NotEmpty
		@Getter @Setter private String champion;

		@NotEmpty
		@Getter @Setter private String items;

		@NotEmpty
		@Getter @Setter private String item;

		@NotEmpty
		@Getter @Setter private String maps;

		@NotEmpty
		@Getter @Setter private String summonerSpells;

		@NotEmpty
		@Getter @Setter private String summonerSpell;

		@NotEmpty
		@Getter @Setter private String versions;

	}

	/**
	 * Configuration for Riot's {@code Data Dragon}, a web service that is meant to centralize League of Legends game
	 * data.
	 */
	public static class Ddragon {

		@NotEmpty
		@Getter @Setter private String baseUrl;

		@NotEmpty
		@Getter @Setter private String scheme;

		@NotEmpty
		@Getter @Setter private String championsImg;

		@NotEmpty
		@Getter @Setter private String itemsImg;

		@NotEmpty
		@Getter @Setter private String mapsImg;

		@NotEmpty
		@Getter @Setter private String summonerSpellsImg;

	}

}
