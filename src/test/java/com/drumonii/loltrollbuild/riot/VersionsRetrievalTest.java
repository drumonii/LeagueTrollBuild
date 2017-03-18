package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Version;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VersionsRetrievalTest extends BaseSpringTestRunner {

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	@Before
	public void before() {
		super.before();

		given(restTemplate.exchange(eq(versionsUri.toString()), eq(HttpMethod.GET), isNull(HttpEntity.class),
				eq(new ParameterizedTypeReference<List<Version>>() {}))).willReturn(ResponseEntity.ok(versions));

		given(restTemplate.getForObject(eq(versionsUri.toString()), eq(Version[].class)))
				.willReturn(versions.toArray(new Version[versions.size()]));
	}

	@Test
	public void versions() throws Exception {
		mockMvc.perform(get("/riot/versions").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(versions)));
	}

	@Test
	public void latestPatch() throws Exception {
		mockMvc.perform(get("/riot/versions/latest").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(versions.get(0))));

		given(restTemplate.exchange(eq(versionsUri.toString()), eq(HttpMethod.GET), isNull(HttpEntity.class),
				eq(new ParameterizedTypeReference<List<Version>>() {}))).willReturn(ResponseEntity.ok(new ArrayList<>()));

		mockMvc.perform(get("/riot/versions/latest").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(new Version("0", 0, 0, 0))));
	}

	@Test
	public void saveLatestPatch() throws Exception {
		mockMvc.perform(post("/riot/versions/latest").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(versions.get(0))));

		assertThat(versionsRepository.latestVersion()).isEqualTo(versions.get(0));
	}

	@Test
	public void saveLatestPatchNoChange() throws Exception {
		versionsRepository.save(versions.get(0));

		mockMvc.perform(post("/riot/versions/latest").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(versions.get(0))));

		assertThat(versionsRepository.latestVersion()).isNotNull()
				.isEqualTo(versions.get(0));
	}

}
