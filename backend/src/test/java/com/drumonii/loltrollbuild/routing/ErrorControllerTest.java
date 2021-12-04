package com.drumonii.loltrollbuild.routing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles({ TESTING })
class ErrorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void forwardsToAdminIndexHtml() throws Exception {
        String path = "/admin/not-found";

        MvcResult result = mockMvc.perform(get(path).accept(MediaType.TEXT_HTML))
                .andExpect(status().isNotFound())
                .andReturn();

        mockMvc.perform(new ErrorDispatcher(result, "/error", path))
                .andExpect(forwardedUrl("/admin/index.html"));
    }

    @Test
    void forwardsToTrollBuildIndexHtml() throws Exception {
        String path = "/not-found";

        MvcResult result = mockMvc.perform(get(path).accept(MediaType.TEXT_HTML))
                .andExpect(status().isNotFound())
                .andReturn();

        mockMvc.perform(new ErrorDispatcher(result, "/error", path))
                .andExpect(forwardedUrl("/troll-build/index.html"));
    }

    private static class ErrorDispatcher implements RequestBuilder {

        private MvcResult result;
        private String errorPath;
        private String requestPath;

        ErrorDispatcher(MvcResult result, String errorPath, String requestPath) {
            this.result = result;
            this.errorPath = errorPath;
            this.requestPath = requestPath;
        }

        @Override
        public MockHttpServletRequest buildRequest(ServletContext servletContext) {
            MockHttpServletRequest request = result.getRequest();
            request.setDispatcherType(DispatcherType.ERROR);
            request.setRequestURI(errorPath);
            request.setAttribute("javax.servlet.error.request_uri", requestPath);
            return request;
        }

    }

}
