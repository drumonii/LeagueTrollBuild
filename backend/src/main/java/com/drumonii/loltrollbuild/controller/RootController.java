package com.drumonii.loltrollbuild.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for root route {@code /} to forward requests to Angular so it can resolve the route.
 *
 * This was handled by the WelcomePageHandlerMapping but since there are multiple index.html from multiple Angular
 * project apps, the autoconfiguration for {@code forward:index.html} is no longer is triggered.
 */
@Controller
@RequestMapping("/")
public class RootController {

    @GetMapping
    public String getRoot() {
        return "forward:/troll-build/index.html";
    }

}
