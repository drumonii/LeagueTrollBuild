package com.drumonii.loltrollbuild.batch.versions;

import com.drumonii.loltrollbuild.model.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * {@link ItemProcessor} for processing {@link Version}s from Riot's API.
 */
public class VersionsRetrievalItemProcessor implements ItemProcessor<Version, Version> {

	private static final Logger LOGGER = LoggerFactory.getLogger(VersionsRetrievalItemProcessor.class);

	private final Version latestVersion;

	public VersionsRetrievalItemProcessor(Version latestVersion) {
		this.latestVersion = latestVersion;
	}

	@Override
	public Version process(Version version) {
		LOGGER.info("Processing Version: {}", version.getPatch());
		if (latestVersion == null || version.compareTo(latestVersion) > 0) {
			return version;
		}
		return null;
	}

}
