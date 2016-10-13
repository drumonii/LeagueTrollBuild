package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
	@Qualifier("versions")
	private UriComponents versionsUri;

	@Autowired
	private VersionsRepository versionsRepository;

	private MockRestServiceServer mockServer;

	@Before
	public void before() {
		super.before();

		mockServer = MockRestServiceServer.createServer(restTemplate);

		String versionsResponseBody = null;
		try {
			versionsResponseBody = objectMapper.writeValueAsString(versions);
		} catch (JsonProcessingException e) {
			fail("Unable to marshal the Versions.", e);
		}

		mockServer.expect(requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		restTemplate = mock(RestTemplate.class);
		when(restTemplate.getForObject(versionsUri.toString(), Version[].class))
				.thenReturn(versions.toArray(new Version[versions.size()]));
	}

	@After
	public void after() {
		versionsRepository.deleteAll();
	}

	@Test
	public void versions() throws Exception {
		mockMvc.perform(get("/riot/versions").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(versions)));
		mockServer.verify();
	}

	@Test
	public void latestPatch() throws Exception {
		mockMvc.perform(get("/riot/versions/latest").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(versions.get(0))));
		mockServer.verify();
		mockServer.reset();

		mockServer.expect(requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(new ArrayList<>()), MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(get("/riot/versions/latest").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(new Version("0", 0, 0, 0))));
		mockServer.verify();
	}

	@Test
	public void saveLatestPatch() throws Exception {
		mockMvc.perform(post("/riot/versions/latest").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(versions.get(0))));
		mockServer.verify();

		assertThat(versionsRepository.latestVersion()).isEqualTo(versions.get(0));
	}

	@Test
	public void saveLatestPatchNoChange() throws Exception {
		versionsRepository.save(versions.get(0));

		mockMvc.perform(post("/riot/versions/latest").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(versions.get(0))));
		mockServer.verify();

		assertThat(versionsRepository.latestVersion()).isNotNull()
				.isEqualTo(versions.get(0));
	}

}
