package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.Version;

import java.util.List;

/**
 * {@code @Service} for {@link SummonerSpell}s.
 */
public interface SummonerSpellsService {

	/**
	 * Returns the {@link List} of {@link SummonerSpell} from Riot using the specified patch {@link Version}.
	 *
	 * @param version the patch {@link Version} to use
	 * @return the {@link List} of {@link SummonerSpell}
	 */
	List<SummonerSpell> getSummonerSpells(Version version);

}
