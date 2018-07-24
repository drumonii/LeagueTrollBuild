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

	/*
	 * Database profiles
	 */

	/**
	 * Profile for using an embedded database.
	 */
	public static final String EMBEDDED = "embedded";

	/**
	 * Profile for using an external database.
	 */
	public static final String EXTERNAL = "external";

	/*
	 * Riot API profiles
	 */

	/**
	 * Profile for using Riot's {@code Data Dragon} API.
	 */
	public static final String DDRAGON = "ddragon";

	/**
	 * Profile for local development.
	 */
	public static final String DEV = "dev";

	/**
	 * Profile for running unit tests.
	 */
	public static final String TESTING = "testing";

	/**
	 * Indicates a local dev profile.
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

	/*
	 * Database profiles
	 */

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

	/*
	 * Riot API profiles
	 */

	/**
	 * Indicates a {@code Data Dragon} API profile.
	 */
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@Profile(value = DDRAGON)
	public @interface Ddragon {
	}

}
