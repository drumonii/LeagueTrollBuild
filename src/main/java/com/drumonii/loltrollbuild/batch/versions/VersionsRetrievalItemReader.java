package com.drumonii.loltrollbuild.batch.versions;

import com.drumonii.loltrollbuild.model.Version;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@link ItemReader} for reading {@link Version}s from Riot's API.
 */
public class VersionsRetrievalItemReader extends AbstractItemStreamItemReader<Version> {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	private List<Version> versions;
	private int nextVersion;

	@Override
	public Version read() throws Exception {
		if (nextVersion < versions.size()) {
			return versions.get(nextVersion++);
		}
		return null;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		versions = new ArrayList<>(restTemplate.exchange(versionsUri.toString(), HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Version>>() {}).getBody());
		Collections.sort(versions);
	}

}
