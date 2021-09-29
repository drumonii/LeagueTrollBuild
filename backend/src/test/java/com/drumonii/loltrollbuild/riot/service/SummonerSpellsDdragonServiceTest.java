package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.SummonerSpell;
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

@RestClientTest(SummonerSpellsService.class)
class SummonerSpellsDdragonServiceTest extends AbstractDdragonServiceTests {

	@Autowired
	private SummonerSpellsService summonerSpellsService;

	private String summonerSpellsJson;
	private Version latestVersion;

	@BeforeEach
	void beforeEach() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);

		summonerSpellsJson = JsonTestFilesUtil.getSummonerSpellsJson();

		List<Version> versions = jsonTestFilesUtil.getVersions();
		latestVersion = versions.get(0);
	}

	@Nested
	@DisplayName("getSummonerSpells")
	class GetSummonerSpells {

		@Test
		void fromVersion() throws Exception {
			MockResponse summonerSpellsMockResponse = new MockResponse()
					.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.setBody(summonerSpellsJson);

			mockWebServer.enqueue(summonerSpellsMockResponse);

			List<SummonerSpell> summonerSpells = summonerSpellsService.getSummonerSpells(latestVersion);
			assertThat(summonerSpells).isNotEmpty();

			RecordedRequest recordedRequest = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
			assertThat(recordedRequest).satisfies(new SummonerSpellsRecordedRequest());
		}

		@Test
		void fromVersionWithRestClientException() throws Exception {
			MockResponse summonerSpellsMockResponse = new MockResponse()
					.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

			mockWebServer.enqueue(summonerSpellsMockResponse);

			List<SummonerSpell> summonerSpells = summonerSpellsService.getSummonerSpells(latestVersion);
			assertThat(summonerSpells).isEmpty();

			RecordedRequest recordedRequest = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
			assertThat(recordedRequest).satisfies(new SummonerSpellsRecordedRequest());
		}

	}

	private class SummonerSpellsRecordedRequest extends RiotApiRecordedRequest {

		@Override
		public void accept(RecordedRequest recordedRequest) {
			super.accept(recordedRequest);

			assertThat(recordedRequest.getPath()).as("Path")
					.isEqualTo(UriComponentsBuilder.fromPath(riotApiProperties.getDdragon().getSummonerSpells())
							.buildAndExpand(latestVersion.getPatch(), riotApiProperties.getDdragon().getLocale())
							.toString());
		}

	}

}
