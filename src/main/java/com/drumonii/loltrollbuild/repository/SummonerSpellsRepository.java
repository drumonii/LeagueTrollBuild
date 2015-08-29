package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * CRUD operations repository to the SUMMONER_SPELL table.
 */
@RepositoryRestResource(path = "summoner-spells")
public interface SummonerSpellsRepository extends CrudRepository<SummonerSpell, Integer> {
}
