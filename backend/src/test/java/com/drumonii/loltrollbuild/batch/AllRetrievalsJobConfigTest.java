package com.drumonii.loltrollbuild.batch;

import com.drumonii.loltrollbuild.batch.champions.ChampionsRetrievalJobConfig;
import com.drumonii.loltrollbuild.batch.items.ItemsRetrievalJobConfig;
import com.drumonii.loltrollbuild.batch.maps.MapsRetrievalJobConfig;
import com.drumonii.loltrollbuild.batch.summonerSpells.SummonerSpellsRetrievalJobConfig;
import com.drumonii.loltrollbuild.batch.versions.VersionsRetrievalJobConfig;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.service.*;
import com.drumonii.loltrollbuild.test.batch.BatchTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static com.drumonii.loltrollbuild.batch.scheduling.RetrievalJobsScheduling.LATEST_PATCH_KEY;
import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@BatchTest({ AllRetrievalsJobConfig.class, ChampionsRetrievalJobConfig.class, ItemsRetrievalJobConfig.class,
		MapsRetrievalJobConfig.class, SummonerSpellsRetrievalJobConfig.class, VersionsRetrievalJobConfig.class })
@ActiveProfiles({ TESTING, DDRAGON })
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

	@Test
	public void runsAllRetrievalJob() throws Exception {
		Version latestVersion = new Version("8.1.1");
		given(versionsService.getLatestVersion()).willReturn(latestVersion);
		given(mapsService.getMaps()).willReturn(new ArrayList<>());
		given(summonerSpellsService.getSummonerSpells()).willReturn(new ArrayList<>());
		given(championsService.getChampions()).willReturn(new ArrayList<>());
		given(itemsService.getItems()).willReturn(new ArrayList<>());

		JobParameters jobParameters = getJobParameters();
		assertThat(jobParameters.getParameters()).containsKey(LATEST_PATCH_KEY);

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.FAILED);

		assertThat(jobExecution.getJobParameters().getString(LATEST_PATCH_KEY)).isEqualTo(latestVersion.getPatch());
		assertThat(jobExecution.getJobParameters().getString("another_param")).isNotNull();
		assertThat(jobExecution.getStepExecutions()).extracting(StepExecution::getStepName)
				.containsOnly("versionsRetrievalJobStep", "mapsRetrievalJobStep", "summonerSpellsRetrievalJobStep",
						"championsRetrievalJobStep", "itemsRetrievalJobStep");
	}

	@Test
	public void doesNotRunsAllRetrievalJobWithNoLatestPatch() {
		given(versionsService.getLatestVersion()).willReturn(null);

		JobParameters jobParameters = getJobParameters();

		try {
			jobLauncherTestUtils.launchJob(jobParameters);
			fail("Expected JobParametersInvalidException to be thrown");
		} catch (Exception e) {
			assertThat(e).isInstanceOf(JobParametersInvalidException.class);
		}
	}

	private JobParameters getJobParameters() {
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder()
				.addString("another_param", "another_param_value");
		return jobLauncherTestUtils.getJob().getJobParametersIncrementer().getNext(jobParametersBuilder.toJobParameters());
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
