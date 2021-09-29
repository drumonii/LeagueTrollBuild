package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.RiotApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
public class VersionsDdragonService implements VersionsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(VersionsDdragonService.class);

	private final RiotApiProperties.Ddragon ddragon;
	private final WebClient webClient;

	public VersionsDdragonService(RiotApiProperties riotProperties, WebClient webClient) {
		this.ddragon = riotProperties.getDdragon();
		this.webClient = webClient;
	}

	@Override
	public List<Version> getVersions() {
		LOGGER.info("Getting Versions from Riot");
		return webClient.get()
				.uri(ddragon.getVersions())
				.retrieve()
				.bodyToFlux(Version.class)
				.filter(v -> v.getRevision() != 0) // filter lolpatch_7.17 style which has a 0 revision
				.sort(Collections.reverseOrder())
				.collectList()
				.onErrorResume(WebClientResponseException.class, e -> {
					LOGGER.warn("Unable to retrieve Versions from Data Dragon due to:", e);
					return Mono.just(Collections.emptyList());
				})
				.block();
	}

}
