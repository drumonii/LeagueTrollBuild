package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.service.VersionsService;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VersionsRetrievalTest extends BaseSpringTestRunner {

	@MockBean
	private VersionsService versionsService;

	@Test
	public void versions() throws Exception {
		given(versionsService.getVersions()).willReturn(versions);

		mockMvc.perform(get("/riot/versions").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(versions)));
	}

	@Test
	public void latestPatch() throws Exception {
		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

		mockMvc.perform(get("/riot/versions/latest").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(versions.get(0))));
	}

	@Test
	public void saveLatestPatch() throws Exception {
		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

		mockMvc.perform(post("/riot/versions/latest").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(versions.get(0))));

		assertThat(versionsRepository.latestVersion()).isEqualTo(versions.get(0));
	}

	@Test
	public void saveLatestPatchWithNoPatches() throws Exception {
		given(versionsService.getLatestVersion()).willReturn(null);

		mockMvc.perform(post("/riot/versions/latest").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(new Version("0", 0, 0, 0))));
	}

	@Test
	public void saveLatestPatchWithPreviousPatch() throws Exception {
		given(versionsService.getLatestVersion()).willReturn(versions.get(0));
		versionsRepository.save(versions.get(1));

		mockMvc.perform(post("/riot/versions/latest").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(versions.get(0))));

		assertThat(versionsRepository.latestVersion()).isEqualTo(versions.get(0));
	}

	@Test
	public void saveLatestPatchNoChange() throws Exception {
		given(versionsService.getLatestVersion()).willReturn(versions.get(0));
		versionsRepository.save(versions.get(0));

		mockMvc.perform(post("/riot/versions/latest").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(versions.get(0))));

		assertThat(versionsRepository.latestVersion()).isNotNull()
				.isEqualTo(versions.get(0));
	}

}
