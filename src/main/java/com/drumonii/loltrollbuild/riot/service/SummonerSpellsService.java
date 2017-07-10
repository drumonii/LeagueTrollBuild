package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.SummonerSpell;

import java.util.List;

/**
 * Created by Drummond on 7/8/2017.
 */
public interface SummonerSpellsService {

	/**
	 *
	 * @return
	 */
	List<SummonerSpell> getSummonerSpells();

	/**
	 *
	 * @param id
	 * @return
	 */
	SummonerSpell getSummonerSpell(int id);

}
