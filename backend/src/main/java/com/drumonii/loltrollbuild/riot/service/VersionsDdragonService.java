package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.config.Profiles.Ddragon;
import com.drumonii.loltrollbuild.model.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Ddragon
public class VersionsDdragonService implements VersionsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(VersionsDdragonService.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	@Override
	public List<Version> getVersions() {
		LOGGER.info("Getting Versions from Riot");
		List<Version> versions;
		try {
			versions = restTemplate.exchange(versionsUri.toString(), HttpMethod.GET, null,
					new ParameterizedTypeReference<List<Version>>() {}).getBody();
		} catch (RestClientException e) {
			LOGGER.warn("Unable to retrieve Versions from Data Dragon due to:", e);
			return new ArrayList<>();
		}
		versions = versions.stream()
				.filter(v -> v.getRevision() != 0) // filter lolpatch_7.17 style which has a 0 revision
				.sorted(Collections.reverseOrder())
				.collect(Collectors.toList());
		return versions;
	}

}
