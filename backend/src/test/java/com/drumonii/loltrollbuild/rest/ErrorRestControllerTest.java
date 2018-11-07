package com.drumonii.loltrollbuild.rest;

import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.function.Consumer;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) // needed because ErrorMvcAutoConfiguration requires full servlet environment
@ActiveProfiles({ TESTING, DDRAGON })
public class ErrorRestControllerTest {

    @LocalServerPort
    private int port;

    @Test
    public void getsError() {
        RequestEntity<Void> requestEntity = RequestEntity.get(URI.create(createUrl("/api/not-found")))
                .build();

        ResponseEntity<String> responseEntity = new TestRestTemplate().exchange(requestEntity, String.class);

        assertThat(responseEntity.getBody()).satisfies(new ErrorJson());
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
            JsonPath jsonPath = JsonPath.compile(expression);
            try {
                jsonPath.read(json);
            } catch (Exception e) {
                fail("No value at JSON path \"" + expression + "\"", e);
            }
        }

    }

}