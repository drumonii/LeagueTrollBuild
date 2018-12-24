package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.BatchJobInstance;
import com.drumonii.loltrollbuild.test.batch.BatchDaoTestConfig;
import com.drumonii.loltrollbuild.test.repository.RepositoryTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@Import(BatchDaoTestConfig.class)
@RepositoryTest
public class BatchJobInstancesRepositoryTest {

    @Autowired
    private BatchJobInstancesRepository batchJobInstancesRepository;

    @Autowired
    private JobInstanceDao jobInstanceDao;

    @Autowired
    private JobExecutionDao jobExecutionDao;

    @Test
    public void getsMostRecentAllRetrievalsJob() {
        PageRequest pageRequest = PageRequest.of(0, 1, Sort.by(Order.desc("jobExecution.startTime")));

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

        assertThat(batchJobInstancesRepository.getMostRecentAllRetrievalsJob(pageRequest))
                .extracting(BatchJobInstance::getId)
                .containsOnly(hangingAllRetrievalsJobInstance.getId());
    }

}