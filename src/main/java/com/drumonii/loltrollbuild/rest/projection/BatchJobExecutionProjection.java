package com.drumonii.loltrollbuild.rest.projection;

import com.drumonii.loltrollbuild.model.BatchJobExecution;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDateTime;

/**
 * Projection of the {@link BatchJobExecution} for use in the {@link BatchJobInstanceProjection}.
 */
@Projection(name = "batchJobExecutionProjection", types = { BatchJobExecution.class })
public interface BatchJobExecutionProjection {

	@JsonIgnore
	long getId();

	Long getVersion();

	LocalDateTime getCreateTime();

	LocalDateTime getStartTime();

	LocalDateTime getEndTime();

	String getStatus();

	String getExitCode();

	String getExitMessage();

	LocalDateTime getLastUpdated();

	String getConfigurationLocation();

}
