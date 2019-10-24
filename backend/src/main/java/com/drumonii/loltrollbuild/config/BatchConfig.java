package com.drumonii.loltrollbuild.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for batch components.
 */
@Configuration(proxyBeanMethods = false)
@EnableBatchProcessing
public class BatchConfig {
}
