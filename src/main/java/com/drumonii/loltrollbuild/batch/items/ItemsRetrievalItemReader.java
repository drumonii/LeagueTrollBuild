package com.drumonii.loltrollbuild.batch.items;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.riot.service.ItemsService;
import org.apache.commons.collections4.ListUtils;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * {@link ItemReader} for reading {@link Item}s from Riot's API.
 */
public class ItemsRetrievalItemReader extends AbstractItemStreamItemReader<Item> {

	@Autowired
	private ItemsService itemsService;

	@Autowired
	private ItemsRepository itemsRepository;

	private List<Item> items;
	private int nextItem;

	@Override
	public Item read() {
		if (nextItem < items.size()) {
			return items.get(nextItem++);
		}
		return null;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		items = itemsService.getItems();
		List<Item> deletedItems = ListUtils.subtract(itemsRepository.findAll(), items);
		itemsRepository.deleteAll(deletedItems);
	}

}
