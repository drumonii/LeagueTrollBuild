package com.drumonii.loltrollbuild.test.rest;

import com.drumonii.loltrollbuild.config.CacheConfig;
import com.drumonii.loltrollbuild.config.JpaConfig;
import com.drumonii.loltrollbuild.config.RiotApiConfig;
import com.drumonii.loltrollbuild.config.WebSecurityConfig;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
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
 * Annotation that can be used in combination with {@code @RunWith(SpringRunner.class)} for a typical REST Controller
 * test. Can be used when a test focuses <strong>only</strong> on Spring REST components.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@BootstrapWith(WebMvcRestTestContextBootstrapper.class)
@OverrideAutoConfiguration(enabled = false)
@TypeExcludeFilters(WebMvcRestTypeExcludeFilter.class)
@Transactional
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureWebMvc
@AutoConfigureWebClient
@AutoConfigureMockMvc
@ImportAutoConfiguration({ SpringDataWebAutoConfiguration.class })
@Import({ CacheConfig.class, JpaConfig.class, RiotApiConfig.class, WebSecurityConfig.class })
public @interface WebMvcRestTest {

	/**
	 * Specifies the REST controllers to test. This is an alias of {@link #controllers()} which can be used for brevity
	 * if no other attributes are defined. See {@link #controllers()} for details.
	 *
	 * @see #controllers()
	 * @return the controllers to test
	 */
	@AliasFor("controllers")
	Class<?>[] value() default {};

	/**
	 * Specifies the controllers to test.
	 *
	 * @see #value()
	 * @return the controllers to test
	 */
	@AliasFor("value")
	Class<?>[] controllers() default {};

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
