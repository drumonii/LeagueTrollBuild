package com.drumonii.loltrollbuild.batch.versions;

import com.drumonii.loltrollbuild.model.Version;
import org.springframework.batch.item.ItemProcessor;

/**
 * {@link ItemProcessor} for processing {@link Version}s from Riot's API.
 */
public class VersionsRetrievalItemProcessor implements ItemProcessor<Version, Version> {

	private final Version latestVersion;

	public VersionsRetrievalItemProcessor(Version latestVersion) {
		this.latestVersion = latestVersion;
	}

	@Override
	public Version process(Version version) {
		if (latestVersion == null || version.compareTo(latestVersion) > 0) {
			return version;
		}
		return null;
	}

}
