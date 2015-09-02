package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SummonerSpellsRetrievalTest extends BaseSpringTestRunner {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	@Qualifier("summonerSpells")
	private UriComponents summonerSpellsUri;

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	private MockRestServiceServer mockServer;
	private SummonerSpell cleanse;

	@Before
	public void before() {
		super.before();
		// Only first request is handled. See: http://stackoverflow.com/q/30713734
		mockServer = MockRestServiceServer.createServer(restTemplate);
		String responseBody = "{\"type\":\"summoner\",\"version\":\"5.16.1\",\"data\":{\"SummonerBoost\":{\"name\":" +
				"\"Cleanse\",\"description\":\"Removes all disables and summoner spell debuffs affecting your " +
				"champion and lowers the duration of incoming disables by 65% for 3 seconds.\",\"image\":{\"full\":" +
				"\"SummonerBoost.png\",\"sprite\":\"spell0.png\",\"group\":\"spell\",\"x\":48,\"y\":0,\"w\":48,\"h\"" +
				":48},\"cooldown\":[210.0],\"summonerLevel\":6,\"id\":1,\"key\":\"SummonerBoost\",\"modes\":" +
				"[\"CLASSIC\",\"ODIN\",\"TUTORIAL\",\"ARAM\",\"ASCENSION\"]}}}";
		mockServer.expect(requestTo(summonerSpellsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));
		SummonerSpellsResponse spellsResponse = null;
		try {
			spellsResponse = objectMapper.readValue(responseBody, SummonerSpellsResponse.class);
			cleanse = spellsResponse.getSummonerSpells().get("SummonerBoost");
		} catch (IOException e) {}
		restTemplate = mock(RestTemplate.class);
		when(restTemplate.getForObject(summonerSpellsUri.toString(), SummonerSpellsResponse.class))
				.thenReturn(spellsResponse);
	}

	@After
	public void after() {
		summonerSpellsRepository.deleteAll();
	}

	@Test
	public void summonerSpells() throws Exception {
		mockMvc.perform(get("/riot/summoner-spells"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(cleanse))));
		mockServer.verify();
	}

	@Test
	public void saveSummonerSpells() throws Exception {
		mockMvc.perform(post("/riot/summoner-spells"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(cleanse))));
		mockServer.verify();

		assertThat(summonerSpellsRepository.findOne(cleanse.getId())).isNotNull();
	}

	@Test
	public void saveDifferenceOfSummonerSpells() throws Exception {
		summonerSpellsRepository.save(cleanse);
		mockMvc.perform(post("/riot/summoner-spells"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
		mockServer.verify();

		assertThat(summonerSpellsRepository.findOne(cleanse.getId())).isNotNull();
	}

	@Test
	public void saveSummonerSpellsWithTruncate() throws Exception {
		String responseBody = "{\"type\":\"summoner\",\"version\":\"5.16.1\",\"data\":{\"SummonerFlash\":{\"name\":" +
				"\"Flash\",\"description\":\"Teleports your champion a short distance toward your cursor's location." +
				"\",\"image\":{\"full\":\"SummonerFlash.png\",\"sprite\":\"spell0.png\",\"group\":\"spell\",\"x\":" +
				"240,\"y\":0,\"w\":48,\"h\":48},\"cooldown\":[300.0],\"summonerLevel\":8,\"id\":4,\"key\":" +
				"\"SummonerFlash\",\"modes\":[\"CLASSIC\",\"ODIN\",\"TUTORIAL\",\"ARAM\",\"ASCENSION\"]}}}";
		SummonerSpell teleport = objectMapper.readValue(responseBody, SummonerSpellsResponse.class).getSummonerSpells()
				.get("SummonerFlash");
		summonerSpellsRepository.save(teleport);

		mockMvc.perform(post("/riot/summoner-spells?truncate=true"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(cleanse))));
		mockServer.verify();

		assertThat(summonerSpellsRepository.findOne(teleport.getId())).isNull();
		assertThat(summonerSpellsRepository.findOne(cleanse.getId())).isNotNull();
	}

}
