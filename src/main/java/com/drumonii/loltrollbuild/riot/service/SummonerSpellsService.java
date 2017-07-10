package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.SummonerSpell;

import java.util.List;

/**
 * {@code @Service} for {@link SummonerSpell}s.
 */
public interface SummonerSpellsService {

	/**
	 * Returns the {@link List} of {@link SummonerSpell} from Riot.
	 *
	 * @return the {@link List} of {@link SummonerSpell} from Riot
	 */
	List<SummonerSpell> getSummonerSpells();

	/**
	 * Returns a {@link SummonerSpell} from Riot by its ID.
	 *
	 * @param id the ID to lookup the {@link SummonerSpell} from Riot
	 * @return the {@link SummonerSpell} from Riot
	 */
	SummonerSpell getSummonerSpell(int id);

}
