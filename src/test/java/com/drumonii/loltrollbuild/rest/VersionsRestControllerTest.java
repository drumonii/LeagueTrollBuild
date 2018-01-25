package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.drumonii.loltrollbuild.rest.VersionsRestController.PAGE_SIZE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureCache
@Transactional
public abstract class VersionsRestControllerTest {

	private static final MediaType HAL_JSON_UTF8 = new MediaType("application", "hal+json", UTF_8);

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	protected VersionsRepository versionsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	@Value("${spring.data.rest.base-path}")
	private String apiPath;

	protected List<Version> versions;

	public abstract void before();

	@Test
	public void getVersions() throws Exception {
		// qbe
		mockMvc.perform(get("{apiPath}/versions", apiPath))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.versions").exists())
				.andExpect(jsonPath("$._embedded.versions[0].major", is(versions.get(0).getMajor())))
				.andExpect(jsonPath("$._embedded.versions[0].minor", is(versions.get(0).getMinor())))
				.andExpect(jsonPath("$._embedded.versions[0].revision", is(versions.get(0).getRevision())))
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(PAGE_SIZE)))
				.andExpect(jsonPath("$.page.totalElements").exists())
				.andExpect(jsonPath("$.page.totalPages").exists())
				.andExpect(jsonPath("$.page.number").exists());

		// qbe with no results
		mockMvc.perform(get("{apiPath}/versions", apiPath)
				.param("patch", "1234"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded").doesNotExist())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page.size", is(PAGE_SIZE)))
				.andExpect(jsonPath("$.page.totalElements").exists())
				.andExpect(jsonPath("$.page.totalPages").exists())
				.andExpect(jsonPath("$.page.number").exists());
	}

}