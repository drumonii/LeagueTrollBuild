package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.config.Profiles.StaticData;
import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
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
public class ChampionsStaticDataService implements ChampionsService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("champions")
	private UriComponents championsUri;

	@Autowired
	@Qualifier("champion")
	private UriComponentsBuilder championUri;

	@Value("${riot.static-data.region}")
	private String region;

	@Override
	public List<Champion> getChampions() {
		ChampionsResponse response;
		try {
			response = restTemplate.getForObject(championsUri.toString(), ChampionsResponse.class);
		} catch (RestClientException e) {
			log.warn("Unable to retrieve Champions from lol-static-data-v3 due to:", e);
			return new ArrayList<>();
		}
		return new ArrayList<>(response.getChampions().values());
	}

	@Override
	public Champion getChampion(int id) {
		UriComponents uriComponents = championUri.buildAndExpand(region, id);
		Champion champion;
		try {
			champion = restTemplate.getForObject(uriComponents.toString(), Champion.class);
		} catch (RestClientException e) {
			log.warn("Unable to retrieve the Champion with ID: {} from lol-static-data-v3 due to:", id, e);
			return null;
		}
		return champion;
	}

}
