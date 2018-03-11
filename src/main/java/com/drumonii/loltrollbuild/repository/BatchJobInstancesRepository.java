package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.BatchJobInstance;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository to the BATCH_JOB_INSTANCE table.
 */
public interface BatchJobInstancesRepository extends JpaRepository<BatchJobInstance, Long> {

}
