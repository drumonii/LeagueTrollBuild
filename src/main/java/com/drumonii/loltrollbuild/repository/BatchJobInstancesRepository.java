package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.BatchJobInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * JPA repository to the BATCH_JOB_INSTANCE table.
 */
@RepositoryRestResource(exported = false, collectionResourceRel = "jobInstances")
public interface BatchJobInstancesRepository extends JpaRepository<BatchJobInstance, Long> {

}
