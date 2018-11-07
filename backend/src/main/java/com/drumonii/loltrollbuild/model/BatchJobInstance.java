package com.drumonii.loltrollbuild.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Spring Batch BATCH_JOB_INSTANCE table.
 */
@Entity
@Table(name = "BATCH_JOB_INSTANCE", uniqueConstraints = @UniqueConstraint(columnNames = { "JOB_NAME", "JOB_KEY" }))
public class BatchJobInstance implements Serializable {

	@Id
	@Column(name = "JOB_INSTANCE_ID", unique = true, nullable = false)
	@SequenceGenerator(name = "JOB_INSTANCE_ID", sequenceName = "BATCH_JOB_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JOB_INSTANCE_ID")
	@JsonProperty("id")
	private long id;

	@Column(name = "VERSION")
	@JsonProperty("version")
	private Long version;

	@Column(name = "JOB_NAME", nullable = false, length = 100)
	@JsonProperty("name")
	private String name;

	@Column(name = "JOB_KEY", nullable = false, length = 32)
	@JsonProperty("key")
	private String key;

	@OneToOne(mappedBy = "jobInstance", fetch = FetchType.LAZY)
	@JsonIgnore
	private BatchJobExecution jobExecution; // this is a bit cheating as should be a list

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
        BatchJobInstance that = (BatchJobInstance) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BatchJobInstance{" +
                "id=" + id +
                ", version=" + version +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                '}';
    }

}
