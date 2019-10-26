package com.drumonii.loltrollbuild.routing;

import com.drumonii.loltrollbuild.model.Build;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for {@link Build}s to forward requests to Angular so it can resolve the route.
 */
@Controller
@RequestMapping("/builds")
public class BuildsController {

	@GetMapping
	public String builds() {
		return "forward:/troll-build/index.html";
	}

	@GetMapping("/{id}")
	public String build(@PathVariable int id) {
		return "forward:/troll-build/index.html";
	}

}
