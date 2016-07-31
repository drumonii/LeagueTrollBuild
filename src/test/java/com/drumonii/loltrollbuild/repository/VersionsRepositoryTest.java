package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Version;
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
	public void crudOperations() throws IOException {
		// Create
		versionsRepository.save(versions);

		// Select
		Version versionFromDb = versionsRepository.findOne(versions.get(0).getPatch());
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

		String latestVersion = versionsRepository.latestVersion().getPatch();
		assertThat(latestVersion).isEqualTo(versions.get(0).getPatch());
	}

}