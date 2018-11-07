package com.drumonii.loltrollbuild.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Spring Batch BATCH_STEP_EXECUTION table.
 */
@Entity
@Table(name = "BATCH_STEP_EXECUTION")
public class BatchStepExecution implements Serializable {

	@Id
	@Column(name = "STEP_EXECUTION_ID", unique = true, nullable = false)
	@SequenceGenerator(name = "STEP_EXECUTION_ID", sequenceName = "BATCH_STEP_EXECUTION_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STEP_EXECUTION_ID")
	private long id;

	@Column(name = "VERSION", nullable = false)
	private Long version;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "JOB_EXECUTION_ID", nullable = false)
    @JsonIgnore
	private BatchJobExecution jobExecution;

	@Column(name = "STEP_NAME", nullable = false, length = 100)
	private String name;

	@Column(name = "START_TIME", nullable = false, length = 29)
	private LocalDateTime startTime;

	@Column(name = "END_TIME", length = 29)
	private LocalDateTime endTime;

	@Column(name = "STATUS", length = 10)
	private String status;

	@Column(name = "COMMIT_COUNT")
	private Long commitCount;

	@Column(name = "READ_COUNT")
	private Long readCount;

	@Column(name = "FILTER_COUNT")
	private Long filterCount;

	@Column(name = "WRITE_COUNT")
	private Long writeCount;

	@Column(name = "READ_SKIP_COUNT")
	private Long readSkipCount;

	@Column(name = "WRITE_SKIP_COUNT")
	private Long writeSkipCount;

	@Column(name = "PROCESS_SKIP_COUNT")
	private Long processSkipCount;

	@Column(name = "ROLLBACK_COUNT")
	private Long rollbackCount;

	@Column(name = "EXIT_CODE", length = 2500)
	private String exitCode;

	@Column(name = "EXIT_MESSAGE", length = 2500)
	private String exitMessage;

	@Column(name = "LAST_UPDATED", length = 29)
	private LocalDateTime lastUpdated;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "stepExecution")
	@JsonIgnore
	private BatchStepExecutionContext stepExecutionContext;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public BatchJobExecution getJobExecution() {
        return jobExecution;
    }

    public void setJobExecution(BatchJobExecution jobExecution) {
        this.jobExecution = jobExecution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCommitCount() {
        return commitCount;
    }

    public void setCommitCount(Long commitCount) {
        this.commitCount = commitCount;
    }

    public Long getReadCount() {
        return readCount;
    }

    public void setReadCount(Long readCount) {
        this.readCount = readCount;
    }

    public Long getFilterCount() {
        return filterCount;
    }

    public void setFilterCount(Long filterCount) {
        this.filterCount = filterCount;
    }

    public Long getWriteCount() {
        return writeCount;
    }

    public void setWriteCount(Long writeCount) {
        this.writeCount = writeCount;
    }

    public Long getReadSkipCount() {
        return readSkipCount;
    }

    public void setReadSkipCount(Long readSkipCount) {
        this.readSkipCount = readSkipCount;
    }

    public Long getWriteSkipCount() {
        return writeSkipCount;
    }

    public void setWriteSkipCount(Long writeSkipCount) {
        this.writeSkipCount = writeSkipCount;
    }

    public Long getProcessSkipCount() {
        return processSkipCount;
    }

    public void setProcessSkipCount(Long processSkipCount) {
        this.processSkipCount = processSkipCount;
    }

    public Long getRollbackCount() {
        return rollbackCount;
    }

    public void setRollbackCount(Long rollbackCount) {
        this.rollbackCount = rollbackCount;
    }

    public String getExitCode() {
        return exitCode;
    }

    public void setExitCode(String exitCode) {
        this.exitCode = exitCode;
    }

    public String getExitMessage() {
        return exitMessage;
    }

    public void setExitMessage(String exitMessage) {
        this.exitMessage = exitMessage;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public BatchStepExecutionContext getStepExecutionContext() {
        return stepExecutionContext;
    }

    public void setStepExecutionContext(BatchStepExecutionContext stepExecutionContext) {
        this.stepExecutionContext = stepExecutionContext;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BatchStepExecution that = (BatchStepExecution) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BatchStepExecution{" +
                "id=" + id +
                ", version=" + version +
                ", name='" + name + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status='" + status + '\'' +
                ", commitCount=" + commitCount +
                ", readCount=" + readCount +
                ", filterCount=" + filterCount +
                ", writeCount=" + writeCount +
                ", readSkipCount=" + readSkipCount +
                ", writeSkipCount=" + writeSkipCount +
                ", processSkipCount=" + processSkipCount +
                ", rollbackCount=" + rollbackCount +
                ", exitCode='" + exitCode + '\'' +
                ", exitMessage='" + exitMessage + '\'' +
                ", lastUpdated=" + lastUpdated +
                '}';
    }

}
