package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.config.RiotApiConfig;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.client.ExpectedCount.never;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest({ SummonerSpellsService.class, VersionsService.class })
@Import(RiotApiConfig.class)
@EnableConfigurationProperties(RiotApiProperties.class)
@ActiveProfiles({ TESTING, DDRAGON })
public class SummonerSpellsDdragonServiceTest {

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

	@Before
	public void before() {
		ClassPathResource summonerSpellsJsonResource = new ClassPathResource("summoners_data_dragon.json");
		try {
			summonerSpellsJson = new String(Files.readAllBytes(summonerSpellsJsonResource.getFile().toPath()));
		} catch (IOException e) {
			fail("Unable to read the Summoner Spells JSON.", e);
		}
		ClassPathResource versionsJsonResource = new ClassPathResource("versions_data_dragon.json");
		try {
			versionsJson = new String(Files.readAllBytes(versionsJsonResource.getFile().toPath()));
		} catch (IOException e) {
			fail("Unable to read the Versions JSON.", e);
		}
		try {
			List<Version> versions = objectMapper.readValue(versionsJson, new TypeReference<List<Version>>() {});
			latestVersion = versions.get(0);
		} catch (IOException e) {
			fail("Unable to unmarshal the Versions response.", e);
		}
	}

	@Test
	public void getSummonerSpells() {
		mockServer.expect(requestTo(versionsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsJson, MediaType.parseMediaType("text/json;charset=UTF-8")));

		mockServer.expect(requestTo(summonerSpellsUri.buildAndExpand(latestVersion.getPatch(), locale).toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(summonerSpellsJson, MediaType.APPLICATION_JSON_UTF8));

		List<SummonerSpell> summonerSpells = summonerSpellsService.getSummonerSpells();
		mockServer.verify();

		assertThat(summonerSpells).isNotEmpty();
	}

	@Test
	public void getSummonerSpellsWithRestClientException() {
		mockServer.expect(requestTo(versionsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsJson, MediaType.parseMediaType("text/json;charset=UTF-8")));

		mockServer.expect(requestTo(summonerSpellsUri.buildAndExpand(latestVersion.getPatch(), locale).toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		List<SummonerSpell> summonerSpells = summonerSpellsService.getSummonerSpells();
		mockServer.verify();

		assertThat(summonerSpells).isEmpty();
	}

	@Test
	public void getSummonerSpellsWithRestClientExceptionFromVersions() {
		mockServer.expect(requestTo(versionsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		mockServer.expect(never(), requestTo(summonerSpellsUri.buildAndExpand(latestVersion.getPatch(), locale).toString()))
				.andExpect(method(HttpMethod.GET));

		List<SummonerSpell> summonerSpells = summonerSpellsService.getSummonerSpells();
		mockServer.verify();

		assertThat(summonerSpells).isEmpty();
	}

	@Test
	public void getSummonerSpell() {
		int smiteId = 11;

		mockServer.expect(requestTo(versionsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsJson, MediaType.parseMediaType("text/json;charset=UTF-8")));

		mockServer.expect(requestTo(summonerSpellsUri.buildAndExpand(latestVersion.getPatch(), locale).toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(summonerSpellsJson, MediaType.APPLICATION_JSON_UTF8));

		SummonerSpell summonerSpell = summonerSpellsService.getSummonerSpell(smiteId);
		mockServer.verify();

		assertThat(summonerSpell).isNotNull();
	}

	@Test
	public void getSummonerSpellWithRestClientException() {
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
	public void getSummonerSpellWithRestClientExceptionFromVersions() {
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