package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.Item;

import java.util.List;

/**
 * {@code @Service} for {@link Item}s.
 */
public interface ItemsService {

	/**
	 * Returns the {@link List} of {@link Item} from Riot.
	 *
	 * @return the {@link List} of {@link Item} from Riot
	 */
	List<Item> getItems();

	/**
	 * Returns a {@link Item} from Riot by its ID.
	 *
	 * @param id the ID to lookup the {@link Item} from Riot
	 * @return the {@link Item} from Riot
	 */
	Item getItem(int id);

}
