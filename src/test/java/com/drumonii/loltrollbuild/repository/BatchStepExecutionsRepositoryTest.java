package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.BatchJobExecution;
import com.drumonii.loltrollbuild.model.BatchJobInstance;
import com.drumonii.loltrollbuild.model.BatchStepExecution;
import com.drumonii.loltrollbuild.test.repository.RepositoryTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@RepositoryTest
@ActiveProfiles({ TESTING })
public class BatchStepExecutionsRepositoryTest {

	@Autowired
	private BatchStepExecutionsRepository batchStepExecutionsRepository;

	@Autowired
	private TestEntityManager testEntityManager;

	@Test
	public void findByJobInstanceId() {
		BatchJobInstance jobInstance = new BatchJobInstance();
		jobInstance.setName("jobInstanceName");
		jobInstance.setKey("jobInstanceKey");
		jobInstance = testEntityManager.persistAndFlush(jobInstance);

		BatchJobExecution jobExecution = new BatchJobExecution();
		jobExecution.setCreateTime(LocalDateTime.now());
		jobExecution.setJobInstance(jobInstance);
		jobExecution = testEntityManager.persistAndFlush(jobExecution);

		BatchStepExecution stepExecution = new BatchStepExecution();
		stepExecution.setVersion(1L);
		stepExecution.setName("step1");
		stepExecution.setStartTime(LocalDateTime.now());
		stepExecution.setJobExecution(jobExecution);
		testEntityManager.persistAndFlush(stepExecution);

		assertThat(batchStepExecutionsRepository.findByJobInstanceId(jobInstance.getId())).isNotEmpty();
		assertThat(batchStepExecutionsRepository.findByJobInstanceId(-1)).isEmpty();
	}

}