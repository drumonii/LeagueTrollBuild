package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.util.Arrays;
import java.util.List;

/**
 * A {@link RestController} which retrieves the list of versions from Riot's {@code lol-static-data-v1.2} API with the
 * {@code /riot/versions} URL mapping.
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
	 * Creates a {@link ModelAttribute} of a {@link List} of {@link Version}s from Riot.
	 *
	 * @return a {@link List} of {@link Version}s from Riot
	 */
	@ModelAttribute
	public List<Version> versionsFromResponse() {
		return Arrays.asList(restTemplate.getForObject(versionsUri.toString(), Version[].class));
	}

	/**
	 * Returns the {@link List} of {@link Version}s from Riot for the most current patch.
	 *
	 * @param versions the {@link ModelAttribute} of {@link List} of {@link Version}s from Riot
	 * @return the {@link List} of {@link Version}s from Riot
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<Version> versions(@ModelAttribute List<Version> versions) {
		return versions;
	}

	/**
	 * Returns the latest patch {@link Version} from Riot.
	 *
	 * @param versions the {@link ModelAttribute} of {@link List} of {@link Version}s from Riot
	 * @return the latest patch {@link Version} from Riot
	 */
	@RequestMapping(value = "/latest", method = RequestMethod.GET)
	public Version latestVersion(@ModelAttribute List<Version> versions) {
		return versions.get(0);
	}

	/**
	 * Persists the latest patch {@link Version} from Riot. If the previous latest patch version is the same as the
	 * current version from Riot, then no database changes are made and the existing patch version is returned.
	 * Otherwise, the previous patch version is deleted from the database and the newest patch version is persisted.
	 *
	 * @param versions versions the {@link ModelAttribute} of {@link List} of {@link Version}s from Riot
	 * @return the latest patch {@link Version} is persisted to the database or the existing patch version
	 */
	@RequestMapping(value = "/latest", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Version saveLatestVersion(@ModelAttribute List<Version> versions) {
		Version latestPatchVersion = versions.get(0);
		if (!latestPatchVersion.equals(versionsRepository.latestVersion())) {
			versionsRepository.deleteAll();
			return versionsRepository.save(latestPatchVersion);
		}
		return latestPatchVersion;
	}

}
