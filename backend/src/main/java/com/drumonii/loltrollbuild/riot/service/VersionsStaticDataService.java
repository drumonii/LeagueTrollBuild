package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.config.Profiles.StaticData;
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

@Service
@Slf4j
@StaticData
public class VersionsStaticDataService implements VersionsService {

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
			log.warn("Unable to retrieve Versions from lol-static-data-v3 due to:", e);
			return new ArrayList<>();
		}
		versions.sort(Collections.reverseOrder());
		return versions;
	}

}
