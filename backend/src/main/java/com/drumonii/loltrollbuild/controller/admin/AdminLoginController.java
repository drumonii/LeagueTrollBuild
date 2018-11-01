package com.drumonii.loltrollbuild.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for admin login to forward requests to Angular so it can resolve the route.
 */
@Controller
@RequestMapping("/admin/login")
public class AdminLoginController {

    @GetMapping
    public String adminLogin() {
        return "forward:/index.html";
    }

}
