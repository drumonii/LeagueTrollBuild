package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChampionsRetrievalTest extends BaseSpringTestRunner {

	private static final int MAX_RESPONSE_SIZE = 10;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("champions")
	private UriComponents championsUri;

	@Autowired
	@Qualifier("champion")
	private UriComponentsBuilder championUriBuilder;

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	@Autowired
	private ChampionsRepository championsRepository;

	private MockRestServiceServer mockServer;

	private ChampionsResponse championsResponseSlice;
	private String championsResponseBody;

	private String championResponseBody;
	private UriComponents championUri;
	private Champion leeSin;

	private String versionsResponseBody;

	@Before
	public void before() {
		super.before();

		mockServer = MockRestServiceServer.createServer(restTemplate);

		// Create a random "slice" of ChampionsResponse with size of MAX_RESPONSE_SIZE
		championsResponseSlice = new ChampionsResponse();
		championsResponseSlice.setType(championsResponse.getType());
		championsResponseSlice.setVersion(championsResponse.getVersion());
		championsResponseSlice.setChampions(RandomizeUtil.getRandoms(
				championsResponse.getChampions().values(), MAX_RESPONSE_SIZE).stream()
				.collect(Collectors.toMap(champion -> String.valueOf(champion.getId()), champion -> champion)));

		try {
			championsResponseBody = objectMapper.writeValueAsString(championsResponseSlice);
		} catch (JsonProcessingException e) {
			fail("Unable to marshal the Champions response.", e);
		}

		leeSin = championsResponse.getChampions().get("LeeSin");
		try {
			championResponseBody = objectMapper.writeValueAsString(leeSin);
		} catch (JsonProcessingException e) {
			fail("Unable to marshal the Champion.", e);
		}
		championUri = championUriBuilder.buildAndExpand("na", leeSin.getId());

		try {
			versionsResponseBody = objectMapper.writeValueAsString(versions);
		} catch (JsonProcessingException e) {
			fail("Unable to marshal the Versions.", e);
		}
	}

	@After
	public void after() {
		championsRepository.deleteAll();
	}

	@Test
	public void champions() throws Exception {
		mockServer.expect(requestTo(championsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(championsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(get("/riot/champions").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(championsResponseSlice.getChampions()
						.values())));
		mockServer.verify();
	}

	@Test
	public void saveChampions() throws Exception {
		mockServer.expect(requestTo(championsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(championsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/champions").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(championsResponseSlice.getChampions()
						.values())));
		mockServer.verify();

		assertThat(championsRepository.findAll())
				.containsOnlyElementsOf(championsResponseSlice.getChampions().values());
	}

	@Test
	public void saveDifferenceOfChampions() throws Exception {
		mockServer.expect(requestTo(championsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(championsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		List<Champion> champions = championsRepository.save(championsResponseSlice.getChampions().values());

		mockMvc.perform(post("/riot/champions").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
		mockServer.verify();

		assertThat(championsRepository.findAll()).containsOnlyElementsOf(champions);
	}

	@Test
	public void saveDifferenceOfChampionsWithDeleted() throws Exception {
		List<Champion> champions = championsRepository.save(championsResponseSlice.getChampions().values());
		Champion championToDelete = RandomizeUtil.getRandom(champions);
		championsResponseSlice.getChampions().remove(String.valueOf(championToDelete.getId()));

		mockServer.expect(requestTo(championsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(championsResponseSlice),
						MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/champions").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
		mockServer.verify();

		assertThat(championsRepository.findOne(championToDelete.getId())).isNull();
	}

	@Test
	public void saveChampionsWithTruncate() throws Exception {
		mockServer.expect(requestTo(championsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(championsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/champions?truncate=true").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(championsResponseSlice.getChampions()
						.values())));
		mockServer.verify();

		assertThat(championsRepository.findAll())
				.containsOnlyElementsOf(championsResponseSlice.getChampions().values());
	}

	@Test
	public void champion() throws Exception {
		mockServer.expect(requestTo(championUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(championResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(get("/riot/champions/{id}", leeSin.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(leeSin)));
		mockServer.verify();
	}

	@Test
	public void championNotFound() throws Exception {
		mockServer.expect(requestTo(championUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.NOT_FOUND));

		mockMvc.perform(get("/riot/champions/{id}", leeSin.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isNotFound());
		mockServer.verify();
	}

	@Test
	public void saveChampion() throws Exception {
		mockServer.expect(requestTo(championUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(championResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/champions/{id}", leeSin.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(leeSin)));
		mockServer.verify();

		assertThat(championsRepository.findOne(leeSin.getId())).isNotNull();
	}

	@Test
	public void saveChampionNotFound() throws Exception {
		mockServer.expect(requestTo(championUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.NOT_FOUND));

		mockMvc.perform(post("/riot/champions/{id}", leeSin.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isNotFound());
		mockServer.verify();
	}

	@Test
	public void saveChampionWithOverwrite() throws Exception {
		Champion newLeeSin = championsResponse.getChampions().get("LeeSin");
		newLeeSin.setTags(new TreeSet<>(Arrays.asList("NEW_TAG")));
		newLeeSin = championsRepository.save(newLeeSin);

		mockServer.expect(requestTo(championUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(newLeeSin), MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/champions/{id}", leeSin.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(newLeeSin)));
		mockServer.verify();

		assertThat(championsRepository.findOne(newLeeSin.getId())).isNotNull()
				.isEqualTo(newLeeSin);
	}

}