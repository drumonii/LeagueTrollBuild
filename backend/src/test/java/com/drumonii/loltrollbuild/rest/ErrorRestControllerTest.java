package com.drumonii.loltrollbuild.rest;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.JsonPathExpectationsHelper;

import java.net.URI;
import java.util.function.Consumer;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) // needed because ErrorMvcAutoConfiguration requires full servlet environment
@ActiveProfiles({ TESTING, DDRAGON })
public class ErrorRestControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void getsErrorForPermitAllRequest() {
        RequestEntity<Void> requestEntity = RequestEntity.get(URI.create(createUrl("/api/not-found")))
                .build();

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(requestEntity, String.class);

        assertThat(responseEntity.getBody()).satisfies(new ErrorJson());
    }

    @Test
    public void getsErrorForSecureRequest() {
        RequestEntity<Void> requestEntity = RequestEntity.get(URI.create(createUrl("/api/admin/job-instances")))
                .build();

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(requestEntity, String.class);

        assertThat(responseEntity.getBody()).satisfies(new ErrorUnauthorizedJson("/api/admin/job-instances"));
    }

    private String createUrl(String path) {
        return "http://localhost:" + port + path;
    }

    private class ErrorJson implements Consumer<String> {

        @Override
        public void accept(String json) {
            pathExists("$.timestamp", json);
            pathExists("$.status", json);
            pathExists("$.error", json);
            pathExists("$.message", json);
            pathExists("$.path", json);
        }

        private void pathExists(String expression, String json) {
            new JsonPathExpectationsHelper(expression).hasJsonPath(json);
        }

    }

    private class ErrorUnauthorizedJson extends ErrorJson {

        private String path;

        ErrorUnauthorizedJson(String path) {
            this.path = path;
        }

        @Override
        public void accept(String json) {
            super.accept(json);

            pathHasValue("$.status", json, is(401));
            pathHasValue("$.error", json, is("Unauthorized"));
            pathHasValue("$.message", json, is("Access is denied"));
            pathHasValue("$.path", json, is(path));
        }

        private <T> void pathHasValue(String expression, String json, Matcher<T> matcher) {
            new JsonPathExpectationsHelper(expression).assertValue(json, matcher);
        }

    }

}
