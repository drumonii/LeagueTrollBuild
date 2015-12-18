package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
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
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
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
	@Qualifier("summonerSpell")
	private UriComponentsBuilder summonerSpellBuilder;

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	private VersionsRepository versionsRepository;

	private MockRestServiceServer mockServer;

	private String summonerSpellsResponseBody;
	private SummonerSpell cleanse;

	private String summonerSpellResponseBody;
	private UriComponents summonerSpellUri;
	private SummonerSpell ignite;

	@Before
	public void before() {
		super.before();

		// Only first request is handled. See: http://stackoverflow.com/q/30713734
		mockServer = MockRestServiceServer.createServer(restTemplate);
		summonerSpellsResponseBody = "{\"type\":\"summoner\",\"version\":\"5.22.3\",\"data\":{\"SummonerBoost\":" +
				"{\"name\":\"Cleanse\",\"description\":\"Removes all disables and summoner spell debuffs affecting " +
				"your champion and lowers the duration of incoming disables by 65% for 3 seconds.\",\"image\":" +
				"{\"full\":\"SummonerBoost.png\",\"sprite\":\"spell0.png\",\"group\":\"spell\",\"x\":48,\"y\":0," +
				"\"w\":48,\"h\":48},\"cooldown\":[210.0],\"summonerLevel\":6,\"id\":1,\"key\":\"SummonerBoost\"," +
				"\"modes\":[\"CLASSIC\",\"ODIN\",\"TUTORIAL\",\"ARAM\",\"ASCENSION\"]}}}";
		try {
			cleanse = objectMapper.readValue(summonerSpellsResponseBody, SummonerSpellsResponse.class)
					.getSummonerSpells().get("SummonerBoost");
		} catch (IOException e) {
			fail("Unable to marshal the Summoner Spells response.", e);
		}

		summonerSpellResponseBody = "{\"name\":\"Ignite\",\"description\":\"Ignites target enemy champion, dealing " +
				"70-410 true damage (depending on champion level) over 5 seconds, grants you vision of the target, " +
				"and reduces healing effects on them for the duration.\",\"image\":{\"full\":\"SummonerDot.png\"," +
				"\"sprite\":\"spell0.png\",\"group\":\"spell\",\"x\":144,\"y\":0,\"w\":48,\"h\":48},\"cooldown\":" +
				"[180],\"summonerLevel\":10,\"id\":14,\"key\":\"SummonerDot\",\"modes\":[\"CLASSIC\",\"ODIN\"," +
				"\"TUTORIAL\",\"ARAM\",\"ASCENSION\"]}";
		try {
			ignite = objectMapper.readValue(summonerSpellResponseBody, SummonerSpell.class);
			summonerSpellUri = summonerSpellBuilder.buildAndExpand("na", ignite.getId());
		} catch (IOException e) {
			fail("Unable to unmarshal the Summoner Spell by ID response.", e);
		}

		versionsRepository.save(new Version("latest patch version"));
	}

	@After
	public void after() {
		summonerSpellsRepository.deleteAll();
		versionsRepository.deleteAll();
	}

	@Test
	public void summonerSpells() throws Exception {
		mockServer.expect(requestTo(summonerSpellsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(summonerSpellsResponseBody, MediaType.APPLICATION_JSON));

		mockMvc.perform(get("/riot/summoner-spells").with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(cleanse))));
		mockServer.verify();
	}

	@Test
	public void saveSummonerSpells() throws Exception {
		mockServer.expect(requestTo(summonerSpellsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(summonerSpellsResponseBody, MediaType.APPLICATION_JSON));

		mockMvc.perform(post("/riot/summoner-spells").with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(cleanse))));
		mockServer.verify();

		assertThat(summonerSpellsRepository.findOne(cleanse.getId())).isNotNull();
	}

	@Test
	public void saveSummonerSpellsNoPatchVersion() throws Exception {
		mockServer.expect(requestTo(summonerSpellsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(summonerSpellsResponseBody, MediaType.APPLICATION_JSON));

		versionsRepository.deleteAll();

		mockMvc.perform(post("/riot/summoner-spells").with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isNotFound());
		mockServer.verify();
	}

	@Test
	public void saveDifferenceOfSummonerSpells() throws Exception {
		mockServer.expect(requestTo(summonerSpellsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(summonerSpellsResponseBody, MediaType.APPLICATION_JSON));
		summonerSpellsRepository.save(cleanse);

		mockMvc.perform(post("/riot/summoner-spells").with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
		mockServer.verify();

		assertThat(summonerSpellsRepository.findOne(cleanse.getId())).isNotNull();
	}

	@Test
	public void saveDifferenceOfSummonerSpellsWithDeleted() throws Exception {
		mockServer.expect(requestTo(summonerSpellsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(summonerSpellsResponseBody, MediaType.APPLICATION_JSON));
		summonerSpellsRepository.save(cleanse);
		summonerSpellsRepository.save(ignite);

		mockMvc.perform(post("/riot/summoner-spells").with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
		mockServer.verify();

		assertThat(summonerSpellsRepository.findOne(cleanse.getId())).isNotNull();
		assertThat(summonerSpellsRepository.findOne(ignite.getId())).isNull();
	}

	@Test
	public void saveSummonerSpellsWithTruncate() throws Exception {
		String responseBody = "{\"type\":\"summoner\",\"version\":\"5.22.3\",\"data\":{\"SummonerFlash\":{\"name\":" +
				"\"Flash\",\"description\":\"Teleports your champion a short distance toward your cursor's location." +
				"\",\"image\":{\"full\":\"SummonerFlash.png\",\"sprite\":\"spell0.png\",\"group\":\"spell\",\"x\":" +
				"240,\"y\":0,\"w\":48,\"h\":48},\"cooldown\":[300],\"summonerLevel\":8,\"id\":4,\"key\":" +
				"\"SummonerFlash\",\"modes\":[\"CLASSIC\",\"ODIN\",\"TUTORIAL\",\"ARAM\",\"ASCENSION\"]}}}";
		SummonerSpell teleport = null;
		try {
			teleport = objectMapper.readValue(responseBody, SummonerSpellsResponse.class).getSummonerSpells()
					.get("SummonerFlash");
		} catch (IOException e) {
			fail("Unable to unmarshal the Summoner Spell by ID response.", e);
		}
		summonerSpellsRepository.save(teleport);

		mockServer.expect(requestTo(summonerSpellsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(summonerSpellsResponseBody, MediaType.APPLICATION_JSON));

		mockMvc.perform(post("/riot/summoner-spells?truncate=true").with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(cleanse))));
		mockServer.verify();

		assertThat(summonerSpellsRepository.findOne(teleport.getId())).isNull();
		assertThat(summonerSpellsRepository.findOne(cleanse.getId())).isNotNull();
	}

	@Test
	public void summonerSpell() throws Exception {
		mockServer.expect(requestTo(summonerSpellUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(summonerSpellResponseBody, MediaType.APPLICATION_JSON));

		mockMvc.perform(get("/riot/summoner-spells/{id}", ignite.getId()).with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(ignite)));
		mockServer.verify();
	}

	@Test
	public void summonerSpellNotFound() throws Exception {
		mockServer.expect(requestTo(summonerSpellUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.NOT_FOUND));

		mockMvc.perform(get("/riot/summoner-spells/{id}", ignite.getId()).with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isNotFound());
		mockServer.verify();
	}

	@Test
	public void saveSummonerSpell() throws Exception {
		mockServer.expect(requestTo(summonerSpellUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(summonerSpellResponseBody, MediaType.APPLICATION_JSON));

		mockMvc.perform(post("/riot/summoner-spells/{id}", ignite.getId()).with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(ignite)));
		mockServer.verify();

		assertThat(summonerSpellsRepository.findOne(ignite.getId())).isNotNull();
	}

	@Test
	public void saveSummonerSpellNotFound() throws Exception {
		mockServer.expect(requestTo(summonerSpellUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.NOT_FOUND));

		mockMvc.perform(post("/riot/summoner-spells/{id}", ignite.getId()).with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isNotFound());
		mockServer.verify();
	}

	@Test
	public void saveChampionNoPatchVersion() throws Exception {
		mockServer.expect(requestTo(summonerSpellUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(summonerSpellResponseBody, MediaType.APPLICATION_JSON));

		versionsRepository.deleteAll();

		mockMvc.perform(post("/riot/summoner-spells/{id}", ignite.getId()).with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isNotFound());
		mockServer.verify();
	}

	@Test
	public void saveSummonerSpellWithOverwrite() throws Exception {
		SummonerSpell newIgnite = null;
		try {
			newIgnite = objectMapper.readValue(summonerSpellResponseBody, SummonerSpell.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Summoner Spell by ID response.", e);
		}
		newIgnite.setName("New Ignite");
		summonerSpellsRepository.save(newIgnite);

		mockServer.expect(requestTo(summonerSpellUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(summonerSpellResponseBody, MediaType.APPLICATION_JSON));

		mockMvc.perform(post("/riot/summoner-spells/{id}", ignite.getId()).with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(ignite)));
		mockServer.verify();

		assertThat(summonerSpellsRepository.findOne(ignite.getId())).isNotNull();
		assertThat(summonerSpellsRepository.findOne(ignite.getId()).getName()).isEqualTo(ignite.getName());
	}

}
