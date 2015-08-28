package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import org.springframework.data.repository.CrudRepository;

/**
 * CRUD operations repository to the SUMMONER_SPELL table.
 */
public interface SummonerSpellsRepository extends CrudRepository<SummonerSpell, Integer> {
}
