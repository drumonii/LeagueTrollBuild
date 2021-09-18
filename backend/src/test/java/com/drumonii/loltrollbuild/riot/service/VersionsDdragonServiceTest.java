package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.config.RiotApiConfig;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.UriComponents;

import java.util.List;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(VersionsService.class)
@Import(RiotApiConfig.class)
@EnableConfigurationProperties(RiotApiProperties.class)
@ActiveProfiles({ TESTING })
class VersionsDdragonServiceTest {

	@Autowired
	private VersionsService versionsService;

	@Autowired
	private MockRestServiceServer mockServer;

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	@Autowired
	private ObjectMapper objectMapper;

	private List<Version> versions;
	private long lolpatchStyleSize;

	@BeforeEach
	void beforeEach() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);

		versions = jsonTestFilesUtil.getVersions();
		lolpatchStyleSize = versions.stream()
				.filter(version -> version.getRevision() == 0)
				.count();
	}

	@AfterEach
	void afterEach() {
		mockServer.reset();
	}

	@Nested
	@DisplayName("getVersions")
	class GetVersions {

		@Test
		void getsAll() throws Exception {
			mockServer.expect(requestTo(versionsUri.toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess(objectMapper.writeValueAsString(versions), MediaType.parseMediaType("text/json;charset=UTF-8")));

			List<Version> allVersions = versionsService.getVersions();
			mockServer.verify();

			assertThat(allVersions).isNotEmpty();
			assertThat(allVersions).hasSize(versions.size() - (int) lolpatchStyleSize); // see if lolpatch_7.17 style was filtered out
		}

		@Test
		void withRestClientException() {
			mockServer.expect(requestTo(versionsUri.toString()))
					.andRespond(withServerError());

			List<Version> versions = versionsService.getVersions();
			mockServer.verify();

			assertThat(versions).isEmpty();
		}

	}

	@Nested
	@DisplayName("getLatestVersion")
	class GetsVersions {

		@Test
		void getsLatest() throws Exception {
			mockServer.expect(requestTo(versionsUri.toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess(objectMapper.writeValueAsString(versions), MediaType.parseMediaType("text/json;charset=UTF-8")));

			Version version = versionsService.getLatestVersion();
			mockServer.verify();

			assertThat(version).isNotNull();
			assertThat(version).isEqualTo(versions.get(0));
		}

		@Test
		void withRestClientException() {
			mockServer.expect(requestTo(versionsUri.toString()))
					.andRespond(withServerError());

			Version version = versionsService.getLatestVersion();
			mockServer.verify();

			assertThat(version).isNull();
		}

	}

}
