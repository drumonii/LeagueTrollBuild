package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.id.BatchJobExecutionParamsId;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Spring Batch BATCH_JOB_EXECUTION_PARAMS table.
 */
@Entity
@Table(name = "BATCH_JOB_EXECUTION_PARAMS")
public class BatchJobExecutionParams implements Serializable {

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "id", column = @Column(name = "JOB_EXECUTION_ID", nullable = false)),
			@AttributeOverride(name = "typeCd", column = @Column(name = "TYPE_CD", nullable = false, length = 6)),
			@AttributeOverride(name = "keyName", column = @Column(name = "KEY_NAME", nullable = false, length = 100)),
			@AttributeOverride(name = "stringVal", column = @Column(name = "STRING_VAL", length = 250)),
			@AttributeOverride(name = "dateVal", column = @Column(name = "DATE_VAL", length = 29)),
			@AttributeOverride(name = "longVal", column = @Column(name = "LONG_VAL")),
			@AttributeOverride(name = "doubleVal", column = @Column(name = "DOUBLE_VAL", precision = 17, scale = 17)),
			@AttributeOverride(name = "identifying", column = @Column(name = "IDENTIFYING", nullable = false, length = 1)) })
	private BatchJobExecutionParamsId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "JOB_EXECUTION_ID", nullable = false, insertable = false, updatable = false)
	@JsonBackReference
	private BatchJobExecution jobExecution;

	public BatchJobExecutionParamsId getId() {
		return id;
	}

	public void setId(BatchJobExecutionParamsId id) {
		this.id = id;
	}

	public BatchJobExecution getJobExecution() {
		return jobExecution;
	}

	public void setJobExecution(BatchJobExecution jobExecution) {
		this.jobExecution = jobExecution;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		BatchJobExecutionParams that = (BatchJobExecutionParams) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "BatchJobExecutionParams{" +
				"id=" + id +
				'}';
	}

}
