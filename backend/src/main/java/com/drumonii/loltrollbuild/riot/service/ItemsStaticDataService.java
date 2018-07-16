package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.config.Profiles.StaticData;
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
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
@StaticData
public class ItemsStaticDataService implements ItemsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ItemsStaticDataService.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("items")
	private UriComponents itemsUri;

	@Autowired
	@Qualifier("item")
	private UriComponentsBuilder itemUri;

	@Value("${riot.static-data.region}")
	private String region;

	@Override
	public List<Item> getItems(Version version) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(itemsUri.toUriString())
				.queryParam("version", version.getPatch());
		return getItems(builder.toUriString());
	}

	@Override
	public List<Item> getItems() {
		return getItems(itemsUri.toString());
	}

	private List<Item> getItems(String url) {
		try {
			return new ArrayList<>(restTemplate.getForObject(url, ItemsResponse.class).getItems().values());
		} catch (RestClientException e) {
			LOGGER.warn("Unable to retrieve Items from lol-static-data-v3 due to:", e);
			return new ArrayList<>();
		}
	}

	@Override
	public Item getItem(int id) {
		UriComponents uriComponents = itemUri.buildAndExpand(region, id);
		Item item;
		try {
			item = restTemplate.getForObject(uriComponents.toString(), Item.class);
		} catch (RestClientException e) {
			LOGGER.warn("Unable to retrieve the Item with ID: {} from lol-static-data-v3 due to:", id, e);
			return null;
		}
		return item;
	}

}
