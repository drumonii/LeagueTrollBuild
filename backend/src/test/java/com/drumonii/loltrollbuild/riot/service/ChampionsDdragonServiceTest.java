package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
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
import static org.assertj.core.api.Assertions.atIndex;

@RestClientTest(ChampionsService.class)
class ChampionsDdragonServiceTest extends AbstractDdragonServiceTests {

	@Autowired
	private ChampionsService championsService;

	private String championsJson;
	private ChampionsResponse championsResponse;
	private Version latestVersion;

	@BeforeEach
	void beforeEach() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);

		championsJson = JsonTestFilesUtil.getChampionsJson();
		championsResponse = jsonTestFilesUtil.getChampionsResponse();

		List<Version> versions = jsonTestFilesUtil.getVersions();
		latestVersion = versions.get(0);
	}

	@Nested
	@DisplayName("getChampions")
	class GetChampions {

		// Note, for some reason this test fails when running all tests with 'Connection refused: no further information'
		// but it will pass if run all tests in riot package
		@Test
		void fromVersion() throws Exception {
			MockResponse championsMockResponse = new MockResponse()
					.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.setBody(championsJson);
			mockWebServer.enqueue(championsMockResponse);

			List<String> championKeys = championsResponse.getChampions().values().stream()
					.map(Champion::getKey)
					.toList();
			for (String championKey : championKeys) {
				mockWebServer.enqueue(new MockResponse()
						.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
						.setBody(JsonTestFilesUtil.getChampionJson(championKey)));
			}

			List<Champion> champions = championsService.getChampions(latestVersion);
			assertThat(champions).isNotEmpty();
			for (int i = 0; i < champions.size(); i++) {
				assertThat(champions)
						.satisfies(champion -> {
							assertThat(champion.getPassive()).as("Passive").isNotNull();
							assertThat(champion.getSpells()).as("Spells").isNotEmpty();
						}, atIndex(i));
			}

			RecordedRequest recordedRequest1 = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
			assertThat(recordedRequest1).satisfies(new ChampionsRecordedRequest());
			for (int i = 0; i < championKeys.size(); i++) {
				RecordedRequest recordedRequest = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
				assertThat(recordedRequest).satisfies(new ChampionRecordedRequest(championKeys));
			}
		}

		@Test
		void fromVersionWithRestClientException() throws Exception {
			MockResponse championsMockResponse = new MockResponse()
					.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

			mockWebServer.enqueue(championsMockResponse);

			List<Champion> champions = championsService.getChampions(latestVersion);
			assertThat(champions).isEmpty();

			RecordedRequest recordedRequest1 = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
			assertThat(recordedRequest1).satisfies(new ChampionsRecordedRequest());
		}

	}

	private class ChampionsRecordedRequest extends RiotApiRecordedRequest {

		@Override
		public void accept(RecordedRequest recordedRequest) {
			super.accept(recordedRequest);

			assertThat(recordedRequest.getPath()).as("Path")
					.isEqualTo(UriComponentsBuilder.fromPath(riotApiProperties.getDdragon().getChampions())
							.buildAndExpand(latestVersion.getPatch(), riotApiProperties.getDdragon().getLocale())
							.toString());
		}

	}

	private class ChampionRecordedRequest extends RiotApiRecordedRequest {

		private List<String> championUrls;

		public ChampionRecordedRequest(List<String> championKeys) {
			this.championUrls = championKeys.stream()
					.map(championKey -> UriComponentsBuilder.fromPath(riotApiProperties.getDdragon().getChampion())
							.buildAndExpand(latestVersion.getPatch(), riotApiProperties.getDdragon().getLocale(), championKey)
							.toString())
					.toList();
		}

		@Override
		public void accept(RecordedRequest recordedRequest) {
			super.accept(recordedRequest);

			assertThat(recordedRequest.getPath()).as("Path")
					.isIn(championUrls);
		}

	}

}
