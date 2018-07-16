package com.drumonii.loltrollbuild.model;

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
		String[] versioning = patch.split("\\."); // 7.10.1 style
		if (versioning.length == 3) {
			this.patch = patch;
			this.major = Integer.parseInt(versioning[0]);
			this.minor = Integer.parseInt(versioning[1]);
			this.revision = Integer.parseInt(versioning[2]);
		} else {
			versioning = patch.substring(patch.lastIndexOf('_') + 1, patch.length()).split("\\."); // lolpatch_7.17 style
			this.major = Integer.parseInt(versioning[0]);
			this.minor = Integer.parseInt(versioning[1]);
			this.patch = major + "." + minor + "." + 0;
		}
	}

	@Id
	@Column(name = "PATCH", nullable = false)
	@JsonProperty("patch")
	private String patch;

	@Column(name = "MAJOR", nullable = false)
	@JsonProperty("major")
	private int major;

	@Column(name = "MINOR", nullable = false)
	@JsonProperty("minor")
	private int minor;

	@Column(name = "REVISION", nullable = false)
	@JsonProperty("revision")
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
