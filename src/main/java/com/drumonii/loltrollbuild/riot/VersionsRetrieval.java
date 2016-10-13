package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.util.Collections;
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
		List<Version> versions = restTemplate.exchange(versionsUri.toString(), HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Version>>() {}).getBody();
		Collections.sort(versions, Collections.reverseOrder());
		return versions;
	}

	/**
	 * Returns the {@link List} of {@link Version}s from Riot for the most current patch.
	 *
	 * @param versions the {@link ModelAttribute} of {@link List} of {@link Version}s from Riot
	 * @return the {@link List} of {@link Version}s from Riot
	 */
	@GetMapping
	public List<Version> versions(@ModelAttribute List<Version> versions) {
		return versions;
	}

	/**
	 * Returns the latest patch {@link Version} from Riot.
	 *
	 * @param versions the {@link ModelAttribute} of {@link List} of {@link Version}s from Riot
	 * @return the latest patch {@link Version} from Riot
	 */
	@GetMapping(value = "/latest")
	public Version latestVersion(@ModelAttribute List<Version> versions) {
		if (versions.isEmpty()) {
			return new Version("0", 0, 0, 0);
		}
		return versions.get(0);
	}

	/**
	 * Persists the latest patch {@link Version} from Riot. If the previous latest patch version is the same as the
	 * current version from Riot, then no database changes are made and the existing patch version is returned.
	 * Otherwise, the newest patch version is persisted.
	 *
	 * @param versions versions the {@link ModelAttribute} of {@link List} of {@link Version}s from Riot
	 * @return the latest patch {@link Version} is persisted to the database or the existing patch version
	 */
	@PostMapping(value = "/latest", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Version saveLatestVersion(@ModelAttribute List<Version> versions) {
		Version latestPatchVersion = versions.stream().findFirst().orElse(new Version("0.0.0"));
		if (!latestPatchVersion.equals(versionsRepository.latestVersion())) {
			return versionsRepository.save(latestPatchVersion);
		}
		return latestPatchVersion;
	}

}
