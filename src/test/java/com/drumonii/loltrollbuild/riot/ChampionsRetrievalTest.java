package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.riot.service.ChampionsService;
import com.drumonii.loltrollbuild.riot.service.VersionsService;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChampionsRetrievalTest extends BaseSpringTestRunner {

	@MockBean
	private ChampionsService championsService;

	@MockBean
	private VersionsService versionsService;

	private Champion leeSin;

	@Before
	public void before() {
		super.before();

		leeSin = championsResponse.getChampions().get("LeeSin");
	}

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

		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

		mockMvc.perform(post("/riot/champions").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(championsResponse.getChampions().values())));

		verify(imageFetcher, times(3))
				.setImgsSrcs(anyListOf(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(championsRepository.findAll()).containsOnlyElementsOf(championsResponse.getChampions().values());
	}

	@WithMockAdminUser
	@Test
	public void saveDifferenceOfChampions() throws Exception {
		given(championsService.getChampions()).willReturn(new ArrayList<>(championsResponse.getChampions().values()));

		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

		List<Champion> champions = championsRepository.save(championsResponse.getChampions().values());

		mockMvc.perform(post("/riot/champions").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));

		verify(imageFetcher, times(3))
				.setImgsSrcs(anyListOf(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(championsRepository.findAll()).containsOnlyElementsOf(champions);
	}

	@WithMockAdminUser
	@Test
	public void saveDifferenceOfChampionsWithDeleted() throws Exception {
		List<Champion> champions = championsRepository.save(championsResponse.getChampions().values());
		Champion championToDelete = RandomizeUtil.getRandom(champions);
		championsResponse.getChampions().remove(championToDelete.getKey());

		given(championsService.getChampions()).willReturn(new ArrayList<>(championsResponse.getChampions().values()));

		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

		mockMvc.perform(post("/riot/champions").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));

		verify(imageFetcher, times(3))
				.setImgsSrcs(anyListOf(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(championsRepository.findOne(championToDelete.getId())).isNull();
	}

	@WithMockAdminUser
	@Test
	public void saveChampionsWithTruncate() throws Exception {
		given(championsService.getChampions()).willReturn(new ArrayList<>(championsResponse.getChampions().values()));

		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

		mockMvc.perform(post("/riot/champions?truncate=true").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(championsResponse.getChampions().values())));

		verify(imageFetcher, times(3))
				.setImgsSrcs(anyListOf(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

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

		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

		mockMvc.perform(post("/riot/champions/{id}", leeSin.getId()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(leeSin)));

		verify(imageFetcher, times(2))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));
		verify(imageFetcher, times(1))
				.setImgsSrcs(anyListOf(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(championsRepository.findOne(leeSin.getId())).isNotNull();
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

		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

		mockMvc.perform(post("/riot/champions/{id}", leeSin.getId()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(newLeeSin)));

		verify(imageFetcher, times(2))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));
		verify(imageFetcher, times(1))
				.setImgsSrcs(anyListOf(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(championsRepository.findOne(newLeeSin.getId())).isNotNull()
				.isEqualTo(newLeeSin);
	}

}