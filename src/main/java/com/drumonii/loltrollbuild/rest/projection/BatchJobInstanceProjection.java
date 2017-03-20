package com.drumonii.loltrollbuild.rest.projection;

import com.drumonii.loltrollbuild.model.BatchJobExecution;
import com.drumonii.loltrollbuild.model.BatchJobInstance;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.rest.core.config.Projection;

/**
 * Projection of the {@link BatchJobInstance} to include its {@link BatchJobExecution}.
 */
@Projection(name = "batchJobInstanceProjection", types = { BatchJobInstance.class })
public interface BatchJobInstanceProjection {

	@JsonIgnore
	long getId();

	String getName();

	BatchJobExecutionProjection getJobExecution();

}
