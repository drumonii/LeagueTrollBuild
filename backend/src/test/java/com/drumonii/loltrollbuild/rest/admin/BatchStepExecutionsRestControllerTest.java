package com.drumonii.loltrollbuild.rest.admin;

import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import com.drumonii.loltrollbuild.test.batch.BatchDaoTestConfiguration;
import com.drumonii.loltrollbuild.test.batch.StepExecutionRowMapper;
import com.drumonii.loltrollbuild.test.rest.WebMvcRestTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Fail.fail;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcRestTest(BatchStepExecutionsRestController.class)
@Import(BatchDaoTestConfiguration.class)
@ActiveProfiles({ TESTING })
public class BatchStepExecutionsRestControllerTest {

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

	@Value("${api.base-path}")
	private String apiPath;

	private JobInstance jobInstance;
	private StepExecution stepExecution;

	@Before
	public void before() {
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
	public void getBatchStepExecutions() throws Exception {
		mockMvc.perform(get("{apiPath}/job-instances/{jobInstanceId}/step-executions", apiPath, jobInstance.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*]", hasSize(1)));

		mockMvc.perform(get("{apiPath}//job-instances/{jobInstanceId}/step-executions", apiPath, -1))
				.andExpect(status().isNotFound());
	}

	@WithMockAdminUser
	@Test
	public void getBatchStepExecution() throws Exception {
		mockMvc.perform(get("{apiPath}/job-instances/{jobInstanceId}/step-executions/{stepExecutionId}", apiPath,
				jobInstance.getId(), stepExecution.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$..*").isNotEmpty());

		mockMvc.perform(get("{apiPath}/job-instances/{jobInstanceId}/step-executions/{stepExecutionId}", apiPath, -1, -1))
				.andExpect(status().isNotFound());
	}

}