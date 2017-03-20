package com.drumonii.loltrollbuild.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Spring Batch BATCH_STEP_EXECUTION_CONTEXT table.
 */
@Entity
@Table(name = "BATCH_STEP_EXECUTION_CONTEXT")
@EqualsAndHashCode(of = "id")
@ToString(exclude = "stepExecution")
public class BatchStepExecutionContext implements Serializable {

	@Id
	@Column(name = "STEP_EXECUTION_ID", unique = true, nullable = false)
	@Getter @Setter private long id;

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	@JsonIgnore
	@Getter @Setter private BatchStepExecution stepExecution;

	@Column(name = "SHORT_CONTEXT", nullable = false, length = 2500)
	@Getter @Setter private String shortContext;

	@Column(name = "SERIALIZED_CONTEXT")
	@Getter @Setter private String serializedContext;

}
