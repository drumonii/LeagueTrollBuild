package com.drumonii.loltrollbuild.annotation;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.*;

import static com.drumonii.loltrollbuild.config.WebSecurityConfig.UserRole.ADMIN;
import static com.drumonii.loltrollbuild.config.WebSecurityConfig.WebDevTestingSecurityConfig.IN_MEM_PASSWORD;
import static com.drumonii.loltrollbuild.config.WebSecurityConfig.WebDevTestingSecurityConfig.IN_MEM_USERNAME;

/**
 * Annotation that can be added to a test method which emulates running with an mocked admin user.
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithMockUser(username = IN_MEM_USERNAME, password = IN_MEM_PASSWORD, roles = ADMIN)
public @interface WithMockAdminUser {
}
