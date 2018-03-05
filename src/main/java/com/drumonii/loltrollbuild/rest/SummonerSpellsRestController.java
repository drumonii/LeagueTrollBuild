package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.SummonerSpell.GameMode;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.rest.specification.SummonerSpellsSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.BasicLinkBuilder.linkToCurrentMapping;

/**
 * Repository REST controller for {@link SummonerSpell}s.
 */
@RestController
@RequestMapping("/${spring.data.rest.base-path}/summoner-spells")
@RepositoryRestController
public class SummonerSpellsRestController {

	static final int PAGE_SIZE = 20;

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	private PagedResourcesAssembler<SummonerSpell> pagedAssembler;

	/**
	 * Gets a {@link PagedResources} of {@link SummonerSpell} {@link Resource} from the pagination and search parameters.
	 *
	 * @param pageable the {@link Pageable}
	 * @param summonerSpell the search {@link SummonerSpell} to define as the QBE
	 * @return the {@link PagedResources} of {@link SummonerSpell} {@link Resource}
	 */
	@GetMapping
	public PagedResources<Resource<SummonerSpell>> getSummonerSpells(
			@PageableDefault(size = PAGE_SIZE, sort = "name", direction = Direction.ASC) Pageable pageable,
			SummonerSpell summonerSpell) {
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher("name", GenericPropertyMatcher::contains)
				.withIgnoreCase()
				.withIgnorePaths("id", "version")
				.withIgnoreNullValues();
		Example<SummonerSpell> example = Example.of(summonerSpell, exampleMatcher);
		return pagedAssembler.toResource(summonerSpellsRepository.findAll(new SummonerSpellsSpecification(example), pageable));
	}

	/**
	 * Gets a {@link Resources} of {@link SummonerSpell} {@link Resource} for the troll build based on the specified
	 * {@link GameMode}. See {@link SummonerSpellsRepository#forTrollBuild(GameMode mode)} for details on data retrieved.
	 *
	 * @param mode the {@link GameMode} to get eligible {@link SummonerSpell}s for the troll build
	 * @return the {@link Resources} of {@link SummonerSpell} {@link Resource}
	 */
	@GetMapping(path = "/for-troll-build")
	public Resources<Resource<SummonerSpell>> getForTrollBuild(
			@RequestParam(required = false, defaultValue = "CLASSIC") GameMode mode) {
		return new Resources<>(summonerSpellsRepository.forTrollBuild(mode).stream()
				.map(spell -> new Resource<>(spell))
				.collect(Collectors.toList()), linkToCurrentMapping().withSelfRel());
	}

}
