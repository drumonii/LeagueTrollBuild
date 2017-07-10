package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.riot.service.MapsService;
import com.drumonii.loltrollbuild.riot.service.VersionsService;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

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

public class MapsRetrievalTest extends BaseSpringTestRunner {

	@MockBean
	private MapsService mapsService;

	@MockBean
	private VersionsService versionsService;

	private GameMap summonersRift;

	@Before
	public void before() {
		super.before();

		summonersRift = mapsResponse.getMaps().get(SUMMONERS_RIFT);
	}

	@Test
	public void maps() throws Exception {
		given(mapsService.getMaps()).willReturn(new ArrayList<>(mapsResponse.getMaps().values()));

		mockMvc.perform(get("/riot/maps").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(mapsResponse.getMaps().values())));
	}

	@Test
	public void saveMaps() throws Exception {
		given(mapsService.getMaps()).willReturn(new ArrayList<>(mapsResponse.getMaps().values()));

		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

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
		given(mapsService.getMaps()).willReturn(new ArrayList<>(mapsResponse.getMaps().values()));

		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

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

		given(mapsService.getMaps()).willReturn(new ArrayList<>(mapsResponse.getMaps().values()));

		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

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
		given(mapsService.getMaps()).willReturn(new ArrayList<>(mapsResponse.getMaps().values()));

		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

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
		given(mapsService.getMap(eq(summonersRift.getMapId()))).willReturn(summonersRift);

		mockMvc.perform(get("/riot/maps/{id}", summonersRift.getMapId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(summonersRift)));
	}

	@Test
	public void mapNotFound() throws Exception {
		given(mapsService.getMap(eq(summonersRift.getMapId()))).willReturn(null);

		mockMvc.perform(get("/riot/maps/{id}", summonersRift.getMapId()).with(adminUser()).with(csrf()))
				.andExpect(status().isNotFound());
	}

	@Test
	public void saveMap() throws Exception {
		given(mapsService.getMap(eq(summonersRift.getMapId()))).willReturn(summonersRift);

		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

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
		given(mapsService.getMap(eq(summonersRift.getMapId()))).willReturn(null);

		mockMvc.perform(post("/riot/maps/{id}", summonersRift.getMapId()).with(adminUser()).with(csrf()))
				.andExpect(status().isNotFound());
	}

	@Test
	public void saveMapWithOverwrite() throws Exception {
		mapsRepository.save(summonersRift);
		GameMap newSummonersRift = mapsResponse.getMaps().get(SUMMONERS_RIFT);
		newSummonersRift.setMapName("New Summoners Rift");
		mapsResponse.getMaps().put(String.valueOf(newSummonersRift.getMapId()), newSummonersRift);

		given(mapsService.getMap(eq(summonersRift.getMapId()))).willReturn(newSummonersRift);

		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

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