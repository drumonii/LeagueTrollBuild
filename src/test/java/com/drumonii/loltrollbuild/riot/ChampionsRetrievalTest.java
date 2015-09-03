package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChampionsRetrievalTest extends BaseSpringTestRunner {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	@Qualifier("champions")
	private UriComponents championsUri;

	@Autowired
	@Qualifier("champion")
	private UriComponentsBuilder championUriBuilder;

	@Autowired
	private ChampionsRepository championsRepository;

	private MockRestServiceServer mockServer;

	private String championsResponseBody;
	private ChampionsResponse championsResponse;
	private Champion thresh;

	private String championResponseBody;
	private UriComponents championUri;
	private Champion leeSin;

	@Before
	public void before() {
		super.before();
		// Only first request is handled. See: http://stackoverflow.com/q/30713734
		mockServer = MockRestServiceServer.createServer(restTemplate);

		championsResponseBody = "{\"type\":\"champion\",\"version\":\"5.16.1\",\"data\":{\"Thresh\":{\"id\":412,\"" +
				"key\":\"Thresh\",\"name\":\"Thresh\",\"title\":\"The Chain Warden\",\"image\":{\"full\":" +
				"\"Thresh.png\",\"sprite\":\"champion3.png\",\"group\":\"champion\",\"x\":336,\"y\":0,\"w\":48," +
				"\"h\":48},\"tags\":[\"Support\",\"Fighter\"],\"partype\":\"Mana\"}}}";

		try {
			championsResponse = objectMapper.readValue(championsResponseBody, ChampionsResponse.class);
			thresh = championsResponse.getChampions().get("Thresh");
		} catch (IOException e) {
			fail("Unable to unmarshal the Champions response.");
		}

		championResponseBody = "{\"id\":64,\"key\":\"LeeSin\",\"name\":\"Lee Sin\",\"title\":\"The Blind Monk\"," +
				"\"image\":{\"full\":\"LeeSin.png\",\"sprite\":\"champion1.png\",\"group\":\"champion\",\"x\":0," +
				"\"y\":96,\"w\":48,\"h\":48},\"tags\":[\"Fighter\",\"Assassin\"],\"partype\":\"Energy\"}";
		try {
			leeSin = objectMapper.readValue(championResponseBody, Champion.class);
			championUri = championUriBuilder.buildAndExpand("na", leeSin.getId());
		} catch (IOException e) {
			fail("Unable to unmarshal the Champion by ID response.");
		}

		restTemplate = mock(RestTemplate.class);
	}

	@After
	public void after() {
		championsRepository.deleteAll();
	}

	@Test
	public void champions() throws Exception {
		mockServer.expect(requestTo(championsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(championsResponseBody, MediaType.APPLICATION_JSON));
		when(restTemplate.getForObject(championsUri.toString(), ChampionsResponse.class))
				.thenReturn(championsResponse);

		mockMvc.perform(get("/riot/champions"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(thresh))));
		mockServer.verify();
	}

	@Test
	public void saveChampions() throws Exception {
		mockServer.expect(requestTo(championsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(championsResponseBody, MediaType.APPLICATION_JSON));
		when(restTemplate.getForObject(championsUri.toString(), ChampionsResponse.class))
				.thenReturn(championsResponse);

		mockMvc.perform(post("/riot/champions"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(thresh))));
		mockServer.verify();

		assertThat(championsRepository.findOne(thresh.getId())).isNotNull();
	}

	@Test
	public void saveDifferenceOfChampions() throws Exception {
		mockServer.expect(requestTo(championsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(championsResponseBody, MediaType.APPLICATION_JSON));
		when(restTemplate.getForObject(championsUri.toString(), ChampionsResponse.class))
				.thenReturn(championsResponse);

		championsRepository.save(thresh);
		mockMvc.perform(post("/riot/champions"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
		mockServer.verify();

		assertThat(championsRepository.findOne(thresh.getId())).isNotNull();
	}

	@Test
	public void saveChampionsWithTruncate() throws Exception {
		String responseBody = "{\"type\":\"champion\",\"version\":\"5.16.1\",\"data\":{\"LeeSin\":{\"id\":64,\"key\":" +
				"\"LeeSin\",\"name\":\"Lee Sin\",\"title\":\"The Blind Monk\",\"image\":{\"full\":\"LeeSin.png\"," +
				"\"sprite\":\"champion1.png\",\"group\":\"champion\",\"x\":0,\"y\":96,\"w\":48,\"h\":48},\"tags\":" +
				"[\"Fighter\",\"Assassin\"],\"partype\":\"Energy\"}}}}";
		Champion leeSin = objectMapper.readValue(responseBody, ChampionsResponse.class).getChampions().get("LeeSin");
		championsRepository.save(leeSin);

		mockServer.expect(requestTo(championsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(championsResponseBody, MediaType.APPLICATION_JSON));
		when(restTemplate.getForObject(championsUri.toString(), ChampionsResponse.class))
				.thenReturn(championsResponse);

		mockMvc.perform(post("/riot/champions?truncate=true"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(thresh))));
		mockServer.verify();

		assertThat(championsRepository.findOne(leeSin.getId())).isNull();
		assertThat(championsRepository.findOne(thresh.getId())).isNotNull();
	}

	@Test
	public void champion() throws Exception {
		mockServer.expect(requestTo(championUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(championResponseBody, MediaType.APPLICATION_JSON));
		when(restTemplate.getForObject(championUri.toString(), Champion.class))
				.thenReturn(leeSin);

		mockMvc.perform(get("/riot/champions/{id}", leeSin.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(leeSin)));
		mockServer.verify();
	}

	@Test
	public void championNotFound() throws Exception {
		mockServer.expect(requestTo(championUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.NOT_FOUND));
		when(restTemplate.getForObject(championUri.toString(), Champion.class))
				.thenReturn(leeSin);

		mockMvc.perform(get("/riot/champions/{id}", leeSin.getId()))
				.andExpect(status().isNotFound());
		mockServer.verify();
	}

	@Test
	public void saveChampion() throws Exception {
		mockServer.expect(requestTo(championUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(championResponseBody, MediaType.APPLICATION_JSON));
		when(restTemplate.getForObject(championUri.toString(), Champion.class))
				.thenReturn(leeSin);

		mockMvc.perform(post("/riot/champions/{id}", leeSin.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(leeSin)));
		mockServer.verify();

		assertThat(championsRepository.findOne(leeSin.getId())).isNotNull();
	}

	@Test
	public void saveChampionNotFound() throws Exception {
		mockServer.expect(requestTo(championUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.NOT_FOUND));
		when(restTemplate.getForObject(championUri.toString(), Champion.class))
				.thenReturn(leeSin);

		mockMvc.perform(post("/riot/champions/{id}", leeSin.getId()))
				.andExpect(status().isNotFound());
		mockServer.verify();
	}

	@Test
	public void saveChampionWithOverwrite() throws Exception {
		Champion newLeeSin = objectMapper.readValue(championResponseBody, Champion.class);
		newLeeSin.setName("New Lee Sin");
		championsRepository.save(newLeeSin);

		mockServer.expect(requestTo(championUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(championResponseBody, MediaType.APPLICATION_JSON));
		when(restTemplate.getForObject(championUri.toString(), Champion.class))
				.thenReturn(leeSin);

		mockMvc.perform(post("/riot/champions/{id}?overwrite=true", leeSin.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(leeSin)));
		mockServer.verify();

		assertThat(championsRepository.findOne(leeSin.getId())).isNotNull();
		assertThat(championsRepository.findOne(leeSin.getId()).getName()).isEqualTo(leeSin.getName());
	}

	@Test
	public void saveChampionWithOverwriteAndNoPrevious() throws Exception {
		mockServer.expect(requestTo(championUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(championResponseBody, MediaType.APPLICATION_JSON));
		when(restTemplate.getForObject(championUri.toString(), Champion.class))
				.thenReturn(leeSin);

		mockMvc.perform(post("/riot/champions/{id}?overwrite=true", leeSin.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(leeSin)));
		mockServer.verify();

		assertThat(championsRepository.findOne(leeSin.getId())).isNotNull();
	}

}