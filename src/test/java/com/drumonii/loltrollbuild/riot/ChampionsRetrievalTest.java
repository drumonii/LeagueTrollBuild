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
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
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
	private ChampionsRepository championsRepository;

	private MockRestServiceServer mockServer;
	private Champion thresh;

	@Before
	public void before() {
		super.before();
		// Only first request is handled. See: http://stackoverflow.com/q/30713734
		mockServer = MockRestServiceServer.createServer(restTemplate);
		String responseBody = "{\"type\":\"champion\",\"version\":\"5.16.1\",\"data\":{\"Thresh\":{\"id\":412,\"" +
				"key\":\"Thresh\",\"name\":\"Thresh\",\"title\":\"the Chain Warden\",\"image\":{\"full\":" +
				"\"Thresh.png\",\"sprite\":\"champion3.png\",\"group\":\"champion\",\"x\":336,\"y\":0,\"w\":48," +
				"\"h\":48},\"tags\":[\"Support\",\"Fighter\"],\"partype\":\"Mana\"}}}";
		mockServer.expect(requestTo(championsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));
		ChampionsResponse championsResponse = null;
		try {
			championsResponse = objectMapper.readValue(responseBody, ChampionsResponse.class);
			thresh = championsResponse.getChampions().get("Thresh");
		} catch (IOException e) {}
		restTemplate = mock(RestTemplate.class);
		when(restTemplate.getForObject(championsUri.toString(), ChampionsResponse.class)).thenReturn(championsResponse);
	}

	@After
	public void after() {
		championsRepository.deleteAll();
	}

	@Test
	public void champions() throws Exception {
		mockMvc.perform(get("/riot/champions"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(thresh))));
		mockServer.verify();
	}

	@Test
	public void saveChampions() throws Exception {
		mockMvc.perform(post("/riot/champions"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(thresh))));
		mockServer.verify();

		assertThat(championsRepository.findOne(thresh.getId())).isNotNull();
	}

	@Test
	public void saveDifferenceOfChampions() throws Exception {
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
				"\"LeeSin\",\"name\":\"Lee Sin\",\"title\":\"the Blind Monk\",\"image\":{\"full\":\"LeeSin.png\"," +
				"\"sprite\":\"champion1.png\",\"group\":\"champion\",\"x\":0,\"y\":96,\"w\":48,\"h\":48},\"tags\":" +
				"[\"Fighter\",\"Assassin\"],\"partype\":\"Energy\"}}}}";
		Champion leeSin = objectMapper.readValue(responseBody, ChampionsResponse.class).getChampions().get("LeeSin");
		championsRepository.save(leeSin);

		mockMvc.perform(post("/riot/champions?truncate=true"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(thresh))));
		mockServer.verify();

		assertThat(championsRepository.findOne(leeSin.getId())).isNull();
		assertThat(championsRepository.findOne(thresh.getId())).isNotNull();
	}

}