package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.test.repository.RepositoryTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
abstract class VersionsRepositoryTest {

	@Autowired
	private VersionsRepository versionsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	private List<Version> versions;

	protected abstract List<Version> getVersions();

	@BeforeEach
	void beforeEach() {
		versions = getVersions();
		versionsRepository.saveAll(versions);
	}

	@Test
	void latestPatch() {
		String latestVersion = versionsRepository.latestVersion().getPatch();
		assertThat(versions).element(0).extracting(Version::getPatch).isEqualTo(latestVersion);
	}

}
