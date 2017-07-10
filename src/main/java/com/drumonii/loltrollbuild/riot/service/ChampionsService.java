package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.Champion;

import java.util.List;

/**
 * {@code @Service} for {@link Champion}s.
 */
public interface ChampionsService {

	/**
	 * Returns the {@link List} of {@link Champion} from Riot.
	 *
	 * @return the {@link List} of {@link Champion}
	 */
	List<Champion> getChampions();

	/**
	 * Returns a {@link Champion} from Riot by its ID.
	 *
	 * @param id the ID to lookup the {@link Champion} from Riot
	 * @return the {@link Champion} from Riot
	 */
	Champion getChampion(int id);

}
