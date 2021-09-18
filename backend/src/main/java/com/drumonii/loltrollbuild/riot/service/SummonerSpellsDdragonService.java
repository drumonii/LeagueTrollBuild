package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SummonerSpellsDdragonService implements SummonerSpellsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SummonerSpellsDdragonService.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("summonerSpells")
	private UriComponentsBuilder summonerSpellsUri;

	@Autowired
	private VersionsService versionsService;

	@Override
	public List<SummonerSpell> getSummonerSpells(Version version) {
		LOGGER.info("Getting Summoner Spells from Riot");
		SummonerSpellsResponse response;
		try {
			response = restTemplate.getForObject(summonerSpellsUri.buildAndExpand(version.getPatch()).toString(),
					SummonerSpellsResponse.class);
		} catch (RestClientException e) {
			LOGGER.warn("Unable to retrieve Summoner Spells from Data Dragon due to:", e);
			return new ArrayList<>();
		}
		return new ArrayList<>(response.getSummonerSpells().values());
	}

	private List<SummonerSpell> getSummonerSpells() {
		Version version = versionsService.getLatestVersion();
		if (version == null) {
			return new ArrayList<>();
		}
		return getSummonerSpells(version);
	}

	@Override
	public SummonerSpell getSummonerSpell(int id) {
		LOGGER.info("Getting Summoner Spell with id: {} from Riot", id);
		Optional<SummonerSpell> summonerSpell = getSummonerSpells().stream()
				.filter(s -> s.getId() == id)
				.findFirst();
		return summonerSpell.orElse(null);
	}

}
