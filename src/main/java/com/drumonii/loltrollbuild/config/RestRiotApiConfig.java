package com.drumonii.loltrollbuild.config;

import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties.Api;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties.Ddragon;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties.StaticData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class RestRiotApiConfig {

	@Autowired
	private RiotApiProperties riotProperties;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	private Api api ;
	private StaticData staticData;
	private Ddragon ddragon;

	@PostConstruct
	public void postConstruct() {
		api = riotProperties.getApi();
		staticData = api.getStaticData();
		ddragon = api.getDdragon();
	}

	@Bean
	@Qualifier("summonerSpells")
	public UriComponents summonerSpellsUri() {
		return UriComponentsBuilder.newInstance()
				.scheme(staticData.getScheme())
				.host(staticData.getBaseUrl())
				.path(staticData.getSummonerSpells())
				.queryParam("spellData", "cooldown,image,modes")
				.queryParam(staticData.getParam(), api.getKey())
				.buildAndExpand(staticData.getRegion());
	}

	@Bean
	@Qualifier("summonerSpell")
	public UriComponentsBuilder summonerSpellUri() {
		return UriComponentsBuilder.newInstance()
				.scheme(staticData.getScheme())
				.host(staticData.getBaseUrl())
				.path(staticData.getSummonerSpell())
				.queryParam("spellData", "cooldown,image,modes")
				.queryParam(staticData.getParam(), api.getKey());
	}

	@Bean
	@Qualifier("summonerSpellsImg")
	public UriComponentsBuilder summonerSpellImgUri() {
		return UriComponentsBuilder.newInstance()
				.scheme(ddragon.getScheme())
				.host(ddragon.getBaseUrl())
				.path(ddragon.getSummonerSpellsImg());
	}

	@Bean
	@Qualifier("items")
	public UriComponents itemsUri() {
		return UriComponentsBuilder.newInstance()
				.scheme(staticData.getScheme())
				.host(staticData.getBaseUrl())
				.path(staticData.getItems())
				.queryParam("itemListData", "consumed,from,gold,image,into,maps,requiredChampion")
				.queryParam(staticData.getParam(), api.getKey())
				.buildAndExpand(staticData.getRegion());
	}

	@Bean
	@Qualifier("item")
	public UriComponentsBuilder itemUri() {
		return UriComponentsBuilder.newInstance()
				.scheme(staticData.getScheme())
				.host(staticData.getBaseUrl())
				.path(staticData.getItem())
				.queryParam("itemData", "consumed,from,gold,image,into,maps")
				.queryParam(staticData.getParam(), api.getKey());
	}

	@Bean
	@Qualifier("itemsImg")
	public UriComponentsBuilder itemsImgUri() {
		return UriComponentsBuilder.newInstance()
				.scheme(ddragon.getScheme())
				.host(ddragon.getBaseUrl())
				.path(ddragon.getItemsImg());
	}

	@Bean
	@Qualifier("champions")
	public UriComponents championsUri() {
		return UriComponentsBuilder.newInstance()
				.scheme(staticData.getScheme())
				.host(staticData.getBaseUrl())
				.path(staticData.getChampions())
				.queryParam("champData", "image,partype,tags")
				.queryParam(staticData.getParam(), api.getKey())
				.buildAndExpand(staticData.getRegion());
	}

	@Bean
	@Qualifier("champion")
	public UriComponentsBuilder championUri() {
		return UriComponentsBuilder.newInstance()
				.scheme(staticData.getScheme())
				.host(staticData.getBaseUrl())
				.path(staticData.getChampion())
				.queryParam("champData", "image,partype,tags")
				.queryParam(staticData.getParam(), api.getKey());
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
	@Qualifier("versions")
	public UriComponents versionsUri() {
		return UriComponentsBuilder.newInstance()
				.scheme(staticData.getScheme())
				.host(staticData.getBaseUrl())
				.path(staticData.getVersions())
				.queryParam(staticData.getParam(), api.getKey())
				.buildAndExpand(staticData.getRegion());
	}

}
