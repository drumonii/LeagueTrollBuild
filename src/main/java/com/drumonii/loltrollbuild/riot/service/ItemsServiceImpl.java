package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ItemsServiceImpl implements ItemsService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("items")
	private UriComponents itemsUri;

	@Autowired
	@Qualifier("item")
	private UriComponentsBuilder itemUri;

	@Value("${riot.api.static-data.region}")
	private String region;

	@Override
	public List<Item> getItems() {
		ItemsResponse response;
		try {
			response = restTemplate.getForObject(itemsUri.toString(), ItemsResponse.class);
		} catch (RestClientException e) {
			log.warn("Unable to retrieve Items from Riot's API due to:", e);
			return new ArrayList<>();
		}
		return new ArrayList<>(response.getItems().values());
	}

	@Override
	public Item getItem(int id) {
		UriComponents uriComponents = itemUri.buildAndExpand(region, id);
		Item item;
		try {
			item = restTemplate.getForObject(uriComponents.toString(), Item.class);
		} catch (RestClientException e) {
			log.warn("Unable to retrieve the Item with ID: {} from Riot's API due to:", id, e);
			return null;
		}
		return item;
	}

}
