package com.drumonii.loltrollbuild.config;

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

}
