package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MapsRetrievalTest extends BaseSpringTestRunner {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	@Qualifier("maps")
	private UriComponents mapsUri;

	@Autowired
	private MapsRepository mapsRepository;

	@Autowired
	private VersionsRepository versionsRepository;

	private MockRestServiceServer mockServer;

	private String mapsResponseBody;
	private GameMap provingGrounds;
	private GameMap summonersRift;

	@Before
	public void before() {
		super.before();

		// Only first request is handled. See: http://stackoverflow.com/q/30713734
		mockServer = MockRestServiceServer.createServer(restTemplate);
		mapsResponseBody = "{\"type\":\"map\",\"version\":\"5.24.2\",\"data\":{\"12\":{\"mapName\":" +
				"\"ProvingGroundsNew\",\"mapId\":12,\"image\":{\"full\":\"map12.png\",\"sprite\":\"map0.png\"," +
				"\"group\":\"map\",\"x\":48,\"y\":0,\"w\":48,\"h\":48}}}}";
		try {
			provingGrounds = objectMapper.readValue(mapsResponseBody, MapsResponse.class).getMaps().get("12");
		} catch (IOException e) {
			fail("Unable to unmarshal the Maps response.", e);
		}

		String responseBody = "{\"type\":\"map\",\"version\":\"5.24.2\",\"data\":{\"1\":{\"mapName\":" +
				"\"SummonersRift\",\"mapId\":1,\"image\":{\"full\":\"map1.png\",\"sprite\":\"map0.png\",\"group\":" +
				"\"map\",\"x\":96,\"y\":0,\"w\":48,\"h\":48}}}}";
		try {
			summonersRift = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("1");
		} catch (IOException e) {
			fail("Unable to unmarshal the Maps response.", e);
		}

		versionsRepository.save(new Version("latest patch version"));
	}

	@After
	public void after() {
		mapsRepository.deleteAll();
		versionsRepository.deleteAll();
	}

	@Test
	public void maps() throws Exception {
		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mapsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(get("/riot/maps").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(provingGrounds))));
		mockServer.verify();
	}

	@Test
	public void saveMaps() throws Exception {
		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mapsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/maps").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(provingGrounds))));
		mockServer.verify();

		assertThat(mapsRepository.findOne(provingGrounds.getMapId())).isNotNull();
	}

	@Test
	public void saveMapsNoPatchVersion() throws Exception {
		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mapsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		versionsRepository.deleteAll();

		mockMvc.perform(post("/riot/maps").with(adminUser()).with(csrf()))
				.andExpect(status().isNotFound());
		mockServer.verify();
	}

	@Test
	public void saveDifferenceOfMaps() throws Exception {
		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mapsResponseBody, MediaType.APPLICATION_JSON_UTF8));
		mapsRepository.save(provingGrounds);

		mockMvc.perform(post("/riot/maps").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
		mockServer.verify();

		assertThat(mapsRepository.findOne(provingGrounds.getMapId())).isNotNull();
	}

	@Test
	public void saveDifferenceOfMapsWithDelete() throws Exception {
		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mapsResponseBody, MediaType.APPLICATION_JSON_UTF8));
		mapsRepository.save(provingGrounds);
		mapsRepository.save(summonersRift);

		mockMvc.perform(post("/riot/maps").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
		mockServer.verify();

		assertThat(mapsRepository.findOne(provingGrounds.getMapId())).isNotNull();
		assertThat(mapsRepository.findOne(summonersRift.getMapId())).isNull();
	}

	@Test
	public void saveMapsWithTruncate() throws Exception {
		String responseBody = "{\"type\":\"map\",\"version\":\"5.24.2\",\"data\":{\"1\":{\"mapName\":" +
				"\"SummonersRift\",\"mapId\":1,\"image\":{\"full\":\"map1.png\",\"sprite\":\"map0.png\",\"group\":" +
				"\"map\",\"x\":96,\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap summonersRift = null;
		try {
			summonersRift = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("1");
		} catch (IOException e) {
			fail("Unable to unmarshal the GameMap by ID response.", e);
		}
		mapsRepository.save(summonersRift);

		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mapsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/maps?truncate=true").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(provingGrounds))));
		mockServer.verify();

		assertThat(mapsRepository.findOne(summonersRift.getMapId())).isNull();
		assertThat(mapsRepository.findOne(provingGrounds.getMapId())).isNotNull();
	}

	@Test
	public void map() throws Exception {
		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mapsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(get("/riot/maps/{id}", provingGrounds.getMapId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(provingGrounds)));
		mockServer.verify();
	}

	@Test
	public void mapNotFound() throws Exception {
		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mapsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(get("/riot/maps/{id}", 10001).with(adminUser()).with(csrf()))
				.andExpect(status().isNotFound());
		mockServer.verify();
	}

	@Test
	public void saveMap() throws Exception {
		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mapsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/maps/{id}", provingGrounds.getMapId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(provingGrounds)));
		mockServer.verify();

		assertThat(mapsRepository.findOne(provingGrounds.getMapId())).isNotNull();
	}

	@Test
	public void saveMapNotFound() throws Exception {
		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mapsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/maps/{id}", 10001).with(adminUser()).with(csrf()))
				.andExpect(status().isNotFound());
		mockServer.verify();
	}

	@Test
	public void saveMapNoPatchVersion() throws Exception {
		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mapsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		versionsRepository.deleteAll();

		mockMvc.perform(post("/riot/maps/{id}", provingGrounds.getMapId()).with(adminUser()).with(csrf()))
				.andExpect(status().isNotFound());
		mockServer.verify();
	}

	@Test
	public void saveMapWithOverwrite() throws Exception {
		GameMap newProvingGrounds = objectMapper.readValue(mapsResponseBody, MapsResponse.class).getMaps().get("12");
		newProvingGrounds.setMapName("New ProvingGrounds");
		mapsRepository.save(newProvingGrounds);

		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mapsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/maps/{id}", provingGrounds.getMapId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(provingGrounds)));
		mockServer.verify();

		assertThat(mapsRepository.findOne(provingGrounds.getMapId())).isNotNull();
		assertThat(mapsRepository.findOne(provingGrounds.getMapId()).getMapName()).isEqualTo(provingGrounds.getMapName());
	}

}