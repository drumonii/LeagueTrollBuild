package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.config.JpaConfig;
import com.drumonii.loltrollbuild.model.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest(includeFilters = @Filter(JsonComponent.class))
@ImportAutoConfiguration(JacksonAutoConfiguration.class)
@Import(JpaConfig.class)
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
		assertThat(latestVersion).isEqualTo(versions.get(0).getPatch());
	}

}