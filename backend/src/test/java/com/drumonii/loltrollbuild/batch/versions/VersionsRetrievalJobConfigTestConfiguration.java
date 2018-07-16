package com.drumonii.loltrollbuild.batch.versions;

import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class VersionsRetrievalJobConfigTestConfiguration {

	@Bean
	public JobLauncherTestUtils jobLauncherTestUtils() {
		return new VersionsRetrievalJobLauncherTestUtils();
	}

}
