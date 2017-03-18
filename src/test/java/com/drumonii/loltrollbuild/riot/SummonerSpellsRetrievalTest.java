package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.util.List;
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

public class SummonerSpellsRetrievalTest extends BaseSpringTestRunner {

	private static final int MAX_RESPONSE_SIZE = 10;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("summonerSpells")
	private UriComponents summonerSpellsUri;

	@Autowired
	@Qualifier("summonerSpell")
	private UriComponentsBuilder summonerSpellBuilder;

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	private MockRestServiceServer mockServer;

	private SummonerSpellsResponse summonerSpellsResponseSlice;
	private String summonerSpellsResponseBody;

	private String summonerSpellResponseBody;
	private UriComponents summonerSpellUri;
	private SummonerSpell ignite;

	private String versionsResponseBody;

	@Before
	public void before() {
		super.before();

		mockServer = MockRestServiceServer.createServer(restTemplate);

		// Create a random "slice" of ItemsResponse with size of MAX_RESPONSE_SIZE
		summonerSpellsResponseSlice = new SummonerSpellsResponse();
		summonerSpellsResponseSlice.setType(championsResponse.getType());
		summonerSpellsResponseSlice.setVersion(championsResponse.getVersion());
		summonerSpellsResponseSlice.setSummonerSpells(RandomizeUtil.getRandoms(
				summonerSpellsResponse.getSummonerSpells().values(), MAX_RESPONSE_SIZE).stream()
				.collect(Collectors.toMap(spell -> String.valueOf(spell.getId()), spell -> spell)));

		try {
			summonerSpellsResponseBody = objectMapper.writeValueAsString(summonerSpellsResponseSlice);
		} catch (JsonProcessingException e) {
			fail("Unable to marshal the Summoner Spells response.", e);
		}

		ignite = summonerSpellsResponse.getSummonerSpells().get("SummonerDot");
		try {
			summonerSpellResponseBody = objectMapper.writeValueAsString(ignite);
		} catch (JsonProcessingException e) {
			fail("Unable to marshal the Summoner Spell.", e);
		}
		summonerSpellUri = summonerSpellBuilder.buildAndExpand("na", ignite.getId());

		try {
			versionsResponseBody = objectMapper.writeValueAsString(versions);
		} catch (JsonProcessingException e) {
			fail("Unable to marshal the Versions.", e);
		}
	}

	}

	@Test
	public void summonerSpells() throws Exception {
		mockServer.expect(requestTo(summonerSpellsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(summonerSpellsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(get("/riot/summoner-spells").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(summonerSpellsResponseSlice
						.getSummonerSpells().values())));
		mockServer.verify();
	}

	@Test
	public void saveSummonerSpells() throws Exception {
		mockServer.expect(requestTo(summonerSpellsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(summonerSpellsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/summoner-spells").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(summonerSpellsResponseSlice
						.getSummonerSpells().values())));
		mockServer.verify();

		assertThat(summonerSpellsRepository.findAll())
				.containsOnlyElementsOf(summonerSpellsResponseSlice.getSummonerSpells().values());
	}

	@Test
	public void saveDifferenceOfSummonerSpells() throws Exception {
		mockServer.expect(requestTo(summonerSpellsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(summonerSpellsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		List<SummonerSpell> summonerSpells = summonerSpellsRepository.save(summonerSpellsResponseSlice
				.getSummonerSpells().values());

		mockMvc.perform(post("/riot/summoner-spells").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
		mockServer.verify();

		assertThat(summonerSpellsRepository.findAll()).containsOnlyElementsOf(summonerSpells);
	}

	@Test
	public void saveDifferenceOfSummonerSpellsWithDeleted() throws Exception {
		List<SummonerSpell> summonerSpells = summonerSpellsRepository.save(summonerSpellsResponseSlice
				.getSummonerSpells().values());
		SummonerSpell summonerSpellToDelete = RandomizeUtil.getRandom(summonerSpells);
		summonerSpellsResponseSlice.getSummonerSpells().remove(String.valueOf(summonerSpellToDelete.getId()));

		mockServer.expect(requestTo(summonerSpellsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(summonerSpellsResponseSlice),
						MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/summoner-spells").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
		mockServer.verify();

		assertThat(summonerSpellsRepository.findOne(summonerSpellToDelete.getId())).isNull();
	}

	@Test
	public void saveSummonerSpellsWithTruncate() throws Exception {
		mockServer.expect(requestTo(summonerSpellsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(summonerSpellsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/summoner-spells?truncate=true").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(summonerSpellsResponseSlice
						.getSummonerSpells().values())));
		mockServer.verify();

		assertThat(summonerSpellsRepository.findAll())
				.containsOnlyElementsOf(summonerSpellsResponseSlice.getSummonerSpells().values());
	}

	@Test
	public void summonerSpell() throws Exception {
		mockServer.expect(requestTo(summonerSpellUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(summonerSpellResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(get("/riot/summoner-spells/{id}", ignite.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(ignite)));
		mockServer.verify();
	}

	@Test
	public void summonerSpellNotFound() throws Exception {
		mockServer.expect(requestTo(summonerSpellUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.NOT_FOUND));

		mockMvc.perform(get("/riot/summoner-spells/{id}", ignite.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isNotFound());
		mockServer.verify();
	}

	@Test
	public void saveSummonerSpell() throws Exception {
		mockServer.expect(requestTo(summonerSpellUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(summonerSpellResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/summoner-spells/{id}", ignite.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(ignite)));
		mockServer.verify();

		assertThat(summonerSpellsRepository.findOne(ignite.getId())).isNotNull();
	}

	@Test
	public void saveSummonerSpellNotFound() throws Exception {
		mockServer.expect(requestTo(summonerSpellUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.NOT_FOUND));

		mockMvc.perform(post("/riot/summoner-spells/{id}", ignite.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isNotFound());
		mockServer.verify();
	}

	@Test
	public void saveSummonerSpellWithOverwrite() throws Exception {
		SummonerSpell newIgnite = summonerSpellsResponse.getSummonerSpells().get("SummonerDot");
		newIgnite.setName("New Ignite");
		summonerSpellsRepository.save(newIgnite);

		mockServer.expect(requestTo(summonerSpellUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(newIgnite), MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/summoner-spells/{id}", ignite.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(newIgnite)));
		mockServer.verify();

		assertThat(summonerSpellsRepository.findOne(newIgnite.getId())).isNotNull()
				.isEqualTo(newIgnite);
	}

}
