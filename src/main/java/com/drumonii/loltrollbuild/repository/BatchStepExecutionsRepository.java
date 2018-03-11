package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.BatchStepExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * JPA repository to the BATCH_STEP_EXECUTION table.
 */
public interface BatchStepExecutionsRepository extends JpaRepository<BatchStepExecution, Long> {

	@Query("select e from BatchStepExecution e where e.jobExecution.jobInstance.id = :jobInstanceId")
	List<BatchStepExecution> findByJobInstanceId(@Param("jobInstanceId") long jobInstanceId);

}
