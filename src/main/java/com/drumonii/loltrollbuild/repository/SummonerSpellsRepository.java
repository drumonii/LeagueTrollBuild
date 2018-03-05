package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.SummonerSpell.GameMode;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

/**
 * JPA repository to the SUMMONER_SPELL table.
 */
@RepositoryRestResource(path = "summoner-spells")
@CacheConfig(cacheNames = "summonerSpells")
public interface SummonerSpellsRepository extends JpaRepository<SummonerSpell, Integer>, JpaSpecificationExecutor<SummonerSpell> {

	/**
	 * Gets a {@link List} of {@link SummonerSpell}s eligible for the troll build based on a game mode.
	 *
	 * @param mode the game mode to search
	 * @return a {@link List} of {@link SummonerSpell}s eligible for the troll build
	 * @see <a href="http://leagueoflegends.wikia.com/wiki/Category:Game_modes">Game Modes</a>
	 */
	@Query("select s from SummonerSpell s join s.modes m " +
		   "where m in (:mode) " +
	       "group by s.id")
	@RestResource(exported = false)
	@Cacheable(key = "{#root.methodName, #mode}")
	List<SummonerSpell> forTrollBuild(@Param("mode") GameMode mode);

	@Cacheable
	@Override
	List<SummonerSpell> findAll();

	@CacheEvict(allEntries = true)
	@Override
	<S extends SummonerSpell> List<S> saveAll(Iterable<S> entities);

	@CacheEvict(allEntries = true)
	@Override
	<S extends SummonerSpell> S save(S entity);

	@Cacheable
	@Override
	Optional<SummonerSpell> findById(Integer integer);

	@CacheEvict(allEntries = true)
	@Override
	void deleteById(Integer integer);

	@CacheEvict(allEntries = true)
	@Override
	void deleteAll(Iterable<? extends SummonerSpell> entities);

	@CacheEvict(allEntries = true)
	@Override
	void deleteAll();

	@Cacheable(key = "{#spec.example.probe, #pageable}")
	@Override
	Page<SummonerSpell> findAll(Specification<SummonerSpell> spec, Pageable pageable);

}
