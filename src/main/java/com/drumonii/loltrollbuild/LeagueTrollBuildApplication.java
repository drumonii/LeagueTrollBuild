package com.drumonii.loltrollbuild;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableJpaAuditing
@EnableBatchProcessing
@EnableScheduling
public class LeagueTrollBuildApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeagueTrollBuildApplication.class, args);
	}

}
