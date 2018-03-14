package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.SummonerSpell.GameMode;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.rest.specification.SummonerSpellsSpecification;
import com.drumonii.loltrollbuild.rest.status.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Repository REST controller for {@link SummonerSpell}s.
 */
@RestController
@RequestMapping("${api.base-path}/summoner-spells")
public class SummonerSpellsRestController {

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	/**
	 * Gets a {@link List} of {@link SummonerSpell}s from the sort and search parameters.
	 *
	 * @param sort the {@link Sort}
	 * @param summonerSpell the search {@link SummonerSpell} to define as the QBE
	 * @return the {@link List} of {@link SummonerSpell}s
	 */
	@GetMapping
	public List<SummonerSpell> getSummonerSpells(
			@SortDefault(sort = "name", direction = Direction.ASC) Sort sort, SummonerSpell summonerSpell) {
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher("name", GenericPropertyMatcher::contains)
				.withIgnoreCase()
				.withIgnorePaths("id", "version")
				.withIgnoreNullValues();
		Example<SummonerSpell> example = Example.of(summonerSpell, exampleMatcher);
		return summonerSpellsRepository.findAll(new SummonerSpellsSpecification(example), sort);
	}

	/**
	 * Gets a {@link SummonerSpell} by its ID. If not found, returns a 404, otherwise a 200.
	 *
	 * @param id the ID to lookup the {@link SummonerSpell}
	 * @return the {@link SummonerSpell}
	 */
	@GetMapping(path = "/{id}")
	public SummonerSpell getSummonerSpell(@PathVariable int id) {
		Optional<SummonerSpell> summonerSpell = summonerSpellsRepository.findById(id);
		return summonerSpell.orElseThrow(() -> new ResourceNotFoundException("Unable to find a Summoner Spell with Id: " + id));
	}

	/**
	 * Gets a {@link List} of {@link SummonerSpell}s for the troll build based on the specified {@link GameMode}. See
	 * {@link SummonerSpellsRepository#forTrollBuild(GameMode mode)} for details on data retrieved.
	 *
	 * @param mode the {@link GameMode} to get eligible {@link SummonerSpell}s for the troll build
	 * @return the {@link List} of {@link SummonerSpell}s
	 */
	@GetMapping(path = "/for-troll-build")
	public List<SummonerSpell> getForTrollBuild(@RequestParam(required = false, defaultValue = "CLASSIC") GameMode mode) {
		return summonerSpellsRepository.forTrollBuild(mode);
	}

}
