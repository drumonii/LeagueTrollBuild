package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.Version;

import java.util.List;

/**
 * {@code @Service} for {@link Version}s.
 */
public interface VersionsService {

	/**
	 * Returns the {@link List} of {@link Version}s from Riot.
	 *
	 * @return the {@link List} of {@link Version}s from Riot
	 */
	List<Version> getVersions();

	/**
	 * The latest patch {@link Version} from Riot.
	 *
	 * @return the latest patch {@link Version} from Riot
	 */
	Version getLatestVersion();

}
