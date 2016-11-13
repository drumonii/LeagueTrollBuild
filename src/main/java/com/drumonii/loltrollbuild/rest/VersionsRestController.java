package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Repository REST controller for {@link Version}s.
 */
@RestController
@RequestMapping("/${spring.data.rest.base-path}/versions")
@RepositoryRestController
public class VersionsRestController {

	@Autowired
	private VersionsRepository versionsRepository;

	@Autowired
	private PagedResourcesAssembler<Version> pagedAssembler;

	/**
	 * Gets a {@link PagedResources} of {@link Version} {@link Resource} from the pagination and search parameters.
	 *
	 * @param pageable the {@link Pageable}
	 * @param version the search {@link Version} to define as the QBE
	 * @return the {@link PagedResources} of {@link Version} {@link Resource}
	 */
	@GetMapping
	public PagedResources<Resource<Version>> getVersions(
			@PageableDefault(size = 20, sort =  { "major", "minor", "revision" }, direction = Direction.DESC)
					Pageable pageable, Version version) {
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withIgnorePaths("major", "minor", "revision");
		Example<Version> example = Example.of(version, exampleMatcher);
		return pagedAssembler.toResource(versionsRepository.findAll(example, pageable));
	}

}
