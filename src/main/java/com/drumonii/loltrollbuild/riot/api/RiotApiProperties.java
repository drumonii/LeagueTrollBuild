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
		@Getter @Setter private String items;

		@NotEmpty
		@Getter @Setter private String summonerSpells;

		@NotEmpty
		@Getter @Setter private String versions;

	}

}
