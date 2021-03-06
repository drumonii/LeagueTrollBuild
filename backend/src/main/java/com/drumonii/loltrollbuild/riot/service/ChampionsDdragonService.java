package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.config.Profiles.Ddragon;
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
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Ddragon
public class ChampionsDdragonService implements ChampionsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ChampionsDdragonService.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("champions")
	private UriComponentsBuilder championsUri;

	@Autowired
	@Qualifier("champion")
	private UriComponentsBuilder championUri;

	@Autowired
	private VersionsService versionsService;

	@Value("${riot.ddragon.locale}")
	private String locale;

	@Override
	public List<Champion> getChampions(Version version) {
		LOGGER.info("Getting Champions from Riot");
		ChampionsResponse response = getChampionsResponse(version);
		if (response == null) {
			return new ArrayList<>();
		}
		return new ArrayList<>(response.getChampions().entrySet().stream()
				.collect(Collectors.toMap(Entry::getKey, entry -> getChampion(version, entry.getKey()))).values());
	}

	private ChampionsResponse getChampionsResponse(Version version) {
		try {
			return restTemplate.getForObject(championsUri.buildAndExpand(version.getPatch(), locale).toString(),
					ChampionsResponse.class);
		} catch (RestClientException e) {
			LOGGER.warn("Unable to retrieve Champions from Data Dragon due to:", e);
			return null;
		}
	}

	@Override
	public Champion getChampion(int id) {
		LOGGER.info("Getting Champion with id: {} from Riot", id);
		Version version = versionsService.getLatestVersion();
		if (version == null) {
			return null;
		}
		ChampionsResponse response = getChampionsResponse(version);
		if (response == null) {
			return null;
		}
		Optional<Champion> optionalChampion = response.getChampions().values().stream()
				.filter(c -> c.getId() == id)
				.findFirst();
		return optionalChampion.map(champion -> getChampion(version, champion.getKey())).orElse(null);
	}

	private Champion getChampion(Version version, String key) {
		LOGGER.info("Getting Champion {} from Riot", key);
		UriComponents uriComponents = championUri.buildAndExpand(version.getPatch(), locale, key);
		try {
			ChampionsResponse response = restTemplate.getForObject(uriComponents.toString(), ChampionsResponse.class);
			return response.getChampions().get(key);
		} catch (RestClientException e) {
			LOGGER.warn("Unable to retrieve the Champion {} from Data Dragon due to:", key, e);
			return null;
		}
	}

}
