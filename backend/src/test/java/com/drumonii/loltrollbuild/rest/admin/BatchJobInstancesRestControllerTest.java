package com.drumonii.loltrollbuild.rest.admin;

import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import com.drumonii.loltrollbuild.test.batch.BatchDaoTestConfig;
import com.drumonii.loltrollbuild.test.rest.WebMvcRestTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcRestTest(BatchJobInstancesRestController.class)
@Import(BatchDaoTestConfig.class)
@ActiveProfiles({ TESTING })
public class BatchJobInstancesRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JobInstanceDao jobInstanceDao;

	@Autowired
	private JobExecutionDao jobExecutionDao;

	@Autowired
	private StepExecutionDao stepExecutionDao;

	@MockBean
	private JobLauncher jobLauncher;

	@MockBean
	@Qualifier("allRetrievalsJob")
	private Job allRetrievalsJob;

	@Value("${api.base-path}")
	private String apiPath;

	private JobInstance jobInstance;

	@Before
	public void before() {
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("someJobParameter", "someJobParameterValue")
				.toJobParameters();

		jobInstance = jobInstanceDao.createJobInstance("jobName", jobParameters);

		jobExecutionDao.saveJobExecution(new JobExecution(jobInstance, jobParameters, "jobConfigName"));
		JobExecution jobExecution = jobExecutionDao.getLastJobExecution(jobInstance);

		stepExecutionDao.saveStepExecution(new StepExecution("stepName", jobExecution));
	}

	@WithMockAdminUser
	@Test
	public void getBatchJobInstances() throws Exception {
		// qbe
		mockMvc.perform(get("{apiPath}/admin/job-instances", apiPath))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with name
		mockMvc.perform(get("{apiPath}/admin/job-instances", apiPath)
				.param("name", jobInstance.getJobName().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with no results
		mockMvc.perform(get("{apiPath}/admin/job-instances", apiPath)
				.param("name", "abcd1234"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());
	}

	@WithMockAdminUser
	@Test
	public void getBatchJobInstance() throws Exception {
		mockMvc.perform(get("{apiPath}/admin/job-instances/{jobInstanceId}", apiPath, jobInstance.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.jobExecution").exists());

		mockMvc.perform(get("{apiPath}/admin/job-instances/{jobInstanceId}", apiPath, -1))
				.andExpect(status().isNotFound());
	}

	@WithMockAdminUser
	@Test
	public void restartAllRetrievalsJob() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("someJobParameter", "someJobParameterValue")
				.toJobParameters();

		JobInstance allRetrievalsJobInstance = jobInstanceDao.createJobInstance("allRetrievalsJob", jobParameters);

		JobExecution allRetrievalsJobExecution = new JobExecution(allRetrievalsJobInstance, jobParameters);
		allRetrievalsJobExecution.setStartTime(new Date());
		allRetrievalsJobExecution.setEndTime(new Date());
		allRetrievalsJobExecution.setLastUpdated(new Date());
		allRetrievalsJobExecution.setStatus(BatchStatus.COMPLETED);
		jobExecutionDao.saveJobExecution(allRetrievalsJobExecution);

		allRetrievalsJobExecution = jobExecutionDao.getLastJobExecution(jobInstance);
		if (allRetrievalsJobExecution == null) {
			fail("Unable to get the last job execution");
		}

		jobExecutionDao.updateJobExecution(allRetrievalsJobExecution);

		JobExecution jobExecution = jobExecutionDao.getLastJobExecution(allRetrievalsJobInstance);

		stepExecutionDao.saveStepExecution(new StepExecution("allRetrievalsJobStep", jobExecution));

		given(jobLauncher.run(eq(this.allRetrievalsJob), any(JobParameters.class)))
				.willReturn(jobExecution);

		given(this.allRetrievalsJob.getJobParametersIncrementer()).willReturn(new RunIdIncrementer());

		mockMvc.perform(post("{apiPath}/admin/job-instances/restart", apiPath).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.version").exists())
				.andExpect(jsonPath("$.createTime").exists())
				.andExpect(jsonPath("$.startTime").exists())
				.andExpect(jsonPath("$.endTime").exists())
				.andExpect(jsonPath("$.status").exists())
				.andExpect(jsonPath("$.exitCode").exists())
				.andExpect(jsonPath("$.exitMessage").isEmpty())
				.andExpect(jsonPath("$.lastUpdated").exists());

		ArgumentCaptor<JobParameters> jobParametersArgument = ArgumentCaptor.forClass(JobParameters.class);
		verify(jobLauncher, times(1)).run(eq(this.allRetrievalsJob), jobParametersArgument.capture());
		assertThat(jobParametersArgument.getValue().getParameters()).containsKey("forced");

		given(jobLauncher.run(eq(this.allRetrievalsJob), any(JobParameters.class)))
				.willThrow(new NullPointerException());

		mockMvc.perform(post("{apiPath}/admin/job-instances/restart", apiPath).with(csrf()))
				.andExpect(status().isInternalServerError());
	}

	@WithMockAdminUser
	@Test
	public void hasRecentFailedAllRetrievalsJobDueToHangingJob() throws Exception {
		// Save a FAILED allRetrievalsJob as least recent

		JobParameters failedJobParameters = new JobParametersBuilder()
				.addString("failedJobParameter", "someJobParameterValue")
				.toJobParameters();

		JobInstance failedAllRetrievalsJobInstance = jobInstanceDao.createJobInstance("allRetrievalsJob", failedJobParameters);
		JobExecution failedAllRetrievalsJobExecution = new JobExecution(failedAllRetrievalsJobInstance, failedJobParameters);

		LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1L);
		Date failedDate = Date.from(oneHourAgo.atZone(ZoneId.systemDefault()).toInstant());

		failedAllRetrievalsJobExecution.setCreateTime(failedDate);
		failedAllRetrievalsJobExecution.setStartTime(failedDate);
		failedAllRetrievalsJobExecution.setEndTime(failedDate);
		failedAllRetrievalsJobExecution.setLastUpdated(failedDate);
		failedAllRetrievalsJobExecution.setStatus(BatchStatus.FAILED);
		jobExecutionDao.saveJobExecution(failedAllRetrievalsJobExecution);

		// Save a COMPLETED allRetrievalsJob as more recent

		JobParameters completedJobParameters = new JobParametersBuilder()
				.addString("completedJobParameters", "someJobParameterValue")
				.toJobParameters();

		JobInstance completedAllRetrievalsJobInstance = jobInstanceDao.createJobInstance("allRetrievalsJob", completedJobParameters);
		JobExecution completedAllRetrievalsJobExecution = new JobExecution(completedAllRetrievalsJobInstance, completedJobParameters);

		LocalDateTime thirtyMinutesAgo = LocalDateTime.now().minusMinutes(30L);
		Date completedDate = Date.from(thirtyMinutesAgo.atZone(ZoneId.systemDefault()).toInstant());

		completedAllRetrievalsJobExecution.setCreateTime(completedDate);
		completedAllRetrievalsJobExecution.setStartTime(completedDate);
		completedAllRetrievalsJobExecution.setEndTime(completedDate);
		completedAllRetrievalsJobExecution.setLastUpdated(completedDate);
		completedAllRetrievalsJobExecution.setStatus(BatchStatus.COMPLETED);
		jobExecutionDao.saveJobExecution(completedAllRetrievalsJobExecution);

		// Save a STARTING allRetrievalsJob as most recent

		JobParameters hangingJobParameters = new JobParametersBuilder()
				.addString("hangingJobParameter", "someJobParameterValue")
				.toJobParameters();

		JobInstance hangingAllRetrievalsJobInstance = jobInstanceDao.createJobInstance("allRetrievalsJob", hangingJobParameters);
		JobExecution hangingAllRetrievalsJobExecution = new JobExecution(hangingAllRetrievalsJobInstance, hangingJobParameters);

		LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10L);
		Date hangingDate = Date.from(tenMinutesAgo.atZone(ZoneId.systemDefault()).toInstant());

		hangingAllRetrievalsJobExecution.setCreateTime(hangingDate);
		hangingAllRetrievalsJobExecution.setStartTime(hangingDate);
		hangingAllRetrievalsJobExecution.setLastUpdated(hangingDate);
		hangingAllRetrievalsJobExecution.setStatus(BatchStatus.STARTING);
		jobExecutionDao.saveJobExecution(hangingAllRetrievalsJobExecution);

		mockMvc.perform(get("{apiPath}/admin/job-instances/has-failed-all-retrievals-job", apiPath, jobInstance.getId())
				.param("minutes", "1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("hasFailedAllRetrievalsJob", is(true)));
	}

	@WithMockAdminUser
	@Test
	public void hasRecentFailedAllRetrievalsJobDueToFailedJob() throws Exception {
		// Save a COMPLETED allRetrievalsJob as least recent

		JobParameters completedJobParameters = new JobParametersBuilder()
				.addString("completedJobParameters", "someJobParameterValue")
				.toJobParameters();

		JobInstance completedAllRetrievalsJobInstance = jobInstanceDao.createJobInstance("allRetrievalsJob", completedJobParameters);
		JobExecution completedAllRetrievalsJobExecution = new JobExecution(completedAllRetrievalsJobInstance, completedJobParameters);

		LocalDateTime thirtyMinutesAgo = LocalDateTime.now().minusMinutes(30L);
		Date completedDate = Date.from(thirtyMinutesAgo.atZone(ZoneId.systemDefault()).toInstant());

		completedAllRetrievalsJobExecution.setCreateTime(completedDate);
		completedAllRetrievalsJobExecution.setStartTime(completedDate);
		completedAllRetrievalsJobExecution.setEndTime(completedDate);
		completedAllRetrievalsJobExecution.setLastUpdated(completedDate);
		completedAllRetrievalsJobExecution.setStatus(BatchStatus.COMPLETED);
		jobExecutionDao.saveJobExecution(completedAllRetrievalsJobExecution);

		// Save a FAILED allRetrievalsJob as most recent

		JobParameters failedJobParameters = new JobParametersBuilder()
				.addString("failedJobParameter", "someJobParameterValue")
				.toJobParameters();

		JobInstance failedAllRetrievalsJobInstance = jobInstanceDao.createJobInstance("allRetrievalsJob", failedJobParameters);
		JobExecution failedAllRetrievalsJobExecution = new JobExecution(failedAllRetrievalsJobInstance, failedJobParameters);

		LocalDateTime threeMinutesAgo = LocalDateTime.now().minusMinutes(3L);
		Date failedDate = Date.from(threeMinutesAgo.atZone(ZoneId.systemDefault()).toInstant());

		failedAllRetrievalsJobExecution.setCreateTime(failedDate);
		failedAllRetrievalsJobExecution.setStartTime(failedDate);
		failedAllRetrievalsJobExecution.setEndTime(failedDate);
		failedAllRetrievalsJobExecution.setLastUpdated(failedDate);
		failedAllRetrievalsJobExecution.setStatus(BatchStatus.FAILED);
		jobExecutionDao.saveJobExecution(failedAllRetrievalsJobExecution);

		mockMvc.perform(get("{apiPath}/admin/job-instances/has-failed-all-retrievals-job", apiPath, jobInstance.getId())
				.param("minutes", "1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("hasFailedAllRetrievalsJob", is(true)));
	}

	@WithMockAdminUser
	@Test
	public void hasNoRecentFailedAllRetrievalsJobDueToCompletedJob() throws Exception {
		// Save a COMPLETED allRetrievalsJob as most recent

		JobParameters completedJobParameters = new JobParametersBuilder()
				.addString("completedJobParameters", "someJobParameterValue")
				.toJobParameters();

		JobInstance completedAllRetrievalsJobInstance = jobInstanceDao.createJobInstance("allRetrievalsJob", completedJobParameters);
		JobExecution completedAllRetrievalsJobExecution = new JobExecution(completedAllRetrievalsJobInstance, completedJobParameters);

		LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10L);
		Date completedDate = Date.from(tenMinutesAgo.atZone(ZoneId.systemDefault()).toInstant());

		completedAllRetrievalsJobExecution.setCreateTime(completedDate);
		completedAllRetrievalsJobExecution.setStartTime(completedDate);
		completedAllRetrievalsJobExecution.setEndTime(completedDate);
		completedAllRetrievalsJobExecution.setLastUpdated(completedDate);
		completedAllRetrievalsJobExecution.setStatus(BatchStatus.COMPLETED);
		jobExecutionDao.saveJobExecution(completedAllRetrievalsJobExecution);

		mockMvc.perform(get("{apiPath}/admin/job-instances/has-failed-all-retrievals-job", apiPath, jobInstance.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("hasFailedAllRetrievalsJob", is(false)));
	}

}
