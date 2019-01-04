package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.config.RiotApiConfig;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import com.drumonii.loltrollbuild.util.GameMapUtil;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
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

@RunWith(SpringRunner.class)
@RestClientTest({ MapsService.class, VersionsService.class })
@Import(RiotApiConfig.class)
@EnableConfigurationProperties(RiotApiProperties.class)
@ActiveProfiles({ TESTING, DDRAGON })
public class MapsDdragonServiceTest {

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

	@Value("${riot.ddragon.locale}")
	private String locale;

	private String mapsJson;
	private String versionsJson;

	private Version latestVersion;

	@Before
	public void before() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);

		mapsJson = JsonTestFilesUtil.getMapsJson();
		versionsJson = JsonTestFilesUtil.getVersionsJson();

		List<Version> versions = jsonTestFilesUtil.getVersions();
		latestVersion = versions.get(0);
	}

	@Test
	public void getMapsFromVersion() {
		mockServer.expect(requestTo(mapsUri.buildAndExpand(latestVersion.getPatch(), locale).toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mapsJson, MediaType.APPLICATION_JSON_UTF8));

		List<GameMap> maps = mapsService.getMaps(latestVersion);
		mockServer.verify();

		assertThat(maps).isNotEmpty();
	}

	@Test
	public void getMapsFromVersionWithRestClientException() {
		mockServer.expect(requestTo(mapsUri.buildAndExpand(latestVersion.getPatch(), locale).toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		List<GameMap> maps = mapsService.getMaps(latestVersion);
		mockServer.verify();

		assertThat(maps).isEmpty();
	}

	@Test
	public void getMaps() {
		mockServer.expect(requestTo(versionsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsJson, MediaType.parseMediaType("text/json;charset=UTF-8")));

		mockServer.expect(requestTo(mapsUri.buildAndExpand(latestVersion.getPatch(), locale).toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mapsJson, MediaType.APPLICATION_JSON_UTF8));

		List<GameMap> maps = mapsService.getMaps();
		mockServer.verify();

		assertThat(maps).isNotEmpty();
	}

	@Test
	public void getMapsWithRestClientException() {
		mockServer.expect(requestTo(versionsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsJson, MediaType.parseMediaType("text/json;charset=UTF-8")));

		mockServer.expect(requestTo(mapsUri.buildAndExpand(latestVersion.getPatch(), locale).toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		List<GameMap> maps = mapsService.getMaps();
		mockServer.verify();

		assertThat(maps).isEmpty();
	}

	@Test
	public void getMapsWithRestClientExceptionFromVersions() {
		mockServer.expect(requestTo(versionsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		mockServer.expect(never(), requestTo(mapsUri.buildAndExpand(latestVersion.getPatch(), locale).toString()))
				.andExpect(method(HttpMethod.GET));

		List<GameMap> maps = mapsService.getMaps();
		mockServer.verify();

		assertThat(maps).isEmpty();
	}

	@Test
	public void getMap() {
		mockServer.expect(requestTo(versionsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsJson, MediaType.parseMediaType("text/json;charset=UTF-8")));

		mockServer.expect(requestTo(mapsUri.buildAndExpand(latestVersion.getPatch(), locale).toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mapsJson, MediaType.APPLICATION_JSON_UTF8));

		GameMap map = mapsService.getMap(GameMapUtil.SUMMONERS_RIFT_ID);
		mockServer.verify();

		assertThat(map).isNotNull();
	}

	@Test
	public void getMapWithRestClientException() {
		mockServer.expect(requestTo(versionsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsJson, MediaType.parseMediaType("text/json;charset=UTF-8")));

		mockServer.expect(requestTo(mapsUri.buildAndExpand(latestVersion.getPatch(), locale).toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		GameMap map = mapsService.getMap(GameMapUtil.SUMMONERS_RIFT_ID);
		mockServer.verify();

		assertThat(map).isNull();
	}

	@Test
	public void getMapWithRestClientExceptionFromVersions() {
		mockServer.expect(requestTo(versionsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		mockServer.expect(never(), requestTo(mapsUri.buildAndExpand(latestVersion.getPatch(), locale).toString()))
				.andExpect(method(HttpMethod.GET));

		GameMap map = mapsService.getMap(GameMapUtil.SUMMONERS_RIFT_ID);
		mockServer.verify();

		assertThat(map).isNull();
	}

}