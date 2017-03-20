package com.drumonii.loltrollbuild.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Spring Batch BATCH_JOB_EXECUTION_CONTEXT table.
 */
@Entity
@Table(name = "BATCH_JOB_EXECUTION_CONTEXT")
@EqualsAndHashCode(of = "id")
@ToString(exclude = "jobExecution")
public class BatchJobExecutionContext implements Serializable {

	@Id
	@Column(name = "JOB_EXECUTION_ID", unique = true, nullable = false)
	@JsonProperty("id")
	@Getter @Setter private long id;

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	@JsonBackReference
	@RestResource(exported = false)
	@Getter @Setter private BatchJobExecution jobExecution;

	@Column(name = "SHORT_CONTEXT", nullable = false, length = 2500)
	@JsonProperty("shortContext")
	@Getter @Setter private String shortContext;

	@Column(name = "SERIALIZED_CONTEXT")
	@JsonProperty("serializedContext")
	@Getter @Setter private String serializedContext;

}
