package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.config.Profiles.StaticData;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class MapsStaticDataService implements MapsService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("maps")
	private UriComponents mapsUri;

	@Override
	public List<GameMap> getMaps(Version version) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(mapsUri.toUriString())
				.queryParam("version", version.getPatch());
		return getMaps(builder.toUriString());
	}

	@Override
	public List<GameMap> getMaps() {
		return getMaps(mapsUri.toString());
	}

	private List<GameMap> getMaps(String url) {
		try {
			return new ArrayList<>(restTemplate.getForObject(url, MapsResponse.class).getMaps().values());
		} catch (RestClientException e) {
			log.warn("Unable to retrieve Maps from lol-static-data-v3 due to:", e);
			return new ArrayList<>();
		}
	}

	@Override
	public GameMap getMap(int id) {
		MapsResponse response;
		try {
			response = restTemplate.getForObject(mapsUri.toString(), MapsResponse.class);
		} catch (RestClientException e) {
			log.warn("Unable to retrieve the Map with ID: {} from lol-static-data-v3 due to:", id, e);
			return null;
		}
		return response.getMaps().get(String.valueOf(id));
	}

}
