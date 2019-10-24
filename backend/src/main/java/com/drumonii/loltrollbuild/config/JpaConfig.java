package com.drumonii.loltrollbuild.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration for Data JPA components.
 */
@Configuration(proxyBeanMethods = false)
@EnableJpaAuditing
public class JpaConfig {
}
