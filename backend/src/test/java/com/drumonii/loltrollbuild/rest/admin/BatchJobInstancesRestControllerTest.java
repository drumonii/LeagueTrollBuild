package com.drumonii.loltrollbuild.rest.admin;

import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import com.drumonii.loltrollbuild.test.batch.BatchDaoTestConfiguration;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcRestTest(BatchJobInstancesRestController.class)
@Import(BatchDaoTestConfiguration.class)
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
		mockMvc.perform(get("{apiPath}/job-instances", apiPath))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with name
		mockMvc.perform(get("{apiPath}/job-instances", apiPath)
				.param("name", jobInstance.getJobName().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with no results
		mockMvc.perform(get("{apiPath}/job-instances", apiPath)
				.param("name", "abcd1234"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());
	}

	@WithMockAdminUser
	@Test
	public void getBatchJobInstance() throws Exception {
		mockMvc.perform(get("{apiPath}/job-instances/{jobInstanceId}", apiPath, jobInstance.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.jobExecution").exists());

		mockMvc.perform(get("{apiPath}/job-instances/{jobInstanceId}", apiPath, -1))
				.andExpect(status().isNotFound());
	}

}