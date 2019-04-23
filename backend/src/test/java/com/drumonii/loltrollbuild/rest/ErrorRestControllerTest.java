package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.test.rest.AbstractRestTests;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.JsonPathExpectationsHelper;

import java.net.URI;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

public class ErrorRestControllerTest extends AbstractRestTests {

    @Test
    public void getsErrorForPermitAllRequest() {
        RequestEntity<Void> requestEntity = RequestEntity.get(URI.create(createUrl("/not-found")))
                .build();

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(requestEntity, String.class);

        assertThat(responseEntity.getBody()).satisfies(new ErrorNotFoundJson("/api/not-found"));
    }

    @Test
    public void getsErrorForSecureRequest() {
        RequestEntity<Void> requestEntity = RequestEntity.get(URI.create(createUrl("/admin/job-instances")))
                .build();

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(requestEntity, String.class);

        assertThat(responseEntity.getBody()).satisfies(new ErrorUnauthorizedJson("/api/admin/job-instances"));
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

        protected <T> void pathHasValue(String expression, String json, Matcher<T> matcher) {
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
