package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.Version;

import java.util.List;

/**
 * Created by Drummond on 7/8/2017.
 */
public interface VersionsService {

	/**
	 *
	 * @return
	 */
	List<Version> getVersions();

	/**
	 *
	 * @return
	 */
	Version getLatestVersion();

}
