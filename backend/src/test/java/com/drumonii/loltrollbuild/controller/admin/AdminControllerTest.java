package com.drumonii.loltrollbuild.controller.admin;

import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import com.drumonii.loltrollbuild.config.WebSecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

@RunWith(SpringRunner.class)
@WebMvcTest(AdminController.class)
@Import(WebSecurityConfig.class)
@ActiveProfiles({ TESTING })
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @WithMockAdminUser
    @Test
    public void adminLogin() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(forwardedUrl("/index.html"));
    }

}