package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.BatchJobInstance;
import com.drumonii.loltrollbuild.repository.BatchJobInstancesRepository;
import com.drumonii.loltrollbuild.rest.projection.BatchJobInstanceProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.domain.ExampleMatcher.StringMatcher.CONTAINING;

/**
 * Repository REST controller for {@link BatchJobInstance}s.
 */
@RestController
@RequestMapping("/${spring.data.rest.base-path}/job-instances")
@RepositoryRestController
@PreAuthorize("hasRole('ADMIN')")
public class BatchJobInstancesRestController {

	static final int PAGE_SIZE = 20;

	@Autowired
	private ProjectionFactory projectionFactory;

	@Autowired
	private BatchJobInstancesRepository batchJobInstancesRepository;

	@Autowired
	private PagedResourcesAssembler<BatchJobInstanceProjection> pagedAssembler;

	/**
	 * Gets a {@link PagedResources} of {@link BatchJobInstance} {@link Resource} from the pagination and search parameters.
	 *
	 * @param pageable the {@link Pageable}
	 * @param jobInstance the search {@link BatchJobInstance} to define as the QBE
	 * @return the {@link PagedResources} of {@link BatchJobInstance} {@link Resource}
	 */
	@GetMapping
	public PagedResources<Resource<BatchJobInstanceProjection>> getBatchJobInstances(
			@PageableDefault(size = PAGE_SIZE, sort = "id", direction = Direction.ASC) Pageable pageable,
			BatchJobInstance jobInstance) {
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher("name", matcher -> matcher.stringMatcher(CONTAINING))
				.withIgnoreCase()
				.withIgnorePaths("id")
				.withIgnoreNullValues();
		Example<BatchJobInstance> example = Example.of(jobInstance, exampleMatcher);
		return pagedAssembler.toResource(batchJobInstancesRepository.findAll(example, pageable)
				.map(ji -> projectionFactory.createProjection(BatchJobInstanceProjection.class, ji)));
	}

	/**
	 * Gets a {@link BatchJobInstanceProjection} from the job instance Id, if found.
	 *
	 * @param jobInstanceId the job instance Id to lookup
	 * @return the {@link BatchJobInstanceProjection} {@link Resource}
	 */
	@GetMapping(value = "/{jobInstanceId}")
	public Resource<BatchJobInstanceProjection> getBatchJobInstance(@PathVariable long jobInstanceId) {
		BatchJobInstance jobInstance = batchJobInstancesRepository.findOne(jobInstanceId);
		if (jobInstance == null) {
			throw new ResourceNotFoundException("Unable to find Batch Job Instance with Id: " + jobInstanceId);
		}
		return new Resource<>(projectionFactory.createProjection(BatchJobInstanceProjection.class, jobInstance));
	}

}
