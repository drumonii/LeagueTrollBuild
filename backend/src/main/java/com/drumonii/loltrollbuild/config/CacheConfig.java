package com.drumonii.loltrollbuild.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for cache components.
 */
@Configuration(proxyBeanMethods = false)
@EnableCaching
public class CacheConfig {
}
