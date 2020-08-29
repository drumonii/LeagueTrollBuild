package com.drumonii.loltrollbuild.api;

import com.drumonii.loltrollbuild.test.api.AbstractRestControllerTests;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.test.util.JsonPathExpectationsHelper;

import java.util.Collections;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

class ErrorRestControllerTest extends AbstractRestControllerTests {

    @Test
    void getsErrorForPermitAllRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = testRestTemplate.exchange("/api/not-found", HttpMethod.GET,
                httpEntity, String.class);

        assertThat(responseEntity)
                .extracting(re -> re.getHeaders().getContentType())
                .isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(responseEntity)
                .extracting(ResponseEntity::getBody)
                .satisfies(new ErrorNotFoundJson("/api/not-found"));
    }

    @Test
    void getsErrorForSecureRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = testRestTemplate.exchange("/api/admin/job-instances", HttpMethod.GET,
                httpEntity, String.class);

        assertThat(responseEntity)
                .extracting(re -> re.getHeaders().getContentType())
                .isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(responseEntity)
                .extracting(ResponseEntity::getBody)
                .satisfies(new ErrorUnauthorizedJson("/api/admin/job-instances"));
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

        <T> void pathHasValue(String expression, String json, Matcher<T> matcher) {
            new JsonPathExpectationsHelper(expression).assertValue(json, matcher);
        }

    }

    private class ErrorNotFoundJson extends ErrorJson {

        private String path;

        ErrorNotFoundJson(String path) {
            this.path = path;
        }

        @Override
        public void accept(String json) {
            super.accept(json);

            pathHasValue("$.status", json, is(404));
            pathHasValue("$.error", json, is("Not Found"));
            pathHasValue("$.message", json, is("No message available"));
            pathHasValue("$.path", json, is(path));
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

    }

}
