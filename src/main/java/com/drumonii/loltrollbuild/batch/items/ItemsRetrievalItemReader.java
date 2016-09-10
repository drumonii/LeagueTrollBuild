package com.drumonii.loltrollbuild.batch.items;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import org.apache.commons.collections4.ListUtils;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ItemReader} for reading {@link Item}s from Riot's API.
 */
public class ItemsRetrievalItemReader implements ItemReader<Item> {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("items")
	private UriComponents itemsUri;

	@Autowired
	private ItemsRepository itemsRepository;

	private List<Item> items;
	private int nextItem;

	@Override
	public Item read() throws Exception {
		if (isNotInitialized()) {
			items = new ArrayList<>(restTemplate.getForObject(itemsUri.toString(), ItemsResponse.class).getItems()
					.values());
			List<Item> deletedItems = ListUtils.subtract(itemsRepository.findAll(), items);
			itemsRepository.delete(deletedItems);
		}

		Item item = null;
		if (nextItem < items.size()) {
			item = items.get(nextItem);
			nextItem++;
		}

		return item;
	}

	private boolean isNotInitialized() {
		return items == null;
	}

}
