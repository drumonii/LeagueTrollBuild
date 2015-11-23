package com.drumonii.loltrollbuild.controller;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

import static org.springframework.data.domain.Sort.Direction.ASC;

/**
 * Controller for {@link Champion}s and their views.
 */
@Controller
@RequestMapping("/champions")
public class ChampionsController {

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

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
		return "champions/champion";
	}

	@RequestMapping(value = "/{id}/troll-build", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, List<?>> trollBuild(@PathVariable("id") Champion champion) {
		Map<String, List<?>> trollBuild = new HashMap<>();

		// Items
		List<Item> items = new ArrayList<>();
		// Add boots first
		items.add(RandomizeUtil.getRandom(itemsRepository.boots()));
		// If Viktor, add his starting item
		if (champion.isViktor()) {
			items.add(RandomizeUtil.getRandom(itemsRepository.viktorOnly()));
		}
		// Get all items for the troll build
		List<Item> itemsForTrollBuild = itemsRepository.forTrollBuild();
		while (items.size() < 6) {
			items.add(RandomizeUtil.getRandomAndRemove(itemsForTrollBuild));
		}
		trollBuild.put("items", items);

		// Summoner Spells
		List<SummonerSpell> summonerSpells = new ArrayList<>();
		List<SummonerSpell> allSummonerSpells = summonerSpellsRepository.forTrollBuild();
		while (summonerSpells.size() < 2) {
			summonerSpells.add(RandomizeUtil.getRandomAndRemove(allSummonerSpells));
		}
		trollBuild.put("summoner-spells", summonerSpells);

		// Trinket
		trollBuild.put("trinket", Arrays.asList(RandomizeUtil.getRandom(itemsRepository.trinkets())));
		return trollBuild;
	}

}
