package com.drumonii.loltrollbuild.riot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

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
	public CodecCustomizer riotApiCodecCustomizer(ObjectMapper objectMapper) {
		return configurer ->
			configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON,
					MediaType.APPLICATION_OCTET_STREAM, MediaType.parseMediaType("binary/octet-stream"),
					MediaType.parseMediaType("text/json;charset=UTF-8")));
	}

	@Bean
	public WebClient webClient(WebClient.Builder builder) {
		return builder
				.baseUrl(ddragon.getBaseUrl())
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.build();
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
