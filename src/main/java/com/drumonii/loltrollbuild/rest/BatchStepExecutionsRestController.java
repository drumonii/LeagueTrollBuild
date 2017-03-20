package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.BatchJobInstance;
import com.drumonii.loltrollbuild.model.BatchStepExecution;
import com.drumonii.loltrollbuild.repository.BatchStepExecutionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.BasicLinkBuilder.linkToCurrentMapping;

/**
 * Repository REST controller for {@link BatchStepExecution}s.
 */
@RestController
@RequestMapping("/${spring.data.rest.base-path}/job-instances/{jobInstanceId}/step-executions")
@RepositoryRestController
@PreAuthorize("hasRole('ADMIN')")
public class BatchStepExecutionsRestController {

	@Autowired
	private BatchStepExecutionsRepository batchStepExecutionsRepository;

	/**
	 * Gets {@link BatchStepExecution} from its {@link BatchJobInstance} looked up by the job instance Id, if found.
	 *
	 * @param jobInstanceId the job instance Id to lookup
	 * @return the {@link Resources} of {@link BatchStepExecution} {@link Resource}
	 */
	@GetMapping
	public Resources<Resource<BatchStepExecution>> getBatchStepExecutions(@PathVariable long jobInstanceId) {
		List<BatchStepExecution> stepExecutions = batchStepExecutionsRepository.findByJobInstanceId(jobInstanceId);
		if (stepExecutions.isEmpty()) {
			throw new ResourceNotFoundException("Unable to find Batch Step Execution with Job Instance Id: " + jobInstanceId);
		}
		return new Resources<>(stepExecutions.stream()
				.map(item -> new Resource<>(item))
				.collect(Collectors.toList()), linkToCurrentMapping().withSelfRel());
	}

	/**
	 * Gets a {@link BatchStepExecution} from the job instance ID and step execution Id, if found.
	 *
	 * @param jobInstanceId the job instance Id to lookup
	 * @param stepExecutionId the step execution Id to lookup
	 * @return the {@link BatchStepExecution} {@link Resource}
	 */
	@GetMapping(value = "/{stepExecutionId}")
	public Resource<BatchStepExecution> getBatchStepExecution(@PathVariable long jobInstanceId,
			@PathVariable long stepExecutionId) {
		BatchStepExecution stepExecution = batchStepExecutionsRepository.findOne(stepExecutionId);
		if (stepExecution == null) {
			throw new ResourceNotFoundException("Unable to find Batch Step Execution with Job Instance Id: " +
					jobInstanceId + " and Step Execution Id: " + stepExecutionId);
		}
		return new Resource<>(stepExecution);
	}

}
