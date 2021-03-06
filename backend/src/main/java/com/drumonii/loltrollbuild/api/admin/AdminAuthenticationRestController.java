package com.drumonii.loltrollbuild.api.admin;

import com.drumonii.loltrollbuild.api.status.BadRequestException;
import com.drumonii.loltrollbuild.security.AdminUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * REST controller for administration authentication.
 */
@RestController
@RequestMapping("/admin")
public class AdminAuthenticationRestController {

    /**
     * Gets the current authentication principal - {@link AdminUserDetails}.
     *
     * @param userDetails the {@link User}
     * @return the {@link AdminUserDetails} if authenticated, otherwise {@code null}
     */
    @GetMapping("/authentication")
    public AdminUserDetails getAuthentication(@AuthenticationPrincipal User userDetails) {
        return Optional.ofNullable(userDetails)
                .map(AdminUserDetails::from)
                .orElseThrow(() -> new BadRequestException("Authentication was not found"));
    }

}
