package com.drumonii.loltrollbuild.batch;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static com.drumonii.loltrollbuild.batch.scheduling.RetrievalJobsScheduling.LATEST_PATCH_KEY;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({ TESTING })
public class AllRetrievalsJobConfigTest {

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
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Before
	public void before() {
		given(versionsService.getLatestVersion()).willReturn(new Version("8.1.1"));

		given(mapsService.getMaps()).willReturn(new ArrayList<>());

		given(summonerSpellsService.getSummonerSpells()).willReturn(new ArrayList<>());

		given(championsService.getChampions()).willReturn(new ArrayList<>());

		given(itemsService.getItems()).willReturn(new ArrayList<>());
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

	@TestConfiguration
	static class AllRetrievalsJobConfigTestConfiguration {

		@Bean
		public JobLauncherTestUtils jobLauncherTestUtils() {
			return new AllRetrievalsJobLauncherTestUtils();
		}

	}

	static class AllRetrievalsJobLauncherTestUtils extends JobLauncherTestUtils {

		@Autowired
		@Override
		public void setJob(@Qualifier("allRetrievalsJob") Job allRetrievalsJob) {
			super.setJob(allRetrievalsJob);
		}

	}

}