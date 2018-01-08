package com.drumonii.loltrollbuild.controller;

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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.data.domain.Sort.Direction.ASC;

/**
 * Controller for {@link Champion}s and their views.
 */
@Controller
@RequestMapping("/champions")
public class ChampionsController {

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

	@GetMapping
	public String champions(Model model) {
		model.addAttribute("champions", championsRepository.findAll(new Sort(ASC, "name")));
		model.addAttribute("tags", championsRepository.getTags());
		return "champions/champions";
	}

	@GetMapping(value = "/{value}")
	public String champion(@PathVariable String value, Model model) {
		Champion champion;
		try {
			champion = championsRepository.findOne(Integer.valueOf(value));
		} catch (NumberFormatException e) {
			champion = championsRepository.findByName(value);
		}
		if (champion == null) {
			return "redirect:/champions";
		}
		model.addAttribute(champion);
		model.addAttribute("maps", GameMapUtil.eligibleMaps(mapsRepository.findAll()));
		return "champions/champion";
	}

	/**
	 * Generates the full troll build based on the specified {@link Champion} and map ID.
	 *
	 * @param id the {@link Champion} to create the troll build for
	 * @param mapId the map ID to generate the troll build
	 * @return a {@link Map} of build type key with {@link List} of values.
	 */
	@GetMapping(value = "/{id}/troll-build")
	@ResponseBody
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
		trollBuild.put("trinket", Arrays.asList(RandomizeUtil.getRandom(itemsRepository.trinkets(mapId))));
		return trollBuild;
	}

}
