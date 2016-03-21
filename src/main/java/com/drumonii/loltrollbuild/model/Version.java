package com.drumonii.loltrollbuild.model;

import lombok.*;
import org.apache.commons.lang3.builder.CompareToBuilder;

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
	@Getter @Setter private String patch;

	@Column(name = "MAJOR", nullable = false)
	@Getter @Setter private int major;

	@Column(name = "MINOR", nullable = false)
	@Getter @Setter private int minor;

	@Column(name = "REVISION", nullable = false)
	@Getter @Setter private int revision;

	@Override
	public int compareTo(Version o) {
		return new CompareToBuilder()
				.append(major, o.getMajor())
				.append(minor, o.getMinor())
				.append(revision, o.getRevision())
				.toComparison();
	}

}
