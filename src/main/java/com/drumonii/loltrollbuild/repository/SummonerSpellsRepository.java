package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * CRUD operations repository to the SUMMONER_SPELL table.
 */
@RepositoryRestResource(path = "summoner-spells")
public interface SummonerSpellsRepository extends CrudRepository<SummonerSpell, Integer> {

	/**
	 * Gets a {@link List} of {@link SummonerSpell}s eligible for the troll build (all Summoner Spells used on
	 * Summoner's Rift).
	 *
	 * @return a {@link List} of {@link SummonerSpell}s eligible for the troll build
	 */
	@Query("select s from SummonerSpell s join s.modes m where m in ('CLASSIC')")
	List<SummonerSpell> forTrollBuild();

}
