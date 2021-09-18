package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.config.RiotApiConfig;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import com.drumonii.loltrollbuild.util.GameMapUtil;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.ExpectedCount.never;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest({ MapsService.class, VersionsService.class })
@Import(RiotApiConfig.class)
@EnableConfigurationProperties(RiotApiProperties.class)
@ActiveProfiles({ TESTING })
class MapsDdragonServiceTest {

	@Autowired
	private MapsService mapsService;

	@Autowired
	private MockRestServiceServer mockServer;

	@Autowired
	@Qualifier("maps")
	private UriComponentsBuilder mapsUri;

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	@Autowired
	private ObjectMapper objectMapper;

	private String mapsJson;
	private String versionsJson;

	private Version latestVersion;

	@BeforeEach
	void beforeEach() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);

		mapsJson = JsonTestFilesUtil.getMapsJson();
		versionsJson = JsonTestFilesUtil.getVersionsJson();

		List<Version> versions = jsonTestFilesUtil.getVersions();
		latestVersion = versions.get(0);
	}

	@AfterEach
	void afterEach() {
		mockServer.reset();
	}

	@Nested
	@DisplayName("getMaps")
	class GetsVersions {

		@Test
		void fromVersion() {
			mockServer.expect(requestTo(mapsUri.buildAndExpand(latestVersion.getPatch()).toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess(mapsJson, MediaType.APPLICATION_JSON));

			List<GameMap> maps = mapsService.getMaps(latestVersion);
			mockServer.verify();

			assertThat(maps).isNotEmpty();
		}

		@Test
		void fromVersionWithRestClientException() {
			mockServer.expect(requestTo(mapsUri.buildAndExpand(latestVersion.getPatch()).toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withServerError());

			List<GameMap> maps = mapsService.getMaps(latestVersion);
			mockServer.verify();

			assertThat(maps).isEmpty();
		}

	}

	@Nested
	@DisplayName("getMap")
	class GetMap {

		@Test
		void fromMapId() {
			mockServer.expect(requestTo(versionsUri.toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess(versionsJson, MediaType.parseMediaType("text/json;charset=UTF-8")));

			mockServer.expect(requestTo(mapsUri.buildAndExpand(latestVersion.getPatch()).toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess(mapsJson, MediaType.APPLICATION_JSON));

			GameMap map = mapsService.getMap(GameMapUtil.SUMMONERS_RIFT_ID);
			mockServer.verify();

			assertThat(map).isNotNull();
		}

		@Test
		void fromMapIdWithRestClientException() {
			mockServer.expect(requestTo(versionsUri.toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess(versionsJson, MediaType.parseMediaType("text/json;charset=UTF-8")));

			mockServer.expect(requestTo(mapsUri.buildAndExpand(latestVersion.getPatch()).toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withServerError());

			GameMap map = mapsService.getMap(GameMapUtil.SUMMONERS_RIFT_ID);
			mockServer.verify();

			assertThat(map).isNull();
		}

		@Test
		void fromMapIdWithRestClientExceptionFromVersions() {
			mockServer.expect(requestTo(versionsUri.toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withServerError());

			mockServer.expect(never(), requestTo(mapsUri.buildAndExpand(latestVersion.getPatch()).toString()))
					.andExpect(method(HttpMethod.GET));

			GameMap map = mapsService.getMap(GameMapUtil.SUMMONERS_RIFT_ID);
			mockServer.verify();

			assertThat(map).isNull();
		}

	}

}
