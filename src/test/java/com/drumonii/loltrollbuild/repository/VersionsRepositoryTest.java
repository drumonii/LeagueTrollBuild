package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class VersionsRepositoryTest extends BaseSpringTestRunner {

	@Autowired
	private VersionsRepository versionsRepository;

	@After
	public void after() {
		versionsRepository.deleteAll();
	}

	@Test
	public void latestPatch() throws IOException {
		versionsRepository.save(versions);

		String latestVersion = versionsRepository.latestVersion().getPatch();
		assertThat(latestVersion).isEqualTo(versions.get(0).getPatch());
	}

}