package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.SummonerSpell.GameMode;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.StringMatcher.CONTAINING;
import static org.springframework.hateoas.mvc.BasicLinkBuilder.linkToCurrentMapping;

/**
 * Repository REST controller for {@link SummonerSpell}s.
 */
@RestController
@RequestMapping("/${spring.data.rest.base-path}/summoner-spells")
@RepositoryRestController
public class SummonerSpellsRestController {

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
	@RequestMapping(method = RequestMethod.GET)
	public PagedResources<Resource<SummonerSpell>> getSummonerSpells(Pageable pageable, SummonerSpell summonerSpell) {
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher("name", matcher -> matcher.stringMatcher(CONTAINING))
				.withIgnoreCase()
				.withIgnorePaths("id")
				.withIgnoreNullValues();
		Example<SummonerSpell> example = Example.of(summonerSpell, exampleMatcher);
		return pagedAssembler.toResource(summonerSpellsRepository.findAll(example, pageable));
	}

	/**
	 * Gets a {@link Resources} of {@link SummonerSpell} {@link Resource} for the troll build based on the specified
	 * {@link GameMode}. See {@link SummonerSpellsRepository#forTrollBuild(GameMode mode)} for details on data retrieved.
	 *
	 * @param mode the {@link GameMode} to get eligible {@link SummonerSpell}s for the troll build
	 * @return the {@link Resources} of {@link SummonerSpell} {@link Resource}
	 */
	@RequestMapping(value = "/for-troll-build", method = RequestMethod.GET)
	public Resources<Resource<SummonerSpell>> getForTrollBuild(@RequestParam GameMode mode) {
		return new Resources<>(summonerSpellsRepository.forTrollBuild(mode).stream()
				.map(spell -> new Resource<>(spell))
				.collect(Collectors.toList()), linkToCurrentMapping().withSelfRel());
	}

}
