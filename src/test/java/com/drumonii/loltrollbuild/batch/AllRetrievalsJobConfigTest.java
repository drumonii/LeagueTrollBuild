package com.drumonii.loltrollbuild.batch;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.riot.service.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;

import static com.drumonii.loltrollbuild.batch.scheduling.RetrievalJobsScheduling.LATEST_PATCH_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class AllRetrievalsJobConfigTest extends BaseSpringTestRunner {

	@MockBean
	private VersionsService versionsService;

	@MockBean
	private MapsService mapsService;

	@MockBean
	private SummonerSpellsService summonerSpellsService;

	@MockBean
	private ChampionsService championsService;

	@MockBean
	private ItemsService itemsService;

	@Autowired
	@Qualifier("allRetrievalsJob")
	private Job allRetrievalsJob;

	@Before
	public void before() {
		super.before();

		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

		given(mapsService.getMaps()).willReturn(new ArrayList<>());

		given(summonerSpellsService.getSummonerSpells()).willReturn(new ArrayList<>());

		given(championsService.getChampions()).willReturn(new ArrayList<>());

		given(itemsService.getItems()).willReturn(new ArrayList<>());

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
		assertThat(jobExecution.getStepExecutions()).extracting(StepExecution::getStepName)
				.containsExactly("versionsRetrievalJobStep", "mapsRetrievalJobStep", "summonerSpellsRetrievalJobStep",
						"championsRetrievalJobStep", "itemsRetrievalJobStep");
	}

}