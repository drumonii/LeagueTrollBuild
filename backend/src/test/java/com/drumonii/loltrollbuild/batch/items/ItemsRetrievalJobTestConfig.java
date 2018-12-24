package com.drumonii.loltrollbuild.batch.items;

import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ItemsRetrievalJobTestConfig {

	@Bean
	public JobLauncherTestUtils jobLauncherTestUtils() {
		return new ItemsRetrievalJobLauncherTestUtils();
	}

}
