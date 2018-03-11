package com.drumonii.loltrollbuild.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Spring Batch BATCH_JOB_EXECUTION table.
 */
@Entity
@Table(name = "BATCH_JOB_EXECUTION")
@EqualsAndHashCode(of = "id")
@ToString(exclude = { "jobInstance", "stepExecutions", "executionContext", "executionParams" })
public class BatchJobExecution implements Serializable {

	@Id
	@Column(name = "JOB_EXECUTION_ID", unique = true, nullable = false)
	@SequenceGenerator(name = "JOB_EXECUTION_ID", sequenceName = "BATCH_JOB_EXECUTION_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JOB_EXECUTION_ID")
	@JsonProperty("id")
	@Getter @Setter private long id;

	@Column(name = "VERSION")
	@JsonProperty("version")
	@Getter @Setter private Long version;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "JOB_INSTANCE_ID", nullable = false)
	@JsonBackReference
	@Getter @Setter private BatchJobInstance jobInstance;

	@Column(name = "CREATE_TIME", nullable = false, length = 29)
	@JsonProperty("createTime")
	@Getter @Setter private LocalDateTime createTime;

	@Column(name = "START_TIME", length = 29)
	@JsonProperty("startTime")
	@Getter @Setter private LocalDateTime startTime;

	@Column(name = "END_TIME", length = 29)
	@JsonProperty("endTime")
	@Getter @Setter private LocalDateTime endTime;

	@Column(name = "STATUS", length = 10)
	@JsonProperty("status")
	@Getter @Setter private String status;

	@Column(name = "EXIT_CODE", length = 2500)
	@JsonProperty("exitCode")
	@Getter @Setter private String exitCode;

	@Column(name = "EXIT_MESSAGE", length = 2500)
	@JsonProperty("exitMessage")
	@Getter @Setter private String exitMessage;

	@Column(name = "LAST_UPDATED", length = 29)
	@JsonProperty("lastUpdated")
	@Getter @Setter private LocalDateTime lastUpdated;

	@Column(name = "JOB_CONFIGURATION_LOCATION", length = 2500)
	@JsonIgnore
	@Getter @Setter private String configurationLocation;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "jobExecution")
	@JsonManagedReference
	@Getter @Setter private Set<BatchStepExecution> stepExecutions;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "jobExecution")
	@JsonManagedReference
	@Getter @Setter private BatchJobExecutionContext executionContext;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "jobExecution")
	@JsonManagedReference
	@Getter @Setter private Set<BatchJobExecutionParams> executionParams;

}
