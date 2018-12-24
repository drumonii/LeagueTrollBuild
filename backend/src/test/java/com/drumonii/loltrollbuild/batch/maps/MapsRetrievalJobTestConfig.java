package com.drumonii.loltrollbuild.batch.maps;

import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MapsRetrievalJobTestConfig {

	@Bean
	public JobLauncherTestUtils jobLauncherTestUtils() {
		return new MapsRetrievalJobLauncherTestUtils();
	}

}
