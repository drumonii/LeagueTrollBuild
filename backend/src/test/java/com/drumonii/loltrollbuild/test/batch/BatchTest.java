package com.drumonii.loltrollbuild.test.batch;

import com.drumonii.loltrollbuild.config.BatchConfig;
import com.drumonii.loltrollbuild.config.CacheConfig;
import com.drumonii.loltrollbuild.config.JpaConfig;
import com.drumonii.loltrollbuild.config.RiotApiConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;

/**
 * Annotation that can be used when a test focuses <strong>only</strong> on Spring Batch jobs.
 * Using this annotation will disable full auto-configuration and instead apply only configuration relevant to batch tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@BootstrapWith(SpringBootTestContextBootstrapper.class)
@ExtendWith(SpringExtension.class)
@OverrideAutoConfiguration(enabled = false)
@TypeExcludeFilters(BatchTypeExcludeFilter.class)
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureWebClient
@ImportAutoConfiguration({ BatchAutoConfiguration.class })
@Import({ BatchConfig.class, CacheConfig.class, JobLauncherTestUtilsConfig.class, JpaConfig.class, RiotApiConfig.class })
@Sql("/CLEAN_TABLES.sql") // have to use because Spring Batch complains about @Transactional
@TestPropertySource(properties = "spring.datasource.generate-unique-name=true")
public @interface BatchTest {

	/**
	 * Specifies the batch jobs to test. This is an alias of {@link #jobs()} which can be used for brevity
	 * if no other attributes are defined. See {@link #jobs()} for details.
	 *
	 * @see #jobs()
	 * @return the batch jobs to test
	 */
	@AliasFor("jobs")
	Class<?>[] value() default {};

	/**
	 * Specifies the batch jobs to test.
	 *
	 * @see #value()
	 * @return the jobs to test
	 */
	@AliasFor("value")
	Class<?>[] jobs() default {};

	/**
	 * A set of include filters which can be used to add otherwise filtered beans to the application context.
	 *
	 * @return include filters to apply
	 */
	Filter[] includeFilters() default {};

	/**
	 * A set of exclude filters which can be used to filter beans that would otherwise be added to the application context.
	 *
	 * @return exclude filters to apply
	 */
	Filter[] excludeFilters() default {};

}
