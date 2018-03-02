package com.drumonii.loltrollbuild.controller;

import com.drumonii.loltrollbuild.model.Build;
import com.drumonii.loltrollbuild.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import java.util.Random;

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

	private static final Random RANDOM = new Random();

	@GetMapping
	public String builds() {
		int start = 1;
		int end = (int) buildsRepository.count() + 1;
		return "redirect:/builds/" + (start == end ? start : start + RANDOM.nextInt(end - start));
	}

	@GetMapping(value = "/{id}")
	public String build(@PathVariable int id, Model model) {
		Optional<Build> build = buildsRepository.findById(id);
		if (!build.isPresent()) {
			return "builds/notFound";
		}

		build.get().setChampion(championsRepository.findById(build.get().getChampionId()).orElse(null));
		build.get().setItem1(itemsRepository.findById(build.get().getItem1Id()).orElse(null));
		build.get().setItem2(itemsRepository.findById(build.get().getItem2Id()).orElse(null));
		build.get().setItem3(itemsRepository.findById(build.get().getItem3Id()).orElse(null));
		build.get().setItem4(itemsRepository.findById(build.get().getItem4Id()).orElse(null));
		build.get().setItem5(itemsRepository.findById(build.get().getItem5Id()).orElse(null));
		build.get().setItem6(itemsRepository.findById(build.get().getItem6Id()).orElse(null));
		build.get().setSummonerSpell1(summonerSpellsRepository.findById(build.get().getSummonerSpell1Id()).orElse(null));
		build.get().setSummonerSpell2(summonerSpellsRepository.findById(build.get().getSummonerSpell2Id()).orElse(null));
		build.get().setTrinket(itemsRepository.findById(build.get().getTrinketId()).orElse(null));
		build.get().setMap(mapsRepository.findById(build.get().getMapId()).orElse(null));

		BindingResult result = new BeanPropertyBindingResult(build.get(), "build");
		build.get().validate(build.get(), result);
		if (result.hasErrors()) {
			return "builds/invalidAttributes";
		}

		model.addAttribute(build.get());
		return "builds/build";
	}

}
