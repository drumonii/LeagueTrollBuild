package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.util.Arrays;
import java.util.List;

/**
 * A {@link RestController} which retrieves the list of String versions from Riot's {@code lol-static-data-v1.2} API
 * with the {@code /riot/versions} URL mapping.
 */
@RestController
@RequestMapping("/riot/versions")
public class VersionsRetrieval {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	@Autowired
	private VersionsRepository versionsRepository;

	/**
	 * Creates a {@link ModelAttribute} of a {@link List} of String versions from Riot.
	 *
	 * @return a {@link List} of String versions from Riot
	 */
	@ModelAttribute
	public List<String> versionsFromResponse() {
		return Arrays.asList(restTemplate.getForObject(versionsUri.toString(), String[].class));
	}

	/**
	 * Returns the {@link List} of String versions from Riot for the most current patch.
	 *
	 * @param versions the {@link ModelAttribute} of {@link List} of String versions from Riot
	 * @return the {@link List} of String versions from Riot
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<String> versions(@ModelAttribute List<String> versions) {
		return versions;
	}

	/**
	 * Returns the latest patch String version from Riot.
	 *
	 * @param versions the {@link ModelAttribute} of {@link List} of String versions from Riot
	 * @return the latest patch String version from Riot
	 */
	@RequestMapping(value = "/latest", method = RequestMethod.GET)
	public String latestPatch(@ModelAttribute List<String> versions) {
		return versions.get(0);
	}

	/**
	 * Persists the latest patch String version from Riot. If the previous latest patch version is the same as the
	 * current version from Riot, then no database changes are made and the existing patch versionn is returned.
	 * Otherwise, the previous patch version is deleted from the database and the newest patch version is persisted.
	 *
	 * @param versions versions the {@link ModelAttribute} of {@link List} of String versions from Riot
	 * @return the latest patch String version persisted to the database or the existing patch version
	 */
	@RequestMapping(value = "/latest", method = RequestMethod.POST)
	public String saveLatestPatch(@ModelAttribute List<String> versions) {
		Version latestPatchVersion = new Version(versions.get(0));
		if (!latestPatchVersion.equals(versionsRepository.latestVersion())) {
			versionsRepository.deleteAll();
			return versionsRepository.save(latestPatchVersion).getPatch();
		}
		return latestPatchVersion.getPatch();
	}

}
