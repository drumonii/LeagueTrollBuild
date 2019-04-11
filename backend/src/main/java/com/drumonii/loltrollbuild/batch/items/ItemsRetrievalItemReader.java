package com.drumonii.loltrollbuild.batch.items;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
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
	private ItemsRepository itemsRepository;

	private List<Item> items;

	public ItemsRetrievalItemReader(List<Item> items) {
		this.items = items;
	}

	@Override
	public Item read() {
		if (!items.isEmpty()) {
			return items.remove(0);
		}
		return null;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		if (!items.isEmpty()) {
			List<Item> deletedItems = ListUtils.subtract(itemsRepository.findAll(), items);
			itemsRepository.deleteAll(deletedItems);
		}
	}

}
