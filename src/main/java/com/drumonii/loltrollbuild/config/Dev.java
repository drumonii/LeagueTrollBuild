package com.drumonii.loltrollbuild.config;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.drumonii.loltrollbuild.config.Profiles.DEV;

/**
 * Indicates an embedded database profile.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Profile(value = DEV)
public @interface Dev {
}
