package com.drumonii.loltrollbuild.admin;

import com.drumonii.loltrollbuild.model.*;
import com.drumonii.loltrollbuild.repository.*;
import com.drumonii.loltrollbuild.riot.service.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;

/**
 * Controller for the administration area only used and authorized by the admin user.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private VersionsService versionsService;

	@ModelAttribute("latestRiotPatch")
	public String latestRiotPatch() {
		Version latestVersion = versionsService.getLatestVersion();
		return latestVersion == null ? null : latestVersion.getPatch();
	}

	@GetMapping
	public String admin(Model model) {
		model.addAttribute("activeTab", "home");
		return "admin/admin";
	}

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	private SummonerSpellsService summonerSpellsService;

	@GetMapping(value = "/summoner-spells")
	public String summonerSpells(@ModelAttribute("latestSavedPatch") String latestSavedPatch,
			RedirectAttributes redirectAttrs, Model model) {
		if (latestSavedPatch == null) {
			redirectAttrs.addFlashAttribute("noSavedPatch", "Summoner Spells");
			return "redirect:/admin";
		}
		model.addAttribute("activeTab", "summonerSpells");
		return "admin/summonerSpells/summonerSpells";
	}

	@GetMapping(value = "/summoner-spells/diff")
	@ResponseBody
	public Collection<SummonerSpell> summonerSpellsDifference() {
		return CollectionUtils.subtract(summonerSpellsService.getSummonerSpells(), summonerSpellsRepository.findAll());
	}

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private ItemsService itemsService;

	@GetMapping(value = "/items")
	public String items(@ModelAttribute("latestSavedPatch") String latestSavedPatch,
			RedirectAttributes redirectAttrs, Model model) {
		if (latestSavedPatch == null) {
			redirectAttrs.addFlashAttribute("noSavedPatch", "Items");
			return "redirect:/admin";
		}
		model.addAttribute("activeTab", "items");
		return "admin/items/items";
	}

	@GetMapping(value = "/items/diff")
	@ResponseBody
	public Collection<Item> itemsDifference() {
		return CollectionUtils.subtract(itemsService.getItems(), itemsRepository.findAll());
	}

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	private ChampionsService championsService;

	@GetMapping(value = "/champions")
	public String champions(@ModelAttribute("latestSavedPatch") String latestSavedPatch,
			RedirectAttributes redirectAttrs, Model model) {
		if (latestSavedPatch == null) {
			redirectAttrs.addFlashAttribute("noSavedPatch", "Champions");
			return "redirect:/admin";
		}
		model.addAttribute("activeTab", "champions");
		return "admin/champions/champions";
	}

	@GetMapping(value = "/champions/diff")
	@ResponseBody
	public Collection<Champion> championsDifference() {
		return CollectionUtils.subtract(championsService.getChampions(), championsRepository.findAll());
	}

	@Autowired
	private MapsRepository mapsRepository;

	@Autowired
	private MapsService mapsService;

	@GetMapping(value = "/maps")
	public String maps(@ModelAttribute("latestSavedPatch") String latestSavedPatch,
			RedirectAttributes redirectAttrs, Model model) {
		if (latestSavedPatch == null) {
			redirectAttrs.addFlashAttribute("noSavedPatch", "Maps");
			return "redirect:/admin";
		}
		model.addAttribute("activeTab", "maps");
		return "admin/maps/maps";
	}

	@GetMapping(value = "/maps/diff")
	@ResponseBody
	public Collection<GameMap> mapsDifference() {
		return CollectionUtils.subtract(mapsService.getMaps(), mapsRepository.findAll());
	}

	@GetMapping(value = "/job-instances")
	public String jobInstances(Model model) {
		model.addAttribute("activeTab", "jobInstances");
		return "admin/jobs/jobInstances";
	}

	@Autowired
	private BatchJobInstancesRepository batchJobInstancesRepository;

	@GetMapping(value = "/job-instances/{jobInstanceId}/step-executions")
	public String stepExecutions(@PathVariable long jobInstanceId, Model model) {
		model.addAttribute("jobInstance", batchJobInstancesRepository.findById(jobInstanceId).get());
		model.addAttribute("activeTab", "jobInstances");
		return "admin/jobs/stepExecutions";
	}

}
