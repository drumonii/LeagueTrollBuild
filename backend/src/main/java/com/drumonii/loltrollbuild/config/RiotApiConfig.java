package com.drumonii.loltrollbuild.config;

import com.drumonii.loltrollbuild.config.Profiles.Ddragon;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import com.drumonii.loltrollbuild.riot.api.RiotClientHttpRequestInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

/**
 * Configuration for using a {@link RestTemplate} to retrieve models from Riot's API.
 */
@Configuration
public class RiotApiConfig {

	/**
	 * Configuration for the {@code Ddragon} {@link UriComponentsBuilder}s.
	 */
	@Configuration
	@Ddragon
	static class DdragonConfig {

		private final RiotApiProperties riotProperties;

		public DdragonConfig(RiotApiProperties riotProperties) {
			this.riotProperties = riotProperties;
		}

		@Bean
		public RestTemplate restTemplate(RestTemplateBuilder builder, ObjectMapper objectMapper) {
			MappingJackson2HttpMessageConverter textJsonMappingJackson2HttpMessageConverter =
					new MappingJackson2HttpMessageConverter(objectMapper);
			textJsonMappingJackson2HttpMessageConverter
					.setSupportedMediaTypes(Collections.singletonList(MediaType.parseMediaType("text/json;charset=UTF-8")));
			return builder
					.additionalMessageConverters(textJsonMappingJackson2HttpMessageConverter)
					.additionalInterceptors(new RiotClientHttpRequestInterceptor())
					.build();
		}

		/*
	     * Summoner Spells Uri Components
	     */

		@Bean
		@Qualifier("summonerSpells")
		public UriComponentsBuilder summonerSpellsUri() {
			return UriComponentsBuilder.newInstance()
					.scheme("https")
					.host(riotProperties.getDdragon().getBaseUrl())
					.path(riotProperties.getDdragon().getSummonerSpells());
		}

		/*
		 * Items Uri Components
		 */

		@Bean
		@Qualifier("items")
		public UriComponentsBuilder itemsUri() {
			return UriComponentsBuilder.newInstance()
					.scheme("https")
					.host(riotProperties.getDdragon().getBaseUrl())
					.path(riotProperties.getDdragon().getItems());
		}

		/*
		 * Champions Uri Components
		 */

		@Bean
		@Qualifier("champions")
		public UriComponentsBuilder championsUri() {
			return UriComponentsBuilder.newInstance()
					.scheme("https")
					.host(riotProperties.getDdragon().getBaseUrl())
					.path(riotProperties.getDdragon().getChampions());
		}

		/*
		 * Champions Uri Components
		 */

		@Bean
		@Qualifier("champion")
		public UriComponentsBuilder championUri() {
			return UriComponentsBuilder.newInstance()
					.scheme("https")
					.host(riotProperties.getDdragon().getBaseUrl())
					.path(riotProperties.getDdragon().getChampion());
		}

		/*
		 * Maps Uri Components
		 */

		@Bean
		@Qualifier("maps")
		public UriComponentsBuilder mapsUri() {
			return UriComponentsBuilder.newInstance()
					.scheme("https")
					.host(riotProperties.getDdragon().getBaseUrl())
					.path(riotProperties.getDdragon().getMaps());
		}

		/*
		 * Versions Uri Components
		 */

		@Bean
		@Qualifier("versions")
		public UriComponents versionsUri() {
			return UriComponentsBuilder.newInstance()
					.scheme("https")
					.host(riotProperties.getDdragon().getBaseUrl())
					.path(riotProperties.getDdragon().getVersions())
					.buildAndExpand();
		}

	}

	/**
	 * Configuration for Data Dragon image {@link UriComponentsBuilder}.
	 */
	@Configuration
	static class DdragonImgConfig {

		private final RiotApiProperties riotProperties;

		public DdragonImgConfig(RiotApiProperties riotProperties) {
			this.riotProperties = riotProperties;
		}

		/*
	     * Summoner Spells Img Uri Components
	     */

		@Bean
		@Qualifier("summonerSpellsImg")
		public UriComponentsBuilder summonerSpellImgUri() {
			return UriComponentsBuilder.newInstance()
					.scheme("https")
					.host(riotProperties.getDdragon().getBaseUrl())
					.path(riotProperties.getDdragon().getSummonerSpellsImg());
		}

		/*
		 * Items Img Uri Components
		 */

		@Bean
		@Qualifier("itemsImg")
		public UriComponentsBuilder itemsImgUri() {
			return UriComponentsBuilder.newInstance()
					.scheme("https")
					.host(riotProperties.getDdragon().getBaseUrl())
					.path(riotProperties.getDdragon().getItemsImg());
		}

		/*
		 * Champions Uri Components
		 */

		@Bean
		@Qualifier("championsImg")
		public UriComponentsBuilder championsImgUri() {
			return UriComponentsBuilder.newInstance()
					.scheme("https")
					.host(riotProperties.getDdragon().getBaseUrl())
					.path(riotProperties.getDdragon().getChampionsImg());
		}

		@Bean
		@Qualifier("championsSpellImg")
		public UriComponentsBuilder championsSpellImgUri() {
			return UriComponentsBuilder.newInstance()
					.scheme("https")
					.host(riotProperties.getDdragon().getBaseUrl())
					.path(riotProperties.getDdragon().getChampionsSpellImg());
		}

		@Bean
		@Qualifier("championsPassiveImg")
		public UriComponentsBuilder championsPassiveImgUri() {
			return UriComponentsBuilder.newInstance()
					.scheme("https")
					.host(riotProperties.getDdragon().getBaseUrl())
					.path(riotProperties.getDdragon().getChampionsPassiveImg());
		}

		/*
		 * Maps Uri Components
		 */

		@Bean
		@Qualifier("mapsImg")
		public UriComponentsBuilder mapsImgUri() {
			return UriComponentsBuilder.newInstance()
					.scheme("https")
					.host(riotProperties.getDdragon().getBaseUrl())
					.path(riotProperties.getDdragon().getMapsImg());
		}

	}

}
