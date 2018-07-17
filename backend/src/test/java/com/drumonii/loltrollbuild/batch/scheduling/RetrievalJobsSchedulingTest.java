package com.drumonii.loltrollbuild.batch.scheduling;

import com.drumonii.loltrollbuild.batch.AllRetrievalsJobConfig;
import com.drumonii.loltrollbuild.batch.champions.ChampionsRetrievalJobConfig;
import com.drumonii.loltrollbuild.batch.items.ItemsRetrievalJobConfig;
import com.drumonii.loltrollbuild.batch.maps.MapsRetrievalJobConfig;
import com.drumonii.loltrollbuild.batch.summonerSpells.SummonerSpellsRetrievalJobConfig;
import com.drumonii.loltrollbuild.batch.versions.VersionsRetrievalJobConfig;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.service.VersionsService;
import com.drumonii.loltrollbuild.test.batch.BatchTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static com.drumonii.loltrollbuild.batch.scheduling.RetrievalJobsScheduling.CRON_SCHEDULE;
import static com.drumonii.loltrollbuild.batch.scheduling.RetrievalJobsScheduling.LATEST_PATCH_KEY;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@BatchTest(includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = RetrievalJobsScheduling.class),
		jobs = { AllRetrievalsJobConfig.class, ChampionsRetrievalJobConfig.class, ItemsRetrievalJobConfig.class,
				MapsRetrievalJobConfig.class, SummonerSpellsRetrievalJobConfig.class, VersionsRetrievalJobConfig.class })
@ActiveProfiles({ TESTING })
public class RetrievalJobsSchedulingTest {

	@Autowired
	private RetrievalJobsScheduling retrievalJobsScheduling;

	@MockBean
	private VersionsService versionsService;

	@MockBean
	private JobLauncher jobLauncher;

	@Autowired
	@Qualifier("allRetrievalsJob")
	private Job allRetrievalsJob;

	@Test
	public void allRetrievalsCronExpression() {
		CronTrigger cronTrigger = new CronTrigger(CRON_SCHEDULE);
		Date triggerDate = Date.from(LocalDateTime.now().withHour(4).withMinute(0).withSecond(0).withNano(0)
				.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime nextExecutionTime = LocalDateTime.ofInstant(cronTrigger.nextExecutionTime(
				new SimpleTriggerContext(triggerDate, triggerDate, triggerDate)).toInstant(), ZoneId.systemDefault());
		assertThat(nextExecutionTime)
				.isEqualTo(LocalDateTime.now().plusDays(1).withHour(4).withMinute(0).withSecond(0).withNano(0));
	}

	@Test
	public void runsAllRetrievalsJob() throws Exception {
		given(versionsService.getLatestVersion()).willReturn(new Version("8.7.1"));

		retrievalJobsScheduling.runAllRetrievalsJob();

		verify(jobLauncher, times(1)).run(eq(allRetrievalsJob), any(JobParameters.class));
	}

	@Test
	public void skipsAllRetrievalsJobRunWithNoLatestPatch() throws Exception {
		given(versionsService.getLatestVersion()).willReturn(null);

		given(jobLauncher.run(eq(allRetrievalsJob), any(JobParameters.class)))
				.willThrow(new JobParametersInvalidException("The JobParameters do not contain required keys: " + LATEST_PATCH_KEY));

		retrievalJobsScheduling.runAllRetrievalsJob();
	}

	@Test
	public void runsAllRetrievalJobWithInstanceOfJobExecutionExceptionThrown() throws Exception {
		given(jobLauncher.run(eq(allRetrievalsJob), any(JobParameters.class)))
				.willThrow(new JobExecutionAlreadyRunningException("A job execution for this job is already running"));

		retrievalJobsScheduling.runAllRetrievalsJob();
	}

	@Test
	public void runsAllRetrievalJobWithExceptionThrown() throws Exception {
		given(versionsService.getLatestVersion()).willReturn(new Version("8.7.1"));

		given(jobLauncher.run(eq(allRetrievalsJob), any(JobParameters.class)))
				.willThrow(new NullPointerException());

		retrievalJobsScheduling.runAllRetrievalsJob();
	}

}