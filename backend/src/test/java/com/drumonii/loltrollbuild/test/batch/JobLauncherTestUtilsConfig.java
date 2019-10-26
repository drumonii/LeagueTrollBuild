package com.drumonii.loltrollbuild.test.batch;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class JobLauncherTestUtilsConfig {

    @Bean
    public JobLauncherTestUtilsBeanFactoryPostProcessor jobLauncherTestUtilsBeanFactoryPostProcessor() {
        return new JobLauncherTestUtilsBeanFactoryPostProcessor();
    }

}
