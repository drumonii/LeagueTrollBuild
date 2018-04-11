package com.drumonii.loltrollbuild.batch.versions;

import com.drumonii.loltrollbuild.model.Version;
import org.springframework.batch.item.ItemReader;

import java.util.List;

/**
 * {@link ItemReader} for reading {@link Version}s from Riot's API.
 */
public class VersionsRetrievalItemReader implements ItemReader<Version> {

	private List<Version> versions;

	public VersionsRetrievalItemReader(List<Version> versions) {
		this.versions = versions;
	}

	@Override
	public Version read() {
		if (!versions.isEmpty()) {
			return versions.remove(0);
		}
		return null;
	}

}
