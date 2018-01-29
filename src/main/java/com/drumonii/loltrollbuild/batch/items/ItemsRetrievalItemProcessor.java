package com.drumonii.loltrollbuild.batch.items;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.riot.api.ImageFetcher;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.util.UriComponentsBuilder;

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

	private final Version latestVersion;

	public ItemsRetrievalItemProcessor(Version latestVersion) {
		this.latestVersion = latestVersion;
	}

	@Override
	public Item process(Item item) {
		Item itemFromDb = itemsRepository.findOne(item.getId());
		if (itemFromDb != null && itemFromDb.equals(item)) {
			return null;
		}
		imageFetcher.setImgSrc(item.getImage(), itemsImgUri, latestVersion);
		return item;
	}

}
