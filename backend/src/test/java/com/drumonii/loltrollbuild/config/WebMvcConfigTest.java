package com.drumonii.loltrollbuild.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.RequestPath;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.util.ServletRequestPathUtils;

import java.util.Map;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles({ TESTING })
class WebMvcConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Nested
    @DisplayName("admin static resources")
    class admin {

        @Test
        void indexHtmlHasNoStoreCacheControl() throws Exception {
            SimpleUrlHandlerMapping handlerMapping = getSimpleUrlHandlerMapping();
            Map<String, Object> handlerMap = handlerMapping.getHandlerMap();
            assertThat(handlerMap).hasSize(2);

            ResourceHttpRequestHandler resourceHandler = (ResourceHttpRequestHandler) handlerMap.get("/*/index.html");

            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setMethod(HttpMethod.GET.name());
            request.setRequestURI("/admin/index.html");
            request.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, "admin/index.html");
            request.setAttribute(ServletRequestPathUtils.PATH_ATTRIBUTE, RequestPath.parse("/admin/index.html", ""));

            MockHttpServletResponse response = new MockHttpServletResponse();

            HandlerExecutionChain chain = handlerMapping.getHandler(request);
            assertThat(chain.getHandler()).isEqualTo(resourceHandler);

            resourceHandler.handleRequest(request, response);

            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.getContentType()).isEqualTo(MediaType.TEXT_HTML_VALUE);
            assertThat(response.getHeaderNames()).contains("Cache-Control");
            assertThat(response.getHeader("Cache-Control")).isEqualTo("no-store");
        }

        @CsvSource({
                "main.85b446f791ee3b18e1c5.js, application/javascript",
                "styles.26c46902ddc723992338.css, text/css"
        })
        @ParameterizedTest(name = "resource=''{0}'', contentType=''{1}''")
        void staticResourceHasMaxAgeCacheControl(String resource, String contentType) throws Exception {
            SimpleUrlHandlerMapping handlerMapping = getSimpleUrlHandlerMapping();
            Map<String, Object> handlerMap = handlerMapping.getHandlerMap();
            assertThat(handlerMap).hasSize(2);

            ResourceHttpRequestHandler resourceHandler = (ResourceHttpRequestHandler) handlerMap.get("/**");

            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setMethod(HttpMethod.GET.name());
            request.setRequestURI("/admin/" + resource);
            request.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, "admin/" + resource);
            request.setAttribute(ServletRequestPathUtils.PATH_ATTRIBUTE, RequestPath.parse("/admin/" + resource, ""));

            MockHttpServletResponse response = new MockHttpServletResponse();

            HandlerExecutionChain chain = handlerMapping.getHandler(request);
            assertThat(chain.getHandler()).isEqualTo(resourceHandler);

            resourceHandler.handleRequest(request, response);

            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.getContentType()).isEqualTo(contentType);
            assertThat(response.getHeaderNames()).contains("Cache-Control");
            assertThat(response.getHeader("Cache-Control")).isEqualTo("max-age=31536000");
        }

    }

    @Nested
    @DisplayName("troll build static resources")
    class trollBuild {

        @Test
        void indexHtmlHasNoStoreCacheControl() throws Exception {
            SimpleUrlHandlerMapping handlerMapping = getSimpleUrlHandlerMapping();
            Map<String, Object> handlerMap = handlerMapping.getHandlerMap();
            assertThat(handlerMap).hasSize(2);

            ResourceHttpRequestHandler resourceHandler = (ResourceHttpRequestHandler) handlerMap.get("/*/index.html");

            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setMethod(HttpMethod.GET.name());
            request.setRequestURI("/troll-build/index.html");
            request.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, "troll-build/index.html");
            request.setAttribute(ServletRequestPathUtils.PATH_ATTRIBUTE, RequestPath.parse("/troll-build/index.html", ""));

            MockHttpServletResponse response = new MockHttpServletResponse();

            HandlerExecutionChain chain = handlerMapping.getHandler(request);
            assertThat(chain.getHandler()).isEqualTo(resourceHandler);

            resourceHandler.handleRequest(request, response);

            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.getContentType()).isEqualTo(MediaType.TEXT_HTML_VALUE);
            assertThat(response.getHeaderNames()).contains("Cache-Control");
            assertThat(response.getHeader("Cache-Control")).isEqualTo("no-store");
        }

        @CsvSource({
                "main.56796bffe1238c65623f.js, application/javascript",
                "styles.64f6947700cdf03522dd.css, text/css"
        })
        @ParameterizedTest(name = "resource=''{0}'', contentType=''{1}''")
        void staticResourceHasMaxAgeCacheControl(String resource, String contentType) throws Exception {
            SimpleUrlHandlerMapping handlerMapping = getSimpleUrlHandlerMapping();
            Map<String, Object> handlerMap = handlerMapping.getHandlerMap();
            assertThat(handlerMap).hasSize(2);

            ResourceHttpRequestHandler resourceHandler = (ResourceHttpRequestHandler) handlerMap.get("/**");

            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setMethod(HttpMethod.GET.name());
            request.setRequestURI("/troll-build/" + resource);
            request.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, "troll-build/" + resource);
            request.setAttribute(ServletRequestPathUtils.PATH_ATTRIBUTE, RequestPath.parse("/troll-build/" + resource, ""));

            MockHttpServletResponse response = new MockHttpServletResponse();

            HandlerExecutionChain chain = handlerMapping.getHandler(request);
            assertThat(chain.getHandler()).isEqualTo(resourceHandler);

            resourceHandler.handleRequest(request, response);

            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.getContentType()).isEqualTo(contentType);
            assertThat(response.getHeaderNames()).contains("Cache-Control");
            assertThat(response.getHeader("Cache-Control")).isEqualTo("max-age=31536000");
        }

    }

    private SimpleUrlHandlerMapping getSimpleUrlHandlerMapping() {
        return applicationContext.getBean(SimpleUrlHandlerMapping.class);
    }

}
