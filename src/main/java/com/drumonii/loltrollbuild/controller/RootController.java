package com.drumonii.loltrollbuild.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for the root URL "/".
 */
@Controller
@RequestMapping("/")
public class RootController {

	@RequestMapping(method = RequestMethod.GET)
	public String redirectToChampions(Model model) {
		return "redirect:/champions";
	}

}
