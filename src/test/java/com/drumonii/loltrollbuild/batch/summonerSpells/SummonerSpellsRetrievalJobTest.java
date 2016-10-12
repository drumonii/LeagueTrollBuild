package com.drumonii.loltrollbuild.batch.summonerSpells;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
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
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class SummonerSpellsRetrievalJobTest extends BaseSpringTestRunner {

	private static final int MAX_RESPONSE_SIZE = 10;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("summonerSpells")
	private UriComponents summonerSpellsUri;

	@Autowired
	@Qualifier("summonerSpellsRetrievalJob")
	private Job summonerSpellsRetrievalJob;

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	private MockRestServiceServer mockServer;

	private SummonerSpellsResponse summonerSpellsResponseSlice;
	private String summonerSpellsResponseBody;

	private String versionsResponseBody;

	@Before
	public void before() {
		super.before();

		mockServer = MockRestServiceServer.createServer(restTemplate);

		// Create a random "slice" of summonerSpellsResponse with size of MAX_RESPONSE_SIZE
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

		try {
			versionsResponseBody = objectMapper.writeValueAsString(versions);
		} catch (JsonProcessingException e) {
			fail("Unable to marshal the Versions.", e);
		}

		jobLauncherTestUtils.setJob(summonerSpellsRetrievalJob);
	}

	@After
	public void after() {
		summonerSpellsRepository.deleteAll();
	}

	@Test
	public void savesNewSummonerSpells() throws Exception {
		mockServer.expect(requestTo(summonerSpellsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(summonerSpellsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));
		
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		assertThat(summonerSpellsRepository.findAll()).containsOnlyElementsOf(summonerSpellsResponseSlice
				.getSummonerSpells().values());
	}

	@Test
	public void savesSummonerSpellsDifference() throws Exception {
		mockServer.expect(requestTo(summonerSpellsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(summonerSpellsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		List<SummonerSpell> summonerSpells = summonerSpellsRepository.save(summonerSpellsResponseSlice
				.getSummonerSpells().values());

		SummonerSpell summonerSpellToEdit = RandomizeUtil.getRandom(summonerSpells);
		summonerSpellToEdit.setDescription("New Description");

		summonerSpellsRepository.save(summonerSpellToEdit);
		
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		assertThat(summonerSpellsRepository.findAll()).containsOnlyElementsOf(summonerSpellsResponseSlice
				.getSummonerSpells().values());
	}

	@Test
	public void deletesSummonerSpellsDifference() throws Exception {
		List<SummonerSpell> summonerSpells = summonerSpellsRepository.save(summonerSpellsResponseSlice
				.getSummonerSpells().values());

		SummonerSpell summonerSpellToDelete = RandomizeUtil.getRandom(summonerSpells);
		summonerSpellsResponseSlice.getSummonerSpells().remove(String.valueOf(summonerSpellToDelete.getId()));

		mockServer.expect(requestTo(summonerSpellsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(summonerSpellsResponseSlice),
						MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));
		
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		assertThat(summonerSpellsRepository.findAll()).containsOnlyElementsOf(summonerSpellsResponseSlice
				.getSummonerSpells().values());
	}

}