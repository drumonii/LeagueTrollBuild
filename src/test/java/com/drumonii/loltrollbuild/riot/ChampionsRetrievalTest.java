package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.riot.api.ImageFetcher;
import com.drumonii.loltrollbuild.riot.service.ChampionsService;
import com.drumonii.loltrollbuild.riot.service.VersionsService;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
public abstract class ChampionsRetrievalTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	private ChampionsRepository championsRepository;

	@MockBean
	private ImageFetcher imageFetcher;

	@MockBean
	private ChampionsService championsService;

	@MockBean
	private VersionsService versionsService;

	protected ChampionsResponse championsResponse;

	protected Champion leeSin;

	protected Version latestVersion;

	public abstract void before();

	@WithMockAdminUser
	@Test
	public void champions() throws Exception {
		given(championsService.getChampions()).willReturn(new ArrayList<>(championsResponse.getChampions().values()));

		mockMvc.perform(get("/riot/champions").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(championsResponse.getChampions().values())));
	}

	@WithMockAdminUser
	@Test
	public void saveChampions() throws Exception {
		given(championsService.getChampions()).willReturn(new ArrayList<>(championsResponse.getChampions().values()));

		given(versionsService.getLatestVersion()).willReturn(latestVersion);

		mockMvc.perform(post("/riot/champions").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(championsResponse.getChampions().values())));

		verify(imageFetcher, times(3))
				.setImgsSrcs(anyList(), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(championsRepository.findAll()).containsOnlyElementsOf(championsResponse.getChampions().values());
	}

	@WithMockAdminUser
	@Test
	public void saveDifferenceOfChampions() throws Exception {
		given(championsService.getChampions()).willReturn(new ArrayList<>(championsResponse.getChampions().values()));

		given(versionsService.getLatestVersion()).willReturn(latestVersion);

		List<Champion> champions = championsRepository.saveAll(championsResponse.getChampions().values());

		mockMvc.perform(post("/riot/champions").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));

		verify(imageFetcher, times(3))
				.setImgsSrcs(anyList(), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(championsRepository.findAll()).containsOnlyElementsOf(champions);
	}

	@WithMockAdminUser
	@Test
	public void saveDifferenceOfChampionsWithDeleted() throws Exception {
		List<Champion> champions = championsRepository.saveAll(championsResponse.getChampions().values());
		Champion championToDelete = RandomizeUtil.getRandom(champions);
		championsResponse.getChampions().remove(championToDelete.getKey());

		given(championsService.getChampions()).willReturn(new ArrayList<>(championsResponse.getChampions().values()));

		given(versionsService.getLatestVersion()).willReturn(latestVersion);

		mockMvc.perform(post("/riot/champions").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));

		verify(imageFetcher, times(3))
				.setImgsSrcs(anyList(), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(championsRepository.findById(championToDelete.getId())).isNotPresent();
	}

	@WithMockAdminUser
	@Test
	public void saveChampionsWithTruncate() throws Exception {
		given(championsService.getChampions()).willReturn(new ArrayList<>(championsResponse.getChampions().values()));

		given(versionsService.getLatestVersion()).willReturn(latestVersion);

		mockMvc.perform(post("/riot/champions?truncate=true").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(championsResponse.getChampions().values())));

		verify(imageFetcher, times(3))
				.setImgsSrcs(anyList(), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(championsRepository.findAll()).containsOnlyElementsOf(championsResponse.getChampions().values());
	}

	@WithMockAdminUser
	@Test
	public void champion() throws Exception {
		given(championsService.getChampion(eq(leeSin.getId()))).willReturn(leeSin);

		mockMvc.perform(get("/riot/champions/{id}", leeSin.getId()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(leeSin)));
	}

	@WithMockAdminUser
	@Test
	public void championNotFound() throws Exception {
		given(championsService.getChampion(eq(leeSin.getId()))).willReturn(null);

		mockMvc.perform(get("/riot/champions/{id}", leeSin.getId()).with(csrf()))
				.andExpect(status().isNotFound());
	}

	@WithMockAdminUser
	@Test
	public void saveChampion() throws Exception {
		given(championsService.getChampion(eq(leeSin.getId()))).willReturn(leeSin);

		given(versionsService.getLatestVersion()).willReturn(latestVersion);

		mockMvc.perform(post("/riot/champions/{id}", leeSin.getId()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(leeSin)));

		verify(imageFetcher, times(2))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));
		verify(imageFetcher, times(1))
				.setImgsSrcs(anyList(), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(championsRepository.findById(leeSin.getId())).isPresent();
	}

	@WithMockAdminUser
	@Test
	public void saveChampionNotFound() throws Exception {
		given(championsService.getChampion(eq(leeSin.getId()))).willReturn(null);

		mockMvc.perform(post("/riot/champions/{id}", leeSin.getId()).with(csrf()))
				.andExpect(status().isNotFound());
	}

	@WithMockAdminUser
	@Test
	public void saveChampionWithOverwrite() throws Exception {
		Champion newLeeSin = championsResponse.getChampions().get("LeeSin");
		newLeeSin.setTags(new TreeSet<>(Collections.singletonList("NEW_TAG")));
		newLeeSin = championsRepository.save(newLeeSin);

		given(championsService.getChampion(eq(leeSin.getId()))).willReturn(newLeeSin);

		given(versionsService.getLatestVersion()).willReturn(latestVersion);

		mockMvc.perform(post("/riot/champions/{id}", leeSin.getId()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(newLeeSin)));

		verify(imageFetcher, times(2))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));
		verify(imageFetcher, times(1))
				.setImgsSrcs(anyList(), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(championsRepository.findById(newLeeSin.getId())).isPresent()
				.get().isEqualTo(newLeeSin);
	}

}