package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.BatchJobExecution;
import com.drumonii.loltrollbuild.model.BatchJobInstance;
import com.drumonii.loltrollbuild.model.BatchStepExecution;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BatchStepExecutionsRestControllerTest extends BaseSpringTestRunner {

	private BatchJobInstance jobInstance;
	private BatchStepExecution stepExecution;

	@Before
	public void before() {
		super.before();

		jobInstance = new BatchJobInstance();
		jobInstance.setName(RandomStringUtils.randomAlphabetic(7));
		jobInstance.setKey(RandomStringUtils.randomAlphabetic(10));
		jobInstance = batchJobInstancesRepository.save(jobInstance);

		BatchJobExecution jobExecution = new BatchJobExecution();
		jobExecution.setCreateTime(LocalDateTime.now());
		jobExecution.setJobInstance(jobInstance);
		jobExecution = batchJobExecutionsRepository.save(jobExecution);

		stepExecution = new BatchStepExecution();
		stepExecution.setVersion(RandomUtils.nextLong());
		stepExecution.setName(RandomStringUtils.randomAlphabetic(10));
		stepExecution.setStartTime(LocalDateTime.now());
		stepExecution.setJobExecution(jobExecution);
		batchStepExecutionsRepository.save(stepExecution);
	}

	@Test
	public void getBatchStepExecutions() throws Exception {
		mockMvc.perform(get(apiPath + "/job-instances/{jobInstanceId}/step-executions", jobInstance.getId()).with(adminUser()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.stepExecutions", hasSize(1)))
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists());

		mockMvc.perform(get(apiPath + "/job-instances/{jobInstanceId}/step-executions", -1).with(adminUser()))
				.andExpect(status().isNotFound());
	}

	@Test
	public void getBatchStepExecution() throws Exception {
		mockMvc.perform(get(apiPath + "/job-instances/{jobInstanceId}/step-executions/{stepExecutionId}",
				jobInstance.getId(), stepExecution.getId()).with(adminUser()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists());

		mockMvc.perform(get(apiPath + "/job-instances/{jobInstanceId}/step-executions/{stepExecutionId}",
				-1, -1).with(adminUser()))
				.andExpect(status().isNotFound());

	}

}