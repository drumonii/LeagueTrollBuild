package com.drumonii.loltrollbuild.model.builder;

import com.drumonii.loltrollbuild.model.BatchJobExecution;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Builder for {@link BatchJobExecution}s.
 */
public final class BatchJobExecutionBuilder {

    private long id;
    private Long version;
    private LocalDateTime createTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String exitCode;
    private String exitMessage;
    private LocalDateTime lastUpdated;
    private String configurationLocation;

    public BatchJobExecutionBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public BatchJobExecutionBuilder withVersion(Long version) {
        this.version = version;
        return this;
    }

    public BatchJobExecutionBuilder withVersion(Integer version) {
        withVersion((long) version);
        return this;
    }

    public BatchJobExecutionBuilder withCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public BatchJobExecutionBuilder withCreateTime(Date createTime) {
        if (createTime != null) {
            withCreateTime(createTime.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
        }
        return this;
    }

    public BatchJobExecutionBuilder withStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public BatchJobExecutionBuilder withStartTime(Date startTime) {
        if (startTime != null) {
            withStartTime(startTime.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
        }
        return this;
    }

    public BatchJobExecutionBuilder withEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public BatchJobExecutionBuilder withEndTime(Date endTime) {
        if (endTime != null) {
            withEndTime(endTime.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
        }
        return this;
    }

    public BatchJobExecutionBuilder withStatus(String status) {
        this.status = status;
        return this;
    }

    public BatchJobExecutionBuilder withStatus(BatchStatus status) {
        withStatus(status.name());
        return this;
    }

    public BatchJobExecutionBuilder withExitCode(String exitCode) {
        this.exitCode = exitCode;
        return this;
    }

    public BatchJobExecutionBuilder withExitCode(ExitStatus exitCode) {
        withExitCode(exitCode.getExitCode());
        return this;
    }

    public BatchJobExecutionBuilder withExitMessage(String exitMessage) {
        this.exitMessage = exitMessage;
        return this;
    }

    public BatchJobExecutionBuilder withExitMessage(ExitStatus exitCode) {
        withExitMessage(exitCode.getExitDescription());
        return this;
    }

    public BatchJobExecutionBuilder withLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public BatchJobExecutionBuilder withLastUpdated(Date lastUpdated) {
        if (lastUpdated != null) {
            withLastUpdated(lastUpdated.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
        }
        return this;
    }

    public BatchJobExecutionBuilder withConfigurationLocation(String configurationLocation) {
        this.configurationLocation = configurationLocation;
        return this;
    }

    public BatchJobExecutionBuilder fromJobExecution(JobExecution jobExecution) {
        withId(jobExecution.getId());
        withVersion(jobExecution.getVersion());
        withStatus(jobExecution.getStatus());
        withStartTime(jobExecution.getStartTime());
        withCreateTime(jobExecution.getCreateTime());
        withEndTime(jobExecution.getEndTime());
        withLastUpdated(jobExecution.getLastUpdated());
        withExitCode(jobExecution.getExitStatus());
        withExitMessage(jobExecution.getExitStatus());
        withConfigurationLocation(jobExecution.getJobConfigurationName());
        return this;
    }

    public BatchJobExecution build() {
        BatchJobExecution batchJobExecution = new BatchJobExecution();
        batchJobExecution.setId(id);
        batchJobExecution.setVersion(version);
        batchJobExecution.setCreateTime(createTime);
        batchJobExecution.setStartTime(startTime);
        batchJobExecution.setEndTime(endTime);
        batchJobExecution.setStatus(status);
        batchJobExecution.setExitCode(exitCode);
        batchJobExecution.setExitMessage(exitMessage);
        batchJobExecution.setLastUpdated(lastUpdated);
        batchJobExecution.setConfigurationLocation(configurationLocation);
        return batchJobExecution;
    }

}
