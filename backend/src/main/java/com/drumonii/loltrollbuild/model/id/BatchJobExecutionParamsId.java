package com.drumonii.loltrollbuild.model.id;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Composite key to the Spring Batch BATCH_JOB_EXECUTION table.
 */
@Embeddable
public class BatchJobExecutionParamsId implements Serializable {

	@Column(name = "JOB_EXECUTION_ID", nullable = false)
	@JsonProperty("id")
	private long id;

	@Column(name = "TYPE_CD", nullable = false, length = 6)
	@JsonProperty("typeCd")
	private String typeCd;

	@Column(name = "KEY_NAME", nullable = false, length = 100)
	@JsonProperty("keyName")
	private String keyName;

	@Column(name = "STRING_VAL", length = 250)
	@JsonProperty("stringVal")
	private String stringVal;

	@Column(name = "DATE_VAL", length = 29)
	@JsonProperty("dateVal")
	private LocalDateTime dateVal;

	@Column(name = "LONG_VAL")
	@JsonProperty("longVal")
	private Long longVal;

	@Column(name = "DOUBLE_VAL", precision = 17, scale = 17)
	@JsonProperty("doubleVal")
	private Double doubleVal;

	@Type(type = "yes_no")
	@Column(name = "IDENTIFYING", nullable = false, length = 1)
	@JsonProperty("identifying")
	private boolean identifying;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTypeCd() {
		return typeCd;
	}

	public void setTypeCd(String typeCd) {
		this.typeCd = typeCd;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getStringVal() {
		return stringVal;
	}

	public void setStringVal(String stringVal) {
		this.stringVal = stringVal;
	}

	public LocalDateTime getDateVal() {
		return dateVal;
	}

	public void setDateVal(LocalDateTime dateVal) {
		this.dateVal = dateVal;
	}

	public Long getLongVal() {
		return longVal;
	}

	public void setLongVal(Long longVal) {
		this.longVal = longVal;
	}

	public Double getDoubleVal() {
		return doubleVal;
	}

	public void setDoubleVal(Double doubleVal) {
		this.doubleVal = doubleVal;
	}

	public boolean isIdentifying() {
		return identifying;
	}

	public void setIdentifying(boolean identifying) {
		this.identifying = identifying;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		BatchJobExecutionParamsId that = (BatchJobExecutionParamsId) o;
		return id == that.id &&
				identifying == that.identifying &&
				Objects.equals(typeCd, that.typeCd) &&
				Objects.equals(keyName, that.keyName) &&
				Objects.equals(stringVal, that.stringVal) &&
				Objects.equals(dateVal, that.dateVal) &&
				Objects.equals(longVal, that.longVal) &&
				Objects.equals(doubleVal, that.doubleVal);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, typeCd, keyName, stringVal, dateVal, longVal, doubleVal, identifying);
	}

	@Override
	public String toString() {
		return "BatchJobExecutionParamsId{" +
				"id=" + id +
				", typeCd='" + typeCd + '\'' +
				", keyName='" + keyName + '\'' +
				", stringVal='" + stringVal + '\'' +
				", dateVal=" + dateVal +
				", longVal=" + longVal +
				", doubleVal=" + doubleVal +
				", identifying=" + identifying +
				'}';
	}

}
