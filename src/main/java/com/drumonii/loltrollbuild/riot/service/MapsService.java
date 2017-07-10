package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.GameMap;

import java.util.List;

/**
 * {@code @Service} for {@link GameMap}s.
 */
public interface MapsService {

	/**
	 * Returns the {@link List} of {@link GameMap} from Riot.
	 *
	 * @return the {@link List} of {@link GameMap} from Riot
	 */
	List<GameMap> getMaps();

	/**
	 * Returns a {@link GameMap} from Riot by its ID.
	 *
	 * @param id the ID to lookup the {@link GameMap} from Riot
	 * @return the {@link GameMap} from Riot
	 */
	GameMap getMap(int id);

}
