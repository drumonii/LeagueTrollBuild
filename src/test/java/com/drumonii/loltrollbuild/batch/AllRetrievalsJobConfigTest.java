package com.drumonii.loltrollbuild.batch;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;

import java.util.List;

import static com.drumonii.loltrollbuild.batch.scheduling.RetrievalJobsScheduling.LATEST_PATCH_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;

public class AllRetrievalsJobConfigTest extends BaseSpringTestRunner {

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

	@Before
	public void before() {
		super.before();

		given(restTemplate.exchange(eq(versionsUri.toString()), eq(HttpMethod.GET), isNull(HttpEntity.class),
				eq(new ParameterizedTypeReference<List<Version>>() {}))).willReturn(ResponseEntity.ok(versions));

		given(restTemplate.getForObject(eq(mapsUri.toString()), eq(MapsResponse.class)))
				.willReturn(new MapsResponse());

		given(restTemplate.getForObject(eq(summonerSpellsUri.toString()), eq(SummonerSpellsResponse.class)))
				.willReturn(new SummonerSpellsResponse());

		given(restTemplate.getForObject(eq(championsUri.toString()), eq(ChampionsResponse.class)))
				.willReturn(new ChampionsResponse());

		given(restTemplate.getForObject(eq(itemsUri.toString()), eq(ItemsResponse.class)))
				.willReturn(new ItemsResponse());

		jobLauncherTestUtils.setJob(allRetrievalsJob);
	}

	@Test
	public void runsAllRetrievalJobs() throws Exception {
		JobParameters jobParameters = jobLauncherTestUtils.getJob().getJobParametersIncrementer()
				.getNext(new JobParameters());
		assertThat(jobParameters.getParameters()).containsKey(LATEST_PATCH_KEY);

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
		assertThat(jobExecution.getJobParameters().getString(LATEST_PATCH_KEY)).isNotNull();
	}

}