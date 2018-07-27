package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.riot.api.ImageFetcher;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.drumonii.loltrollbuild.riot.service.MapsService;
import com.drumonii.loltrollbuild.riot.service.VersionsService;
import com.drumonii.loltrollbuild.test.riot.RetrievalTest;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.drumonii.loltrollbuild.util.GameMapUtil.SUMMONERS_RIFT_SID;
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
@RetrievalTest(MapsRetrieval.class)
public abstract class MapsRetrievalTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	private MapsRepository mapsRepository;

	@MockBean
	private ImageFetcher imageFetcher;

	@MockBean
	private MapsService mapsService;

	@MockBean
	private VersionsService versionsService;

	protected MapsResponse mapsResponse;

	protected GameMap summonersRift;

	protected Version latestVersion;

	public abstract void before();

	@WithMockAdminUser
	@Test
	public void maps() throws Exception {
		given(mapsService.getMaps()).willReturn(new ArrayList<>(mapsResponse.getMaps().values()));

		mockMvc.perform(get("/riot/maps").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(mapsResponse.getMaps().values())));
	}

	@WithMockAdminUser
	@Test
	public void saveMaps() throws Exception {
		given(mapsService.getMaps()).willReturn(new ArrayList<>(mapsResponse.getMaps().values()));

		given(versionsService.getLatestVersion()).willReturn(latestVersion);

		mockMvc.perform(post("/riot/maps").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(mapsResponse.getMaps().values())));

		verify(imageFetcher, times(1))
				.setImgsSrcs(anyList(), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(mapsRepository.findAll())
				.containsOnlyElementsOf(mapsResponse.getMaps().values());
	}

	@WithMockAdminUser
	@Test
	public void saveDifferenceOfMaps() throws Exception {
		given(mapsService.getMaps()).willReturn(new ArrayList<>(mapsResponse.getMaps().values()));

		given(versionsService.getLatestVersion()).willReturn(latestVersion);

		List<GameMap> maps = mapsRepository.saveAll(mapsResponse.getMaps().values());

		mockMvc.perform(post("/riot/maps").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));

		verify(imageFetcher, times(1))
				.setImgsSrcs(anyList(), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(mapsRepository.findAll()).containsOnlyElementsOf(maps);
	}

	@WithMockAdminUser
	@Test
	public void saveDifferenceOfMapsWithDelete() throws Exception {
		List<GameMap> items = mapsRepository.saveAll(mapsResponse.getMaps().values());
		GameMap mapToDelete = RandomizeUtil.getRandom(items);
		mapsResponse.getMaps().remove(String.valueOf(mapToDelete.getMapId()));

		given(mapsService.getMaps()).willReturn(new ArrayList<>(mapsResponse.getMaps().values()));

		given(versionsService.getLatestVersion()).willReturn(latestVersion);

		mockMvc.perform(post("/riot/maps").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));

		verify(imageFetcher, times(1))
				.setImgsSrcs(anyList(), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(mapsRepository.findById(mapToDelete.getMapId())).isNotPresent();
	}

	@WithMockAdminUser
	@Test
	public void saveMapsWithTruncate() throws Exception {
		given(mapsService.getMaps()).willReturn(new ArrayList<>(mapsResponse.getMaps().values()));

		given(versionsService.getLatestVersion()).willReturn(latestVersion);

		mockMvc.perform(post("/riot/maps").with(csrf())
				.param("truncate", "true"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(mapsResponse.getMaps().values())));

		verify(imageFetcher, times(1))
				.setImgsSrcs(anyList(), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(mapsRepository.findAll())
				.containsOnlyElementsOf(mapsResponse.getMaps().values());
	}

	@WithMockAdminUser
	@Test
	public void map() throws Exception {
		given(mapsService.getMap(eq(summonersRift.getMapId()))).willReturn(summonersRift);

		mockMvc.perform(get("/riot/maps/{id}", summonersRift.getMapId()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(summonersRift)));
	}

	@WithMockAdminUser
	@Test
	public void mapNotFound() throws Exception {
		given(mapsService.getMap(eq(summonersRift.getMapId()))).willReturn(null);

		mockMvc.perform(get("/riot/maps/{id}", summonersRift.getMapId()).with(csrf()))
				.andExpect(status().isNotFound());
	}

	@WithMockAdminUser
	@Test
	public void saveMap() throws Exception {
		given(mapsService.getMap(eq(summonersRift.getMapId()))).willReturn(summonersRift);

		given(versionsService.getLatestVersion()).willReturn(latestVersion);

		mockMvc.perform(post("/riot/maps/{id}", summonersRift.getMapId()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(summonersRift)));

		verify(imageFetcher, times(1))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(mapsRepository.findById(summonersRift.getMapId())).isPresent();
	}

	@WithMockAdminUser
	@Test
	public void saveMapNotFound() throws Exception {
		given(mapsService.getMap(eq(summonersRift.getMapId()))).willReturn(null);

		mockMvc.perform(post("/riot/maps/{id}", summonersRift.getMapId()).with(csrf()))
				.andExpect(status().isNotFound());
	}

	@WithMockAdminUser
	@Test
	public void saveMapWithOverwrite() throws Exception {
		mapsRepository.save(summonersRift);
		GameMap newSummonersRift = mapsResponse.getMaps().get(SUMMONERS_RIFT_SID);
		newSummonersRift.setMapName("New Summoners Rift");
		mapsResponse.getMaps().put(String.valueOf(newSummonersRift.getMapId()), newSummonersRift);

		given(mapsService.getMap(eq(summonersRift.getMapId()))).willReturn(newSummonersRift);

		given(versionsService.getLatestVersion()).willReturn(latestVersion);

		mockMvc.perform(post("/riot/maps/{id}", summonersRift.getMapId()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(newSummonersRift)));

		verify(imageFetcher, times(1))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(mapsRepository.findById(newSummonersRift.getMapId())).isPresent()
				.get().isEqualTo(newSummonersRift);
	}

}