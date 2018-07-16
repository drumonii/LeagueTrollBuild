package com.drumonii.loltrollbuild.test.riot;

import com.drumonii.loltrollbuild.config.CacheConfig;
import com.drumonii.loltrollbuild.config.JpaConfig;
import com.drumonii.loltrollbuild.config.RiotApiConfig;
import com.drumonii.loltrollbuild.config.WebSecurityConfig;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.BootstrapWith;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * Annotation that can be used in combination with {@code @RunWith(SpringRunner.class)} for a typical retrievals test.
 * Can be used when a test focuses <strong>only</strong> on retrievals components.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@BootstrapWith(RetrievalTestContextBootstrapper.class)
@OverrideAutoConfiguration(enabled = false)
@TypeExcludeFilters(RetrievalTypeExcludeFilter.class)
@Transactional
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureWebMvc
@AutoConfigureWebClient
@AutoConfigureMockMvc(secure = false)
@ImportAutoConfiguration
@Import({ CacheConfig.class, JpaConfig.class, RiotApiConfig.class, WebSecurityConfig.class })
public @interface RetrievalTest {

	/**
	 * Specifies the retrievals to test. This is an alias of {@link #retrievals()} which can be used for brevity
	 * if no other attributes are defined. See {@link #retrievals()} for details.
	 *
	 * @see #retrievals()
	 * @return the controllers to test
	 */
	@AliasFor("retrievals")
	Class<?>[] value() default {};

	/**
	 * Specifies the retrievals to test.
	 *
	 * @see #value()
	 * @return the retrievals to test
	 */
	@AliasFor("value")
	Class<?>[] retrievals() default {};

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
