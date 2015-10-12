package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

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
		String responseBody = "[\"5.16.1\",\"5.15.1\",\"5.14.1\",\"5.13.1\",\"5.12.1\",\"5.11.1\",\"5.10.1\"," +
				"\"5.9.1\",\"5.8.1\",\"5.7.2\",\"5.7.1\",\"5.6.2\",\"5.6.1\",\"5.5.3\",\"5.5.2\",\"5.5.1\"," +
				"\"5.4.1\",\"5.3.1\",\"5.2.2\",\"5.2.1\",\"5.1.2\",\"5.1.1\"]";
		Version latestPatchFromRiot = new Version(objectMapper.readValue(responseBody, String[].class)[0]);
		versionsRepository.save(latestPatchFromRiot);

		Version versionFromDb = versionsRepository.findOne(latestPatchFromRiot.getPatch());
		assertThat(latestPatchFromRiot).isEqualTo(versionFromDb);

		latestPatchFromRiot.setPatch("5.17.1");
		assertThat(latestPatchFromRiot).isNotEqualTo(versionFromDb);
	}

}