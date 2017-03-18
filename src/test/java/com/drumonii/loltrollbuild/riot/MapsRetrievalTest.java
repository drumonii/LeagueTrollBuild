package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MapsRetrievalTest extends BaseSpringTestRunner {

	private static final int MAX_RESPONSE_SIZE = 10;

	@Autowired
	@Qualifier("maps")
	private UriComponents mapsUri;

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	@Autowired
	private MapsRepository mapsRepository;

	private MockRestServiceServer mockServer;

	private MapsResponse mapsResponseSlice;
	private String mapsResponseBody;

	private GameMap summonersRift;

	private String versionsResponseBody;

	@Before
	public void before() {
		super.before();

		mockServer = MockRestServiceServer.createServer(restTemplate);

		// Create a random "slice" of MapsResponse with size of MAX_RESPONSE_SIZE
		mapsResponseSlice = new MapsResponse();
		mapsResponseSlice.setType(championsResponse.getType());
		mapsResponseSlice.setVersion(championsResponse.getVersion());
		mapsResponseSlice.setMaps(RandomizeUtil.getRandoms(
				mapsResponse.getMaps().values(), MAX_RESPONSE_SIZE).stream()
				.collect(Collectors.toMap(map -> String.valueOf(map.getMapId()), map -> map)));

		try {
			mapsResponseBody = objectMapper.writeValueAsString(mapsResponseSlice);
		} catch (JsonProcessingException e) {
			fail("Unable to marshal the Maps response.", e);
		}

		summonersRift = mapsResponse.getMaps().get(SUMMONERS_RIFT);

		try {
			versionsResponseBody = objectMapper.writeValueAsString(versions);
		} catch (JsonProcessingException e) {
			fail("Unable to marshal the Versions.", e);
		}
	}

	}

	@Test
	public void maps() throws Exception {
		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mapsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(get("/riot/maps").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(mapsResponseSlice.getMaps().values())));
		mockServer.verify();
	}

	@Test
	public void saveMaps() throws Exception {
		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mapsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/maps").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(mapsResponseSlice.getMaps().values())));
		mockServer.verify();

		assertThat(mapsRepository.findAll())
				.containsOnlyElementsOf(mapsResponseSlice.getMaps().values());
	}

	@Test
	public void saveDifferenceOfMaps() throws Exception {
		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mapsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		List<GameMap> maps = mapsRepository.save(mapsResponseSlice.getMaps().values());

		mockMvc.perform(post("/riot/maps").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
		mockServer.verify();

		assertThat(mapsRepository.findAll()).containsOnlyElementsOf(maps);
	}

	@Test
	public void saveDifferenceOfMapsWithDelete() throws Exception {
		List<GameMap> items = mapsRepository.save(mapsResponseSlice.getMaps().values());
		GameMap mapToDelete = RandomizeUtil.getRandom(items);
		mapsResponseSlice.getMaps().remove(String.valueOf(mapToDelete.getMapId()));

		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(mapsResponseSlice),
						MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/maps").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
		mockServer.verify();

		assertThat(mapsRepository.findOne(mapToDelete.getMapId())).isNull();
	}

	@Test
	public void saveMapsWithTruncate() throws Exception {
		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mapsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/maps?truncate=true").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(mapsResponseSlice.getMaps().values())));
		mockServer.verify();

		assertThat(mapsRepository.findAll())
				.containsOnlyElementsOf(mapsResponseSlice.getMaps().values());
	}

	@Test
	public void map() throws Exception {
		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mapsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(get("/riot/maps/{id}", summonersRift.getMapId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(summonersRift)));
		mockServer.verify();
	}

	@Test
	public void mapNotFound() throws Exception {
		mapsResponseSlice.getMaps().remove(String.valueOf(summonersRift.getMapId()));

		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(mapsResponseSlice),
						MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(get("/riot/maps/{id}", summonersRift.getMapId()).with(adminUser()).with(csrf()))
				.andExpect(status().isNotFound());
		mockServer.verify();
	}

	@Test
	public void saveMap() throws Exception {
		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mapsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/maps/{id}", summonersRift.getMapId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(summonersRift)));
		mockServer.verify();

		assertThat(mapsRepository.findOne(summonersRift.getMapId())).isNotNull();
	}

	@Test
	public void saveMapNotFound() throws Exception {
		mapsResponseSlice.getMaps().remove(String.valueOf(summonersRift.getMapId()));

		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(mapsResponseSlice),
						MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/maps/{id}", summonersRift.getMapId()).with(adminUser()).with(csrf()))
				.andExpect(status().isNotFound());
		mockServer.verify();
	}

	@Test
	public void saveMapWithOverwrite() throws Exception {
		mapsRepository.save(summonersRift);
		GameMap newSummonersRift = objectMapper.readValue(mapsResponseBody, MapsResponse.class).getMaps()
				.get(SUMMONERS_RIFT);
		newSummonersRift.setMapName("New Summoners Rift");
		mapsResponseSlice.getMaps().put(String.valueOf(newSummonersRift.getMapId()), newSummonersRift);

		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(mapsResponseSlice),
						MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/maps/{id}", summonersRift.getMapId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(newSummonersRift)));
		mockServer.verify();

		assertThat(mapsRepository.findOne(newSummonersRift.getMapId())).isNotNull()
				.isEqualTo(newSummonersRift);
	}

}