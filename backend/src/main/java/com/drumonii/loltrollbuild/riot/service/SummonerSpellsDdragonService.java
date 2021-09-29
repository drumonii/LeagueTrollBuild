package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.RiotApiProperties;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
public class SummonerSpellsDdragonService implements SummonerSpellsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SummonerSpellsDdragonService.class);

	private final RiotApiProperties.Ddragon ddragon;
	private final WebClient webClient;

	public SummonerSpellsDdragonService(RiotApiProperties riotProperties, WebClient webClient) {
		this.ddragon = riotProperties.getDdragon();
		this.webClient = webClient;
	}

	@Override
	public List<SummonerSpell> getSummonerSpells(Version version) {
		LOGGER.info("Getting Summoner Spells from Riot");
		return webClient.get()
				.uri(ddragon.getSummonerSpells(), version.getPatch(), ddragon.getLocale())
				.retrieve()
				.bodyToMono(SummonerSpellsResponse.class)
				.onErrorResume(WebClientResponseException.class, e -> {
					LOGGER.warn("Unable to retrieve Summoner Spells from Data Dragon due to:", e);
					return Mono.just(new SummonerSpellsResponse());
				})
				.map((Function<SummonerSpellsResponse, List<SummonerSpell>>) response ->
						new ArrayList<>(response.getSummonerSpells().values()))
				.block();
	}

}
