package com.drumonii.loltrollbuild.rest.admin;

import com.drumonii.loltrollbuild.rest.status.BadRequestException;
import com.drumonii.loltrollbuild.security.AdminUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for administration authentication.
 */
@RestController
@RequestMapping("${api.base-path}/admin")
public class AdminAuthenticationRestController {

    /**
     * Gets the current authentication principal - {@link AdminUserDetails}.
     *
     * @param userDetails the {@link User}
     * @return the {@link AdminUserDetails} if authenticated, otherwise {@code null}
     */
    @GetMapping(path = "/authentication")
    public AdminUserDetails getAuthentication(@AuthenticationPrincipal User userDetails) {
        if (userDetails == null) {
            throw new BadRequestException("Authentication was not found");
        }
        return new AdminUserDetails(userDetails);
    }

}
