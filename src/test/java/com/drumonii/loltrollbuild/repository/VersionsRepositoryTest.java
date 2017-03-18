package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class VersionsRepositoryTest extends BaseSpringTestRunner {

	@Test
	public void latestPatch() throws IOException {
		versionsRepository.save(versions);

		String latestVersion = versionsRepository.latestVersion().getPatch();
		assertThat(latestVersion).isEqualTo(versions.get(0).getPatch());
	}

}