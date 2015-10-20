package com.drumonii.loltrollbuild.admin;

import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import com.drumonii.loltrollbuild.riot.ChampionsRetrieval;
import com.drumonii.loltrollbuild.riot.ItemsRetrieval;
import com.drumonii.loltrollbuild.riot.SummonerSpellsRetrieval;
import com.drumonii.loltrollbuild.riot.VersionsRetrieval;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@ModelAttribute("latestSavedPatch")
	public String latestSavedPatch() {
		return versionsRepository.latestPatch();
	}

	@Autowired
	private VersionsRepository versionsRepository;

	@Autowired
	private VersionsRetrieval versionsRetrieval;

	@RequestMapping(method = RequestMethod.GET)
	public String admin(Model model) {
		model.addAttribute("latestRiotPatch", versionsRetrieval.latestPatch(versionsRetrieval.versionsFromResponse()));
		return "admin/admin";
	}

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	private SummonerSpellsRetrieval summonerSpellsRetrieval;

	@RequestMapping(value = "/summoner-spells", method = RequestMethod.GET)
	public String summonerSpells(Model model) {
		model.addAttribute("difference", CollectionUtils.subtract(summonerSpellsRetrieval.summonerSpells(),
				summonerSpellsRepository.findAll()));
		return "admin/summonerSpells/summonerSpells";
	}

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private ItemsRetrieval itemsRetrieval;

	@RequestMapping(value = "/items", method = RequestMethod.GET)
	public String items(Model model) {
		model.addAttribute("difference", CollectionUtils.subtract(itemsRetrieval.items(), itemsRepository.findAll()));
		return "admin/items/items";
	}

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	private ChampionsRetrieval championsRetrieval;

	@RequestMapping(value = "/champions", method = RequestMethod.GET)
	public String champions(Model model) {
		model.addAttribute("difference", CollectionUtils.subtract(championsRetrieval.champions(),
				championsRepository.findAll()));
		return "admin/champions/champions";
	}

}
