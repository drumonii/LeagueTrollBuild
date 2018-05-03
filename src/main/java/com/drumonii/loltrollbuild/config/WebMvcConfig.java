package com.drumonii.loltrollbuild.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration for Web MVC components.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// for Angular
		registry.addMapping("/**")
				.allowedOrigins("http://localhost:4200")
				.allowCredentials(true)
				.exposedHeaders("Location");
	}

}