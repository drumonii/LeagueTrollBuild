package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.Build;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * JPA repository to the CHAMPION table.
 */
@CacheConfig(cacheNames = "builds")
public interface BuildsRepository extends JpaRepository<Build, Integer> {

	@Cacheable(unless = "#result.isEmpty()")
	@Override
	List<Build> findAll();

	@CacheEvict(allEntries = true)
	@Override
	<S extends Build> List<S> saveAll(Iterable<S> entities);

	@CacheEvict(allEntries = true)
	@Override
	<S extends Build> S save(S entity);

	@Cacheable(unless = "#result == null")
	@Override
	Optional<Build> findById(Integer integer);

	@CacheEvict(allEntries = true)
	@Override
	void deleteById(Integer integer);

	@CacheEvict(allEntries = true)
	@Override
	void deleteAll(Iterable<? extends Build> entities);

	@CacheEvict(allEntries = true)
	@Override
	void deleteAll();

	@Cacheable(key = "{#example.probe, #pageable}", unless = "#result.isEmpty()")
	@Override
	<S extends Build> Page<S> findAll(Example<S> example, Pageable pageable);

}
