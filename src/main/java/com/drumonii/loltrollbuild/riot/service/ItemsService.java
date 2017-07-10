package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.Item;

import java.util.List;

/**
 * Created by Drummond on 7/8/2017.
 */
public interface ItemsService {

	/**
	 *
	 * @return
	 */
	List<Item> getItems();

	/**
	 *
	 * @return
	 */
	Item getItem(int id);

}
