package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class VersionTest extends BaseSpringTestRunner {

	@Test
	public void equals() throws IOException {
		Version latestPatchFromRiot = versions.get(0);
		versionsRepository.save(latestPatchFromRiot);

		Version versionFromDb = versionsRepository.findOne(latestPatchFromRiot.getPatch());
		assertThat(latestPatchFromRiot).isEqualTo(versionFromDb);

		latestPatchFromRiot.setPatch("5.17.1");
		assertThat(latestPatchFromRiot).isNotEqualTo(versionFromDb);
	}

	@Test
	public void comparable() throws IOException {
		Version version1 = new Version("5.13.1");
		Version version2 = new Version("6.2.1");
		assertThat(version1.compareTo(version2)).isEqualTo(-1); // less than
		assertThat(version1.compareTo(version1)).isZero(); // equal
		assertThat(version2.compareTo(version1)).isOne(); // greater than

		Collections.sort(versions, Collections.reverseOrder());
		assertThat(versions).isSortedAccordingTo(Collections.reverseOrder());
		assertThat(versions.get(0)).satisfies(version -> {
			assertThat(version).isGreaterThan(versions.get(versions.size() - 1));
			assertThat(versions.get(versions.size() - 1)).isLessThan(version);
		});
	}

}