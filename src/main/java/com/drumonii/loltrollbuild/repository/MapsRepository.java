package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.GameMap;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * JPA repository to the MAP table.
 */
@RepositoryRestResource(path = "maps", collectionResourceRel = "maps")
@CacheConfig(cacheNames = "maps")
public interface MapsRepository extends JpaRepository<GameMap, Integer> {

	@Cacheable
	@Override
	List<GameMap> findAll();

	@CacheEvict(allEntries = true)
	@Override
	<S extends GameMap> List<S> save(Iterable<S> entities);

	@CacheEvict(allEntries = true)
	@Override
	<S extends GameMap> S save(S entity);

	@Cacheable
	@Override
	GameMap findOne(Integer integer);

	@CacheEvict(allEntries = true)
	@Override
	void delete(Integer integer);

	@CacheEvict(allEntries = true)
	@Override
	void delete(Iterable<? extends GameMap> entities);

	@CacheEvict(allEntries = true)
	@Override
	void deleteAll();

	@Cacheable(key = "{#example.probe, #pageable}")
	@Override
	<S extends GameMap> Page<S> findAll(Example<S> example, Pageable pageable);

}
