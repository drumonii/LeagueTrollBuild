package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.id.BatchJobExecutionParamsId;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Spring Batch BATCH_JOB_EXECUTION_PARAMS table.
 */
@Entity
@Table(name = "BATCH_JOB_EXECUTION_PARAMS")
@EqualsAndHashCode(of = "id")
@ToString(exclude = "jobExecution")
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
	@Getter @Setter private BatchJobExecutionParamsId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "JOB_EXECUTION_ID", nullable = false, insertable = false, updatable = false)
	@JsonBackReference
	@RestResource(exported = false)
	@Getter @Setter private BatchJobExecution jobExecution;

}
