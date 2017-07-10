package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Version;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class VersionsServiceTest extends BaseSpringTestRunner {

	@Autowired
	private RestTemplate restTemplate;

	private MockRestServiceServer mockServer;

	@Autowired
	private VersionsService versionsService;

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	@Before
	public void before() {
		super.before();

		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	public void getVersions() throws Exception {
		mockServer.expect(requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(versions), MediaType.APPLICATION_JSON_UTF8));

		List<Version> versions = versionsService.getVersions();
		mockServer.verify();

		assertThat(versions).isNotEmpty();
	}

	@Test
	public void getVersionsWithRestClientException() throws Exception {
		mockServer.expect(requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		List<Version> versions = versionsService.getVersions();
		mockServer.verify();

		assertThat(versions).isEmpty();
	}

	@Test
	public void getLatestVersion() throws Exception {
		mockServer.expect(requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(versions), MediaType.APPLICATION_JSON_UTF8));

		Version version = versionsService.getLatestVersion();
		mockServer.verify();

		assertThat(version).isNotNull();
		assertThat(version).isEqualTo(versions.get(0));
	}

	@Test
	public void getLatestVersionWithRestClientException() throws Exception {
		mockServer.expect(requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		Version version = versionsService.getLatestVersion();
		mockServer.verify();

		assertThat(version).isNull();
	}

}