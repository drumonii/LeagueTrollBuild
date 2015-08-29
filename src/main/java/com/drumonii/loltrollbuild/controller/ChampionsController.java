package com.drumonii.loltrollbuild.controller;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for {@link Champion}s and their views.
 */
@Controller
@RequestMapping("/champions")
public class ChampionsController {

	@Autowired
	private ChampionsRepository championsRepository;

	@RequestMapping(method = RequestMethod.GET)
	public String champions(Model model) {
		model.addAttribute("champions", championsRepository.findAll());
		return "champions/champions";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String champion(@PathVariable("id") Champion champion, Model model) {
		if (champion == null) {
			return "redirect:/champions";
		}
		model.addAttribute(champion);
		return "champions/champion";
	}

}
