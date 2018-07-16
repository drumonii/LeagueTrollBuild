package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.config.Profiles.StaticData;
import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@StaticData
public class ChampionsStaticDataService implements ChampionsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ChampionsStaticDataService.class);

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
	public List<Champion> getChampions(Version version) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(championsUri.toUriString())
				.queryParam("version", version.getPatch());
		return getChampions(builder.toUriString());
	}

	@Override
	public List<Champion> getChampions() {
		return getChampions(championsUri.toString());
	}

	private List<Champion> getChampions(String url) {
		try {
			return new ArrayList<>(restTemplate.getForObject(url, ChampionsResponse.class).getChampions().values());
		} catch (RestClientException e) {
			LOGGER.warn("Unable to retrieve Champions from lol-static-data-v3 due to:", e);
			return new ArrayList<>();
		}
	}

	@Override
	public Champion getChampion(int id) {
		UriComponents uriComponents = championUri.buildAndExpand(region, id);
		Champion champion;
		try {
			champion = restTemplate.getForObject(uriComponents.toString(), Champion.class);
		} catch (RestClientException e) {
			LOGGER.warn("Unable to retrieve the Champion with ID: {} from lol-static-data-v3 due to:", id, e);
			return null;
		}
		return champion;
	}

}
