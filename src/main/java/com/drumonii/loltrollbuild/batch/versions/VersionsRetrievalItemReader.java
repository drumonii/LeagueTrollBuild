package com.drumonii.loltrollbuild.batch.versions;

import com.drumonii.loltrollbuild.model.Version;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.util.Collections;
import java.util.List;

/**
 * {@link ItemReader} for reading {@link Version}s from Riot's API.
 */
public class VersionsRetrievalItemReader implements ItemReader<Version> {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	private List<Version> versions;
	private int nextChampion;

	@Override
	public Version read() throws Exception {
		if (isNotInitialized()) {
			versions = restTemplate.exchange(versionsUri.toString(), HttpMethod.GET, null,
					new ParameterizedTypeReference<List<Version>>() {}).getBody();
			Collections.sort(versions);
		}

		Version version = null;
		if (nextChampion < versions.size()) {
			version = versions.get(nextChampion);
			nextChampion++;
		}

		return version;
	}

	private boolean isNotInitialized() {
		return versions == null;
	}

}
