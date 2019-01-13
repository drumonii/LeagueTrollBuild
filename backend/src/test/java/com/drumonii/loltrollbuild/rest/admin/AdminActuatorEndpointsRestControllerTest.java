package com.drumonii.loltrollbuild.rest.admin;

import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles({ TESTING, DDRAGON })
public class AdminActuatorEndpointsRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${api.base-path}")
    private String apiPath;

    @WithMockAdminUser
    @Test
    public void systemCpuUsageEndpoint() throws Exception {
        mockMvc.perform(get("{apiPath}/admin/actuator/metrics/system.cpu.usage", apiPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.measurements.length()", is(1)))
                .andExpect(jsonPath("$.measurements[0].statistic", is("VALUE")))
                .andExpect(jsonPath("$.measurements[0].value").isNumber());
    }

    @WithMockAdminUser
    @Test
    public void systemCpuCountEndpoint() throws Exception {
        mockMvc.perform(get("{apiPath}/admin/actuator/metrics/system.cpu.count", apiPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.measurements.length()", is(1)))
                .andExpect(jsonPath("$.measurements[0].statistic", is("VALUE")))
                .andExpect(jsonPath("$.measurements[0].value").isNumber());
    }

    @WithMockAdminUser
    @Test
    public void tomcatGlobalErrorEndpoint() throws Exception {
        mockMvc.perform(get("{apiPath}/admin/actuator/metrics/tomcat.global.error", apiPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.measurements.length()", is(1)))
                .andExpect(jsonPath("$.measurements[0].statistic", is("COUNT")))
                .andExpect(jsonPath("$.measurements[0].value").isNumber());
    }

    @WithMockAdminUser
    @Test
    public void jvmMemoryUsedEndpoint() throws Exception {
        mockMvc.perform(get("{apiPath}/admin/actuator/metrics/jvm.memory.used", apiPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.measurements.length()", is(1)))
                .andExpect(jsonPath("$.measurements[0].statistic", is("VALUE")))
                .andExpect(jsonPath("$.measurements[0].value").isNumber());
    }

}
