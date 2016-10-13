package com.drumonii.loltrollbuild.batch;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.springframework.test.web.client.ExpectedCount.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class AllRetrievalsJobTest extends BaseSpringTestRunner {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	@Autowired
	@Qualifier("maps")
	private UriComponents mapsUri;

	@Autowired
	@Qualifier("summonerSpells")
	private UriComponents summonerSpellsUri;

	@Autowired
	@Qualifier("champions")
	private UriComponents championsUri;

	@Autowired
	@Qualifier("items")
	private UriComponents itemsUri;

	@Autowired
	@Qualifier("allRetrievalsJob")
	private Job allRetrievalsJob;

	private MockRestServiceServer mockServer;

	@Before
	public void before() {
		super.before();

		mockServer = MockRestServiceServer.createServer(restTemplate);

		String versionsResponseBody = null;
		try {
			versionsResponseBody = objectMapper.writeValueAsString(new ArrayList<Version>());
		} catch (JsonProcessingException e) {
			Assertions.fail("Unable to marshal the Versions.", e);
		}

		// times(2) fails with incorrect order for some reason
		mockServer.expect(requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));
		mockServer.expect(requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		String mapsResponseBody = null;
		try {
			mapsResponseBody = objectMapper.writeValueAsString(new MapsResponse());
		} catch (JsonProcessingException e) {
			fail("Unable to marshal the Maps response.", e);
		}

		mockServer.expect(requestTo(mapsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(mapsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		String summonerSpellsResponseBody = null;
		try {
			summonerSpellsResponseBody = objectMapper.writeValueAsString(new SummonerSpellsResponse());
		} catch (JsonProcessingException e) {
			fail("Unable to marshal the Summoner Spells response.", e);
		}

		mockServer.expect(requestTo(summonerSpellsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(summonerSpellsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		String championsResponseBody = null;
		try {
			championsResponseBody = objectMapper.writeValueAsString(new ChampionsResponse());
		} catch (JsonProcessingException e) {
			fail("Unable to marshal the Champions response.", e);
		}

		mockServer.expect(requestTo(championsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(championsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		String itemsResponseBody = null;
		try {
			itemsResponseBody = objectMapper.writeValueAsString(new ItemsResponse());
		} catch (JsonProcessingException e) {
			fail("Unable to marshal the Items response.", e);
		}

		mockServer.expect(requestTo(itemsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		jobLauncherTestUtils.setJob(allRetrievalsJob);
	}

	@Test
	public void runsAllRetrievalJobs() throws Exception {
		JobParameters jobParameters = jobLauncherTestUtils.getJob().getJobParametersIncrementer().getNext(null);
		assertThat(jobParameters.getParameters()).containsKey("latestRiotPatch");

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
		mockServer.verify();
	}

}