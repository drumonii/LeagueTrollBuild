package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.BatchJobExecution;
import com.drumonii.loltrollbuild.model.BatchJobInstance;
import com.drumonii.loltrollbuild.model.BatchStepExecution;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class BatchStepExecutionsRepositoryTest extends BaseSpringTestRunner {

	@Test
	public void findByJobInstanceId() throws Exception {
		BatchJobInstance jobInstance = new BatchJobInstance();
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

		assertThat(batchStepExecutionsRepository.findByJobInstanceId(jobInstance.getId())).isNotEmpty();
		assertThat(batchStepExecutionsRepository.findByJobInstanceId(-1)).isEmpty();
	}

}