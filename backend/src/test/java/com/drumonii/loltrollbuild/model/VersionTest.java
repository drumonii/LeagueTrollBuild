package com.drumonii.loltrollbuild.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class VersionTest {

	@Test
	public void comparable() {
		Version version1 = new Version("5.13.1");
		Version version2 = new Version("6.2.1");
		assertThat(version1.compareTo(version2)).isEqualTo(-1); // less than
		assertThat(version1.compareTo(version1)).isZero(); // equal
		assertThat(version2.compareTo(version1)).isOne(); // greater than

		List<Version> versions = Arrays.asList(version1, version2);

		versions.sort(Collections.reverseOrder());
		assertThat(versions).isSortedAccordingTo(Collections.reverseOrder());
		assertThat(versions.get(0)).satisfies(version -> {
			assertThat(version).isGreaterThan(versions.get(versions.size() - 1));
			assertThat(versions.get(versions.size() - 1)).isLessThan(version);
		});
	}

}