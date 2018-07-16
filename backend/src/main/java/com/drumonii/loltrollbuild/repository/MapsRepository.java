package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.GameMap;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * JPA repository to the MAP table.
 */
@CacheConfig(cacheNames = "maps")
public interface MapsRepository extends JpaRepository<GameMap, Integer>, JpaSpecificationExecutor<GameMap> {

	/**
	 * Gets {@link List} of all {@link GameMap}s that are eligible for the troll build. Eligible maps: Twisted Treeline,
	 * Summoner's Rift, and Proving Grounds.
	 *
	 * @return only the eligible {@link List} of {@link GameMap}s
	 */
	@Query("select m from GameMap m where m.mapId in ('10', '11', '12') order by m.mapName")
	@Cacheable(key = "#root.methodName")
	List<GameMap> forTrollBuild();

	@Cacheable
	@Override
	List<GameMap> findAll();

	@CacheEvict(allEntries = true)
	@Override
	<S extends GameMap> List<S> saveAll(Iterable<S> entities);

	@CacheEvict(allEntries = true)
	@Override
	<S extends GameMap> S save(S entity);

	@Cacheable
	@Override
	Optional<GameMap> findById(Integer integer);

	@CacheEvict(allEntries = true)
	@Override
	void deleteById(Integer integer);

	@CacheEvict(allEntries = true)
	@Override
	void deleteAll(Iterable<? extends GameMap> entities);

	@CacheEvict(allEntries = true)
	@Override
	void deleteAll();

	@Cacheable(key = "{#spec.example.probe, #sort}")
	@Override
	List<GameMap> findAll(Specification<GameMap> example, Sort sort);

}
