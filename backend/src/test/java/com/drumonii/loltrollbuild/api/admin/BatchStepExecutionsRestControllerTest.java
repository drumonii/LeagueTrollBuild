package com.drumonii.loltrollbuild.api.admin;

import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import com.drumonii.loltrollbuild.test.api.WebMvcRestTest;
import com.drumonii.loltrollbuild.test.batch.BatchDaoTestConfig;
import com.drumonii.loltrollbuild.test.batch.StepExecutionRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Fail.fail;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcRestTest(BatchStepExecutionsRestController.class)
@Import(BatchDaoTestConfig.class)
@ActiveProfiles({ TESTING })
class BatchStepExecutionsRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JobInstanceDao jobInstanceDao;

	@Autowired
	private JobExecutionDao jobExecutionDao;

	@Autowired
	private StepExecutionDao stepExecutionDao;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private JobInstance jobInstance;
	private StepExecution stepExecution;

	@BeforeEach
	void beforeEach() {
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("someJobParameter", "someJobParameterValue")
				.toJobParameters();

		jobInstance = jobInstanceDao.createJobInstance("jobName", jobParameters);

		jobExecutionDao.saveJobExecution(new JobExecution(jobInstance, jobParameters, "jobConfigName"));
		JobExecution jobExecution = jobExecutionDao.getLastJobExecution(jobInstance);

		stepExecutionDao.saveStepExecution(new StepExecution("stepName", jobExecution));
		List<StepExecution> stepExecutions =
				jdbcTemplate.query("SELECT * FROM BATCH_STEP_EXECUTION WHERE JOB_EXECUTION_ID = ? ORDER BY STEP_EXECUTION_ID",
						new StepExecutionRowMapper(jobExecution), jobExecution.getId());
		if (stepExecutions.isEmpty()) {
			fail("Unable to find recent StepExecution for JobExecution Id: " + jobExecution.getId());
		}
		stepExecution = stepExecutions.get(0);
	}

	@WithMockAdminUser
	@Test
	void getBatchStepExecutions() throws Exception {
		mockMvc.perform(get("/api/admin/job-instances/{jobInstanceId}/step-executions", jobInstance.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*]", hasSize(1)));

		mockMvc.perform(get("/api/admin/job-instances/{jobInstanceId}/step-executions", -1))
				.andExpect(status().isNotFound());
	}

	@WithMockAdminUser
	@Test
	void getBatchStepExecution() throws Exception {
		mockMvc.perform(get("/api/admin/job-instances/{jobInstanceId}/step-executions/{stepExecutionId}",
				jobInstance.getId(), stepExecution.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$..*").isNotEmpty());

		mockMvc.perform(get("/api/admin/job-instances/{jobInstanceId}/step-executions/{stepExecutionId}", -1, -1))
				.andExpect(status().isNotFound());
	}

}
