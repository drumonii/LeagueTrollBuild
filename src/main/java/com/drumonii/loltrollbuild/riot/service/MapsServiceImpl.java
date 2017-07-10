package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MapsServiceImpl implements MapsService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("maps")
	private UriComponents mapsUri;

	@Override
	public List<GameMap> getMaps() {
		MapsResponse response;
		try {
			response = restTemplate.getForObject(mapsUri.toString(), MapsResponse.class);
		} catch (RestClientException e) {
			log.warn("Unable to retrieve Maps from Riot's API due to:", e);
			return new ArrayList<>();
		}
		return new ArrayList<>(response.getMaps().values());
	}

	@Override
	public GameMap getMap(int id) {
		MapsResponse response;
		try {
			response = restTemplate.getForObject(mapsUri.toString(), MapsResponse.class);
		} catch (RestClientException e) {
			log.warn("Unable to retrieve the Map with ID: {} from Riot's API due to:", id, e);
			return null;
		}
		return response.getMaps().get(String.valueOf(id));
	}

}
