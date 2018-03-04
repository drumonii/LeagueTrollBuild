package com.drumonii.loltrollbuild.controller;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

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
	private MapsRepository mapsRepository;

	@GetMapping
	public String champions(Model model) {
		model.addAttribute("champions", championsRepository.findAll(new Sort(ASC, "name")));
		model.addAttribute("tags", championsRepository.getTags());
		return "champions/champions";
	}

	@GetMapping(path = "/{value}")
	public String champion(@PathVariable String value, Model model) {
		Optional<Champion> champion;
		try {
			champion = championsRepository.findById(Integer.valueOf(value));
		} catch (NumberFormatException e) {
			champion = championsRepository.findByName(value);
		}
		if (!champion.isPresent()) {
			return "redirect:/champions";
		}
		model.addAttribute(champion.get());
		model.addAttribute("maps", mapsRepository.forTrollBuild());
		return "champions/champion";
	}

}
