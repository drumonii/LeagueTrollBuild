package com.drumonii.loltrollbuild.admin;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for the login page to the administration area. Will redirect to admin's home if the admin user is already
 * authenticated.
 */
@Controller
@RequestMapping("/admin/login")
public class AdminLoginController {

	@GetMapping
	public String getLogin(@AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails == null) {
			return "admin/login";
		}
		return "redirect:/admin";
	}

}
