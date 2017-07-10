package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.riot.service.ItemsService;
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

public class ItemsRetrievalTest extends BaseSpringTestRunner {

	@MockBean
	private ItemsService itemsService;

	@MockBean
	private VersionsService versionsService;

	private Item lichBane;

	@Before
	public void before() {
		super.before();

		lichBane = itemsResponse.getItems().get("3100");
	}

	@Test
	public void items() throws Exception {
		given(itemsService.getItems()).willReturn(new ArrayList<>(itemsResponse.getItems().values()));

		mockMvc.perform(get("/riot/items").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(itemsResponse.getItems().values())));
	}

	@Test
	public void saveItems() throws Exception {
		given(itemsService.getItems()).willReturn(new ArrayList<>(itemsResponse.getItems().values()));

		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

		mockMvc.perform(post("/riot/items").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(itemsResponse.getItems().values())));

		verify(imageFetcher, times(1))
				.setImgsSrcs(anyListOf(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(itemsRepository.findAll())
				.containsOnlyElementsOf(itemsResponse.getItems().values());
	}

	@Test
	public void saveDifferenceOfItems() throws Exception {
		given(itemsService.getItems()).willReturn(new ArrayList<>(itemsResponse.getItems().values()));

		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

		List<Item> champions = itemsRepository.save(itemsResponse.getItems().values());

		mockMvc.perform(post("/riot/items").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));

		verify(imageFetcher, times(1))
				.setImgsSrcs(anyListOf(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(itemsRepository.findAll()).containsOnlyElementsOf(champions);
	}

	@Test
	public void saveDifferenceOfItemsWithDeleted() throws Exception {
		List<Item> items = itemsRepository.save(itemsResponse.getItems().values());
		Item itemToDelete = RandomizeUtil.getRandom(items);
		itemsResponse.getItems().remove(String.valueOf(itemToDelete.getId()));

		given(itemsService.getItems()).willReturn(new ArrayList<>(itemsResponse.getItems().values()));

		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

		mockMvc.perform(post("/riot/items").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));

		verify(imageFetcher, times(1))
				.setImgsSrcs(anyListOf(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(itemsRepository.findOne(itemToDelete.getId())).isNull();
	}

	@Test
	public void saveItemsWithTruncate() throws Exception {
		given(itemsService.getItems()).willReturn(new ArrayList<>(itemsResponse.getItems().values()));

		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

		mockMvc.perform(post("/riot/items?truncate=true").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(itemsResponse.getItems().values())));

		verify(imageFetcher, times(1))
				.setImgsSrcs(anyListOf(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(itemsRepository.findAll())
				.containsOnlyElementsOf(itemsResponse.getItems().values());
	}

	@Test
	public void item() throws Exception {
		given(itemsService.getItem(eq(lichBane.getId()))).willReturn(lichBane);

		mockMvc.perform(get("/riot/items/{id}", lichBane.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(lichBane)));
	}

	@Test
	public void itemNotFound() throws Exception {
		given(itemsService.getItem(eq(lichBane.getId()))).willReturn(null);

		mockMvc.perform(get("/riot/items/{id}", lichBane.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isNotFound());
	}

	@Test
	public void saveItem() throws Exception {
		given(itemsService.getItem(eq(lichBane.getId()))).willReturn(lichBane);

		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

		mockMvc.perform(post("/riot/items/{id}", lichBane.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(lichBane)));

		verify(imageFetcher, times(1))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(itemsRepository.findOne(lichBane.getId())).isNotNull();
	}

	@Test
	public void saveItemNotFound() throws Exception {
		given(itemsService.getItem(eq(lichBane.getId()))).willReturn(null);

		mockMvc.perform(post("/riot/items/{id}", lichBane.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isNotFound());
	}

	@Test
	public void saveItemWithOverwrite() throws Exception {
		itemsRepository.save(lichBane);
		Item newLichBane = itemsResponse.getItems().get("3100");
		newLichBane.setName("New Lich Bane");

		given(itemsService.getItem(eq(lichBane.getId()))).willReturn(newLichBane);

		given(versionsService.getLatestVersion()).willReturn(versions.get(0));

		mockMvc.perform(post("/riot/items/{id}", lichBane.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(newLichBane)));

		verify(imageFetcher, times(1))
				.setImgSrc(any(Image.class), any(UriComponentsBuilder.class), eq(versions.get(0)));

		assertThat(itemsRepository.findOne(newLichBane.getId())).isNotNull()
				.isEqualTo(newLichBane);
	}

}
