package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.config.Profiles.Ddragon;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Ddragon
public class SummonerSpellsDdragonService implements SummonerSpellsService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("summonerSpells")
	private UriComponentsBuilder summonerSpellsUri;

	@Autowired
	private VersionsService versionsService;

	@Value("${riot.ddragon.locale}")
	private String locale;

	@Override
	public List<SummonerSpell> getSummonerSpells() {
		Version version = versionsService.getLatestVersion();
		if (version == null) {
			return new ArrayList<>();
		}
		SummonerSpellsResponse response;
		try {
			response = restTemplate.getForObject(summonerSpellsUri.buildAndExpand(version.getPatch(), locale).toString(),
					SummonerSpellsResponse.class);
		} catch (RestClientException e) {
			log.warn("Unable to retrieve Summoner Spells from Data Dragon due to:", e);
			return new ArrayList<>();
		}
		return new ArrayList<>(response.getSummonerSpells().values());
	}

	@Override
	public SummonerSpell getSummonerSpell(int id) {
		Optional<SummonerSpell> summonerSpell = getSummonerSpells().stream()
				.filter(s -> s.getId() == id)
				.findFirst();
		return summonerSpell.orElse(null);
	}

}
