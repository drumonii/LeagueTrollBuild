package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.repository.specification.ExampleSpecification;
import com.drumonii.loltrollbuild.rest.status.ResourceNotFoundException;
import com.drumonii.loltrollbuild.util.ChampionUtil;
import com.drumonii.loltrollbuild.util.GameMapUtil;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.drumonii.loltrollbuild.util.GameMapUtil.SUMMONERS_RIFT_SID;

/**
 * Repository REST controller for {@link Champion}s.
 */
@RestController
@RequestMapping("${api.base-path}/champions")
public class ChampionsRestController {

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

	/**
	 * Gets a {@link List} of {@link Champion}s from the sort and search parameters.
	 *
	 * @param sort the {@link Sort}
	 * @param champion the search {@link Champion} to define as the QBE
	 * @return the {@link List} of {@link Champion}s
	 */
	@GetMapping
	public List<Champion> getChampions(
			@SortDefault(sort = "name", direction = Direction.ASC) Sort sort, Champion champion) {
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher("name", GenericPropertyMatcher::contains)
				.withMatcher("title", GenericPropertyMatcher::contains)
				.withMatcher("partype", GenericPropertyMatcher::contains)
				.withIgnoreCase()
				.withIgnorePaths("id", "version")
				.withIgnoreNullValues();
		Example<Champion> example = Example.of(champion, exampleMatcher);
		return championsRepository.findAll(new ExampleSpecification<>(example), sort);
	}

	/**
	 * Gets a {@link Champion} by either its ID or name. If not found, returns a 404, otherwise a 200.
	 *
	 * @param value the value to lookup the {@link Champion}
	 * @return the {@link Champion}
	 */
	@GetMapping(path = "/{value}")
	public Champion getChampion(@PathVariable String value) {
		Optional<Champion> champion;
		try {
			champion = championsRepository.findById(Integer.valueOf(value));
		} catch (NumberFormatException e) {
			champion = championsRepository.findByName(value);
		}
		return champion.orElseThrow(() -> new ResourceNotFoundException("Unable to find a Champion with value: " + value));
	}

	/**
	 * Gets the distinct {@link List} of {@link Champion} tags.
	 *
	 * @return the {@link List} of tags
	 */
	@GetMapping(path = "/tags")
	public List<String> getTags() {
		return championsRepository.getTags();
	}

	/**
	 * Generates the full troll build based on the specified {@link Champion} and map ID.
	 *
	 * @param value the value to lookup the {@link Champion} to create the troll build for
	 * @param mapId the map ID to generate the troll build
	 * @return a {@link Map} of build type key with {@link List} of values.
	 */
	@GetMapping(path = "/{value}/troll-build")
	public Map<String, List<?>> trollBuild(@PathVariable String value,
			@RequestParam(required = false, defaultValue = SUMMONERS_RIFT_SID) int mapId) {
		Map<String, List<?>> trollBuild = new HashMap<>();
		Optional<Champion> champion;
		try {
			champion = championsRepository.findById(Integer.valueOf(value));
		} catch (NumberFormatException e) {
			champion = championsRepository.findByName(value);
		}
		if (!champion.isPresent()) {
			return trollBuild;
		}

		// Items
		List<Item> items = new ArrayList<>();
		// Add boots first
		items.add(RandomizeUtil.getRandom(itemsRepository.boots(mapId)));
		// If Viktor, add his starting item
		if (ChampionUtil.isViktor(champion.get())) {
			items.add(RandomizeUtil.getRandom(itemsRepository.viktorOnly()));
		}
		// Get all items for the troll build
		items.addAll(RandomizeUtil.getRandoms(itemsRepository.forTrollBuild(mapId),
				ChampionUtil.isViktor(champion.get()) ? ITEMS_SIZE - 2: ITEMS_SIZE - 1));
		trollBuild.put("items", items);

		// Summoner Spells
		trollBuild.put("summoner-spells", RandomizeUtil.getRandoms(summonerSpellsRepository
				.forTrollBuild(GameMapUtil.getModeFromMap(mapsRepository.findById(mapId).orElse(null))), SPELLS_SIZE));

		// Trinket
		trollBuild.put("trinket", Collections.singletonList(RandomizeUtil.getRandom(itemsRepository.trinkets(mapId))));
		return trollBuild;
	}

}
