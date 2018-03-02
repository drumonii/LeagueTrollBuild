package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.annotation.WithMockAdminUser;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.riot.api.ImageFetcher;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.riot.service.ItemsService;
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
import java.util.List;

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
public abstract class ItemsRetrievalTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	private ItemsRepository itemsRepository;

	@MockBean
	private ImageFetcher imageFetcher;

	@MockBean
	private ItemsService itemsService;

	@MockBean
	private VersionsService versionsService;

	protected ItemsResponse itemsResponse;

	protected Item lichBane;

	protected Version latestVersion;

	public abstract void before();

	@WithMockAdminUser
	@Test
	public void items() throws Exception {
		given(itemsService.getItems()).willReturn(new ArrayList<>(itemsResponse.getItems().values()));

		mockMvc.perform(get("/riot/items").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(itemsResponse.getItems().values())));
	}

	@WithMockAdminUser
	@Test
	public void saveItems() throws Exception {
		given(itemsService.getItems()).willReturn(new ArrayList<>(itemsResponse.getItems().values()));

		given(versionsService.getLatestVersion()).willReturn(latestVersion);

		mockMvc.perform(post("/riot/items").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(itemsResponse.getItems().values())));

		verify(imageFetcher, times(1))
				.setImgsSrcs(anyList(), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(itemsRepository.findAll())
				.containsOnlyElementsOf(itemsResponse.getItems().values());
	}

	@WithMockAdminUser
	@Test
	public void saveDifferenceOfItems() throws Exception {
		given(itemsService.getItems()).willReturn(new ArrayList<>(itemsResponse.getItems().values()));

		given(versionsService.getLatestVersion()).willReturn(latestVersion);

		List<Item> champions = itemsRepository.saveAll(itemsResponse.getItems().values());

		mockMvc.perform(post("/riot/items").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));

		verify(imageFetcher, times(1))
				.setImgsSrcs(anyList(), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(itemsRepository.findAll()).containsOnlyElementsOf(champions);
	}

	@WithMockAdminUser
	@Test
	public void saveDifferenceOfItemsWithDeleted() throws Exception {
		List<Item> items = itemsRepository.saveAll(itemsResponse.getItems().values());
		Item itemToDelete = RandomizeUtil.getRandom(items);
		itemsResponse.getItems().remove(String.valueOf(itemToDelete.getId()));

		given(itemsService.getItems()).willReturn(new ArrayList<>(itemsResponse.getItems().values()));

		given(versionsService.getLatestVersion()).willReturn(latestVersion);

		mockMvc.perform(post("/riot/items").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));

		verify(imageFetcher, times(1))
				.setImgsSrcs(anyList(), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(itemsRepository.findById(itemToDelete.getId())).isNotPresent();
	}

	@WithMockAdminUser
	@Test
	public void saveItemsWithTruncate() throws Exception {
		given(itemsService.getItems()).willReturn(new ArrayList<>(itemsResponse.getItems().values()));

		given(versionsService.getLatestVersion()).willReturn(latestVersion);

		mockMvc.perform(post("/riot/items?truncate=true").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(itemsResponse.getItems().values())));

		verify(imageFetcher, times(1))
				.setImgsSrcs(anyList(), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(itemsRepository.findAll())
				.containsOnlyElementsOf(itemsResponse.getItems().values());
	}

	@WithMockAdminUser
	@Test
	public void item() throws Exception {
		given(itemsService.getItem(eq(lichBane.getId()))).willReturn(lichBane);

		mockMvc.perform(get("/riot/items/{id}", lichBane.getId()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(lichBane)));
	}

	@WithMockAdminUser
	@Test
	public void itemNotFound() throws Exception {
		given(itemsService.getItem(eq(lichBane.getId()))).willReturn(null);

		mockMvc.perform(get("/riot/items/{id}", lichBane.getId()).with(csrf()))
				.andExpect(status().isNotFound());
	}

	@WithMockAdminUser
	@Test
	public void saveItem() throws Exception {
		given(itemsService.getItem(eq(lichBane.getId()))).willReturn(lichBane);

		given(versionsService.getLatestVersion()).willReturn(latestVersion);

		mockMvc.perform(post("/riot/items/{id}", lichBane.getId()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(lichBane)));

		verify(imageFetcher, times(1))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(itemsRepository.findById(lichBane.getId())).isPresent();
	}

	@WithMockAdminUser
	@Test
	public void saveItemNotFound() throws Exception {
		given(itemsService.getItem(eq(lichBane.getId()))).willReturn(null);

		mockMvc.perform(post("/riot/items/{id}", lichBane.getId()).with(csrf()))
				.andExpect(status().isNotFound());
	}

	@WithMockAdminUser
	@Test
	public void saveItemWithOverwrite() throws Exception {
		Item newLichBane = itemsResponse.getItems().get("3100");
		newLichBane.setName("New Lich Bane");

		given(itemsService.getItem(eq(lichBane.getId()))).willReturn(newLichBane);

		given(versionsService.getLatestVersion()).willReturn(latestVersion);

		mockMvc.perform(post("/riot/items/{id}", lichBane.getId()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(newLichBane)));

		verify(imageFetcher, times(1))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(latestVersion));

		assertThat(itemsRepository.findById(newLichBane.getId())).isPresent()
				.get().isEqualTo(newLichBane);
	}

}
