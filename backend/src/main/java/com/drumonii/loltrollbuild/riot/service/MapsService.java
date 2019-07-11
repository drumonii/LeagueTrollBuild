package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Version;

import java.util.List;

/**
 * {@code @Service} for {@link GameMap}s.
 */
public interface MapsService {

	/**
	 * Returns the {@link List} of {@link GameMap} from Riot using the specified patch {@link Version}.
	 *
	 * @param version the patch {@link Version} to use
	 * @return the {@link List} of {@link GameMap}
	 */
	List<GameMap> getMaps(Version version);

	/**
	 * Returns a {@link GameMap} from Riot by its ID.
	 *
	 * @param id the ID to lookup the {@link GameMap} from Riot
	 * @return the {@link GameMap} from Riot
	 */
	GameMap getMap(int id);

}
