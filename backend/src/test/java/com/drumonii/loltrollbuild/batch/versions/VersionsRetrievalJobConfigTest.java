package com.drumonii.loltrollbuild.batch.versions;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import com.drumonii.loltrollbuild.riot.service.VersionsService;
import com.drumonii.loltrollbuild.test.batch.AbstractBatchTests;
import com.drumonii.loltrollbuild.test.batch.BatchTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.data.domain.Sort.Direction.DESC;

@BatchTest(VersionsRetrievalJobConfig.class)
abstract class VersionsRetrievalJobConfigTest extends AbstractBatchTests {

	private static final Sort SORT = Sort.by(DESC, "major", "minor", "revision");

	@MockBean
	protected VersionsService versionsService;

	@Autowired
	private VersionsRepository versionsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	protected List<Version> versions;

	@Test
	void savesNewVersions() throws Exception {
		given(versionsService.getVersions()).willReturn(new ArrayList<>(versions));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		assertThat(versionsRepository.findAll(SORT))
				.containsExactlyElementsOf(versions);
	}

	@Test
	void savesNewerVersion() throws Exception {
		List<Version> versions = versionsRepository.saveAll(this.versions);

		Version newerVersion = Version.patch(latestVersion.getPatch());
		newerVersion.setMajor(newerVersion.getMajor() + 1);
		newerVersion.setMinor(newerVersion.getMinor() + 1);
		newerVersion.setRevision(newerVersion.getRevision() + 1);
		newerVersion.setPatch(newerVersion.getMajor() + "." + newerVersion.getMinor() + "." + newerVersion.getRevision());

		versions.add(newerVersion);
		versions.sort(Collections.reverseOrder());

		given(versionsService.getVersions()).willReturn(new ArrayList<>(versions));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		assertThat(versionsRepository.findAll(SORT))
				.containsExactlyElementsOf(versions);
	}

	@Test
	void ignoresOldVersions() throws Exception {
		List<Version> versions = versionsRepository.saveAll(this.versions);

		given(versionsService.getVersions()).willReturn(new ArrayList<>(versions));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		assertThat(versionsRepository.findAll(SORT))
				.containsExactlyElementsOf(versions);
	}

}
