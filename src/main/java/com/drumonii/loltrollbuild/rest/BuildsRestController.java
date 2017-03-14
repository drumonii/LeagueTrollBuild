package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.Build;
import com.drumonii.loltrollbuild.repository.BuildsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Repository REST controller for {@link Build}s.
 */
@RestController
@RequestMapping("/${spring.data.rest.base-path}/builds")
@RepositoryRestController
public class BuildsRestController {

	@Autowired
	private BuildsRepository buildsRepository;

	@Autowired
	private PagedResourcesAssembler<Build> pagedAssembler;

	@Autowired
	@Qualifier("mvcValidator")
	private Validator validator;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
	}

	/**
	 * Gets a {@link PagedResources} of {@link Build} {@link Resource} from the pagination and search parameters.
	 *
	 * @param pageable the {@link Pageable}
	 * @param build the search {@link Build} to define as the QBE
	 * @return the {@link PagedResources} of {@link Build} {@link Resource}
	 */
	@GetMapping
	public PagedResources<Resource<Build>> getBuilds(
			@PageableDefault(size = 20, sort = "id", direction = Direction.ASC) Pageable pageable, Build build) {
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withIgnorePaths("id")
				.withIgnoreNullValues();
		Example<Build> example = Example.of(build, exampleMatcher);
		return pagedAssembler.toResource(buildsRepository.findAll(example, pageable));
	}

	/**
	 * Saves a {@link Build}.
	 *
	 * @param build the {@link Build} to save, if valid
	 * @return the {@link Resource} of {@link Build}
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public Resource<Build> saveBuild(@RequestBody @Valid Build build) {
		return new Resource<>(buildsRepository.save(build));
	}

}
