package com.drumonii.loltrollbuild.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.util.ClassUtils;
import org.springframework.util.StopWatch;

public class LoggingJobDurationListener implements JobExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingJobDurationListener.class);

    private StopWatch stopWatch;

    private String job;

    public LoggingJobDurationListener(Class<?> jobConfigClass) {
        String shortName = ClassUtils.getShortName(jobConfigClass);
        String nameWithoutConfig = shortName.substring(0, shortName.indexOf("Config"));
        this.job = nameWithoutConfig.substring(0, 1).toLowerCase() + nameWithoutConfig.substring(1);
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        stopWatch = new StopWatch();
        stopWatch.start(job);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        stopWatch.stop();

        double totalTimeSeconds = stopWatch.getLastTaskInfo().getTimeSeconds();

        LOGGER.info("Job: [name={}] completed in {} seconds", job, totalTimeSeconds);
    }

}
