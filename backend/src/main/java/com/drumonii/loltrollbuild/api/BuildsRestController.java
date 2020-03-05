package com.drumonii.loltrollbuild.api;

import com.drumonii.loltrollbuild.api.status.BadRequestException;
import com.drumonii.loltrollbuild.api.status.ResourceNotFoundException;
import com.drumonii.loltrollbuild.api.view.ApiViews;
import com.drumonii.loltrollbuild.config.Profiles.Disabled;
import com.drumonii.loltrollbuild.model.Build;
import com.drumonii.loltrollbuild.repository.*;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Repository REST controller for {@link Build}s.
 */
@RestController
@RequestMapping("/builds")
@Disabled
public class BuildsRestController {

	static final int PAGE_SIZE = 20;

	@Autowired
	private BuildsRepository buildsRepository;

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	private MapsRepository mapsRepository;

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
	 * Gets a {@link Build} by its ID. If not found, returns a 404, or if some of its attributes no longer exist a 400,
	 * otherwise a 200.
	 *
	 * @param id the ID to lookup the {@link Build}
	 * @return the {@link Build}
	 */
	@JsonView({ ApiViews.LtbApi.class })
	@GetMapping("/{id}")
	public Build getBuild(@PathVariable int id) {
		Optional<Build> optionalBuild = buildsRepository.findById(id);
		if (optionalBuild.isEmpty()) {
			throw new ResourceNotFoundException("Unable to find a Build with Id: " + id);
		}
		Build build = optionalBuild.get();
		build.setChampion(championsRepository.findById(build.getChampionId()).orElse(null));
		build.setItem1(itemsRepository.findById(build.getItem1Id()).orElse(null));
		build.setItem2(itemsRepository.findById(build.getItem2Id()).orElse(null));
		build.setItem3(itemsRepository.findById(build.getItem3Id()).orElse(null));
		build.setItem4(itemsRepository.findById(build.getItem4Id()).orElse(null));
		build.setItem5(itemsRepository.findById(build.getItem5Id()).orElse(null));
		build.setItem6(itemsRepository.findById(build.getItem6Id()).orElse(null));
		build.setSummonerSpell1(summonerSpellsRepository.findById(build.getSummonerSpell1Id()).orElse(null));
		build.setSummonerSpell2(summonerSpellsRepository.findById(build.getSummonerSpell2Id()).orElse(null));
		build.setTrinket(itemsRepository.findById(build.getTrinketId()).orElse(null));
		build.setMap(mapsRepository.findById(build.getMapId()).orElse(null));

		BindingResult result = new BeanPropertyBindingResult(build, "build");
		build.validate(build, result);
		if (result.hasErrors()) {
			throw new BadRequestException(result.getFieldErrors());
		}
		return build;
	}

	/**
	 * Saves a {@link Build}.
	 *
	 * @param build the {@link Build} to save, if valid
	 * @return the {@link Build}
	 */
	@JsonView({ ApiViews.LtbApi.class })
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ResponseEntity<Build> saveBuild(@RequestBody @Valid Build build, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new BadRequestException(bindingResult.getFieldErrors());
		}
		Build savedBuild = buildsRepository.save(build);
		return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedBuild.getId())
				.toUri())
				.body(savedBuild);
	}

	/**
	 * Gets the number of saved {@link Build}s.
	 *
	 * @return the count of saved {@link Build}s
	 */
	@GetMapping("/count")
	public long countBuilds() {
		return buildsRepository.count();
	}

}
