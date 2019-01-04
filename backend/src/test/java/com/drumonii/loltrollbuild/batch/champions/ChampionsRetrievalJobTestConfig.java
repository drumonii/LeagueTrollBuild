package com.drumonii.loltrollbuild.batch.champions;

import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ChampionsRetrievalJobTestConfig {

	@Bean
	public JobLauncherTestUtils jobLauncherTestUtils() {
		return new ChampionsRetrievalJobLauncherTestUtils();
	}

}