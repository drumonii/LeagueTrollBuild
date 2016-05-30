package com.drumonii.loltrollbuild.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

	@RequestMapping(value = "/env", method = RequestMethod.GET)
	public String env(Model model) {
		model.addAttribute("accordion", "env");
		return "admin/actuator/env";
	}

	@RequestMapping(value = "/flyway", method = RequestMethod.GET)
	public String flyway(Model model) {
		model.addAttribute("accordion", "flyway");
		return "admin/actuator/flyway";
	}

}
