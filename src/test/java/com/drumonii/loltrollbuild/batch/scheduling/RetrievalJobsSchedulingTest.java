package com.drumonii.loltrollbuild.batch.scheduling;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.service.VersionsService;
import com.drumonii.loltrollbuild.test.batch.BatchTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
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
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@BatchTest(includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = RetrievalJobsScheduling.class))
@ActiveProfiles({ TESTING })
public class RetrievalJobsSchedulingTest {

	@Autowired
	private RetrievalJobsScheduling retrievalJobsScheduling;

	@MockBean
	private VersionsService versionsService;

	@MockBean
	private JobLauncher jobLauncher;

	@MockBean
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
		Version latestVersion = new Version("8.7.1");
		given(versionsService.getLatestVersion()).willReturn(latestVersion);

		retrievalJobsScheduling.runAllRetrievalsJob();

		verify(jobLauncher, times(1)).run(eq(allRetrievalsJob), eq(buildJobParameters(latestVersion)));
	}

	@Test
	public void skipsAllRetrievalsJobRunWithNoLatestPatch() throws Exception {
		given(versionsService.getLatestVersion()).willReturn(null);

		retrievalJobsScheduling.runAllRetrievalsJob();

		verify(jobLauncher, never()).run(eq(allRetrievalsJob), any(JobParameters.class));
	}

	@Test
	public void runsAllRetrievalJobWithInstanceOfJobExecutionExceptionThrown() throws Exception {
		Version latestVersion = new Version("8.7.1");
		given(versionsService.getLatestVersion()).willReturn(latestVersion);

		given(jobLauncher.run(eq(allRetrievalsJob), eq(buildJobParameters(latestVersion))))
				.willThrow(new JobExecutionAlreadyRunningException("A job execution for this job is already running"));

		retrievalJobsScheduling.runAllRetrievalsJob();
	}

	@Test
	public void runsAllRetrievalJobWithExceptionThrown() throws Exception {
		Version latestVersion = new Version("8.7.1");
		given(versionsService.getLatestVersion()).willReturn(latestVersion);

		given(jobLauncher.run(eq(allRetrievalsJob), eq(buildJobParameters(latestVersion))))
				.willThrow(new NullPointerException());

		retrievalJobsScheduling.runAllRetrievalsJob();
	}

	private JobParameters buildJobParameters(Version latestVersion) {
		return new JobParametersBuilder()
				.addString(LATEST_PATCH_KEY, latestVersion.getPatch())
				.toJobParameters();
	}

}