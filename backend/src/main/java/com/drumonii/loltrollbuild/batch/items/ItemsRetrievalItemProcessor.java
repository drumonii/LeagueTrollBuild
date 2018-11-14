package com.drumonii.loltrollbuild.batch.items;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.riot.ImageFetcher;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

/**
 * {@link ItemProcessor} for processing {@link Item}s from Riot's API.
 */
public class ItemsRetrievalItemProcessor implements ItemProcessor<Item, Item> {

	@Autowired
	@Qualifier("itemsImg")
	private UriComponentsBuilder itemsImgUri;

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private ImageFetcher imageFetcher;

	private final Version latestRiotPatch;

	public ItemsRetrievalItemProcessor(Version latestRiotPatch) {
		this.latestRiotPatch = latestRiotPatch;
	}

	@Override
	public Item process(Item item) {
		Optional<Item> itemFromDb = itemsRepository.findById(item.getId());
		if (itemFromDb.isPresent() && itemFromDb.get().equals(item)) {
			return null;
		}
		imageFetcher.setImgSrc(item.getImage(), itemsImgUri, latestRiotPatch);
		return item;
	}

}
