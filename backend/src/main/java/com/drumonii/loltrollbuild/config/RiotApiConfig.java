package com.drumonii.loltrollbuild.config;

import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import com.drumonii.loltrollbuild.riot.api.RiotClientHttpRequestInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;

/**
 * Configuration for using a {@link RestTemplate} to retrieve models from Riot's API.
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RiotApiProperties.class)
public class RiotApiConfig {

	private final RiotApiProperties.Ddragon ddragon;

	public RiotApiConfig(RiotApiProperties riotProperties) {
		this.ddragon = riotProperties.getDdragon();
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder, ObjectMapper objectMapper) {
		MappingJackson2HttpMessageConverter textJsonMappingJackson2HttpMessageConverter =
				new MappingJackson2HttpMessageConverter(objectMapper);
		textJsonMappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(
				MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM,
				MediaType.parseMediaType("binary/octet-stream"), MediaType.parseMediaType("text/json;charset=UTF-8")));
		return builder
				.additionalMessageConverters(textJsonMappingJackson2HttpMessageConverter)
				.additionalInterceptors(new RiotClientHttpRequestInterceptor())
				.build();
	}

	/*
	 * JSON Uri Components
	 */

	@Bean
	@Qualifier("summonerSpells")
	public UriComponentsBuilder summonerSpellsUri() {
		return UriComponentsBuilder.fromHttpUrl(ddragon.getBaseUrl())
				.path(ddragon.getSummonerSpells().replace("{locale}", ddragon.getLocale().toString()));
	}

	@Bean
	@Qualifier("items")
	public UriComponentsBuilder itemsUri() {
		return UriComponentsBuilder.fromHttpUrl(ddragon.getBaseUrl())
				.path(ddragon.getItems().replace("{locale}", ddragon.getLocale().toString()));
	}

	@Bean
	@Qualifier("champions")
	public UriComponentsBuilder championsUri() {
		return UriComponentsBuilder.fromHttpUrl(ddragon.getBaseUrl())
				.path(ddragon.getChampions().replace("{locale}", ddragon.getLocale().toString()));
	}

	@Bean
	@Qualifier("champion")
	public UriComponentsBuilder championUri() {
		return UriComponentsBuilder.fromHttpUrl(ddragon.getBaseUrl())
				.path(ddragon.getChampion().replace("{locale}", ddragon.getLocale().toString()));
	}

	@Bean
	@Qualifier("maps")
	public UriComponentsBuilder mapsUri() {
		return UriComponentsBuilder.fromHttpUrl(ddragon.getBaseUrl())
				.path(ddragon.getMaps().replace("{locale}", ddragon.getLocale().toString()));
	}

	@Bean
	@Qualifier("versions")
	public UriComponents versionsUri() {
		return UriComponentsBuilder.fromHttpUrl(ddragon.getBaseUrl())
				.path(ddragon.getVersions())
				.buildAndExpand();
	}

	/*
	 * Image Uri Components
	 */

	@Bean
	@Qualifier("summonerSpellsImg")
	public UriComponentsBuilder summonerSpellImgUri() {
		return UriComponentsBuilder.fromHttpUrl(ddragon.getBaseUrl())
				.path(ddragon.getSummonerSpellsImg());
	}

	@Bean
	@Qualifier("itemsImg")
	public UriComponentsBuilder itemsImgUri() {
		return UriComponentsBuilder.fromHttpUrl(ddragon.getBaseUrl())
				.path(ddragon.getItemsImg());
	}

	@Bean
	@Qualifier("championsImg")
	public UriComponentsBuilder championsImgUri() {
		return UriComponentsBuilder.fromHttpUrl(ddragon.getBaseUrl())
				.path(ddragon.getChampionsImg());
	}

	@Bean
	@Qualifier("championsSpellImg")
	public UriComponentsBuilder championsSpellImgUri() {
		return UriComponentsBuilder.fromHttpUrl(ddragon.getBaseUrl())
				.path(ddragon.getChampionsSpellImg());
	}

	@Bean
	@Qualifier("championsPassiveImg")
	public UriComponentsBuilder championsPassiveImgUri() {
		return UriComponentsBuilder.fromHttpUrl(ddragon.getBaseUrl())
				.path(ddragon.getChampionsPassiveImg());
	}

	@Bean
	@Qualifier("mapsImg")
	public UriComponentsBuilder mapsImgUri() {
		return UriComponentsBuilder.fromHttpUrl(ddragon.getBaseUrl())
				.path(ddragon.getMapsImg());
	}

}
