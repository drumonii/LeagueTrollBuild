package com.drumonii.loltrollbuild.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Comparator;

/**
 * League of Legend Version (patch).
 *
 * @see <a href="http://leagueoflegends.wikia.com/wiki/Patch">Patch</a>
 */
@Entity
@Table(name = "VERSION")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "patch" })
@ToString
public class Version implements Serializable, Comparable<Version> {

	public Version(String patch) {
		String[] versioning = patch.split("\\.");
		this.patch = patch;
		this.major = Integer.parseInt(versioning[0]);
		this.minor = Integer.parseInt(versioning[1]);
		this.revision = Integer.parseInt(versioning[2]);
	}

	@Id
	@Column(name = "PATCH", nullable = false)
	@JsonProperty("patch")
	@Getter @Setter private String patch;

	@Column(name = "MAJOR", nullable = false)
	@JsonProperty("major")
	@Getter @Setter private int major;

	@Column(name = "MINOR", nullable = false)
	@JsonProperty("minor")
	@Getter @Setter private int minor;

	@Column(name = "REVISION", nullable = false)
	@JsonProperty("revision")
	@Getter @Setter private int revision;

	@Override
	public int compareTo(Version version) {
		return Comparator.comparing(Version::getMajor)
				.thenComparing(Version::getMinor)
				.thenComparingInt(Version::getRevision)
				.compare(this, version);
	}

}
