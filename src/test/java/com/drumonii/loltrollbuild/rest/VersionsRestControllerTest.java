package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VersionsRestControllerTest extends BaseSpringTestRunner {

	@Value("${spring.data.rest.base-path}")
	private String apiPath;

	@Before
	public void before() {
		super.before();

		versionsRepository.save(versions);
	}

	@Test
	public void getVersions() throws Exception {
		// qbe
		mockMvc.perform(get(apiPath + "/versions"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.versions", hasSize(20)))
				.andExpect(jsonPath("$._embedded.versions[0].major", is(versions.get(0).getMajor())))
				.andExpect(jsonPath("$._embedded.versions[0].minor", is(versions.get(0).getMinor())))
				.andExpect(jsonPath("$._embedded.versions[0].revision", is(versions.get(0).getRevision())))
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(20)))
				.andExpect(jsonPath("$.page.totalElements", is(versions.size())))
				.andExpect(jsonPath("$.page.totalPages", is((int) Math.ceil((double) versions.size() / (double) 20))))
				.andExpect(jsonPath("$.page.number", is(0)));

		// qbe with no results
		mockMvc.perform(get(apiPath + "/versions")
				.param("patch", "1234"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded").doesNotExist())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page.size", is(20)))
				.andExpect(jsonPath("$.page.totalElements", is(0)))
				.andExpect(jsonPath("$.page.totalPages", is(0)))
				.andExpect(jsonPath("$.page.number", is(0)));
	}

}