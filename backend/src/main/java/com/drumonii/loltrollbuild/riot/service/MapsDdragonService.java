package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.config.Profiles.Ddragon;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Ddragon
public class MapsDdragonService implements MapsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MapsDdragonService.class);

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
	public List<GameMap> getMaps(Version version) {
		LOGGER.info("Getting Maps from Riot");
		MapsResponse response;
		try {
			response = restTemplate.getForObject(mapsUri.buildAndExpand(version.getPatch(), locale).toString(),
					MapsResponse.class);
		} catch (RestClientException e) {
			LOGGER.warn("Unable to retrieve Maps from Data Dragon due to:", e);
			return new ArrayList<>();
		}
		return new ArrayList<>(response.getMaps().values());
	}

	private List<GameMap> getMaps() {
		Version version = versionsService.getLatestVersion();
		if (version == null) {
			return new ArrayList<>();
		}
		return getMaps(version);
	}

	@Override
	public GameMap getMap(int id) {
		LOGGER.info("Getting Map with id: {} from Riot", id);
		Optional<GameMap> map = getMaps().stream()
				.filter(m -> m.getMapId() == id)
				.findFirst();
		return map.orElse(null);
	}

}
