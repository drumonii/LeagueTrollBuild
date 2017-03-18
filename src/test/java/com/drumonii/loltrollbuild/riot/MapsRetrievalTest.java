package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
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
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

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

public class MapsRetrievalTest extends BaseSpringTestRunner {

	@Autowired
	@Qualifier("maps")
	private UriComponents mapsUri;

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	private GameMap summonersRift;

	@Before
	public void before() {
		super.before();

		summonersRift = mapsResponse.getMaps().get(SUMMONERS_RIFT);
	}

	@Test
	public void maps() throws Exception {
		given(restTemplate.getForObject(eq(mapsUri.toString()), eq(MapsResponse.class)))
				.willReturn(mapsResponse);

		mockMvc.perform(get("/riot/maps").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(mapsResponse.getMaps().values())));
	}

	@Test
	public void saveMaps() throws Exception {
		given(restTemplate.getForObject(eq(mapsUri.toString()), eq(MapsResponse.class)))
				.willReturn(mapsResponse);

		given(restTemplate.exchange(eq(versionsUri.toString()), eq(HttpMethod.GET), isNull(HttpEntity.class),
				eq(new ParameterizedTypeReference<List<Version>>() {}))).willReturn(ResponseEntity.ok(versions));

		mockMvc.perform(post("/riot/maps").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(mapsResponse.getMaps().values())));

		verify(imageFetcher, times(1))
				.setImgsSrcs(anyListOf(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(mapsRepository.findAll())
				.containsOnlyElementsOf(mapsResponse.getMaps().values());
	}

	@Test
	public void saveDifferenceOfMaps() throws Exception {
		given(restTemplate.getForObject(eq(mapsUri.toString()), eq(MapsResponse.class)))
				.willReturn(mapsResponse);

		given(restTemplate.exchange(eq(versionsUri.toString()), eq(HttpMethod.GET), isNull(HttpEntity.class),
				eq(new ParameterizedTypeReference<List<Version>>() {}))).willReturn(ResponseEntity.ok(versions));

		List<GameMap> maps = mapsRepository.save(mapsResponse.getMaps().values());

		mockMvc.perform(post("/riot/maps").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));

		verify(imageFetcher, times(1))
				.setImgsSrcs(anyListOf(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(mapsRepository.findAll()).containsOnlyElementsOf(maps);
	}

	@Test
	public void saveDifferenceOfMapsWithDelete() throws Exception {
		List<GameMap> items = mapsRepository.save(mapsResponse.getMaps().values());
		GameMap mapToDelete = RandomizeUtil.getRandom(items);
		mapsResponse.getMaps().remove(String.valueOf(mapToDelete.getMapId()));

		given(restTemplate.getForObject(eq(mapsUri.toString()), eq(MapsResponse.class)))
				.willReturn(mapsResponse);

		given(restTemplate.exchange(eq(versionsUri.toString()), eq(HttpMethod.GET), isNull(HttpEntity.class),
				eq(new ParameterizedTypeReference<List<Version>>() {}))).willReturn(ResponseEntity.ok(versions));

		mockMvc.perform(post("/riot/maps").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));

		verify(imageFetcher, times(1))
				.setImgsSrcs(anyListOf(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(mapsRepository.findOne(mapToDelete.getMapId())).isNull();
	}

	@Test
	public void saveMapsWithTruncate() throws Exception {
		given(restTemplate.getForObject(eq(mapsUri.toString()), eq(MapsResponse.class)))
				.willReturn(mapsResponse);

		given(restTemplate.exchange(eq(versionsUri.toString()), eq(HttpMethod.GET), isNull(HttpEntity.class),
				eq(new ParameterizedTypeReference<List<Version>>() {}))).willReturn(ResponseEntity.ok(versions));

		mockMvc.perform(post("/riot/maps?truncate=true").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(mapsResponse.getMaps().values())));

		verify(imageFetcher, times(1))
				.setImgsSrcs(anyListOf(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(mapsRepository.findAll())
				.containsOnlyElementsOf(mapsResponse.getMaps().values());
	}

	@Test
	public void map() throws Exception {
		given(restTemplate.getForObject(eq(mapsUri.toString()), eq(MapsResponse.class)))
				.willReturn(mapsResponse);

		mockMvc.perform(get("/riot/maps/{id}", summonersRift.getMapId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(summonersRift)));
	}

	@Test
	public void mapNotFound() throws Exception {
		mapsResponse.getMaps().remove(String.valueOf(summonersRift.getMapId()));

		given(restTemplate.getForObject(eq(mapsUri.toString()), eq(MapsResponse.class)))
				.willReturn(mapsResponse);

		mockMvc.perform(get("/riot/maps/{id}", summonersRift.getMapId()).with(adminUser()).with(csrf()))
				.andExpect(status().isNotFound());
	}

	@Test
	public void saveMap() throws Exception {
		given(restTemplate.getForObject(eq(mapsUri.toString()), eq(MapsResponse.class)))
				.willReturn(mapsResponse);

		given(restTemplate.exchange(eq(versionsUri.toString()), eq(HttpMethod.GET), isNull(HttpEntity.class),
				eq(new ParameterizedTypeReference<List<Version>>() {}))).willReturn(ResponseEntity.ok(versions));

		mockMvc.perform(post("/riot/maps/{id}", summonersRift.getMapId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(summonersRift)));

		verify(imageFetcher, times(1))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(mapsRepository.findOne(summonersRift.getMapId())).isNotNull();
	}

	@Test
	public void saveMapNotFound() throws Exception {
		mapsResponse.getMaps().remove(String.valueOf(summonersRift.getMapId()));

		given(restTemplate.getForObject(eq(mapsUri.toString()), eq(MapsResponse.class)))
				.willReturn(mapsResponse);

		mockMvc.perform(post("/riot/maps/{id}", summonersRift.getMapId()).with(adminUser()).with(csrf()))
				.andExpect(status().isNotFound());
	}

	@Test
	public void saveMapWithOverwrite() throws Exception {
		mapsRepository.save(summonersRift);
		GameMap newSummonersRift = mapsResponse.getMaps().get(SUMMONERS_RIFT);
		newSummonersRift.setMapName("New Summoners Rift");
		mapsResponse.getMaps().put(String.valueOf(newSummonersRift.getMapId()), newSummonersRift);

		given(restTemplate.getForObject(eq(mapsUri.toString()), eq(MapsResponse.class)))
				.willReturn(mapsResponse);

		given(restTemplate.exchange(eq(versionsUri.toString()), eq(HttpMethod.GET), isNull(HttpEntity.class),
				eq(new ParameterizedTypeReference<List<Version>>() {}))).willReturn(ResponseEntity.ok(versions));

		mockMvc.perform(post("/riot/maps/{id}", summonersRift.getMapId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(newSummonersRift)));

		verify(imageFetcher, times(1))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(mapsRepository.findOne(newSummonersRift.getMapId())).isNotNull()
				.isEqualTo(newSummonersRift);
	}

}