package com.drumonii.loltrollbuild.routing.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for admin to forward requests to Angular so it can resolve the route.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String admin() {
        return "forward:/admin/index.html";
    }

    @GetMapping("/batch")
    public String batch() {
        return "forward:/admin/index.html";
    }

    @GetMapping("/flyway")
    public String flyway() {
        return "forward:/admin/index.html";
    }

}
