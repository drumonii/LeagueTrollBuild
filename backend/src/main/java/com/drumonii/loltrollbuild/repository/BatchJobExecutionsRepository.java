package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.BatchJobExecution;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository to the BATCH_JOB_EXECUTION table.
 */
public interface BatchJobExecutionsRepository extends JpaRepository<BatchJobExecution, Long>  {

}
