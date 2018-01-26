package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import com.drumonii.loltrollbuild.riot.service.VersionsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureCache
@AutoConfigureTestDatabase
@Transactional
public abstract class VersionsRetrievalTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	private VersionsRepository versionsRepository;

	@MockBean
	private VersionsService versionsService;

	protected List<Version> versions;
	protected Version latestVersion;

	public abstract void before();

	@WithMockAdminUser
	@Test
	public void versions() throws Exception {
		given(versionsService.getVersions()).willReturn(versions);

		mockMvc.perform(get("/riot/versions").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(versions)));
	}

	@WithMockAdminUser
	@Test
	public void latestPatch() throws Exception {
		given(versionsService.getLatestVersion()).willReturn(latestVersion);

		mockMvc.perform(get("/riot/versions/latest").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(latestVersion)));
	}

	@WithMockAdminUser
	@Test
	public void saveLatestPatch() throws Exception {
		given(versionsService.getLatestVersion()).willReturn(latestVersion);

		mockMvc.perform(post("/riot/versions/latest").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(latestVersion)));

		assertThat(versionsRepository.latestVersion()).isEqualTo(latestVersion);
	}

	@WithMockAdminUser
	@Test
	public void saveLatestPatchWithNoPatches() throws Exception {
		given(versionsService.getLatestVersion()).willReturn(null);

		mockMvc.perform(post("/riot/versions/latest").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(new Version("0.0.0"))));
	}

	@WithMockAdminUser
	@Test
	public void saveLatestPatchWithPreviousPatch() throws Exception {
		given(versionsService.getLatestVersion()).willReturn(latestVersion);
		versionsRepository.save(versions.get(1));

		mockMvc.perform(post("/riot/versions/latest").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(latestVersion)));

		assertThat(versionsRepository.latestVersion()).isEqualTo(latestVersion);
	}

	@WithMockAdminUser
	@Test
	public void saveLatestPatchNoChange() throws Exception {
		given(versionsService.getLatestVersion()).willReturn(latestVersion);
		versionsRepository.save(latestVersion);

		mockMvc.perform(post("/riot/versions/latest").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(latestVersion)));

		assertThat(versionsRepository.latestVersion()).isEqualTo(latestVersion);
	}

}
