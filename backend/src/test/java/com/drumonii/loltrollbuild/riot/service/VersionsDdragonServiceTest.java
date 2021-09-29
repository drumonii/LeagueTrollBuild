package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.test.api.service.AbstractDdragonServiceTests;
import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RestClientTest(VersionsService.class)
class VersionsDdragonServiceTest extends AbstractDdragonServiceTests {

	@Autowired
	private VersionsService versionsService;

	private String versionsJson;
	private List<Version> versions;
	private Version latestVersion;
	private long lolpatchStyleSize;

	@BeforeEach
	void beforeEach() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);

		versionsJson = JsonTestFilesUtil.getVersionsJson();
		versions = jsonTestFilesUtil.getVersions();
		latestVersion = versions.get(0);
		lolpatchStyleSize = versions.stream()
				.filter(version -> version.getRevision() == 0)
				.count();
	}

	@Nested
	@DisplayName("getVersions")
	class GetVersions {

		@Test
		void getsAll() throws Exception {
			MockResponse versionMockResponse = new MockResponse()
					.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.parseMediaType("text/json;charset=UTF-8"))
					.setBody(versionsJson);

			mockWebServer.enqueue(versionMockResponse);

			List<Version> allVersions = versionsService.getVersions();
			assertThat(allVersions).isNotEmpty();
			assertThat(allVersions).hasSize(versions.size() - (int) lolpatchStyleSize); // see if lolpatch_7.17 style was filtered out

			RecordedRequest recordedRequest = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
			assertThat(recordedRequest).satisfies(new VersionRecordedRequest());
		}

		@Test
		void withRestClientException() throws Exception {
			MockResponse versionMockResponse = new MockResponse()
					.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

			mockWebServer.enqueue(versionMockResponse);

			List<Version> versions = versionsService.getVersions();
			assertThat(versions).isEmpty();

			RecordedRequest recordedRequest = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
			assertThat(recordedRequest).satisfies(new VersionRecordedRequest());
		}

	}

	@Nested
	@DisplayName("getLatestVersion")
	class GetsVersions {

		@Test
		void getsLatest() throws Exception {
			MockResponse versionMockResponse = new MockResponse()
					.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.parseMediaType("text/json;charset=UTF-8"))
					.setBody(versionsJson);

			mockWebServer.enqueue(versionMockResponse);

			Version version = versionsService.getLatestVersion();
			assertThat(version).isEqualTo(latestVersion);

			RecordedRequest recordedRequest = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
			assertThat(recordedRequest).satisfies(new VersionRecordedRequest());
		}

		@Test
		void withRestClientException() throws Exception {
			MockResponse versionMockResponse = new MockResponse()
					.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

			mockWebServer.enqueue(versionMockResponse);

			Version version = versionsService.getLatestVersion();
			assertThat(version).isNull();

			RecordedRequest recordedRequest = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
			assertThat(recordedRequest).satisfies(new VersionRecordedRequest());
		}

	}

	private class VersionRecordedRequest extends RiotApiRecordedRequest {

		@Override
		public void accept(RecordedRequest recordedRequest) {
			super.accept(recordedRequest);

			assertThat(recordedRequest.getPath()).as("Path")
					.isEqualTo(UriComponentsBuilder.fromPath(riotApiProperties.getDdragon().getVersions())
							.build()
							.toString());
		}

	}

}
