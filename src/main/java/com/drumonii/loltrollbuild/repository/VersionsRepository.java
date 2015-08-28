package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.Version;
import org.springframework.data.repository.CrudRepository;

/**
 * CRUD operations repository to the VERSION table.
 */
public interface VersionsRepository extends CrudRepository<Version, String> {
}
