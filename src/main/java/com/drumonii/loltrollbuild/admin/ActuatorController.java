package com.drumonii.loltrollbuild.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for the actuator section of the administration area only used and authorized by the admin user.
 */
@Controller
@RequestMapping("/admin")
public class ActuatorController {

	@ModelAttribute("activeTab")
	public String activeTab() {
		return "actuator";
	}

	@GetMapping(value = "/env")
	public String env(Model model) {
		model.addAttribute("accordion", "env");
		return "admin/actuator/env";
	}

	@GetMapping(value = "/flyway")
	public String flyway(Model model) {
		model.addAttribute("accordion", "flyway");
		return "admin/actuator/flyway";
	}

	@GetMapping(value = "/health")
	public String health(Model model) {
		model.addAttribute("accordion", "health");
		return "admin/actuator/health";
	}

	@GetMapping(value = "/metrics")
	public String metrics(Model model) {
		model.addAttribute("accordion", "metrics");
		return "admin/actuator/metrics";
	}

}
