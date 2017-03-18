package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChampionsRetrievalTest extends BaseSpringTestRunner {

	@Autowired
	@Qualifier("champions")
	private UriComponents championsUri;

	@Autowired
	@Qualifier("champion")
	private UriComponentsBuilder championUriBuilder;

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	private UriComponents championUri;
	private Champion leeSin;

	@Before
	public void before() {
		super.before();

		leeSin = championsResponse.getChampions().get("LeeSin");
		championUri = championUriBuilder.buildAndExpand("na", leeSin.getId());
	}

	@Test
	public void champions() throws Exception {
		given(restTemplate.getForObject(eq(championsUri.toString()), eq(ChampionsResponse.class)))
				.willReturn(championsResponse);

		mockMvc.perform(get("/riot/champions").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(championsResponse.getChampions().values())));
	}

	@Test
	public void saveChampions() throws Exception {
		given(restTemplate.getForObject(eq(championsUri.toString()), eq(ChampionsResponse.class)))
				.willReturn(championsResponse);

		given(restTemplate.exchange(eq(versionsUri.toString()), eq(HttpMethod.GET), isNull(HttpEntity.class),
				eq(new ParameterizedTypeReference<List<Version>>() {}))).willReturn(ResponseEntity.ok(versions));

		mockMvc.perform(post("/riot/champions").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(championsResponse.getChampions().values())));

		verify(imageFetcher, times(3))
				.setImgsSrcs(anyListOf(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(championsRepository.findAll()).containsOnlyElementsOf(championsResponse.getChampions().values());
	}

	@Test
	public void saveDifferenceOfChampions() throws Exception {
		given(restTemplate.getForObject(eq(championsUri.toString()), eq(ChampionsResponse.class)))
				.willReturn(championsResponse);

		given(restTemplate.exchange(eq(versionsUri.toString()), eq(HttpMethod.GET), isNull(HttpEntity.class),
				eq(new ParameterizedTypeReference<List<Version>>() {}))).willReturn(ResponseEntity.ok(versions));

		List<Champion> champions = championsRepository.save(championsResponse.getChampions().values());

		mockMvc.perform(post("/riot/champions").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));

		verify(imageFetcher, times(3))
				.setImgsSrcs(anyListOf(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(championsRepository.findAll()).containsOnlyElementsOf(champions);
	}

	@Test
	public void saveDifferenceOfChampionsWithDeleted() throws Exception {
		List<Champion> champions = championsRepository.save(championsResponse.getChampions().values());
		Champion championToDelete = RandomizeUtil.getRandom(champions);
		championsResponse.getChampions().remove(championToDelete.getKey());

		given(restTemplate.getForObject(eq(championsUri.toString()), eq(ChampionsResponse.class)))
				.willReturn(championsResponse);

		given(restTemplate.exchange(eq(versionsUri.toString()), eq(HttpMethod.GET), isNull(HttpEntity.class),
				eq(new ParameterizedTypeReference<List<Version>>() {}))).willReturn(ResponseEntity.ok(versions));

		mockMvc.perform(post("/riot/champions").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));

		verify(imageFetcher, times(3))
				.setImgsSrcs(anyListOf(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(championsRepository.findOne(championToDelete.getId())).isNull();
	}

	@Test
	public void saveChampionsWithTruncate() throws Exception {
		given(restTemplate.getForObject(eq(championsUri.toString()), eq(ChampionsResponse.class)))
				.willReturn(championsResponse);

		given(restTemplate.exchange(eq(versionsUri.toString()), eq(HttpMethod.GET), isNull(HttpEntity.class),
				eq(new ParameterizedTypeReference<List<Version>>() {}))).willReturn(ResponseEntity.ok(versions));

		mockMvc.perform(post("/riot/champions?truncate=true").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(championsResponse.getChampions().values())));

		verify(imageFetcher, times(3))
				.setImgsSrcs(anyListOf(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(championsRepository.findAll()).containsOnlyElementsOf(championsResponse.getChampions().values());
	}

	@Test
	public void champion() throws Exception {
		given(restTemplate.getForObject(eq(championUri.toString()), eq(Champion.class)))
				.willReturn(leeSin);

		mockMvc.perform(get("/riot/champions/{id}", leeSin.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(leeSin)));
	}

	@Test
	public void championNotFound() throws Exception {
		given(restTemplate.getForObject(eq(championUri.toString()), eq(Champion.class)))
				.willThrow(new RestClientException("404 Not Found"));

		mockMvc.perform(get("/riot/champions/{id}", leeSin.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isNotFound());
	}

	@Test
	public void saveChampion() throws Exception {
		given(restTemplate.getForObject(eq(championUri.toString()), eq(Champion.class)))
				.willReturn(leeSin);

		given(restTemplate.exchange(eq(versionsUri.toString()), eq(HttpMethod.GET), isNull(HttpEntity.class),
				eq(new ParameterizedTypeReference<List<Version>>() {}))).willReturn(ResponseEntity.ok(versions));

		mockMvc.perform(post("/riot/champions/{id}", leeSin.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(leeSin)));

		verify(imageFetcher, times(2))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));
		verify(imageFetcher, times(1))
				.setImgsSrcs(anyListOf(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(championsRepository.findOne(leeSin.getId())).isNotNull();
	}

	@Test
	public void saveChampionNotFound() throws Exception {
		given(restTemplate.getForObject(eq(championUri.toString()), eq(Champion.class)))
				.willThrow(new RestClientException("404 Not Found"));

		mockMvc.perform(post("/riot/champions/{id}", leeSin.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isNotFound());
	}

	@Test
	public void saveChampionWithOverwrite() throws Exception {
		Champion newLeeSin = championsResponse.getChampions().get("LeeSin");
		newLeeSin.setTags(new TreeSet<>(Arrays.asList("NEW_TAG")));
		newLeeSin = championsRepository.save(newLeeSin);

		given(restTemplate.getForObject(eq(championUri.toString()), eq(Champion.class)))
				.willReturn(newLeeSin);

		given(restTemplate.exchange(eq(versionsUri.toString()), eq(HttpMethod.GET), isNull(HttpEntity.class),
				eq(new ParameterizedTypeReference<List<Version>>() {}))).willReturn(ResponseEntity.ok(versions));

		mockMvc.perform(post("/riot/champions/{id}", leeSin.getId()).with(adminUser()).with(csrf()))
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