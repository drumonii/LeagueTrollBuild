package com.drumonii.loltrollbuild.controller;

import com.drumonii.loltrollbuild.model.Build;
import com.drumonii.loltrollbuild.repository.*;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for {@link Build}s and their views.
 */
@Controller
@RequestMapping("/builds")
public class BuildsController {

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

	@GetMapping
	public String builds() {
		return "redirect:/builds/" + RandomUtils.nextInt(1, (int) buildsRepository.count() + 1);
	}

	@GetMapping(value = "/{id}")
	public String build(@PathVariable int id, Model model) {
		Build build = buildsRepository.findOne(id);
		if (build == null) {
			return "builds/notFound";
		}

		build.setChampion(championsRepository.findOne(build.getChampionId()));
		build.setItem1(itemsRepository.findOne(build.getItem1Id()));
		build.setItem2(itemsRepository.findOne(build.getItem2Id()));
		build.setItem3(itemsRepository.findOne(build.getItem3Id()));
		build.setItem4(itemsRepository.findOne(build.getItem4Id()));
		build.setItem5(itemsRepository.findOne(build.getItem5Id()));
		build.setItem6(itemsRepository.findOne(build.getItem6Id()));
		build.setSummonerSpell1(summonerSpellsRepository.findOne(build.getSummonerSpell1Id()));
		build.setSummonerSpell2(summonerSpellsRepository.findOne(build.getSummonerSpell2Id()));
		build.setTrinket(itemsRepository.findOne(build.getTrinketId()));
		build.setMap(mapsRepository.findOne(build.getMapId()));

		BindingResult result = new BeanPropertyBindingResult(build, "build");
		build.validate(build, result);
		if (result.hasErrors()) {
			return "builds/invalidAttributes";
		}

		model.addAttribute(build);
		return "builds/build";
	}

}
