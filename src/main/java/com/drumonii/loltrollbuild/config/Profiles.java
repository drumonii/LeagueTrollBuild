package com.drumonii.loltrollbuild.config;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Spring {@code @Profile} declarations.
 */
public class Profiles {

	/**
	 * Profile for using an embedded database.
	 */
	public static final String EMBEDDED = "embedded";

	/**
	 * Profile for using an external database.
	 */
	public static final String EXTERNAL = "external";

	/**
	 * Profile for local development.
	 */
	public static final String DEV = "dev";

	/**
	 * Profile for running unit tests.
	 */
	public static final String TESTING = "testing";

	/**
	 * Indicates an embedded database profile.
	 */
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@Profile(value = DEV)
	public @interface Dev {
	}

	/**
	 * Indicates a testing profile.
	 */
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@Profile(value = TESTING)
	public @interface Testing {
	}

	/**
	 * Indicates an embedded database profile.
	 */
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@Profile(value = EMBEDDED)
	public @interface Embedded {
	}

	/**
	 * Indicates an external database profile.
	 */
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@Profile(value = EXTERNAL)
	public @interface External {
	}

}
