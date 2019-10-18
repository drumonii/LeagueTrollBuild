package com.drumonii.loltrollbuild.api.admin;

import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import com.drumonii.loltrollbuild.test.api.AbstractRestControllerTests;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminActuatorEndpointsRestControllerTest extends AbstractRestControllerTests {

    @WithMockAdminUser
    @Test
    void systemCpuUsageEndpoint() throws Exception {
        mockMvc.perform(get("{apiPath}/admin/actuator/metrics/system.cpu.usage", apiPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.measurements.length()", is(1)))
                .andExpect(jsonPath("$.measurements[0].statistic", is("VALUE")))
                .andExpect(jsonPath("$.measurements[0].value").isNumber());
    }

    @WithMockAdminUser
    @Test
    void systemCpuCountEndpoint() throws Exception {
        mockMvc.perform(get("{apiPath}/admin/actuator/metrics/system.cpu.count", apiPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.measurements.length()", is(1)))
                .andExpect(jsonPath("$.measurements[0].statistic", is("VALUE")))
                .andExpect(jsonPath("$.measurements[0].value").isNumber());
    }

    @WithMockAdminUser
    @Test
    void tomcatGlobalErrorEndpoint() throws Exception {
        mockMvc.perform(get("{apiPath}/admin/actuator/metrics/tomcat.global.error", apiPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.measurements.length()", is(1)))
                .andExpect(jsonPath("$.measurements[0].statistic", is("COUNT")))
                .andExpect(jsonPath("$.measurements[0].value").isNumber());
    }

    @WithMockAdminUser
    @Test
    void jvmMemoryUsedEndpoint() throws Exception {
        mockMvc.perform(get("{apiPath}/admin/actuator/metrics/jvm.memory.used", apiPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.measurements.length()", is(1)))
                .andExpect(jsonPath("$.measurements[0].statistic", is("VALUE")))
                .andExpect(jsonPath("$.measurements[0].value").isNumber());
    }

}
