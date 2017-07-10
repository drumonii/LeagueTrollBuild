package com.drumonii.loltrollbuild.batch.versions;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.riot.service.VersionsService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.data.domain.Sort.Direction.DESC;

public class VersionsRetrievalJobConfigTest extends BaseSpringTestRunner {

	@MockBean
	private VersionsService versionsService;

	@Autowired
	@Qualifier("versionsRetrievalJob")
	private Job versionsRetrievalJob;

	@Before
	public void before() {
		super.before();

		given(versionsService.getVersions()).willReturn(versions);

		jobLauncherTestUtils.setJob(versionsRetrievalJob);
	}

	@Test
	public void savesNewVersions() throws Exception {
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		assertThat(versionsRepository.findAll(new Sort(DESC, "major", "minor", "revision")))
				.containsExactlyElementsOf(versions);
	}

	@Test
	public void ignoresOldVersions() throws Exception {
		versionsRepository.save(versions);

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(getJobParameters());
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		assertThat(versionsRepository.findAll(new Sort(DESC, "major", "minor", "revision")))
				.containsExactlyElementsOf(versions);
	}

}