package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.config.Profiles.Ddragon;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Ddragon
public class MapsDdragonService implements MapsService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("maps")
	private UriComponentsBuilder mapsUri;

	@Autowired
	private VersionsService versionsService;

	@Value("${riot.ddragon.locale}")
	private String locale;

	@Override
	public List<GameMap> getMaps() {
		Version version = versionsService.getLatestVersion();
		if (version == null) {
			return new ArrayList<>();
		}
		MapsResponse response;
		try {
			response = restTemplate.getForObject(mapsUri.buildAndExpand(version.getPatch(), locale).toString(),
					MapsResponse.class);
		} catch (RestClientException e) {
			log.warn("Unable to retrieve Maps from Data Dragon due to:", e);
			return new ArrayList<>();
		}
		return new ArrayList<>(response.getMaps().values());
	}

	@Override
	public GameMap getMap(int id) {
		Optional<GameMap> map = getMaps().stream()
				.filter(m -> m.getMapId() == id)
				.findFirst();
		return map.orElse(null);
	}

}
