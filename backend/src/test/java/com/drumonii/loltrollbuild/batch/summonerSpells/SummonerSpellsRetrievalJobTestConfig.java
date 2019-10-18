package com.drumonii.loltrollbuild.batch.summonerSpells;

import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class SummonerSpellsRetrievalJobTestConfig {

	@Bean
	JobLauncherTestUtils jobLauncherTestUtils() {
		return new SummonerSpellsRetrievalJobLauncherTestUtils();
	}

}
