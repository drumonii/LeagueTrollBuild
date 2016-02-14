package com.drumonii.loltrollbuild.controller;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.ASC;

/**
 * Controller for {@link Champion}s and their views.
 */
@Controller
@RequestMapping("/champions")
public class ChampionsController {

	private static final int ITEMS_MAX = 6;
	private static final int SPELLS_MAX = 2;

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	private MapsRepository mapsRepository;

	@RequestMapping(method = RequestMethod.GET)
	public String champions(Model model) {
		model.addAttribute("champions", championsRepository.findAll(new Sort(ASC, "name")));
		return "champions/champions";
	}

	@RequestMapping(value = "/{value}", method = RequestMethod.GET)
	public String champion(@PathVariable String value, Model model) {
		Champion champion;
		try {
			int id = Integer.valueOf(value);
			champion = championsRepository.findOne(id);
		} catch (NumberFormatException e) {
			champion = championsRepository.findByName(value);
		}
		if (champion == null) {
			return "redirect:/champions";
		}
		model.addAttribute(champion);
		model.addAttribute("maps", eligibleMaps());
		return "champions/champion";
	}

	@RequestMapping(value = "/{id}/troll-build", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, List<?>> trollBuild(@PathVariable("id") Champion champion, @RequestParam String mapId) {
		Map<String, List<?>> trollBuild = new HashMap<>();

		// Items
		List<Item> items = new ArrayList<>();
		// Add boots first
		items.add(RandomizeUtil.getRandom(itemsRepository.boots(mapId)));
		// If Viktor, add his starting item
		if (champion.isViktor()) {
			items.add(RandomizeUtil.getRandom(itemsRepository.viktorOnly()));
		}
		// Get all items for the troll build
		List<Item> itemsForTrollBuild = itemsRepository.forTrollBuild(mapId);
		while (items.size() < ITEMS_MAX) {
			items.add(RandomizeUtil.getRandomAndRemove(itemsForTrollBuild));
		}
		trollBuild.put("items", items);

		// Summoner Spells
		List<SummonerSpell> summonerSpells = new ArrayList<>();
		List<SummonerSpell> allSummonerSpells = summonerSpellsRepository.forTrollBuild();
		while (summonerSpells.size() < SPELLS_MAX) {
			summonerSpells.add(RandomizeUtil.getRandomAndRemove(allSummonerSpells));
		}
		trollBuild.put("summoner-spells", summonerSpells);

		// Trinket
		trollBuild.put("trinket", Arrays.asList(RandomizeUtil.getRandom(itemsRepository.trinkets(mapId))));
		return trollBuild;
	}

	/**
	 * Gets {@link List} of all {@link GameMap}s that are eligible for the troll build. Map names are transformed into a
	 * more legible format. Eligible maps: Twisted Treeline, Summoner's Rift, and Proving Grounds.
	 *
	 * @return only the eligible {@link List} of {@link GameMap}s
	 */
	public List<GameMap> eligibleMaps() {
		List<GameMap> maps = (List<GameMap>) mapsRepository.findAll();
		for (GameMap map : maps) {
			switch (map.getMapName()) {
				case "NewTwistedTreeline":
					map.setMapName("Twisted Treeline");
					break;
				case "SummonersRiftNew":
					map.setMapName("Summoner's Rift");
					break;
				case "ProvingGroundsNew":
					map.setMapName("Proving Grounds");
					break;
			}
		}
		maps = maps.stream()
				.filter(map -> map.getMapName().equals("Twisted Treeline") ||
						map.getMapName().equals("Summoner's Rift") || map.getMapName().equals("Proving Grounds"))
				.sorted((map1, map2) -> map1.getMapName().compareTo(map2.getMapName()))
				.collect(Collectors.toList());
		return maps;
	}

}
