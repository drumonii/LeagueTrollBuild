package com.drumonii.loltrollbuild.batch.versions;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import com.drumonii.loltrollbuild.riot.service.VersionsService;
import com.drumonii.loltrollbuild.test.batch.BatchTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.data.domain.Sort.Direction.DESC;

@RunWith(SpringRunner.class)
@BatchTest(VersionsRetrievalJobConfig.class)
@Import(VersionsRetrievalJobTestConfig.class)
public abstract class VersionsRetrievalJobConfigTest {

	@MockBean
	protected VersionsService versionsService;

	@Autowired
	private VersionsRepository versionsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	private VersionsRetrievalJobLauncherTestUtils jobLauncherTestUtils;

	protected List<Version> versions;
	protected Version latestVersion;

	public abstract void before();

	@Test
	public void savesNewVersions() throws Exception {
		given(versionsService.getVersions()).willReturn(new ArrayList<>(versions));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		assertThat(versionsRepository.findAll(new Sort(DESC, "major", "minor", "revision")))
				.containsExactlyElementsOf(versions);
	}

	@Test
	public void savesNewerVersion() throws Exception {
		List<Version> versions = versionsRepository.saveAll(this.versions);

		Version newerVersion = new Version();
		newerVersion.setMajor(latestVersion.getMajor() + 1);
		newerVersion.setMinor(latestVersion.getMinor() + 1);
		newerVersion.setRevision(latestVersion.getRevision() + 1);
		newerVersion.setPatch(newerVersion.getMajor() + "." + newerVersion.getMinor() + "." + newerVersion.getRevision());

		versions.add(newerVersion);
		versions.sort(Collections.reverseOrder());

		given(versionsService.getVersions()).willReturn(new ArrayList<>(versions));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		assertThat(versionsRepository.findAll(new Sort(DESC, "major", "minor", "revision")))
				.containsExactlyElementsOf(versions);
	}

	@Test
	public void ignoresOldVersions() throws Exception {
		List<Version> versions = versionsRepository.saveAll(this.versions);

		given(versionsService.getVersions()).willReturn(new ArrayList<>(versions));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		assertThat(versionsRepository.findAll(new Sort(DESC, "major", "minor", "revision")))
				.containsExactlyElementsOf(versions);
	}

	private JobParameters getJobParameters() {
		return new JobParametersBuilder()
				.addString("latestRiotPatch", latestVersion.getPatch())
				.addDouble("random", Math.random())
				.toJobParameters();
	}

}