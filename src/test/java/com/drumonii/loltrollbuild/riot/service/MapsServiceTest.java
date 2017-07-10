package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.util.GameMapUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class MapsServiceTest extends BaseSpringTestRunner {

	@Autowired
	private RestTemplate restTemplate;

	private MockRestServiceServer mockServer;

	@Autowired
	private MapsService mapsService;

	@Autowired
	@Qualifier("maps")
	private UriComponents mapsUri;

	@Value("${riot.api.static-data.region}")
	private String region;

	@Before
	public void before() {
		super.before();

		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	public void getMaps() throws Exception {
		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(mapsResponse), MediaType.APPLICATION_JSON_UTF8));

		List<GameMap> maps = mapsService.getMaps();
		mockServer.verify();

		assertThat(maps).isNotEmpty();
	}

	@Test
	public void getMapsWithRestClientException() throws Exception {
		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		List<GameMap> maps = mapsService.getMaps();
		mockServer.verify();

		assertThat(maps).isEmpty();
	}

	@Test
	public void getMap() throws Exception {
		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(mapsResponse), MediaType.APPLICATION_JSON_UTF8));

		GameMap map = mapsService.getMap(GameMapUtil.SUMMONERS_RIFT_ID);
		mockServer.verify();

		assertThat(map).isNotNull();
	}

	@Test
	public void getMapWithRestClientException() throws Exception {
		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		GameMap map = mapsService.getMap(GameMapUtil.SUMMONERS_RIFT_ID);
		mockServer.verify();

		assertThat(map).isNull();
	}

}