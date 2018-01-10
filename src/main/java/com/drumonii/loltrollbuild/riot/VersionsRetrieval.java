package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import com.drumonii.loltrollbuild.riot.service.VersionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * A {@link RestController} which retrieves the list of versions from Riot's {@code lol-static-data-v3} or
 * {@code Data Dragon} API with the {@code /riot/versions} URL mapping.
 */
@RestController
@RequestMapping("/riot/versions")
public class VersionsRetrieval {

	@Autowired
	private VersionsService versionsService;

	@Autowired
	private VersionsRepository versionsRepository;

	/**
	 * Returns the {@link List} of {@link Version}s from Riot for the most current patch.
	 *
	 * @return the {@link List} of {@link Version}s from Riot
	 */
	@GetMapping
	public List<Version> versions() {
		return versionsService.getVersions();
	}

	/**
	 * Returns the latest patch {@link Version} from Riot.
	 *
	 * @return the latest patch {@link Version} from Riot
	 */
	@GetMapping(value = "/latest")
	public Version latestVersion() {
		return versionsService.getLatestVersion();
	}

	/**
	 * Persists the latest patch {@link Version} from Riot. If the previous latest patch version is the same as the
	 * current version from Riot, then no database changes are made and the existing patch version is returned.
	 * Otherwise, the newest patch version is persisted.
	 *
	 * @return the latest patch {@link Version} is persisted to the database or the existing patch version
	 */
	@PostMapping(value = "/latest", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Version saveLatestVersion() {
		Version latestPatchVersion = versionsService.getLatestVersion();
		Version latestSavedVersion = versionsRepository.latestVersion();
		if (latestPatchVersion == null && latestSavedVersion == null) {
			return new Version("0", 0, 0, 0);
		} else if ((latestPatchVersion != null && latestSavedVersion == null) ||
				(latestPatchVersion != null && latestSavedVersion.compareTo(latestPatchVersion) == -1)) {
			return versionsRepository.save(latestPatchVersion);
		}
		return latestSavedVersion;
	}

}
