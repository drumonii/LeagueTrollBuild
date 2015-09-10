package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class VersionsRepositoryTest extends BaseSpringTestRunner {

	@Autowired
	private VersionsRepository versionsRepository;

	@Autowired
	private ObjectMapper objectMapper;

	private List<Version> versions;

	@Before
	public void before() {
		String responseBody = "[\"5.16.1\",\"5.15.1\",\"5.14.1\",\"5.13.1\",\"5.12.1\",\"5.11.1\",\"5.10.1\"," +
				"\"5.9.1\",\"5.8.1\",\"5.7.2\",\"5.7.1\",\"5.6.2\",\"5.6.1\",\"5.5.3\",\"5.5.2\",\"5.5.1\",\"5.4.1\"," +
				"\"5.3.1\",\"5.2.2\",\"5.2.1\",\"5.1.2\",\"5.1.1\"]";
		List<String> unmarshalledVersions;
		try {
			unmarshalledVersions = Arrays.asList(objectMapper.readValue(responseBody, String[].class));
			versions = unmarshalledVersions.stream()
					.map(Version::new)
					.collect(Collectors.toList());
		} catch (IOException e) {}
	}

	@After
	public void after() {
		versionsRepository.deleteAll();
	}

	@Test
	public void crudOperations() throws IOException {
		// Create
		versionsRepository.save(versions);

		// Select
		Version versionFromDb = versionsRepository.findOne("5.16.1");
		assertThat(versionFromDb).isNotNull();
		assertThat(versionFromDb).isEqualTo(versions.get(0));

		// Update
		versionFromDb.setPatch("patch version String");
		versionsRepository.save(versionFromDb);
		versionFromDb = versionsRepository.findOne("patch version String");
		assertThat(versionFromDb.getPatch()).isEqualTo("patch version String");

		// Delete
		versionsRepository.delete("patch version String");
		assertThat(versionsRepository.findOne("patch version String")).isNull();
	}

	@Test
	public void latestPatch() throws IOException {
		versionsRepository.save(versions);

		String latestVersion = versionsRepository.latestPatch();
		assertThat(latestVersion).isEqualTo(versions.get(0).getPatch());
	}

}