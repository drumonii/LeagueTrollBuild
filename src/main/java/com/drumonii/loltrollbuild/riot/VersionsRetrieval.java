package com.drumonii.loltrollbuild.riot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

	@RequestMapping(method = RequestMethod.GET)
	public List<String> versions() {
		return Arrays.asList(restTemplate.getForObject(versionsUri.toString(), String[].class));
	}

}
