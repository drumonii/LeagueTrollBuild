package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.Champion;
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
	@Query("""
           select c
           from Champion c
           where lower(c.name) = lower(:name)
           or lower(c.key) = lower(:name)
           """)
	@Cacheable(unless = "#result == null")
	Optional<Champion> findByName(@Param("name") String name);

	/**
	 * Gets all possible distinct tags a {@link Champion} could have.
	 *
	 * @return the {@link List} of string tags
	 */
	@Query(nativeQuery = true, value =
			"""
            select distinct c.tag
            from champion_tag c
            order by c.tag
			""")
	@Cacheable(key = "#root.methodName", unless = "#result.isEmpty()")
	List<String> getTags();

	@Cacheable(unless = "#result.isEmpty()")
	@Override
	List<Champion> findAll();

	@Cacheable(unless = "#result.isEmpty()")
	@Override
	List<Champion> findAll(Sort sort);

	@CacheEvict(allEntries = true)
	@Override
	<S extends Champion> List<S> saveAll(Iterable<S> entities);

	@CacheEvict(allEntries = true)
	@Override
	<S extends Champion> S save(S entity);

	@Cacheable(unless = "#result == null")
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

	@Cacheable(key = "{#spec.example.probe, #sort}", unless = "#result.isEmpty()")
	@Override
	List<Champion> findAll(Specification<Champion> spec, Sort sort);

}
