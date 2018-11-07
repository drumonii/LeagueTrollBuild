package com.drumonii.loltrollbuild.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

/**
 * Spring Batch BATCH_JOB_EXECUTION table.
 */
@Entity
@Table(name = "BATCH_JOB_EXECUTION")
public class BatchJobExecution implements Serializable {

	@Id
	@Column(name = "JOB_EXECUTION_ID", unique = true, nullable = false)
	@SequenceGenerator(name = "JOB_EXECUTION_ID", sequenceName = "BATCH_JOB_EXECUTION_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JOB_EXECUTION_ID")
	@JsonProperty("id")
	private long id;

	@Column(name = "VERSION")
	@JsonProperty("version")
	private Long version;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "JOB_INSTANCE_ID", nullable = false)
	@JsonBackReference
	private BatchJobInstance jobInstance;

	@Column(name = "CREATE_TIME", nullable = false, length = 29)
	@JsonProperty("createTime")
	private LocalDateTime createTime;

	@Column(name = "START_TIME", length = 29)
	@JsonProperty("startTime")
	private LocalDateTime startTime;

	@Column(name = "END_TIME", length = 29)
	@JsonProperty("endTime")
	private LocalDateTime endTime;

	@Column(name = "STATUS", length = 10)
	@JsonProperty("status")
	private String status;

	@Column(name = "EXIT_CODE", length = 2500)
	@JsonProperty("exitCode")
	private String exitCode;

	@Column(name = "EXIT_MESSAGE", length = 2500)
	@JsonProperty("exitMessage")
	private String exitMessage;

	@Column(name = "LAST_UPDATED", length = 29)
	@JsonProperty("lastUpdated")
	private LocalDateTime lastUpdated;

	@Column(name = "JOB_CONFIGURATION_LOCATION", length = 2500)
	@JsonIgnore
	private String configurationLocation;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "jobExecution")
	@JsonIgnore
	private Set<BatchStepExecution> stepExecutions;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "jobExecution")
	@JsonIgnore
	private BatchJobExecutionContext executionContext;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "jobExecution")
	@JsonIgnore
	private Set<BatchJobExecutionParams> executionParams;

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

	public BatchJobInstance getJobInstance() {
		return jobInstance;
	}

	public void setJobInstance(BatchJobInstance jobInstance) {
		this.jobInstance = jobInstance;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
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

	public String getConfigurationLocation() {
		return configurationLocation;
	}

	public void setConfigurationLocation(String configurationLocation) {
		this.configurationLocation = configurationLocation;
	}

	public Set<BatchStepExecution> getStepExecutions() {
		return stepExecutions;
	}

	public void setStepExecutions(Set<BatchStepExecution> stepExecutions) {
		this.stepExecutions = stepExecutions;
	}

	public BatchJobExecutionContext getExecutionContext() {
		return executionContext;
	}

	public void setExecutionContext(BatchJobExecutionContext executionContext) {
		this.executionContext = executionContext;
	}

	public Set<BatchJobExecutionParams> getExecutionParams() {
		return executionParams;
	}

	public void setExecutionParams(Set<BatchJobExecutionParams> executionParams) {
		this.executionParams = executionParams;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		BatchJobExecution that = (BatchJobExecution) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "BatchJobExecution{" +
				"id=" + id +
				", version=" + version +
				", createTime=" + createTime +
				", startTime=" + startTime +
				", endTime=" + endTime +
				", status='" + status + '\'' +
				", exitCode='" + exitCode + '\'' +
				", exitMessage='" + exitMessage + '\'' +
				", lastUpdated=" + lastUpdated +
				", configurationLocation='" + configurationLocation + '\'' +
				'}';
	}

}
