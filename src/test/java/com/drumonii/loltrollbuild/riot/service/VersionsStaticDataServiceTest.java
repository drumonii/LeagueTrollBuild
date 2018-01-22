package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.config.RiotApiConfig;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.UriComponents;

import java.io.IOException;
import java.util.List;

import static com.drumonii.loltrollbuild.config.Profiles.STATIC_DATA;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(VersionsService.class)
@Import(RiotApiConfig.class)
@EnableConfigurationProperties(RiotApiProperties.class)
@TestPropertySource(properties = "riot.static-data.apiKey=API_KEY")
@ActiveProfiles({ TESTING, STATIC_DATA })
public class VersionsStaticDataServiceTest {

	@Autowired
	private VersionsService versionsService;

	@Autowired
	private MockRestServiceServer mockServer;

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	@Autowired
	private ObjectMapper objectMapper;

	private List<Version> versions;

	@Before
	public void before() {
		ClassPathResource versionsJson = new ClassPathResource("versions_static_data.json");
		try {
			versions = objectMapper.readValue(versionsJson.getFile(), new TypeReference<List<Version>>() {});
		} catch (IOException e) {
			fail("Unable to unmarshal the Versions response.", e);
		}
	}

	@Test
	public void getVersions() throws Exception {
		mockServer.expect(requestTo(versionsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(versions), MediaType.APPLICATION_JSON_UTF8));

		List<Version> versions = versionsService.getVersions();
		mockServer.verify();

		assertThat(versions).isNotEmpty();
	}

	@Test
	public void getVersionsWithRestClientException() {
		mockServer.expect(requestTo(versionsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		List<Version> versions = versionsService.getVersions();
		mockServer.verify();

		assertThat(versions).isEmpty();
	}

	@Test
	public void getLatestVersion() throws Exception {
		mockServer.expect(requestTo(versionsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(versions), MediaType.APPLICATION_JSON_UTF8));

		Version version = versionsService.getLatestVersion();
		mockServer.verify();

		assertThat(version).isNotNull();
		assertThat(version).isEqualTo(versions.get(0));
	}

	@Test
	public void getLatestVersionWithRestClientException() {
		mockServer.expect(requestTo(versionsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		Version version = versionsService.getLatestVersion();
		mockServer.verify();

		assertThat(version).isNull();
	}

}