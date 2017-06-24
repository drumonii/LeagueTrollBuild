package com.drumonii.loltrollbuild.config;

import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties.RiotApi;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties.Ddragon;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties.StaticData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;

/**
 * Configuration for using a {@link RestTemplate} to retrieve models from Riot's API.
 */
@Configuration
public class RiotApiRestConfig {

	@Autowired
	private RiotApiProperties riotProperties;

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	private RiotApi api;
	private StaticData staticData;
	private Ddragon ddragon;

	@PostConstruct
	public void postConstruct() {
		api = riotProperties.getApi();
		staticData = api.getStaticData();
		ddragon = api.getDdragon();
	}

	/*
	 * Summoner Spells Uri Components
	 */

	@Bean
	@Qualifier("summonerSpells")
	public UriComponents summonerSpellsUri() {
		return UriComponentsBuilder.newInstance()
				.scheme(staticData.getScheme())
				.host(staticData.getBaseUrl())
				.path(staticData.getSummonerSpells())
				.queryParam("tags", "cooldown")
				.queryParam("tags", "image")
				.queryParam("tags", "key")
				.queryParam("tags", "modes")
				.queryParam(staticData.getKeyParam(), api.getKey())
				.buildAndExpand(staticData.getRegion());
	}

	@Bean
	@Qualifier("summonerSpell")
	public UriComponentsBuilder summonerSpellUri() {
		return UriComponentsBuilder.newInstance()
				.scheme(staticData.getScheme())
				.host(staticData.getBaseUrl())
				.path(staticData.getSummonerSpell())
				.queryParam("tags", "cooldown")
				.queryParam("tags", "image")
				.queryParam("tags", "key")
				.queryParam("tags", "modes")
				.queryParam(staticData.getKeyParam(), api.getKey());
	}

	@Bean
	@Qualifier("summonerSpellsImg")
	public UriComponentsBuilder summonerSpellImgUri() {
		return UriComponentsBuilder.newInstance()
				.scheme(ddragon.getScheme())
				.host(ddragon.getBaseUrl())
				.path(ddragon.getSummonerSpellsImg());
	}

	/*
	 * Items Uri Components
	 */

	@Bean
	@Qualifier("items")
	public UriComponents itemsUri() {
		return UriComponentsBuilder.newInstance()
				.scheme(staticData.getScheme())
				.host(staticData.getBaseUrl())
				.path(staticData.getItems())
				.queryParam("tags", "consumed")
				.queryParam("tags", "from")
				.queryParam("tags", "gold")
				.queryParam("tags", "image")
				.queryParam("tags", "into")
				.queryParam("tags", "maps")
				.queryParam("tags", "requiredChampion")
				.queryParam(staticData.getKeyParam(), api.getKey())
				.buildAndExpand(staticData.getRegion());
	}

	@Bean
	@Qualifier("item")
	public UriComponentsBuilder itemUri() {
		return UriComponentsBuilder.newInstance()
				.scheme(staticData.getScheme())
				.host(staticData.getBaseUrl())
				.path(staticData.getItem())
				.queryParam("tags", "consumed")
				.queryParam("tags", "from")
				.queryParam("tags", "gold")
				.queryParam("tags", "image")
				.queryParam("tags", "into")
				.queryParam("tags", "maps")
				.queryParam("tags", "requiredChampion")
				.queryParam(staticData.getKeyParam(), api.getKey());
	}

	@Bean
	@Qualifier("itemsImg")
	public UriComponentsBuilder itemsImgUri() {
		return UriComponentsBuilder.newInstance()
				.scheme(ddragon.getScheme())
				.host(ddragon.getBaseUrl())
				.path(ddragon.getItemsImg());
	}

	/*
	 * Champions Uri Components
	 */

	@Bean
	@Qualifier("champions")
	public UriComponents championsUri() {
		return UriComponentsBuilder.newInstance()
				.scheme(staticData.getScheme())
				.host(staticData.getBaseUrl())
				.path(staticData.getChampions())
				.queryParam("tags", "image")
				.queryParam("tags", "info")
				.queryParam("tags", "passive")
				.queryParam("tags", "partype")
				.queryParam("tags", "spells")
				.queryParam("tags", "tags")
				.queryParam(staticData.getKeyParam(), api.getKey())
				.buildAndExpand(staticData.getRegion());
	}

	@Bean
	@Qualifier("champion")
	public UriComponentsBuilder championUri() {
		return UriComponentsBuilder.newInstance()
				.scheme(staticData.getScheme())
				.host(staticData.getBaseUrl())
				.path(staticData.getChampion())
				.queryParam("tags", "image")
				.queryParam("tags", "info")
				.queryParam("tags", "passive")
				.queryParam("tags", "partype")
				.queryParam("tags", "spells")
				.queryParam("tags", "tags")
				.queryParam(staticData.getKeyParam(), api.getKey());
	}

	@Bean
	@Qualifier("championsImg")
	public UriComponentsBuilder championsImgUri() {
		return UriComponentsBuilder.newInstance()
				.scheme(ddragon.getScheme())
				.host(ddragon.getBaseUrl())
				.path(ddragon.getChampionsImg());
	}

	@Bean
	@Qualifier("championsSpellImg")
	public UriComponentsBuilder championsSpellImgUri() {
		return UriComponentsBuilder.newInstance()
				.scheme(ddragon.getScheme())
				.host(ddragon.getBaseUrl())
				.path(ddragon.getChampionsSpellImg());
	}

	@Bean
	@Qualifier("championsPassiveImg")
	public UriComponentsBuilder championsPassiveImgUri() {
		return UriComponentsBuilder.newInstance()
				.scheme(ddragon.getScheme())
				.host(ddragon.getBaseUrl())
				.path(ddragon.getChampionsPassiveImg());
	}

	/*
	 * Maps Uri Components
	 */

	@Bean
	@Qualifier("maps")
	public UriComponents mapsUri() {
		return UriComponentsBuilder.newInstance()
				.scheme(staticData.getScheme())
				.host(staticData.getBaseUrl())
				.path(staticData.getMaps())
				.queryParam(staticData.getKeyParam(), api.getKey())
				.buildAndExpand(staticData.getRegion());
	}

	@Bean
	@Qualifier("mapsImg")
	public UriComponentsBuilder mapsImgUri() {
		return UriComponentsBuilder.newInstance()
				.scheme(ddragon.getScheme())
				.host(ddragon.getBaseUrl())
				.path(ddragon.getMapsImg());
	}

	/*
	 * Versions Uri Components
	 */

	@Bean
	@Qualifier("versions")
	public UriComponents versionsUri() {
		return UriComponentsBuilder.newInstance()
				.scheme(staticData.getScheme())
				.host(staticData.getBaseUrl())
				.path(staticData.getVersions())
				.queryParam(staticData.getKeyParam(), api.getKey())
				.buildAndExpand(staticData.getRegion());
	}

}
