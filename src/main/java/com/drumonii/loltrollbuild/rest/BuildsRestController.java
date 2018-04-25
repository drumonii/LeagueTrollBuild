package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.Build;
import com.drumonii.loltrollbuild.repository.BuildsRepository;
import com.drumonii.loltrollbuild.rest.status.BadRequestException;
import com.drumonii.loltrollbuild.rest.status.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Repository REST controller for {@link Build}s.
 */
@RestController
@RequestMapping("${api.base-path}/builds")
public class BuildsRestController {

	static final int PAGE_SIZE = 20;

	@Autowired
	private BuildsRepository buildsRepository;

	@Autowired
	@Qualifier("mvcValidator")
	private Validator validator;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
	}

	/**
	 * Gets a {@link Page} of {@link Build}s from the pagination and search parameters.
	 *
	 * @param pageable the {@link Pageable}
	 * @param build the search {@link Build} to define as the QBE
	 * @return the {@link Page} of {@link Build}s
	 */
	@GetMapping
	public Page<Build> getBuilds(
			@PageableDefault(size = PAGE_SIZE, sort = "id", direction = Direction.DESC) Pageable pageable, Build build) {
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withIgnorePaths("id")
				.withIgnoreNullValues();
		Example<Build> example = Example.of(build, exampleMatcher);
		return buildsRepository.findAll(example, pageable);
	}

	/**
	 * Gets a {@link Build} by its ID. If not found, returns a 404, otherwise a 200.
	 *
	 * @param id the ID to lookup the {@link Build}
	 * @return the {@link Build}
	 */
	@GetMapping(path = "/{id}")
	public Build getBuild(@PathVariable int id) {
		Optional<Build> build = buildsRepository.findById(id);
		return build.orElseThrow(() -> new ResourceNotFoundException("Unable to find a Build with Id: " + id));
	}

	/**
	 * Saves a {@link Build}.
	 *
	 * @param build the {@link Build} to save, if valid
	 * @return the {@link Build}
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ResponseEntity<Build> saveBuild(@RequestBody @Valid Build build, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new BadRequestException(bindingResult.getAllErrors());
		}
		Build savedBuild = buildsRepository.save(build);
		return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedBuild.getId())
				.toUri())
				.body(savedBuild);
	}

}
