package com.drumonii.loltrollbuild.rest.admin;

import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import com.drumonii.loltrollbuild.config.WebSecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AdminAuthenticationRestController.class)
@Import(WebSecurityConfig.class)
@ActiveProfiles({ TESTING })
public class AdminAuthenticationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${api.base-path}")
    private String apiPath;

    @WithMockAdminUser
    @Test
    public void getsAdminUserDetailsFromAuthentication() throws Exception {
        mockMvc.perform(get("{apiPath}/admin/authentication", apiPath))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.authorities").isNotEmpty())
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.enabled").doesNotExist())
                .andExpect(jsonPath("$.accountNonExpired").doesNotExist())
                .andExpect(jsonPath("$.accountNonLocked").doesNotExist())
                .andExpect(jsonPath("$.credentialsNonExpired").doesNotExist());
    }

    @Test
    public void getsNoAdminUserDetailsFromNoAuthentication() throws Exception {
        mockMvc.perform(get("{apiPath}/admin/authentication", apiPath))
                .andExpect(status().isBadRequest());
    }

}