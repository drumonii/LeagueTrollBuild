package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.Version;

import java.util.List;

/**
 * {@code @Service} for {@link Champion}s.
 */
public interface ChampionsService {

	/**
	 * Returns the {@link List} of {@link Champion} from Riot using the specified patch {@link Version}.
	 *
	 * @param version the patch {@link Version} to use
	 * @return the {@link List} of {@link Champion}
	 */
	List<Champion> getChampions(Version version);

}
