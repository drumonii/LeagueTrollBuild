package com.drumonii.loltrollbuild.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Spring Batch BATCH_STEP_EXECUTION table.
 */
@Entity
@Table(name = "BATCH_STEP_EXECUTION")
@EqualsAndHashCode(of = "id")
@ToString(exclude = { "jobExecution", "stepExecutionContext" })
public class BatchStepExecution implements Serializable {

	@Id
	@Column(name = "STEP_EXECUTION_ID", unique = true, nullable = false)
	@SequenceGenerator(name = "STEP_EXECUTION_ID", sequenceName = "BATCH_STEP_EXECUTION_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STEP_EXECUTION_ID")
	@Getter @Setter private long id;

	@Column(name = "VERSION", nullable = false)
	@Getter @Setter private Long version;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "JOB_EXECUTION_ID", nullable = false)
	@JsonBackReference
	@RestResource(exported = false)
	@Getter @Setter private BatchJobExecution jobExecution;

	@Column(name = "STEP_NAME", nullable = false, length = 100)
	@Getter @Setter private String name;

	@Column(name = "START_TIME", nullable = false, length = 29)
	@Getter @Setter private LocalDateTime startTime;

	@Column(name = "END_TIME", length = 29)
	@Getter @Setter private LocalDateTime endTime;

	@Column(name = "STATUS", length = 10)
	@Getter @Setter private String status;

	@Column(name = "COMMIT_COUNT")
	@Getter @Setter private Long commitCount;

	@Column(name = "READ_COUNT")
	@Getter @Setter private Long readCount;

	@Column(name = "FILTER_COUNT")
	@Getter @Setter private Long filterCount;

	@Column(name = "WRITE_COUNT")
	@Getter @Setter private Long writeCount;

	@Column(name = "READ_SKIP_COUNT")
	@Getter @Setter private Long readSkipCount;

	@Column(name = "WRITE_SKIP_COUNT")
	@Getter @Setter private Long writeSkipCount;

	@Column(name = "PROCESS_SKIP_COUNT")
	@Getter @Setter private Long processSkipCount;

	@Column(name = "ROLLBACK_COUNT")
	@Getter @Setter private Long rollbackCount;

	@Column(name = "EXIT_CODE", length = 2500)
	@Getter @Setter private String exitCode;

	@Column(name = "EXIT_MESSAGE", length = 2500)
	@Getter @Setter private String exitMessage;

	@Column(name = "LAST_UPDATED", length = 29)
	@Getter @Setter private LocalDateTime lastUpdated;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "stepExecution")
	@JsonIgnore
	@Getter @Setter private BatchStepExecutionContext stepExecutionContext;

}
