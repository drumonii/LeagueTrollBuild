package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.test.repository.RepositoryTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@RepositoryTest
public abstract class VersionsRepositoryTest {

	@Autowired
	private VersionsRepository versionsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	private List<Version> versions;

	protected abstract List<Version> getVersions();

	@Before
	public void before() {
		versions = getVersions();
		versionsRepository.saveAll(versions);
	}

	@Test
	public void latestPatch() {
		String latestVersion = versionsRepository.latestVersion().getPatch();
		assertThat(versions).element(0).extracting(Version::getPatch).isEqualTo(latestVersion);
	}

}
