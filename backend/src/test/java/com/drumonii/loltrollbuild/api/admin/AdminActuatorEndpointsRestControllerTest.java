package com.drumonii.loltrollbuild.api.admin;

import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import com.drumonii.loltrollbuild.test.api.AbstractRestControllerTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminActuatorEndpointsRestControllerTest extends AbstractRestControllerTests {

    @Nested
    @DisplayName("/env")
    class Env {

        @WithMockAdminUser
        @Test
        void systemPropertiesEndpoint() throws Exception {
            mockMvc.perform(get("{apiPath}/admin/actuator/env", apiPath))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.propertySources[?(@.name == 'systemProperties')].properties").exists())
                    .andExpect(jsonPath("$.propertySources[?(@.name == 'systemProperties')].properties['java.vm.name'].value").exists())
                    .andExpect(jsonPath("$.propertySources[?(@.name == 'systemProperties')].properties['java.version'].value").exists());
        }

    }

    @Nested
    @DisplayName("/flyway")
    class Flyway {

        @WithMockAdminUser
        @Test
        void flywayEndpoint() throws Exception {
            mockMvc.perform(get("{apiPath}/admin/actuator/flyway", apiPath))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.contexts.application.flywayBeans.flyway.migrations").isArray());
        }

    }

    @Nested
    @DisplayName("/health")
    class Health {

        @WithMockAdminUser
        @Test
        void diskSpaceDetailsEndpoint() throws Exception {
            mockMvc.perform(get("{apiPath}/admin/actuator/health", apiPath))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.components.diskSpace.details").exists())
                    .andExpect(jsonPath("$.components.diskSpace.details.total").isNumber())
                    .andExpect(jsonPath("$.components.diskSpace.details.free").isNumber());
        }

    }

    @Nested
    @DisplayName("/metrics")
    class Metrics {

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
        void jvmMemoryUsedEndpoint() throws Exception {
            mockMvc.perform(get("{apiPath}/admin/actuator/metrics/jvm.memory.used", apiPath))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.measurements.length()", is(1)))
                    .andExpect(jsonPath("$.measurements[0].statistic", is("VALUE")))
                    .andExpect(jsonPath("$.measurements[0].value").isNumber());
        }

        @WithMockAdminUser
        @Test
        void jvmMemoryMaxEndpoint() throws Exception {
            mockMvc.perform(get("{apiPath}/admin/actuator/metrics/jvm.memory.max", apiPath))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.measurements.length()", is(1)))
                    .andExpect(jsonPath("$.measurements[0].statistic", is("VALUE")))
                    .andExpect(jsonPath("$.measurements[0].value").isNumber());
        }

    }

}
