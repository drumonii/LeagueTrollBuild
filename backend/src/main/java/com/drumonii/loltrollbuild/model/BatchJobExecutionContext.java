package com.drumonii.loltrollbuild.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Spring Batch BATCH_JOB_EXECUTION_CONTEXT table.
 */
@Entity
@Table(name = "BATCH_JOB_EXECUTION_CONTEXT")
public class BatchJobExecutionContext implements Serializable {

	@Id
	@Column(name = "JOB_EXECUTION_ID", unique = true, nullable = false)
	@JsonProperty("id")
	private long id;

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	@JsonIgnore
	private BatchJobExecution jobExecution;

	@Column(name = "SHORT_CONTEXT", nullable = false, length = 2500)
	@JsonProperty("shortContext")
	private String shortContext;

	@Column(name = "SERIALIZED_CONTEXT")
	@JsonProperty("serializedContext")
	private String serializedContext;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BatchJobExecution getJobExecution() {
		return jobExecution;
	}

	public void setJobExecution(BatchJobExecution jobExecution) {
		this.jobExecution = jobExecution;
	}

	public String getShortContext() {
		return shortContext;
	}

	public void setShortContext(String shortContext) {
		this.shortContext = shortContext;
	}

	public String getSerializedContext() {
		return serializedContext;
	}

	public void setSerializedContext(String serializedContext) {
		this.serializedContext = serializedContext;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		BatchJobExecutionContext that = (BatchJobExecutionContext) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "BatchJobExecutionContext{" +
				"id=" + id +
				", shortContext='" + shortContext + '\'' +
				", serializedContext='" + serializedContext + '\'' +
				'}';
	}

}
