package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import com.drumonii.loltrollbuild.model.BatchJobExecution;
import com.drumonii.loltrollbuild.model.BatchJobInstance;
import com.drumonii.loltrollbuild.model.BatchStepExecution;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BatchJobInstancesRestControllerTest extends BaseSpringTestRunner {

	private BatchJobInstance jobInstance;

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

		BatchStepExecution stepExecution = new BatchStepExecution();
		stepExecution.setVersion(RandomUtils.nextLong());
		stepExecution.setName(RandomStringUtils.randomAlphabetic(10));
		stepExecution.setStartTime(LocalDateTime.now());
		stepExecution.setJobExecution(jobExecution);
		batchStepExecutionsRepository.save(stepExecution);
	}

	@WithMockAdminUser
	@Test
	public void getBatchJobInstances() throws Exception {
		// qbe
		mockMvc.perform(get(apiPath + "/job-instances"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.jobInstances").exists())
				.andExpect(jsonPath("$._embedded.jobInstances[*].jobExecution").exists())
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(20)))
				.andExpect(jsonPath("$.page.totalElements").exists())
				.andExpect(jsonPath("$.page.totalPages").exists())
				.andExpect(jsonPath("$.page.number").exists());

		// qbe with name
		mockMvc.perform(get(apiPath + "/job-instances")
				.param("name", jobInstance.getName().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.jobInstances", hasSize(1)))
				.andExpect(jsonPath("$._embedded.jobInstances[*].jobExecution").exists())
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(20)))
				.andExpect(jsonPath("$.page.totalElements").exists())
				.andExpect(jsonPath("$.page.totalPages").exists())
				.andExpect(jsonPath("$.page.number").exists());

		// qbe with no results
		mockMvc.perform(get(apiPath + "/job-instances")
				.param("name", "abcd1234"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded").doesNotExist())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page.size", is(20)))
				.andExpect(jsonPath("$.page.totalElements", is(0)))
				.andExpect(jsonPath("$.page.totalPages", is(0)))
				.andExpect(jsonPath("$.page.number", is(0)));
	}

	@WithMockAdminUser
	@Test
	public void getBatchJobInstance() throws Exception {
		mockMvc.perform(get(apiPath + "/job-instances/{jobInstanceId}", jobInstance.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$.jobExecution").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists());

		mockMvc.perform(get(apiPath + "/job-instances/{jobInstanceId}", -1))
				.andExpect(status().isNotFound());
	}

}