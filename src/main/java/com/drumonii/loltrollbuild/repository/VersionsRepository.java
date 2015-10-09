package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.Version;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * CRUD operations repository to the VERSION table.
 */
public interface VersionsRepository extends CrudRepository<Version, String> {

	/**
	 * Gets the latest patch version. It is fault safe if there are more than one patch versions existing in the
	 * database.
	 *
	 * @return gets the latest patch version string
	 */
	@Query(value = "select v.patch from version v limit 1", nativeQuery = true)
	@RestResource(path = "latest-patch", rel = "latest-patch")
	String latestPatch();

	@Override
	@RestResource(exported = false)
	Version save(Version entity);

	@Override
	@RestResource(exported = false)
	<S extends Version> Iterable<S> save(Iterable<S> entities);

	@Override
	@RestResource(exported = false)
	void delete(String id);

	@Override
	@RestResource(exported = false)
	void delete(Version entity);

	@Override
	@RestResource(exported = false)
	void delete(Iterable<? extends Version> entities);

	@Override
	@RestResource(exported = false)
	void deleteAll();

}
