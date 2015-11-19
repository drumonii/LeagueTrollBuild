package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VersionsRetrievalTest extends BaseSpringTestRunner {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	@Autowired
	private VersionsRepository versionsRepository;

	private MockRestServiceServer mockServer;
	private String[] versions;

	@Before
	public void before() {
		super.before();
		// Only first request is handled. See: http://stackoverflow.com/q/30713734
		mockServer = MockRestServiceServer.createServer(restTemplate);
		String responseBody = "[\"5.16.1\",\"5.15.1\",\"5.14.1\",\"5.13.1\",\"5.12.1\",\"5.11.1\",\"5.10.1\"," +
				"\"5.9.1\",\"5.8.1\",\"5.7.2\",\"5.7.1\",\"5.6.2\",\"5.6.1\",\"5.5.3\",\"5.5.2\",\"5.5.1\"," +
				"\"5.4.1\",\"5.3.1\",\"5.2.2\",\"5.2.1\",\"5.1.2\",\"5.1.1\"]";
		mockServer.expect(requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));
		try {
			versions = objectMapper.readValue(responseBody, String[].class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Versions response.", e);
		}
		restTemplate = mock(RestTemplate.class);
		when(restTemplate.getForObject(versionsUri.toString(), String[].class))
				.thenReturn(versions);
	}

	@After
	public void after() {
		versionsRepository.deleteAll();
	}

	@Test
	public void versions() throws Exception {
		mockMvc.perform(get("/riot/versions").with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(versions)));
		mockServer.verify();
	}

	@Test
	public void latestPatch() throws Exception {
		mockMvc.perform(get("/riot/versions/latest").with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(content().contentType(PLAN_TEXT_UTF8))
				.andExpect(content().string(versions[0]));
		mockServer.verify();
	}

	@Test
	public void saveLatestPatch() throws Exception {
		String responseBody = "[\"5.15.1\",\"5.14.1\",\"5.13.1\",\"5.12.1\",\"5.11.1\",\"5.10.1\"," +
				"\"5.9.1\",\"5.8.1\",\"5.7.2\",\"5.7.1\",\"5.6.2\",\"5.6.1\",\"5.5.3\",\"5.5.2\",\"5.5.1\"," +
				"\"5.4.1\",\"5.3.1\",\"5.2.2\",\"5.2.1\",\"5.1.2\",\"5.1.1\"]";
		String[] newVersions = objectMapper.readValue(responseBody, String[].class);
		Version latestVersion = new Version(newVersions[0]);
		versionsRepository.save(latestVersion);

		mockMvc.perform(post("/riot/versions/latest").with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(content().contentType(PLAN_TEXT_UTF8))
				.andExpect(content().string(versions[0]));
		mockServer.verify();

		assertThat(versionsRepository.findOne(latestVersion.getPatch())).isNull();
		assertThat(versionsRepository.latestPatch()).isNotNull();
	}

	@Test
	public void saveLatestPatchNoChange() throws Exception {
		String responseBody = "[\"5.16.1\",\"5.15.1\",\"5.14.1\",\"5.13.1\",\"5.12.1\",\"5.11.1\",\"5.10.1\"," +
				"\"5.9.1\",\"5.8.1\",\"5.7.2\",\"5.7.1\",\"5.6.2\",\"5.6.1\",\"5.5.3\",\"5.5.2\",\"5.5.1\"," +
				"\"5.4.1\",\"5.3.1\",\"5.2.2\",\"5.2.1\",\"5.1.2\",\"5.1.1\"]";
		String[] newVersions = objectMapper.readValue(responseBody, String[].class);
		Version latestVersion = new Version(newVersions[0]);
		versionsRepository.save(latestVersion);

		mockMvc.perform(post("/riot/versions/latest").with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(content().contentType(PLAN_TEXT_UTF8))
				.andExpect(content().string(versions[0]));
		mockServer.verify();

		assertThat(versionsRepository.latestPatch()).isNotNull();
		assertThat(versionsRepository.latestPatch()).isEqualTo("5.16.1");
	}

}
