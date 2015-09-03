package com.drumonii.loltrollbuild.config;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties.StaticData;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.StrictAssertions.entry;

public class RestRiotApiConfigTest extends BaseSpringTestRunner {

	public static final String[] PROPERTIES = BaseSpringTestRunner.class.getAnnotation(
			TestPropertySource.class).properties();

	@Autowired
	private RiotApiProperties riotProperties;

	private StaticData staticData;

	private String scheme;
	private String host;
	private String apiKey;

	@Before
	public void before() {
		staticData = riotProperties.getApi().getStaticData();
		scheme = staticData.getScheme();
		host = staticData.getBaseUrl().replace("{region}", staticData.getRegion());
		apiKey = getApiKeyFromProps(PROPERTIES);
	}

	private String getApiKeyFromProps(String[] properties) {
		String[] splitProps = properties[0].split("riot.api.key=");
		return splitProps[1];
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
		assertThat(summonerSpellsUri.getQueryParams()).contains(entry("api_key", Arrays.asList(apiKey)));
	}

	@Autowired
	@Qualifier("summonerSpell")
	private UriComponentsBuilder summonerSpellUri;

	@Test
	public void summonerSpellUri() {
		int id = 1;
		UriComponents uriComponents = summonerSpellUri.buildAndExpand(staticData.getRegion(), id);
		assertThat(uriComponents.getScheme()).isEqualTo(scheme);
		assertThat(uriComponents.getHost()).isEqualTo(host);
		assertThat(uriComponents.getPath()).isEqualTo(staticData.getSummonerSpell().replace("{id}",
				String.valueOf(id)));
		assertThat(uriComponents.getQueryParams()).contains(entry("spellData",
				Arrays.asList("cooldown,image,modes")));
		assertThat(uriComponents.getQueryParams()).contains(entry("api_key", Arrays.asList(apiKey)));
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
				Arrays.asList("consumed,from,gold,image,into,maps")));
		assertThat(itemsUri.getQueryParams()).contains(entry("api_key", Arrays.asList(apiKey)));
	}

	@Autowired
	@Qualifier("item")
	private UriComponentsBuilder itemUri;

	@Test
	public void itemUri() {
		int id = 1;
		UriComponents uriComponents = itemUri.buildAndExpand(staticData.getRegion(), id);
		assertThat(uriComponents.getScheme()).isEqualTo(scheme);
		assertThat(uriComponents.getHost()).isEqualTo(host);
		assertThat(uriComponents.getPath()).isEqualTo(staticData.getItem().replace("{id}", String.valueOf(id)));
		assertThat(uriComponents.getQueryParams()).contains(entry("itemData",
				Arrays.asList("consumed,from,gold,image,into,maps")));
		assertThat(uriComponents.getQueryParams()).contains(entry("api_key", Arrays.asList(apiKey)));
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
		assertThat(championsUri.getQueryParams()).contains(entry("api_key", Arrays.asList(apiKey)));
	}

	@Autowired
	@Qualifier("champion")
	private UriComponentsBuilder championUri;

	@Test
	public void championUri() {
		int id = 1;
		UriComponents uriComponents = championUri.buildAndExpand(staticData.getRegion(), id);
		assertThat(uriComponents.getScheme()).isEqualTo(scheme);
		assertThat(uriComponents.getHost()).isEqualTo(host);
		assertThat(uriComponents.getPath()).isEqualTo(staticData.getChampion().replace("{id}", String.valueOf(id)));
		assertThat(uriComponents.getQueryParams()).contains(entry("champData", Arrays.asList("image,partype,tags")));
		assertThat(uriComponents.getQueryParams()).contains(entry("api_key", Arrays.asList(apiKey)));
	}

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	@Test
	public void versionsUri() {
		assertThat(versionsUri.getScheme()).isEqualTo(scheme);
		assertThat(versionsUri.getHost()).isEqualTo(host);
		assertThat(versionsUri.getPath()).isEqualTo(staticData.getVersions());
		assertThat(versionsUri.getQueryParams()).contains(entry("api_key", Arrays.asList(apiKey)));
	}

}
