package com.drumonii.loltrollbuild.batch.versions;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * {@link ItemProcessor} for processing {@link Version}s from Riot's API.
 */
public class VersionsRetrievalItemProcessor implements ItemProcessor<Version, Version> {

	@Autowired
	private VersionsRepository versionsRepository;

	@Override
	public Version process(Version version) {
		Version latestVersion = versionsRepository.latestVersion();
		if (latestVersion != null && version.compareTo(latestVersion) <= 0) {
			return null;
		}
		return version;
	}

}
