package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.config.Profiles.Ddragon;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
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
public class ItemsDdragonService implements ItemsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ItemsDdragonService.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("items")
	private UriComponentsBuilder itemsUri;

	@Autowired
	private VersionsService versionsService;

	@Value("${riot.ddragon.locale}")
	private String locale;

	@Override
	public List<Item> getItems(Version version) {
		ItemsResponse response;
		try {
			response = restTemplate.getForObject(itemsUri.buildAndExpand(version.getPatch(), locale).toString(),
					ItemsResponse.class);
		} catch (RestClientException e) {
			LOGGER.warn("Unable to retrieve Items from Data Dragon due to:", e);
			return new ArrayList<>();
		}
		return new ArrayList<>(response.getItems().values());
	}

	@Override
	public List<Item> getItems() {
		Version version = versionsService.getLatestVersion();
		if (version == null) {
			return new ArrayList<>();
		}
		return getItems(version);
	}

	@Override
	public Item getItem(int id) {
		Optional<Item> item = getItems().stream()
				.filter(i -> i.getId() == id)
				.findFirst();
		return item.orElse(null);
	}

}
