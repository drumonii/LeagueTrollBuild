package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.util.ChampionUtil;
import com.drumonii.loltrollbuild.util.GameMapUtil;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
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
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.data.domain.ExampleMatcher.StringMatcher.CONTAINING;

/**
 * Repository REST controller for {@link Champion}s.
 */
@RestController
@RequestMapping("/${spring.data.rest.base-path}/champions")
@RepositoryRestController
public class ChampionsRestController {

	static final int PAGE_SIZE = 20;

	private static final int ITEMS_SIZE = 6;
	private static final int SPELLS_SIZE = 2;

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	private MapsRepository mapsRepository;

	@Autowired
	private PagedResourcesAssembler<Champion> pagedAssembler;

	/**
	 * Gets a {@link PagedResources} of {@link Champion} {@link Resource} from the pagination and search parameters.
	 *
	 * @param pageable the {@link Pageable}
	 * @param champion the search {@link Champion} to define as the QBE
	 * @return the {@link PagedResources} of {@link Champion} {@link Resource}
	 */
	@GetMapping
	public PagedResources<Resource<Champion>> getChampions(
			@PageableDefault(size = PAGE_SIZE, sort = "name", direction = Direction.ASC) Pageable pageable,
			Champion champion) {
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher("name", matcher -> matcher.stringMatcher(CONTAINING))
				.withMatcher("title", matcher -> matcher.stringMatcher(CONTAINING))
				.withMatcher("partype", matcher -> matcher.stringMatcher(CONTAINING))
				.withIgnoreCase()
				.withIgnorePaths("id")
				.withIgnoreNullValues();
		Example<Champion> example = Example.of(champion, exampleMatcher);
		return pagedAssembler.toResource(championsRepository.findAll(example, pageable));
	}

	/**
	 * Generates the full troll build based on the specified {@link Champion} and map ID.
	 *
	 * @param id the {@link Champion} to create the troll build for
	 * @param mapId the map ID to generate the troll build
	 * @return a {@link Map} of build type key with {@link List} of values.
	 */
	@GetMapping(value = "/{id}/troll-build")
	public Map<String, List<?>> trollBuild(@PathVariable int id,
			@RequestParam(required = false, defaultValue = "11") int mapId) {
		Map<String, List<?>> trollBuild = new HashMap<>();
		Champion champion = championsRepository.findOne(id);
		if (champion == null) {
			return trollBuild;
		}

		// Items
		List<Item> items = new ArrayList<>();
		// Add boots first
		items.add(RandomizeUtil.getRandom(itemsRepository.boots(mapId)));
		// If Viktor, add his starting item
		if (ChampionUtil.isViktor(champion)) {
			items.add(RandomizeUtil.getRandom(itemsRepository.viktorOnly()));
		}
		// Get all items for the troll build
		items.addAll(RandomizeUtil.getRandoms(itemsRepository.forTrollBuild(mapId),
				ChampionUtil.isViktor(champion) ? ITEMS_SIZE - 2: ITEMS_SIZE - 1));
		trollBuild.put("items", items);

		// Summoner Spells
		trollBuild.put("summoner-spells",
				RandomizeUtil.getRandoms(summonerSpellsRepository.forTrollBuild(
						GameMapUtil.getModeFromMap(mapsRepository.findOne(mapId))), SPELLS_SIZE));

		// Trinket
		trollBuild.put("trinket", Collections.singletonList(RandomizeUtil.getRandom(itemsRepository.trinkets(mapId))));
		return trollBuild;
	}

}
