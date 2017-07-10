package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.GameMap;

import java.util.List;

/**
 * Created by Drummond on 7/8/2017.
 */
public interface MapsService {

	/**
	 *
	 * @return
	 */
	List<GameMap> getMaps();

	/**
	 *
	 * @return
	 */
	GameMap getMap(int id);

}
