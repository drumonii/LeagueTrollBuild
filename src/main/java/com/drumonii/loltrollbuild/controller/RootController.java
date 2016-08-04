package com.drumonii.loltrollbuild.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for the root URL "/".
 */
@Controller
@RequestMapping("/")
public class RootController {

	@GetMapping
	public String redirectToChampions(Model model) {
		return "redirect:/champions";
	}

}
