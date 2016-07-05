package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.GameMap;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Paging, sorting, and CRUD operations repository to the MAP table.
 */
@RepositoryRestResource(path = "maps", collectionResourceRel = "maps")
@CacheConfig(cacheNames = "maps")
public interface MapsRepository extends JpaRepository<GameMap, Integer> {

	@CacheEvict(allEntries = true)
	@Override
	void deleteAll();

}
