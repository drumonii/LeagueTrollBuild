package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.BatchJobExecution;
import com.drumonii.loltrollbuild.model.BatchJobExecutionParams;
import com.drumonii.loltrollbuild.model.BatchJobInstance;
import com.drumonii.loltrollbuild.model.BatchStepExecution;
import com.drumonii.loltrollbuild.test.repository.RepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
@ActiveProfiles({ TESTING })
class BatchStepExecutionsRepositoryTest {

	@Autowired
	private BatchStepExecutionsRepository batchStepExecutionsRepository;

	@Autowired
	private TestEntityManager testEntityManager;

	@Test
	void findByJobInstanceId() {
		BatchJobInstance jobInstance = new BatchJobInstance();
		jobInstance.setName("jobInstanceName");
		jobInstance.setKey("jobInstanceKey");
		jobInstance = testEntityManager.persistAndFlush(jobInstance);

		BatchJobExecution jobExecution = new BatchJobExecution();
		jobExecution.setCreateTime(LocalDateTime.now());
		jobExecution.setJobInstance(jobInstance);
		jobExecution = testEntityManager.persistAndFlush(jobExecution);

		BatchJobExecutionParams params = new BatchJobExecutionParams();
		params.setId(jobExecution.getId());
		params.setKeyName("keyName");
		params.setStringVal("stringValue");
		params.setTypeCd("typeCd");
		params.setIdentifying(true);
		testEntityManager.persistAndFlush(params);

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
