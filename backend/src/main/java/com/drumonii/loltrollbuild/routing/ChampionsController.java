package com.drumonii.loltrollbuild.routing;

import com.drumonii.loltrollbuild.model.Champion;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for {@link Champion}s to forward requests to Angular so it can resolve the route.
 */
@Controller
@RequestMapping("/champions")
public class ChampionsController {

	@GetMapping
	public String champions() {
		return "forward:/troll-build/index.html";
	}

	@GetMapping(path = "/{value}")
	public String champion(@PathVariable String value) {
		return "forward:/troll-build/index.html";
	}

}
