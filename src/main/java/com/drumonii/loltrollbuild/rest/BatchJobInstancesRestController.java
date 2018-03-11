package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.BatchJobInstance;
import com.drumonii.loltrollbuild.repository.BatchJobInstancesRepository;
import com.drumonii.loltrollbuild.rest.status.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Repository REST controller for {@link BatchJobInstance}s.
 */
@RestController
@RequestMapping("/${api.base-path}/job-instances")
@PreAuthorize("hasRole('ADMIN')")
public class BatchJobInstancesRestController {

	static final int PAGE_SIZE = 20;

	@Autowired
	private BatchJobInstancesRepository batchJobInstancesRepository;

	/**
	 * Gets a {@link Page} of {@link BatchJobInstance}s from the pagination and search parameters.
	 *
	 * @param pageable the {@link Pageable}
	 * @param jobInstance the search {@link BatchJobInstance} to define as the QBE
	 * @return the {@link Page} of {@link BatchJobInstance}
	 */
	@GetMapping
	public Page<BatchJobInstance> getBatchJobInstances(
			@PageableDefault(size = PAGE_SIZE, sort = "id", direction = Direction.ASC) Pageable pageable,
			BatchJobInstance jobInstance) {
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher("name", GenericPropertyMatcher::contains)
				.withMatcher("jobExecution.status", GenericPropertyMatcher::contains)
				.withIgnoreCase()
				.withIgnorePaths("id", "jobExecution.id")
				.withIgnoreNullValues();
		Example<BatchJobInstance> example = Example.of(jobInstance, exampleMatcher);
		return batchJobInstancesRepository.findAll(example, pageable);
	}

	/**
	 * Gets a {@link BatchJobInstance} from the job instance Id, if found.
	 *
	 * @param jobInstanceId the job instance Id to lookup
	 * @return the {@link BatchJobInstance}
	 */
	@GetMapping(path = "/{jobInstanceId}")
	public BatchJobInstance getBatchJobInstance(@PathVariable long jobInstanceId) {
		Optional<BatchJobInstance> jobInstance = batchJobInstancesRepository.findById(jobInstanceId);
		return jobInstance.orElseThrow(() -> new ResourceNotFoundException("Unable to find Batch Job Instance with Id: " + jobInstanceId));
	}

}
