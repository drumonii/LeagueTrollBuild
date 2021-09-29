package com.drumonii.loltrollbuild.test.api.service;

import com.drumonii.loltrollbuild.riot.RiotApiConfig;
import com.drumonii.loltrollbuild.riot.RiotApiProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;
import java.util.function.Consumer;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;

@Import(RiotApiConfig.class)
@EnableConfigurationProperties(RiotApiProperties.class)
@ActiveProfiles({ TESTING })
public abstract class AbstractDdragonServiceTests {

    protected static MockWebServer mockWebServer;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("riot.ddragon.base-url", () -> String.format("http://localhost:%s", mockWebServer.getPort()));
    }

    @BeforeAll
    static void beforeAll() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockWebServer.shutdown();
    }

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected RiotApiProperties riotApiProperties;

    public class RiotApiRecordedRequest implements Consumer<RecordedRequest> {

        @Override
        public void accept(RecordedRequest recordedRequest) {
            assertThat(recordedRequest).isNotNull();
            assertThat(recordedRequest.getHeaders().get(HttpHeaders.ACCEPT)).as("Accept HTTP header")
                    .isNotNull()
                    .isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        }

    }

}
