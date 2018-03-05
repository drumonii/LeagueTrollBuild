package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.Champion;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

/**
 * JPA repository to the CHAMPION table.
 */
@CacheConfig(cacheNames = "champions")
public interface ChampionsRepository extends JpaRepository<Champion, Integer>, JpaSpecificationExecutor<Champion> {

	/**
	 * Finds a {@link Champion} by its name.
	 *
	 * @param name the name to lookup (non case sensitive)
	 * @return a {@link Champion} from its name
	 */
	@Query("select c from Champion c where lower(c.name) = lower(:name) or lower(c.key) = lower(:name)")
	@RestResource(exported = false)
	@Cacheable
	Optional<Champion> findByName(@Param("name") String name);

	/**
	 * Gets all possible distinct tags a {@link Champion} could have.
	 *
	 * @return the {@link List} of string tags
	 */
	@Query(value = "select distinct c.tag from champion_tag c", nativeQuery = true)
	@RestResource(exported = false)
	@Cacheable(key = "#root.methodName")
	List<String> getTags();

	@Cacheable
	@Override
	List<Champion> findAll();

	@Cacheable
	@Override
	List<Champion> findAll(Sort sort);

	@CacheEvict(allEntries = true)
	@Override
	<S extends Champion> List<S> saveAll(Iterable<S> entities);

	@CacheEvict(allEntries = true)
	@Override
	<S extends Champion> S save(S entity);

	@Cacheable
	@Override
	Optional<Champion> findById(Integer integer);

	@CacheEvict(allEntries = true)
	@Override
	void deleteById(Integer integer);

	@CacheEvict(allEntries = true)
	@Override
	void deleteAll(Iterable<? extends Champion> entities);

	@CacheEvict(allEntries = true)
	@Override
	void deleteAll();

	@Cacheable(key = "{#spec.example.probe, #pageable}")
	@Override
	Page<Champion> findAll(Specification<Champion> spec, Pageable pageable);

}
