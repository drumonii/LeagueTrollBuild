package com.drumonii.loltrollbuild.routing.admin;

import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

@WebMvcTest(AdminController.class)
@ActiveProfiles({ TESTING })
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @WithMockAdminUser
    @Test
    void adminLogin() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(forwardedUrl("/admin/index.html"));
    }

    @WithMockAdminUser
    @Test
    void adminBatch() throws Exception {
        mockMvc.perform(get("/admin/batch"))
                .andExpect(forwardedUrl("/admin/index.html"));
    }

    @WithMockAdminUser
    @Test
    void adminFlyway() throws Exception {
        mockMvc.perform(get("/admin/flyway"))
                .andExpect(forwardedUrl("/admin/index.html"));
    }

}
