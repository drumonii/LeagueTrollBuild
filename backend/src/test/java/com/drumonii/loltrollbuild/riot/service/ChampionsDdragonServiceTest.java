package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.config.RiotApiConfig;
import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.ExpectedCount.never;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest({ ChampionsService.class, VersionsService.class })
@Import(RiotApiConfig.class)
@EnableConfigurationProperties(RiotApiProperties.class)
@ActiveProfiles({ TESTING })
class ChampionsDdragonServiceTest {

	@Autowired
	private ChampionsService championsService;

	@Autowired
	private MockRestServiceServer mockServer;

	@Autowired
	@Qualifier("champions")
	private UriComponentsBuilder championsUri;

	@Autowired
	@Qualifier("champion")
	private UriComponentsBuilder championUri;

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	@Autowired
	private ObjectMapper objectMapper;

	private String championsJson;
	private String versionsJson;

	private ChampionsResponse championsResponse;

	private Version latestVersion;

	@BeforeEach
	void beforeEach() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);

		championsJson = JsonTestFilesUtil.getChampionsJson();
		versionsJson = JsonTestFilesUtil.getVersionsJson();

		championsResponse = jsonTestFilesUtil.getChampionsResponse();

		List<Version> versions = jsonTestFilesUtil.getVersions();
		latestVersion = versions.get(0);
	}

	@AfterEach
	void afterEach() {
		mockServer.reset();
	}

	@Nested
	@DisplayName("getChampions")
	class GetChampions {

		@Test
		void fromVersion() {
			mockServer.expect(requestTo(championsUri.buildAndExpand(latestVersion.getPatch()).toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess(championsJson, MediaType.APPLICATION_JSON));

			List<String> championKeys = championsResponse.getChampions().values().stream()
					.map(Champion::getKey)
					.collect(Collectors.toList());

			for (String championKey : championKeys) {
				mockServer.expect(requestTo(championUri.buildAndExpand(latestVersion.getPatch(), championKey).toString()))
						.andExpect(method(HttpMethod.GET))
						.andRespond(withSuccess(JsonTestFilesUtil.getChampionJson(championKey), MediaType.APPLICATION_JSON));
			}

			List<Champion> champions = championsService.getChampions(latestVersion);
			mockServer.verify();

			assertThat(champions).isNotEmpty();
			for (Champion champion : champions) {
				assertThat(champion.getPassive()).isNotNull();
				assertThat(champion.getSpells()).isNotEmpty();
			}
		}

		@Test
		void fromVersionWithRestClientException() {
			mockServer.expect(requestTo(championsUri.buildAndExpand(latestVersion.getPatch()).toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withServerError());

			List<Champion> champions = championsService.getChampions(latestVersion);
			mockServer.verify();

			assertThat(champions).isEmpty();
		}

	}

	@Nested
	@DisplayName("getChampion")
	class GetChampion {

		@Test
		void fromChampionId() {
			String trundle = "Trundle";
			int trundleId = 48;
			String trundleJson =
					"{" + // simplified JSON for brevity
					"  \"type\": \"champion\"," +
					"  \"format\": \"standAloneComplex\"," +
					"  \"version\": \""+ latestVersion.getPatch() + "\"," +
					"  \"data\": {" +
					"    \"Trundle\": {" +
					"      \"id\": \"Trundle\"," +
					"      \"key\": \"48\"," +
					"      \"name\": \"Trundle\"," +
					"      \"title\": \"the Troll King\"," +
					"      \"partype\": \"Mana\"," +
					"      \"info\": {" +
					"        \"attack\": 7," +
					"        \"defense\": 6," +
					"        \"magic\": 2," +
					"        \"difficulty\": 5" +
					"      }," +
					"      \"spells\": []," +
					"      \"passive\": {" +
					"        \"name\": \"King's Tribute\"," +
					"        \"description\": \"When an enemy unit dies near Trundle, he heals for a percent of its maximum Health.\"," +
					"        \"image\": {" +
					"          \"full\": \"Trundle_Passive.png\"," +
					"          \"sprite\": \"passive3.png\"," +
					"          \"group\": \"passive\"," +
					"          \"x\": 144," +
					"          \"y\": 96," +
					"          \"w\": 48," +
					"          \"h\": 48" +
					"        }" +
					"      }," +
					"      \"image\": {" +
					"        \"full\": \"Trundle.png\"," +
					"        \"sprite\": \"champion3.png\"," +
					"        \"group\": \"champion\"," +
					"        \"x\": 144," +
					"        \"y\": 96," +
					"        \"w\": 48," +
					"        \"h\": 48" +
					"      }," +
					"      \"tags\": [" +
					"        \"Fighter\"," +
					"        \"Tank\"" +
					"      ]" +
					"    }" +
					"  }" +
					"}";

			mockServer.expect(requestTo(versionsUri.toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess(versionsJson, MediaType.parseMediaType("text/json;charset=UTF-8")));

			mockServer.expect(requestTo(championsUri.buildAndExpand(latestVersion.getPatch()).toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess(championsJson, MediaType.APPLICATION_JSON));

			mockServer.expect(requestTo(championUri.buildAndExpand(latestVersion.getPatch(), trundle).toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess(trundleJson, MediaType.APPLICATION_JSON));

			Champion champion = championsService.getChampion(trundleId);
			mockServer.verify();

			assertThat(champion).isNotNull();
		}

		@Test
		void fromChampionIdWithNotExistingId() {
			mockServer.expect(requestTo(versionsUri.toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess(versionsJson, MediaType.parseMediaType("text/json;charset=UTF-8")));

			mockServer.expect(requestTo(championsUri.buildAndExpand(latestVersion.getPatch()).toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess(championsJson, MediaType.APPLICATION_JSON));

			mockServer.expect(never(), requestTo(championUri.buildAndExpand(latestVersion.getPatch(), 0).toString()))
					.andExpect(method(HttpMethod.GET));

			Champion champion = championsService.getChampion(0);
			mockServer.verify();

			assertThat(champion).isNull();
		}

		@Test
		void fromChampionIdnWithRestClientException() {
			String akali = "Akali";
			int akaliId = 84;

			mockServer.expect(requestTo(versionsUri.toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess(versionsJson, MediaType.parseMediaType("text/json;charset=UTF-8")));

			mockServer.expect(requestTo(championsUri.buildAndExpand(latestVersion.getPatch()).toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess(championsJson, MediaType.APPLICATION_JSON));

			mockServer.expect(requestTo(championUri.buildAndExpand(latestVersion.getPatch(), akali).toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withServerError());

			Champion champion = championsService.getChampion(akaliId);
			mockServer.verify();

			assertThat(champion).isNull();
		}

		@Test
		void fromChampionIdWithRestClientExceptionFromChampions() {
			String akali = "Akali";
			int akaliId = 84;

			mockServer.expect(requestTo(versionsUri.toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess(versionsJson, MediaType.parseMediaType("text/json;charset=UTF-8")));

			mockServer.expect(requestTo(championsUri.buildAndExpand(latestVersion.getPatch()).toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withServerError());

			mockServer.expect(never(), requestTo(championUri.buildAndExpand(latestVersion.getPatch(), akali).toString()))
					.andExpect(method(HttpMethod.GET));

			Champion champion = championsService.getChampion(akaliId);
			mockServer.verify();

			assertThat(champion).isNull();
		}

		@Test
		void fromChampionIdWithRestClientExceptionFromVersions() {
			String akali = "Akali";
			int akaliId = 84;

			mockServer.expect(requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
					.andRespond(withServerError());

			mockServer.expect(never(), requestTo(championsUri.buildAndExpand(latestVersion.getPatch()).toString()))
					.andExpect(method(HttpMethod.GET));

			mockServer.expect(never(), requestTo(championUri.buildAndExpand(latestVersion.getPatch(), akali).toString()))
					.andExpect(method(HttpMethod.GET));

			Champion champion = championsService.getChampion(akaliId);
			mockServer.verify();

			assertThat(champion).isNull();
		}

	}

}
