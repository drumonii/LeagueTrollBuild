package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class VersionTest extends BaseSpringTestRunner {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private VersionsRepository versionsRepository;

	@After
	public void after() {
		versionsRepository.deleteAll();
	}

	@Test
	public void equals() throws IOException {
		String responseBody = "\"5.16.1\"";
		Version latestPatchFromRiot = objectMapper.readValue(responseBody, Version.class);
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
		assertThat(version1.compareTo(version1)).isEqualTo(0); // equal
		assertThat(version2.compareTo(version1)).isEqualTo(1); // greater than

		String responseBody = "[\"5.13.1\",\"6.4.1\",\"5.22.3\",\"6.2.1\",\"5.14.1\",\"6.4.2\",\"5.24.2\"," +
				"\"5.24.1\",\"6.3.1\",\"5.23.1\",\"6.1.1\",\"5.15.1\",\"5.11.1\",\"5.22.2\",\"5.22.1\",\"5.21.1\"," +
				"\"5.17.1\",\"6.5.1\",\"5.16.1\",\"5.10.1\",\"5.12.1\"]";
		List<Version> versions = Arrays.asList(objectMapper.readValue(responseBody, Version[].class));
		Collections.sort(versions, Collections.reverseOrder());
		assertThat(versions).isSortedAccordingTo(Collections.reverseOrder());
		assertThat(versions.get(0)).isGreaterThan(versions.get(versions.size() - 1));
		assertThat(versions.get(0)).isEqualTo(versions.get(0));
		assertThat(versions.get(versions.size() - 1)).isLessThan(versions.get(0));
	}

}