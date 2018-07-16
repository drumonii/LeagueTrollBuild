package com.drumonii.loltrollbuild.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Spring Batch BATCH_STEP_EXECUTION_CONTEXT table.
 */
@Entity
@Table(name = "BATCH_STEP_EXECUTION_CONTEXT")
public class BatchStepExecutionContext implements Serializable {

	@Id
	@Column(name = "STEP_EXECUTION_ID", unique = true, nullable = false)
	private long id;

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	@JsonIgnore
	private BatchStepExecution stepExecution;

	@Column(name = "SHORT_CONTEXT", nullable = false, length = 2500)
	private String shortContext;

	@Column(name = "SERIALIZED_CONTEXT")
	private String serializedContext;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BatchStepExecution getStepExecution() {
		return stepExecution;
	}

	public void setStepExecution(BatchStepExecution stepExecution) {
		this.stepExecution = stepExecution;
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
		BatchStepExecutionContext that = (BatchStepExecutionContext) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "BatchStepExecutionContext{" +
				"id=" + id +
				", shortContext='" + shortContext + '\'' +
				", serializedContext='" + serializedContext + '\'' +
				'}';
	}

}
