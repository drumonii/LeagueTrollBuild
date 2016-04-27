package com.drumonii.loltrollbuild.config;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties.Ddragon;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties.StaticData;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;

public class RiotApiRestConfigTest extends BaseSpringTestRunner {

	@Autowired
	private RiotApiProperties riotProperties;

	private StaticData staticData;
	private Ddragon ddragon;

	private String scheme;
	private String host;

	@Value("${riot.api.key}")
	private String apiKey;

	@Before
	public void before() {
		staticData = riotProperties.getApi().getStaticData();
		ddragon = riotProperties.getApi().getDdragon();
		scheme = staticData.getScheme();
		host = staticData.getBaseUrl().replace("{region}", staticData.getRegion());
	}

	/*
	 * Summoner Spells Uri Components Tests
	 */

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
	@Qualifier("summonerSpellsImg")
	private UriComponentsBuilder summonerSpellImgBuilder;

	@Test
	public void summonerSpellImgUri() {
		String imgFull = "SummonerBoost.png"; String patch = "some patch number";
		UriComponents summonerSpellImgUri = summonerSpellImgBuilder.buildAndExpand(patch, imgFull);
		assertThat(summonerSpellImgUri.getHost()).isEqualTo(ddragon.getBaseUrl());
		assertThat(summonerSpellImgUri.getPath()).isEqualTo(ddragon.getSummonerSpellsImg().replace("{version}", patch)
				.replace("{summonerSpellImgFull}", imgFull));
		assertThat(summonerSpellImgUri.getScheme()).isEqualTo(ddragon.getScheme());
	}

	/*
	 * Items Uri Components Tests
	 */

	@Autowired
	@Qualifier("items")
	private UriComponents itemsUri;

	@Test
	public void itemsUri() {
		assertThat(itemsUri.getScheme()).isEqualTo(scheme);
		assertThat(itemsUri.getHost()).isEqualTo(host);
		assertThat(itemsUri.getPath()).isEqualTo(staticData.getItems());
		assertThat(itemsUri.getQueryParams()).contains(entry("itemListData",
				Arrays.asList("consumed,from,gold,image,into,maps,requiredChampion")));
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
				Arrays.asList("consumed,from,gold,image,into,maps,requiredChampion")));
		assertThat(uriComponents.getQueryParams()).contains(entry("api_key", Arrays.asList(apiKey)));
	}

	@Autowired
	@Qualifier("itemsImg")
	private UriComponentsBuilder itemsImgBuilder;

	@Test
	public void itemsImgUri() {
		String imgFull = "1.png"; String patch = "some patch number";
		UriComponents itemsImgUri = itemsImgBuilder.buildAndExpand(patch, imgFull);
		assertThat(itemsImgUri.getHost()).isEqualTo(ddragon.getBaseUrl());
		assertThat(itemsImgUri.getPath()).isEqualTo(ddragon.getItemsImg().replace("{version}", patch)
				.replace("{itemImgFull}", imgFull));
		assertThat(itemsImgUri.getScheme()).isEqualTo(ddragon.getScheme());
	}

	/*
	 * Champions Uri Components Tests
	 */

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
	@Qualifier("championsImg")
	private UriComponentsBuilder championsImgBuilder;

	@Test
	public void championsImgUri() {
		String imgFull = "Champion.png"; String patch = "some patch number";
		UriComponents championsImgUri = championsImgBuilder.buildAndExpand(patch, imgFull);
		assertThat(championsImgUri.getHost()).isEqualTo(ddragon.getBaseUrl());
		assertThat(championsImgUri.getPath()).isEqualTo(ddragon.getChampionsImg().replace("{version}", patch)
				.replace("{championImgFull}", imgFull));
		assertThat(championsImgUri.getScheme()).isEqualTo(ddragon.getScheme());
	}

	/*
	 * Maps Uri Components Tests
	 */

	@Autowired
	@Qualifier("maps")
	private UriComponents mapsUri;

	@Test
	public void mapsUri() {
		assertThat(mapsUri.getScheme()).isEqualTo(scheme);
		assertThat(mapsUri.getHost()).isEqualTo(host);
		assertThat(mapsUri.getPath()).isEqualTo(staticData.getMaps());
		assertThat(mapsUri.getQueryParams()).contains(entry("api_key", Arrays.asList(apiKey)));
	}

	@Autowired
	@Qualifier("mapsImg")
	private UriComponentsBuilder mapsImgBuilder;

	@Test
	public void mapsImgUri() {
		String imgFull = "Map.png"; String patch = "some patch number";
		UriComponents mapsImgUri = mapsImgBuilder.buildAndExpand(patch, imgFull);
		assertThat(mapsImgUri.getHost()).isEqualTo(ddragon.getBaseUrl());
		assertThat(mapsImgUri.getPath()).isEqualTo(ddragon.getMapsImg().replace("{version}", patch)
				.replace("{mapImgFull}", imgFull));
		assertThat(mapsImgUri.getScheme()).isEqualTo(ddragon.getScheme());
	}

	/*
	 * Versions Uri Components Tests
	 */

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
