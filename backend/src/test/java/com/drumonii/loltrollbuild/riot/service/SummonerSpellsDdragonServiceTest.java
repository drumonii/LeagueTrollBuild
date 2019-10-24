package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.config.RiotApiConfig;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.ExpectedCount.never;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest({ SummonerSpellsService.class, VersionsService.class })
@Import(RiotApiConfig.class)
@EnableConfigurationProperties(RiotApiProperties.class)
@ActiveProfiles({ TESTING, DDRAGON })
class SummonerSpellsDdragonServiceTest {

	@Autowired
	private SummonerSpellsService summonerSpellsService;

	@Autowired
	private MockRestServiceServer mockServer;

	@Autowired
	@Qualifier("summonerSpells")
	private UriComponentsBuilder summonerSpellsUri;

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${riot.ddragon.locale}")
	private String locale;

	private String summonerSpellsJson;
	private String versionsJson;

	private Version latestVersion;

	@BeforeEach
	void beforeEach() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);

		summonerSpellsJson = JsonTestFilesUtil.getSummonerSpellsJson();
		versionsJson = JsonTestFilesUtil.getVersionsJson();

		List<Version> versions = jsonTestFilesUtil.getVersions();
		latestVersion = versions.get(0);
	}

	@AfterEach
	void afterEach() {
		mockServer.reset();
	}

	@Nested
	@DisplayName("getSummonerSpells")
	class GetSummonerSpells {

		@Test
		void fromVersion() {
			mockServer.expect(requestTo(summonerSpellsUri.buildAndExpand(latestVersion.getPatch(), locale).toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess(summonerSpellsJson, MediaType.APPLICATION_JSON));

			List<SummonerSpell> summonerSpells = summonerSpellsService.getSummonerSpells(latestVersion);
			mockServer.verify();

			assertThat(summonerSpells).isNotEmpty();
		}

		@Test
		void fromVersionWithRestClientException() {
			mockServer.expect(requestTo(summonerSpellsUri.buildAndExpand(latestVersion.getPatch(), locale).toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withServerError());

			List<SummonerSpell> summonerSpells = summonerSpellsService.getSummonerSpells(latestVersion);
			mockServer.verify();

			assertThat(summonerSpells).isEmpty();
		}

	}

	@Nested
	@DisplayName("getSummonerSpell")
	class GetSummonerSpell {

		@Test
		void fromSummonerSpellId() {
			int smiteId = 11;

			mockServer.expect(requestTo(versionsUri.toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess(versionsJson, MediaType.parseMediaType("text/json;charset=UTF-8")));

			mockServer.expect(requestTo(summonerSpellsUri.buildAndExpand(latestVersion.getPatch(), locale).toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess(summonerSpellsJson, MediaType.APPLICATION_JSON));

			SummonerSpell summonerSpell = summonerSpellsService.getSummonerSpell(smiteId);
			mockServer.verify();

			assertThat(summonerSpell).isNotNull();
		}

		@Test
		void fromSummonerSpellIdWithRestClientException() {
			int smiteId = 11;

			mockServer.expect(requestTo(versionsUri.toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess(versionsJson, MediaType.parseMediaType("text/json;charset=UTF-8")));

			mockServer.expect(requestTo(summonerSpellsUri.buildAndExpand(latestVersion.getPatch(), locale).toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withServerError());

			SummonerSpell summonerSpell = summonerSpellsService.getSummonerSpell(smiteId);
			mockServer.verify();

			assertThat(summonerSpell).isNull();
		}

		@Test
		void fromSummonerSpellIdWithRestClientExceptionFromVersions() {
			int smiteId = 11;

			mockServer.expect(requestTo(versionsUri.toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withServerError());

			mockServer.expect(never(), requestTo(summonerSpellsUri.buildAndExpand(latestVersion.getPatch(), locale).toString()))
					.andExpect(method(HttpMethod.GET));

			SummonerSpell summonerSpell = summonerSpellsService.getSummonerSpell(smiteId);
			mockServer.verify();

			assertThat(summonerSpell).isNull();
		}

	}

}
