package com.drumonii.loltrollbuild.batch.items;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.riot.service.ItemsService;
import org.apache.commons.collections4.ListUtils;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * {@link ItemReader} for reading {@link Item}s from Riot's API.
 */
public class ItemsRetrievalItemReader implements ItemReader<Item> {

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private ItemsService itemsService;

	private final Version latestVersion;
	private List<Item> items;

	public ItemsRetrievalItemReader(Version latestVersion) {
		this.latestVersion = latestVersion;
	}

	@Override
	public Item read() {
		if (items == null) {
			getItems();
		}
		if (!items.isEmpty()) {
			return items.remove(0);
		}
		return null;
	}

	private void getItems() {
		items = itemsService.getItems(latestVersion);
		if (items.isEmpty()) {
			items = null;
			throw new ItemsRetrievalException();
		} else {
			List<Item> deletedItems = ListUtils.subtract(itemsRepository.findAll(), items);
			itemsRepository.deleteAll(deletedItems);
		}
	}

}
