package com.drumonii.loltrollbuild.config;

import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuration for web mvc related beans and overriding components.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	public static final String TEMP_DIR = FileUtils.getTempDirectoryPath();
	public static final String RESOURCE_DIR = "loltrollbuild";

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		Path path = Paths.get(TEMP_DIR, RESOURCE_DIR);
		registry.addResourceHandler("/resources/**")
				.addResourceLocations("file:///" + new FileSystemResource(path.toFile()).getPath() + "/");
	}

}
