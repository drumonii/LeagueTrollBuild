package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.config.RiotApiConfig;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import com.drumonii.loltrollbuild.util.GameMapUtil;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.UriComponents;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static com.drumonii.loltrollbuild.config.Profiles.STATIC_DATA;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(MapsService.class)
@Import(RiotApiConfig.class)
@EnableConfigurationProperties(RiotApiProperties.class)
@TestPropertySource(properties = "riot.static-data.apiKey=API_KEY")
@ActiveProfiles({ TESTING, STATIC_DATA })
public class MapsStaticDataServiceTest {

	@Autowired
	private MapsService mapsService;

	@Autowired
	private MockRestServiceServer mockServer;

	@Autowired
	@Qualifier("maps")
	private UriComponents mapsUri;

	@Value("${riot.static-data.region}")
	private String region;

	private String mapsJson;

	@Before
	public void before() {
		ClassPathResource mapsJsonResource = new ClassPathResource("maps_static_data.json");
		try {
			mapsJson = new String(Files.readAllBytes(mapsJsonResource.getFile().toPath()));
		} catch (IOException e) {
			fail("Unable to read the Maps JSON.", e);
		}
	}

	@Test
	public void getMaps() {
		mockServer.expect(requestTo(mapsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mapsJson, MediaType.APPLICATION_JSON_UTF8));

		List<GameMap> maps = mapsService.getMaps();
		mockServer.verify();

		assertThat(maps).isNotEmpty();
	}

	@Test
	public void getMapsWithRestClientException() {
		mockServer.expect(requestTo(mapsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		List<GameMap> maps = mapsService.getMaps();
		mockServer.verify();

		assertThat(maps).isEmpty();
	}

	@Test
	public void getMap() {
		mockServer.expect(requestTo(mapsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mapsJson, MediaType.APPLICATION_JSON_UTF8));

		GameMap map = mapsService.getMap(GameMapUtil.SUMMONERS_RIFT_ID);
		mockServer.verify();

		assertThat(map).isNotNull();
	}

	@Test
	public void getMapWithRestClientException() {
		mockServer.expect(requestTo(mapsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		GameMap map = mapsService.getMap(GameMapUtil.SUMMONERS_RIFT_ID);
		mockServer.verify();

		assertThat(map).isNull();
	}

}