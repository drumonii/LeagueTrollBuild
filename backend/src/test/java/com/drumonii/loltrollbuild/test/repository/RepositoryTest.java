package com.drumonii.loltrollbuild.test.repository;

import com.drumonii.loltrollbuild.config.JpaConfig;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Annotation that can be used when a test focuses <strong>only</strong> on JPA repositories.
 * Using this annotation will disable full auto-configuration and instead apply only configuration relevant to JPA tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@DataJpaTest(includeFilters = @Filter({ JsonComponent.class }))
@ImportAutoConfiguration({ JacksonAutoConfiguration.class })
@Import({ JpaConfig.class })
public @interface RepositoryTest {
}
