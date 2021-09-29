package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.riot.RiotApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

@Service
public class ChampionsDdragonService implements ChampionsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ChampionsDdragonService.class);

	private final RiotApiProperties.Ddragon ddragon;
	private final WebClient webClient;

	public ChampionsDdragonService(RiotApiProperties riotProperties, WebClient webClient) {
		this.ddragon = riotProperties.getDdragon();
		this.webClient = webClient;
	}

	@Override
	public List<Champion> getChampions(Version version) {
		LOGGER.info("Getting Champions from Riot");
		return webClient.get()
				.uri(ddragon.getChampions(), version.getPatch(), ddragon.getLocale())
				.retrieve()
				.bodyToFlux(ChampionsResponse.class)
				.onErrorResume(WebClientResponseException.class, e -> {
					LOGGER.warn("Unable to retrieve Champions from Data Dragon due to:", e);
					return Mono.just(new ChampionsResponse());
				})
				.flatMapIterable((Function<ChampionsResponse, Iterable<Champion>>) response ->
						response.getChampions().values())
				.flatMap((Function<Champion, Mono<Champion>>) champion ->
						getChampion(version, champion.getKey()))
				.collectList()
				.block();
	}

	private Mono<Champion> getChampion(Version version, String key) {
		LOGGER.info("Getting Champion {} from Riot", key);
		return webClient.get()
				.uri(ddragon.getChampion(), version.getPatch(), ddragon.getLocale(), key)
				.retrieve()
				.bodyToMono(ChampionsResponse.class)
				.onErrorResume(WebClientResponseException.class, e -> {
					LOGGER.warn("Unable to retrieve the Champion {} from Data Dragon due to:", key, e);
					return Mono.just(new ChampionsResponse());
				})
				.map(response -> response.getChampions().get(key));
	}

}
