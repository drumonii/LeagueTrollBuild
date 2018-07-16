package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.config.Profiles.Ddragon;
import com.drumonii.loltrollbuild.model.Version;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Ddragon
public class VersionsDdragonService implements VersionsService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	@Override
	public List<Version> getVersions() {
		List<Version> versions;
		try {
			versions = restTemplate.exchange(versionsUri.toString(), HttpMethod.GET, null,
					new ParameterizedTypeReference<List<Version>>() {}).getBody();
		} catch (RestClientException e) {
			log.warn("Unable to retrieve Versions from Data Dragon due to:", e);
			return new ArrayList<>();
		}
		versions = versions.stream()
				.filter(v -> v.getRevision() != 0) // filter lolpatch_7.17 style which has a 0 revision
				.sorted(Collections.reverseOrder())
				.collect(Collectors.toList());
		return versions;
	}

}
