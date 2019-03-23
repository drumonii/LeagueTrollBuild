package com.drumonii.loltrollbuild.rest.admin;

import com.drumonii.loltrollbuild.model.BatchJobExecution;
import com.drumonii.loltrollbuild.model.BatchJobInstance;
import com.drumonii.loltrollbuild.model.builder.BatchJobExecutionBuilder;
import com.drumonii.loltrollbuild.repository.BatchJobInstancesRepository;
import com.drumonii.loltrollbuild.rest.status.ResourceNotFoundException;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repository REST controller for {@link BatchJobInstance}s.
 */
@RestController
@RequestMapping("${api.base-path}/admin/job-instances")
@PreAuthorize("hasRole('ADMIN')")
public class BatchJobInstancesRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BatchJobInstancesRestController.class);

	static final int PAGE_SIZE = 20;

	@Autowired
	private BatchJobInstancesRepository batchJobInstancesRepository;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	@Qualifier("allRetrievalsJob")
	private Job allRetrievalsJob;

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
	 * Restarts the allRetrievalsJob with an additional job parameter, "forced", of a random number value.
	 *
	 * @return the {@link ResponseEntity} of {@link BatchJobExecution} if successfully ran, otherwise a 500.
	 */
	@PostMapping(path = "/restart")
	public ResponseEntity<BatchJobExecution> restartAllRetrievalsJob() {
		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("forced", RandomizeUtil.getRandomLong())
				.toJobParameters();

		JobParametersIncrementer allRetrievalsJobParametersIncrementer = allRetrievalsJob.getJobParametersIncrementer();

		try {
			JobExecution jobExecution = jobLauncher.run(allRetrievalsJob, allRetrievalsJobParametersIncrementer.getNext(jobParameters));
			return ResponseEntity.ok(new BatchJobExecutionBuilder().fromJobExecution(jobExecution).build());
		} catch (Exception e) {
			LOGGER.error("Caught exception while running all retrievals job", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Determines if the most recent allRetrievalsJob run failed to complete. It might be hanging or have failed altogether.
	 *
	 * @return the {@link Map} of "hasFailedAllRetrievalsJob" key and boolean value.
	 */
	@GetMapping(path = "/has-failed-all-retrievals-job")
	public Map<String, Boolean> hasFailedAllRetrievalsJob(@RequestParam(required = false, defaultValue = "5") long minutes) {
		LocalDateTime lastStartTime = LocalDateTime.now().minusMinutes(minutes);

		PageRequest pageRequest = PageRequest.of(0, 1, Sort.by(Order.desc("jobExecution.startTime")));
		List<BatchJobInstance> batchJobInstances =
				batchJobInstancesRepository.getMostRecentAllRetrievalsJob(pageRequest);

		boolean hasFailedAllRetrievalsJob = false;

		if (!batchJobInstances.isEmpty()) {
			BatchJobInstance allRetrievalsJobInstance = batchJobInstances.get(0);
			BatchStatus batchStatus = BatchStatus.match(allRetrievalsJobInstance.getJobExecution().getStatus());
			if (batchStatus.isUnsuccessful() ||
					(batchStatus.isRunning() && allRetrievalsJobInstance.getJobExecution().getStartTime().isBefore(lastStartTime))) {
				hasFailedAllRetrievalsJob = true;
			}
		}

		return Map.of("hasFailedAllRetrievalsJob", hasFailedAllRetrievalsJob);
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
