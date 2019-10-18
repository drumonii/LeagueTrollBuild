package com.drumonii.loltrollbuild.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class VersionTest {

	@Test
	void comparable() {
		Version version1 = Version.patch("5.13.1");
		Version version2 = Version.patch("6.2.1");
		assertThat(version1.compareTo(version2)).isEqualTo(-1); // less than
		assertThat(version1.compareTo(version1)).isZero(); // equal
		assertThat(version2.compareTo(version1)).isOne(); // greater than

		List<Version> versions = Arrays.asList(version1, version2);

		versions.sort(Collections.reverseOrder());
		assertThat(versions).isSortedAccordingTo(Collections.reverseOrder());
	}

}
