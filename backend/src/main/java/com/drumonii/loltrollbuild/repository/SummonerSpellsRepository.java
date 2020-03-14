package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.SummonerSpell.GameMode;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * JPA repository to the SUMMONER_SPELL table.
 */
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
		   "where m in (:mode)")
	@Cacheable(key = "{#root.methodName, #mode}", unless = "#result.isEmpty()")
	List<SummonerSpell> forTrollBuild(@Param("mode") GameMode mode);

	@Cacheable(unless = "#result.isEmpty()")
	@Override
	List<SummonerSpell> findAll();

	@CacheEvict(allEntries = true)
	@Override
	<S extends SummonerSpell> List<S> saveAll(Iterable<S> entities);

	@CacheEvict(allEntries = true)
	@Override
	<S extends SummonerSpell> S save(S entity);

	@Cacheable(unless = "#result == null")
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

	@Cacheable(key = "{#spec.example.probe, #sort}", unless = "#result.isEmpty()")
	@Override
	List<SummonerSpell> findAll(Specification<SummonerSpell> spec, Sort sort);

}
