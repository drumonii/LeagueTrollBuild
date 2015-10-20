package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Paging, sorting, and CRUD operations repository to the SUMMONER_SPELL table.
 */
@RepositoryRestResource(path = "summoner-spells")
public interface SummonerSpellsRepository extends PagingAndSortingRepository<SummonerSpell, Integer> {

	/**
	 * Gets a {@link List} of {@link SummonerSpell}s eligible for the troll build (all Summoner Spells used on
	 * Summoner's Rift).
	 *
	 * @return a {@link List} of {@link SummonerSpell}s eligible for the troll build
	 */
	@Query("select s from SummonerSpell s join s.modes m where m in ('CLASSIC')")
	List<SummonerSpell> forTrollBuild();

	@Override
	@RestResource(exported = false)
	SummonerSpell save(SummonerSpell entity);

	@Override
	@RestResource(exported = false)
	<S extends SummonerSpell> Iterable<S> save(Iterable<S> entities);

	@Override
	@RestResource(exported = false)
	void delete(Integer id);

	@Override
	@RestResource(exported = false)
	void delete(SummonerSpell entity);

	@Override
	@RestResource(exported = false)
	void delete(Iterable<? extends SummonerSpell> entities);

	@Override
	@RestResource(exported = false)
	void deleteAll();

}
