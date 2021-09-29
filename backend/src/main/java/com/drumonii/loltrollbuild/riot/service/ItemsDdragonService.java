package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
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
public class ItemsDdragonService implements ItemsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ItemsDdragonService.class);

	private final RiotApiProperties.Ddragon ddragon;
	private final WebClient webClient;

	public ItemsDdragonService(RiotApiProperties riotProperties, WebClient webClient) {
		this.ddragon = riotProperties.getDdragon();
		this.webClient = webClient;
	}

	@Override
	public List<Item> getItems(Version version) {
		LOGGER.info("Getting Items from Riot");
		return webClient.get()
				.uri(ddragon.getItems(), version.getPatch(), ddragon.getLocale())
				.retrieve()
				.bodyToMono(ItemsResponse.class)
				.onErrorResume(WebClientResponseException.class, e -> {
					LOGGER.warn("Unable to retrieve Items from Data Dragon due to:", e);
					return Mono.just(new ItemsResponse());
				})
				.map((Function<ItemsResponse, List<Item>>) response ->
						new ArrayList<>(response.getItems().values()))
				.block();
	}

}
