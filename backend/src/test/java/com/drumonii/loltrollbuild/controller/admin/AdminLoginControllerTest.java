package com.drumonii.loltrollbuild.controller.admin;

import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import com.drumonii.loltrollbuild.config.WebSecurityConfig.UserRole;
import com.drumonii.loltrollbuild.security.login.LoginResponse.LoginStatus;
import com.drumonii.loltrollbuild.security.logout.LogoutResponse.LogoutStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static com.drumonii.loltrollbuild.config.WebSecurityConfig.WebDevTestingSecurityConfig.IN_MEM_PASSWORD;
import static com.drumonii.loltrollbuild.config.WebSecurityConfig.WebDevTestingSecurityConfig.IN_MEM_USERNAME;
import static org.hamcrest.core.Is.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AdminLoginController.class)
@ActiveProfiles({ TESTING })
public class AdminLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${api.base-path}")
    private String apiPath;

    @WithAnonymousUser
    @Test
    public void adminLogin() throws Exception {
        mockMvc.perform(get("/admin/login"))
                .andExpect(forwardedUrl("/admin/index.html"));
    }

    @Test
    public void adminLoginWithValidCredentials() throws Exception {
        mockMvc.perform(formLogin(apiPath + "/admin/login")
                .user(IN_MEM_USERNAME)
                .password(IN_MEM_PASSWORD))
                .andExpect(authenticated()
                        .withAuthenticationName("admin")
                        .withRoles(UserRole.ADMIN))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(LoginStatus.SUCCESS.name())))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.userDetails").exists())
                .andExpect(jsonPath("$.userDetails.username").exists())
                .andExpect(jsonPath("$.userDetails.authorities").isArray())
                .andExpect(jsonPath("$.userDetails.authorities[0].authority", is("ROLE_" + UserRole.ADMIN)))
                .andExpect(jsonPath("$.userDetails.password").doesNotExist())
                .andExpect(jsonPath("$.userDetails.enabled").doesNotExist())
                .andExpect(jsonPath("$.userDetails.accountNonExpired").doesNotExist())
                .andExpect(jsonPath("$.userDetails.accountNonLocked").doesNotExist())
                .andExpect(jsonPath("$.userDetails.credentialsNonExpired").doesNotExist());
    }

    @Test
    public void adminLoginWithInvalidCredentials() throws Exception {
        mockMvc.perform(formLogin(apiPath + "/admin/login")
                .user("bad_username")
                .password("bad_password"))
                .andExpect(unauthenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(LoginStatus.FAILED.name())))
                .andExpect(jsonPath("$.message").exists());
    }

    @WithMockAdminUser
    @Test
    public void adminLogout() throws Exception {
        mockMvc.perform(post("{baseUrl}/admin/logout", apiPath).with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(unauthenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(LogoutStatus.SUCCESS.name())))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.userDetails").exists())
                .andExpect(jsonPath("$.userDetails.username").exists())
                .andExpect(jsonPath("$.userDetails.authorities").isArray())
                .andExpect(jsonPath("$.userDetails.authorities[0].authority", is("ROLE_" + UserRole.ADMIN)))
                .andExpect(jsonPath("$.userDetails.password").doesNotExist())
                .andExpect(jsonPath("$.userDetails.enabled").doesNotExist())
                .andExpect(jsonPath("$.userDetails.accountNonExpired").doesNotExist())
                .andExpect(jsonPath("$.userDetails.accountNonLocked").doesNotExist())
                .andExpect(jsonPath("$.userDetails.credentialsNonExpired").doesNotExist());
    }

}
