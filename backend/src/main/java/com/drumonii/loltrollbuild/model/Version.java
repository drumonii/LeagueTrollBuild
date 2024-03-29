package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.builder.VersionBuilder;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * League of Legend Version (patch).
 *
 * @see <a href="http://leagueoflegends.wikia.com/wiki/Patch">Patch</a>
 */
@Entity
@Table(name = "VERSION")
public class Version implements Serializable, Comparable<Version> {

	public Version() {}

	public Version(String patch) {
		Version version = patch(patch);
		this.patch = version.getPatch();
		this.major = version.getMajor();
		this.minor = version.getMinor();
		this.revision = version.getRevision();
	}

	@JsonCreator
	public static Version patch(String patch) {
		return new VersionBuilder()
				.fromPatch(patch)
				.build();
	}

	@Id
	@Column(name = "PATCH", nullable = false)
	@JsonProperty
	private String patch;

	@Column(name = "MAJOR", nullable = false)
	@JsonProperty
	private int major;

	@Column(name = "MINOR", nullable = false)
	@JsonProperty
	private int minor;

	@Column(name = "REVISION", nullable = false)
	@JsonProperty
	private int revision;

	public String getPatch() {
		return patch;
	}

	public void setPatch(String patch) {
		this.patch = patch;
	}

	public int getMajor() {
		return major;
	}

	public void setMajor(int major) {
		this.major = major;
	}

	public int getMinor() {
		return minor;
	}

	public void setMinor(int minor) {
		this.minor = minor;
	}

	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}

	@Override
	public int compareTo(Version version) {
		return Comparator.comparing(Version::getMajor)
				.thenComparing(Version::getMinor)
				.thenComparingInt(Version::getRevision)
				.compare(this, version);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Version version = (Version) o;
		return Objects.equals(patch, version.patch);
	}

	@Override
	public int hashCode() {
		return Objects.hash(patch);
	}

	@Override
	public String toString() {
		return "Version{" +
				"patch='" + patch + '\'' +
				", major=" + major +
				", minor=" + minor +
				", revision=" + revision +
				'}';
	}

}
