package com.drumonii.loltrollbuild.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Spring Batch BATCH_JOB_INSTANCE table.
 */
@Entity
@Table(name = "BATCH_JOB_INSTANCE", uniqueConstraints = @UniqueConstraint(columnNames = { "JOB_NAME", "JOB_KEY" }))
@EqualsAndHashCode(of = "id")
@ToString(exclude = "jobExecution")
public class BatchJobInstance implements Serializable {

	@Id
	@Column(name = "JOB_INSTANCE_ID", unique = true, nullable = false)
	@SequenceGenerator(name = "JOB_INSTANCE_ID", sequenceName = "BATCH_JOB_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JOB_INSTANCE_ID")
	@JsonProperty("id")
	@Getter @Setter private long id;

	@Column(name = "VERSION")
	@JsonProperty("version")
	@Getter @Setter private Long version;

	@Column(name = "JOB_NAME", nullable = false, length = 100)
	@JsonProperty("name")
	@Getter @Setter private String name;

	@Column(name = "JOB_KEY", nullable = false, length = 32)
	@JsonProperty("key")
	@Getter @Setter private String key;

	@OneToOne(mappedBy = "jobInstance")
	@JsonManagedReference
	@JsonProperty("jobExecution")
	@Getter @Setter private BatchJobExecution jobExecution; // this is a bit cheating as should be a list

}
