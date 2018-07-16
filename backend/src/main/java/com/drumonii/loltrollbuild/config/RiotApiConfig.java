package com.drumonii.loltrollbuild.config;

import com.drumonii.loltrollbuild.config.Profiles.Ddragon;
import com.drumonii.loltrollbuild.config.Profiles.StaticData;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
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
	 * Configuration for the {@code StaticData} {@link UriComponentsBuilder}s.
	 */
	@Configuration
	@StaticData
	static class StaticDataConfig {

		private final RiotApiProperties riotProperties;

		public StaticDataConfig(RiotApiProperties riotProperties) {
			this.riotProperties = riotProperties;
		}

		@Bean
		public RestTemplate restTemplate(RestTemplateBuilder builder) {
			return builder.build();
		}

		/*
	     * Summoner Spells Uri Components
	     */

		@Bean
		@Qualifier("summonerSpells")
		public UriComponents summonerSpellsUri() {
			return UriComponentsBuilder.newInstance()
					.scheme("https")
					.host(riotProperties.getStaticData().getBaseUrl())
					.path(riotProperties.getStaticData().getSummonerSpells())
					.queryParam("tags", "cooldown")
					.queryParam("tags", "image")
					.queryParam("tags", "key")
					.queryParam("tags", "modes") // &tags=cooldown&tags=image&tags=key&tags=modes
					.queryParam(riotProperties.getStaticData().getLocaleParam(),
							riotProperties.getStaticData().getLocale())
					.queryParam(riotProperties.getStaticData().getKeyParam(), riotProperties.getStaticData().getApiKey())
					.buildAndExpand(riotProperties.getStaticData().getRegion());
		}

		@Bean
		@Qualifier("summonerSpell")
		public UriComponentsBuilder summonerSpellUri() {
			return UriComponentsBuilder.newInstance()
					.scheme("https")
					.host(riotProperties.getStaticData().getBaseUrl())
					.path(riotProperties.getStaticData().getSummonerSpell())
					.queryParam("tags", "cooldown")
					.queryParam("tags", "image")
					.queryParam("tags", "key")
					.queryParam("tags", "modes") // &tags=cooldown&tags=image&tags=key&tags=modes
					.queryParam(riotProperties.getStaticData().getLocaleParam(),
							riotProperties.getStaticData().getLocale())
					.queryParam(riotProperties.getStaticData().getKeyParam(), riotProperties.getStaticData().getApiKey());
		}

		/*
		 * Items Uri Components
		 */

		@Bean
		@Qualifier("items")
		public UriComponents itemsUri() {
			return UriComponentsBuilder.newInstance()
					.scheme("https")
					.host(riotProperties.getStaticData().getBaseUrl())
					.path(riotProperties.getStaticData().getItems())
					.queryParam("tags", "colloq")
					.queryParam("tags", "consumed")
					.queryParam("tags", "from")
					.queryParam("tags", "gold")
					.queryParam("tags", "image")
					.queryParam("tags", "into")
					.queryParam("tags", "maps")
					.queryParam("tags", "requiredChampion") // &tags=colloq&tags=consumed&tags=from&tags=gold&tags=image&tags=into&tags=maps&tags=requiredChampion
					.queryParam(riotProperties.getStaticData().getLocaleParam(),
							riotProperties.getStaticData().getLocale())
					.queryParam(riotProperties.getStaticData().getKeyParam(), riotProperties.getStaticData().getApiKey())
					.buildAndExpand(riotProperties.getStaticData().getRegion());
		}

		@Bean
		@Qualifier("item")
		public UriComponentsBuilder itemUri() {
			return UriComponentsBuilder.newInstance()
					.scheme("https")
					.host(riotProperties.getStaticData().getBaseUrl())
					.path(riotProperties.getStaticData().getItem())
					.queryParam("tags", "colloq")
					.queryParam("tags", "consumed")
					.queryParam("tags", "from")
					.queryParam("tags", "gold")
					.queryParam("tags", "image")
					.queryParam("tags", "into")
					.queryParam("tags", "maps")
					.queryParam("tags", "requiredChampion") // &tags=colloq&tags=consumed&tags=from&tags=gold&tags=image&tags=into&tags=maps&tags=requiredChampion
					.queryParam(riotProperties.getStaticData().getLocaleParam(),
							riotProperties.getStaticData().getLocale())
					.queryParam(riotProperties.getStaticData().getKeyParam(), riotProperties.getStaticData().getApiKey());
		}

		/*
		 * Champions Uri Components
		 */

		@Bean
		@Qualifier("champions")
		public UriComponents championsUri() {
			return UriComponentsBuilder.newInstance()
					.scheme("https")
					.host(riotProperties.getStaticData().getBaseUrl())
					.path(riotProperties.getStaticData().getChampions())
					.queryParam("tags", "image")
					.queryParam("tags", "info")
					.queryParam("tags", "passive")
					.queryParam("tags", "partype")
					.queryParam("tags", "spells")
					.queryParam("tags", "tags") // &tags=image&tags=info&tags=passive&tags=partype&tags=spells&tags=tags
					.queryParam(riotProperties.getStaticData().getLocaleParam(),
							riotProperties.getStaticData().getLocale())
					.queryParam(riotProperties.getStaticData().getKeyParam(), riotProperties.getStaticData().getApiKey())
					.buildAndExpand(riotProperties.getStaticData().getRegion());
		}

		@Bean
		@Qualifier("champion")
		public UriComponentsBuilder championUri() {
			return UriComponentsBuilder.newInstance()
					.scheme("https")
					.host(riotProperties.getStaticData().getBaseUrl())
					.path(riotProperties.getStaticData().getChampion())
					.queryParam("tags", "image")
					.queryParam("tags", "info")
					.queryParam("tags", "passive")
					.queryParam("tags", "partype")
					.queryParam("tags", "spells")
					.queryParam("tags", "tags") // &tags=image&tags=info&tags=passive&tags=partype&tags=spells&tags=tags
					.queryParam(riotProperties.getStaticData().getLocaleParam(),
							riotProperties.getStaticData().getLocale())
					.queryParam(riotProperties.getStaticData().getKeyParam(), riotProperties.getStaticData().getApiKey());
		}

		/*
		 * Maps Uri Components
		 */

		@Bean
		@Qualifier("maps")
		public UriComponents mapsUri() {
			return UriComponentsBuilder.newInstance()
					.scheme("https")
					.host(riotProperties.getStaticData().getBaseUrl())
					.path(riotProperties.getStaticData().getMaps())
					.queryParam(riotProperties.getStaticData().getLocaleParam(),
							riotProperties.getStaticData().getLocale())
					.queryParam(riotProperties.getStaticData().getKeyParam(), riotProperties.getStaticData().getApiKey())
					.buildAndExpand(riotProperties.getStaticData().getRegion());
		}

		/*
		 * Versions Uri Components
		 */

		@Bean
		@Qualifier("versions")
		public UriComponents versionsUri() {
			return UriComponentsBuilder.newInstance()
					.scheme("https")
					.host(riotProperties.getStaticData().getBaseUrl())
					.path(riotProperties.getStaticData().getVersions())
					.queryParam(riotProperties.getStaticData().getLocaleParam(),
							riotProperties.getStaticData().getLocale())
					.queryParam(riotProperties.getStaticData().getKeyParam(), riotProperties.getStaticData().getApiKey())
					.buildAndExpand(riotProperties.getStaticData().getRegion());
		}

	}

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
