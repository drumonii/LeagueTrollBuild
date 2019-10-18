package com.drumonii.loltrollbuild.api;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import com.drumonii.loltrollbuild.test.api.WebMvcRestTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcRestTest(VersionsRestController.class)
abstract class VersionsRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	protected VersionsRepository versionsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	@Value("${api.base-path}")
	private String apiPath;

	protected List<Version> versions;

	protected abstract void beforeEach();

	@Test
	void getVersions() throws Exception {
		// qbe
		mockMvc.perform(get("{apiPath}/versions", apiPath))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[0].major", is(versions.get(0).getMajor())))
				.andExpect(jsonPath("$.[0].minor", is(versions.get(0).getMinor())))
				.andExpect(jsonPath("$.[0].revision", is(versions.get(0).getRevision())));

		// qbe with no results
		mockMvc.perform(get("{apiPath}/versions", apiPath)
				.param("patch", "1234"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(content().json("[]"));
	}

	@Test
	void getVersion() throws Exception {
		// find with non existing version
		mockMvc.perform(get("{apiPath}/versions/{patch}", apiPath, "1234"))
				.andExpect(status().isNotFound());

		// find with existing version
		mockMvc.perform(get("{apiPath}/versions/{patch}", apiPath, versions.get(0).getPatch()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());
	}

	@Test
	void getLatestVersion() throws Exception {
		// get latest version with saved versions
		mockMvc.perform(get("{apiPath}/versions/latest", apiPath))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.major", is(versions.get(0).getMajor())))
				.andExpect(jsonPath("$.minor", is(versions.get(0).getMinor())))
				.andExpect(jsonPath("$.revision", is(versions.get(0).getRevision())));

		versionsRepository.deleteAll();

		// get latest version with no saved versions
		mockMvc.perform(get("{apiPath}/versions/latest", apiPath))
				.andExpect(status().isNotFound());
	}

}
