package com.drumonii.loltrollbuild.model.id;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Composite key to the Spring Batch BATCH_JOB_EXECUTION table.
 */
@Embeddable
@EqualsAndHashCode
@ToString
public class BatchJobExecutionParamsId implements Serializable {

	@Column(name = "JOB_EXECUTION_ID", nullable = false)
	@JsonProperty("id")
	@Getter @Setter private long id;

	@Column(name = "TYPE_CD", nullable = false, length = 6)
	@JsonProperty("typeCd")
	@Getter @Setter private String typeCd;

	@Column(name = "KEY_NAME", nullable = false, length = 100)
	@JsonProperty("keyName")
	@Getter @Setter private String keyName;

	@Column(name = "STRING_VAL", length = 250)
	@JsonProperty("stringVal")
	@Getter @Setter private String stringVal;

	@Column(name = "DATE_VAL", length = 29)
	@JsonProperty("dateVal")
	@Getter @Setter private LocalDateTime dateVal;

	@Column(name = "LONG_VAL")
	@JsonProperty("longVal")
	@Getter @Setter private Long longVal;

	@Column(name = "DOUBLE_VAL", precision = 17, scale = 17)
	@JsonProperty("doubleVal")
	@Getter @Setter private Double doubleVal;

	@Type(type = "yes_no")
	@Column(name = "IDENTIFYING", nullable = false, length = 1)
	@JsonProperty("identifying")
	@Getter @Setter private boolean identifying;

}
