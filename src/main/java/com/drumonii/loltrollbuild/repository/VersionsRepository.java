package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.Version;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * JPA repository to the VERSION table.
 */
@CacheConfig(cacheNames = "versions")
public interface VersionsRepository extends JpaRepository<Version, String> {

	/**
	 * Gets the latest {@link Version}. It is fault safe if there are more than one patch versions existing in the
	 * database.
	 *
	 * @return gets the latest patch version string
	 */
	@Query(value =
			"select v.patch, v.major, v.minor, v.revision from Version v " +
			"order by v.major desc, v.minor desc, v.revision desc limit 1",
			nativeQuery = true)
	@Cacheable
	Version latestVersion();

	@Cacheable
	@Override
	List<Version> findAll();

	@Cacheable
	@Override
	List<Version> findAll(Sort sort);

	@CacheEvict(allEntries = true)
	@Override
	<S extends Version> List<S> saveAll(Iterable<S> entities);

	@CacheEvict(allEntries = true)
	@Override
	<S extends Version> S save(S entity);

	@CacheEvict(allEntries = true)
	@Override
	void delete(Version entity);

	@CacheEvict(allEntries = true)
	@Override
	void deleteAll(Iterable<? extends Version> entities);

	@CacheEvict(allEntries = true)
	@Override
	void deleteAll();

}
