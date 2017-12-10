package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.SummonerSpell;
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
public class SummonerSpellsServiceImpl implements SummonerSpellsService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("summonerSpells")
	private UriComponents summonerSpellsUri;

	@Autowired
	@Qualifier("summonerSpell")
	private UriComponentsBuilder summonerSpellUri;

	@Value("${riot.api.static-data.region}")
	private String region;

	@Override
	public List<SummonerSpell> getSummonerSpells() {
		SummonerSpellsResponse response;
		try {
			response = restTemplate.getForObject(summonerSpellsUri.toString(), SummonerSpellsResponse.class);
		} catch (RestClientException e) {
			log.warn("Unable to retrieve Summoner Spells from Riot's API due to:", e);
			return new ArrayList<>();
		}
		return new ArrayList<>(response.getSummonerSpells().values());
	}

	@Override
	public SummonerSpell getSummonerSpell(int id) {
		UriComponents uriComponents = summonerSpellUri.buildAndExpand(region, id);
		SummonerSpell summonerSpell;
		try {
			summonerSpell = restTemplate.getForObject(uriComponents.toString(), SummonerSpell.class);
		} catch (RestClientException e) {
			log.warn("Unable to retrieve the Summoner Spells with ID: {} from Riot's API due to:", id, e);
			return null;
		}
		return summonerSpell;
	}

}