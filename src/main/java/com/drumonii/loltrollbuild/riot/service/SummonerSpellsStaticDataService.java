package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.config.Profiles.StaticData;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@StaticData
public class SummonerSpellsStaticDataService implements SummonerSpellsService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("summonerSpells")
	private UriComponents summonerSpellsUri;

	@Autowired
	@Qualifier("summonerSpell")
	private UriComponentsBuilder summonerSpellUri;

	@Value("${riot.static-data.region}")
	private String region;

	@Override
	public List<SummonerSpell> getSummonerSpells(Version version) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(summonerSpellsUri.toUriString())
				.queryParam("version", version.getPatch());
		return getSummonerSpells(builder.toUriString());
	}

	@Override
	public List<SummonerSpell> getSummonerSpells() {
		return getSummonerSpells(summonerSpellsUri.toString());
	}

	private List<SummonerSpell> getSummonerSpells(String url) {
		try {
			return new ArrayList<>(restTemplate.getForObject(url, SummonerSpellsResponse.class)
					.getSummonerSpells().values());
		} catch (RestClientException e) {
			log.warn("Unable to retrieve Summoner Spells from lol-static-data-v3 due to:", e);
			return new ArrayList<>();
		}
	}

	@Override
	public SummonerSpell getSummonerSpell(int id) {
		UriComponents uriComponents = summonerSpellUri.buildAndExpand(region, id);
		SummonerSpell summonerSpell;
		try {
			summonerSpell = restTemplate.getForObject(uriComponents.toString(), SummonerSpell.class);
		} catch (RestClientException e) {
			log.warn("Unable to retrieve the Summoner Spells with ID: {} from lol-static-data-v3 due to:", id, e);
			return null;
		}
		return summonerSpell;
	}

}
