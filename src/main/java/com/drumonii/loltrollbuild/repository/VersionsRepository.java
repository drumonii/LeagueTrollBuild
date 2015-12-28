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
	 * Gets the latest {@link Version}. It is fault safe if there are more than one patch versions existing in the
	 * database.
	 *
	 * @return gets the latest patch version string
	 */
	@Query(value = "select v.patch from version v limit 1", nativeQuery = true)
	@RestResource(path = "latest-version", rel = "latest-version")
	Version latestVersion();

}
