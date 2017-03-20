package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.BatchJobExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * JPA repository to the BATCH_JOB_EXECUTION table.
 */
@RepositoryRestResource(exported = false, collectionResourceRel = "jobExecutions")
public interface BatchJobExecutionsRepository extends JpaRepository<BatchJobExecution, Long>  {

}
