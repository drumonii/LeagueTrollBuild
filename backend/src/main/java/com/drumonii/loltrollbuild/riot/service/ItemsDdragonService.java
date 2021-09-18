package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
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
public class ItemsDdragonService implements ItemsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ItemsDdragonService.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("items")
	private UriComponentsBuilder itemsUri;

	@Autowired
	private VersionsService versionsService;

	@Override
	public List<Item> getItems(Version version) {
		LOGGER.info("Getting Items from Riot");
		ItemsResponse response;
		try {
			response = restTemplate.getForObject(itemsUri.buildAndExpand(version.getPatch()).toString(),
					ItemsResponse.class);
		} catch (RestClientException e) {
			LOGGER.warn("Unable to retrieve Items from Data Dragon due to:", e);
			return new ArrayList<>();
		}
		return new ArrayList<>(response.getItems().values());
	}

	private List<Item> getItems() {
		Version version = versionsService.getLatestVersion();
		if (version == null) {
			return new ArrayList<>();
		}
		return getItems(version);
	}

	@Override
	public Item getItem(int id) {
		LOGGER.info("Getting Item with id: {} from Riot", id);
		Optional<Item> item = getItems().stream()
				.filter(i -> i.getId() == id)
				.findFirst();
		return item.orElse(null);
	}

}
