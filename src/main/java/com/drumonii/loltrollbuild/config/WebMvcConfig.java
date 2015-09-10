package com.drumonii.loltrollbuild.config;

import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Configuration for web related beans and overriding components.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	public static final String TEMP_DIR = FileUtils.getTempDirectoryPath();
	public static final String RESOURCE_DIR = "loltrollbuild";

}
