package com.drumonii.loltrollbuild.api.admin;

import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminAuthenticationRestController.class)
@ActiveProfiles({ TESTING })
class AdminAuthenticationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${api.base-path}")
    private String apiPath;

    @WithMockAdminUser
    @Test
    void getsAdminUserDetailsFromAuthentication() throws Exception {
        mockMvc.perform(get("{apiPath}/admin/authentication", apiPath))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.authorities").isNotEmpty())
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.enabled").doesNotExist())
                .andExpect(jsonPath("$.accountNonExpired").doesNotExist())
                .andExpect(jsonPath("$.accountNonLocked").doesNotExist())
                .andExpect(jsonPath("$.credentialsNonExpired").doesNotExist());
    }

    @Test
    void getsNoAdminUserDetailsFromNoAuthentication() throws Exception {
        mockMvc.perform(get("{apiPath}/admin/authentication", apiPath))
                .andExpect(status().isBadRequest());
    }

}
