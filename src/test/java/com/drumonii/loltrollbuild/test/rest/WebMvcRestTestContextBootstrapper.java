package com.drumonii.loltrollbuild.test.rest;

import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.TestContextBootstrapper;
import org.springframework.test.context.web.WebMergedContextConfiguration;

/**
 * {@link TestContextBootstrapper} for {@link WebMvcRestTest @WebMvcRestTest} support.
 */
public class WebMvcRestTestContextBootstrapper extends SpringBootTestContextBootstrapper {

	@Override
	protected MergedContextConfiguration processMergedContextConfiguration(MergedContextConfiguration mergedConfig) {
		return new WebMergedContextConfiguration(super.processMergedContextConfiguration(mergedConfig), "");
	}

}
