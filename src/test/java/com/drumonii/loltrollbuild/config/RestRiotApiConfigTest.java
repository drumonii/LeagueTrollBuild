package com.drumonii.loltrollbuild.config;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties.Api;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties.StaticData;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.util.UriComponents;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.StrictAssertions.entry;

public class RestRiotApiConfigTest extends BaseSpringTestRunner {

	@Autowired
	private RiotApiProperties riotProperties;

	private Api api;
	private StaticData staticData;

	private String scheme;
	private String host;

	@Before
	public void before() {
		api = riotProperties.getApi();
		staticData = api.getStaticData();
		scheme = staticData.getScheme();
		host = staticData.getBaseUrl().replace("{region}", staticData.getRegion());
	}

	@Autowired
	@Qualifier("summonerSpells")
	private UriComponents summonerSpellsUri;

	@Test
	public void summonerSpellsUri() {
		assertThat(summonerSpellsUri.getScheme()).isEqualTo(scheme);
		assertThat(summonerSpellsUri.getHost()).isEqualTo(host);
		assertThat(summonerSpellsUri.getPath()).isEqualTo(staticData.getSummonerSpells());
		assertThat(summonerSpellsUri.getQueryParams()).contains(entry("spellData",
				Arrays.asList("cooldown,image,modes")));
		assertThat(summonerSpellsUri.getQueryParams()).contains(entry("api_key", Arrays.asList("API_KEY")));
	}

	@Autowired
	@Qualifier("items")
	private UriComponents itemsUri;

	@Test
	public void itemsUri() {
		assertThat(itemsUri.getScheme()).isEqualTo(scheme);
		assertThat(itemsUri.getHost()).isEqualTo(host);
		assertThat(itemsUri.getPath()).isEqualTo(staticData.getItems());
		assertThat(itemsUri.getQueryParams()).contains(entry("itemListData",
				Arrays.asList("consumed,from,gold,groups,image,into,maps")));
		assertThat(itemsUri.getQueryParams()).contains(entry("api_key", Arrays.asList("API_KEY")));
	}

	@Autowired
	@Qualifier("champions")
	private UriComponents championsUri;

	@Test
	public void championsUri() {
		assertThat(championsUri.getScheme()).isEqualTo(scheme);
		assertThat(championsUri.getHost()).isEqualTo(host);
		assertThat(championsUri.getPath()).isEqualTo(staticData.getChampions());
		assertThat(championsUri.getQueryParams()).contains(entry("champData", Arrays.asList("image,partype,tags")));
		assertThat(championsUri.getQueryParams()).contains(entry("api_key", Arrays.asList("API_KEY")));
	}

}
