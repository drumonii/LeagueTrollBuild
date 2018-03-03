package com.drumonii.loltrollbuild.batch.versions;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import com.drumonii.loltrollbuild.riot.service.VersionsService;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link ItemReader} for reading {@link Version}s from Riot's API.
 */
public class VersionsRetrievalItemReader extends AbstractItemStreamItemReader<Version> {

	@Autowired
	private VersionsService versionsService;

	@Autowired
	private VersionsRepository versionsRepository;

	private List<Version> versions;
	private int nextVersion;

	@Override
	public Version read() {
		if (nextVersion < versions.size()) {
			return versions.get(nextVersion++);
		}
		return null;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		versions = versionsService.getVersions();
		Version latestVersion = versionsRepository.latestVersion();
		if (latestVersion != null) {
			versions = versions.stream()
					.filter(version -> version.compareTo(latestVersion) > 0)
					.collect(Collectors.toList());
		}
	}

}
