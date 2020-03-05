package com.drumonii.loltrollbuild.api.admin;

import com.drumonii.loltrollbuild.api.status.ResourceNotFoundException;
import com.drumonii.loltrollbuild.model.BatchJobInstance;
import com.drumonii.loltrollbuild.model.BatchStepExecution;
import com.drumonii.loltrollbuild.repository.BatchStepExecutionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * Repository REST controller for {@link BatchStepExecution}s.
 */
@RestController
@RequestMapping("/admin/job-instances/{jobInstanceId}/step-executions")
@PreAuthorize("hasRole('ADMIN')")
public class BatchStepExecutionsRestController {

	@Autowired
	private BatchStepExecutionsRepository batchStepExecutionsRepository;

	/**
	 * Gets {@link BatchStepExecution} from its {@link BatchJobInstance} looked up by the job instance Id, if found.
	 *
	 * @param jobInstanceId the job instance Id to lookup
	 * @return the {@link List} of {@link BatchStepExecution}
	 */
	@GetMapping
	public List<BatchStepExecution> getBatchStepExecutions(@PathVariable long jobInstanceId) {
		List<BatchStepExecution> stepExecutions = batchStepExecutionsRepository.findByJobInstanceId(jobInstanceId);
		if (stepExecutions.isEmpty()) {
			throw new ResourceNotFoundException("Unable to find Batch Step Execution with Job Instance Id: " + jobInstanceId);
		}
		return stepExecutions;
	}

	/**
	 * Gets a {@link BatchStepExecution} from the job instance ID and step execution Id, if found.
	 *
	 * @param jobInstanceId the job instance Id to lookup
	 * @param stepExecutionId the step execution Id to lookup
	 * @return the {@link BatchStepExecution}
	 */
	@GetMapping("/{stepExecutionId}")
	public BatchStepExecution getBatchStepExecution(@PathVariable long jobInstanceId,
			@PathVariable long stepExecutionId) {
		Optional<BatchStepExecution> stepExecution = batchStepExecutionsRepository.findById(stepExecutionId);
		return stepExecution.orElseThrow(() -> new ResourceNotFoundException("Unable to find Batch Step Execution with Job Instance Id: " +
				jobInstanceId + " and Step Execution Id: " + stepExecutionId));
	}

}
