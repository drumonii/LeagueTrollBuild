package com.drumonii.loltrollbuild.admin;

import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private ChampionsRepository championsRepository;

	@RequestMapping(method = RequestMethod.GET)
	public String admin(Model model) {
		return "admin/admin";
	}

	@RequestMapping(value = "/summoner-spells", method = RequestMethod.GET)
	public String summonerSpells(Model model) {
		model.addAttribute("spells", summonerSpellsRepository.findAll());
		return "admin/summonerSpells/summonerSpells";
	}

	@RequestMapping(value = "/items", method = RequestMethod.GET)
	public String items(Model model) {
		model.addAttribute("items", itemsRepository.findAll());
		return "admin/items/items";
	}

	@RequestMapping(value = "/champions", method = RequestMethod.GET)
	public String champions(Model model) {
		model.addAttribute("champions", championsRepository.findAll());
		return "admin/champions/champions";
	}

}
