package com.drumonii.loltrollbuild;

import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RiotApiProperties.class)
public class LeagueTrollBuildApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeagueTrollBuildApplication.class, args);
	}

}
