package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.drumonii.loltrollbuild.riot.RiotApiProperties;
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
public class MapsDdragonService implements MapsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MapsDdragonService.class);

	private final RiotApiProperties.Ddragon ddragon;
	private final WebClient webClient;

	public MapsDdragonService(RiotApiProperties riotProperties, WebClient webClient) {
		this.ddragon = riotProperties.getDdragon();
		this.webClient = webClient;
	}

	@Override
	public List<GameMap> getMaps(Version version) {
		LOGGER.info("Getting Maps from Riot");
		return webClient.get()
				.uri(ddragon.getMaps(), version.getPatch(), ddragon.getLocale())
				.retrieve()
				.bodyToMono(MapsResponse.class)
				.onErrorResume(WebClientResponseException.class, e -> {
					LOGGER.warn("Unable to retrieve Maps from Data Dragon due to:", e);
					return Mono.just(new MapsResponse());
				})
				.map((Function<MapsResponse, List<GameMap>>) response ->
						new ArrayList<>(response.getMaps().values()))
				.block();
	}

}
