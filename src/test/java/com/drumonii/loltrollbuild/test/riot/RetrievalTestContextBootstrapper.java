package com.drumonii.loltrollbuild.test.riot;

import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.TestContextBootstrapper;
import org.springframework.test.context.web.WebMergedContextConfiguration;

/**
 * {@link TestContextBootstrapper} for {@link RetrievalTest @RetrievalTest} support.
 */
public class RetrievalTestContextBootstrapper extends SpringBootTestContextBootstrapper {

	@Override
	protected MergedContextConfiguration processMergedContextConfiguration(MergedContextConfiguration mergedConfig) {
		return new WebMergedContextConfiguration(super.processMergedContextConfiguration(mergedConfig), "");
	}

}
