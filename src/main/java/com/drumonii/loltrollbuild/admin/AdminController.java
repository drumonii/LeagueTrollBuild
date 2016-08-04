package com.drumonii.loltrollbuild.admin;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.*;
import com.drumonii.loltrollbuild.riot.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for the administration area only used and authorized by the admin user.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

	@ModelAttribute("latestSavedPatch")
	public String latestSavedPatch() {
		Version version = versionsRepository.latestVersion();
		return version == null ? null : version.getPatch();
	}

	@Autowired
	private VersionsRepository versionsRepository;

	@Autowired
	private VersionsRetrieval versionsRetrieval;

	@GetMapping
	public String admin(Model model) {
		model.addAttribute("latestRiotPatch", versionsRetrieval.latestVersion(versionsRetrieval.versionsFromResponse())
				.getPatch());
		model.addAttribute("activeTab", "home");
		return "admin/admin";
	}

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	private SummonerSpellsRetrieval summonerSpellsRetrieval;

	@GetMapping(value = "/summoner-spells")
	public String summonerSpells(@ModelAttribute("latestSavedPatch") String latestSavedPatch,
			RedirectAttributes redirectAttrs, Model model) {
		if (latestSavedPatch == null) {
			redirectAttrs.addFlashAttribute("noSavedPatch", "Summoner Spells");
			return "redirect:/admin";
		}
		model.addAttribute("difference", CollectionUtils.subtract(summonerSpellsRetrieval.summonerSpells(),
				summonerSpellsRepository.findAll()));
		model.addAttribute("activeTab", "summonerSpells");
		return "admin/summonerSpells/summonerSpells";
	}

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private ItemsRetrieval itemsRetrieval;

	@GetMapping(value = "/items")
	public String items(@ModelAttribute("latestSavedPatch") String latestSavedPatch,
			RedirectAttributes redirectAttrs, Model model) {
		if (latestSavedPatch == null) {
			redirectAttrs.addFlashAttribute("noSavedPatch", "Items");
			return "redirect:/admin";
		}
		model.addAttribute("difference", CollectionUtils.subtract(itemsRetrieval.items(), itemsRepository.findAll()));
		model.addAttribute("activeTab", "items");
		return "admin/items/items";
	}

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	private ChampionsRetrieval championsRetrieval;

	@GetMapping(value = "/champions")
	public String champions(@ModelAttribute("latestSavedPatch") String latestSavedPatch,
			RedirectAttributes redirectAttrs, Model model) {
		if (latestSavedPatch == null) {
			redirectAttrs.addFlashAttribute("noSavedPatch", "Champions");
			return "redirect:/admin";
		}
		model.addAttribute("difference", CollectionUtils.subtract(championsRetrieval.champions(),
				championsRepository.findAll()));
		model.addAttribute("activeTab", "champions");
		return "admin/champions/champions";
	}

	@Autowired
	private MapsRepository mapsRepository;

	@Autowired
	private MapsRetrieval mapsRetrieval;

	@GetMapping(value = "/maps")
	public String maps(@ModelAttribute("latestSavedPatch") String latestSavedPatch,
			RedirectAttributes redirectAttrs, Model model) {
		if (latestSavedPatch == null) {
			redirectAttrs.addFlashAttribute("noSavedPatch", "Maps");
			return "redirect:/admin";
		}
		model.addAttribute("difference", CollectionUtils.subtract(mapsRetrieval.maps(), mapsRepository.findAll()));
		model.addAttribute("activeTab", "maps");
		return "admin/maps/maps";
	}

}
