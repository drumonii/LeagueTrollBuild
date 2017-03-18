package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ItemsRestControllerTest extends BaseSpringTestRunner {

	private ItemsResponse itemsResponseSlice;

	@Before
	public void before() {
		super.before();

		itemsResponseSlice = new ItemsResponse();
		itemsResponseSlice.setType(itemsResponse.getType());
		itemsResponseSlice.setVersion(itemsResponse.getVersion());
	}

	@Test
	public void getItems() throws Exception {
		itemsResponseSlice.setItems(itemsResponse.getItems().values().stream()
				.filter(item -> item.getRequiredChampion() != null)
				.collect(Collectors.toMap(item -> String.valueOf(item.getId()), item -> item)));
		itemsRepository.save(itemsResponseSlice.getItems().values());

		Item item = RandomizeUtil.getRandom(itemsResponseSlice.getItems().values());

		// qbe
		mockMvc.perform(get(apiPath + "/items"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.items",
						hasSize(itemsResponseSlice.getItems().values().size())))
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(20)))
				.andExpect(jsonPath("$.page.totalElements", is(itemsResponseSlice.getItems().values().size())))
				.andExpect(jsonPath("$.page.totalPages", is(
						(int) Math.ceil((double) itemsResponseSlice.getItems().values().size() / (double) 20))))
				.andExpect(jsonPath("$.page.number", is(0)));

		// qbe with name
		mockMvc.perform(get(apiPath + "/items")
				.param("name", item.getName().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.items").isNotEmpty())
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(20)))
				.andExpect(jsonPath("$.page.totalElements").exists())
				.andExpect(jsonPath("$.page.totalPages", is(1)))
				.andExpect(jsonPath("$.page.number", is(0)));

		// qbe with required champion
		mockMvc.perform(get(apiPath + "/items")
				.param("requiredChampion", item.getRequiredChampion().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.items", hasSize(
						(int) itemsResponseSlice.getItems().values().stream()
								.filter(i -> i.getRequiredChampion().equals(item.getRequiredChampion()))
								.count())))
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(20)))
				.andExpect(jsonPath("$.page.totalElements", is(
						(int) itemsResponseSlice.getItems().values().stream()
								.filter(i -> i.getRequiredChampion().equals(item.getRequiredChampion()))
								.count())))
				.andExpect(jsonPath("$.page.totalPages", is(
						(int) Math.ceil((double) itemsResponseSlice.getItems().values().stream()
								.filter(i -> i.getRequiredChampion().equals(item.getRequiredChampion()))
								.count() / (double) 20))))
				.andExpect(jsonPath("$.page.number", is(0)));

		// qbe with no results
		mockMvc.perform(get(apiPath + "/items")
				.param("name", "abcd1234"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded").doesNotExist())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page.size", is(20)))
				.andExpect(jsonPath("$.page.totalElements", is(0)))
				.andExpect(jsonPath("$.page.totalPages", is(0)))
				.andExpect(jsonPath("$.page.number", is(0)));
	}

	@Test
	public void getBoots() throws Exception {
		itemsResponseSlice.setItems(itemsResponse.getItems().values().stream()
				.filter(item -> item.getFrom() != null && item.getFrom().contains("1001"))
				.collect(Collectors.toMap(item -> String.valueOf(item.getId()), item -> item)));
		itemsRepository.save(itemsResponseSlice.getItems().values());

		mockMvc.perform(get(apiPath + "/items/boots")
				.param("mapId", SUMMONERS_RIFT))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.items").exists())
				.andExpect(jsonPath("$._embedded.items[*].group").exists())
				.andExpect(jsonPath("$._embedded.items[*].from").isNotEmpty())
				.andExpect(jsonPath("$._embedded.items[*].into").exists())
				.andExpect(jsonPath("$._embedded.items[*].maps", hasItem(hasEntry(SUMMONERS_RIFT, true))))
				.andExpect(jsonPath("$._embedded.items[*].gold.purchasable", hasItem(true)))
				.andExpect(jsonPath("$._embedded.items[*]._links").exists())
				.andExpect(jsonPath("$._embedded.items[*]._links.from").exists())
				.andExpect(jsonPath("$._embedded.items[*]._links.into").doesNotExist())
				.andExpect(jsonPath("$._embedded.items[*]._links.maps").exists())
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists());
	}

	@Test
	public void getTrinkets() throws Exception {
		itemsResponseSlice.setItems(itemsResponse.getItems().values().stream()
				.filter(item -> item.getName() != null && item.getName().contains("Trinket") &&
						item.getDescription() != null && item.getDescription().contains("Trinket"))
				.collect(Collectors.toMap(item -> String.valueOf(item.getId()), item -> item)));
		itemsRepository.save(itemsResponseSlice.getItems().values());

		mockMvc.perform(get(apiPath + "/items/trinkets")
				.param("mapId", SUMMONERS_RIFT))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.items").exists())
				.andExpect(jsonPath("$._embedded.items[*].group").exists())
				.andExpect(jsonPath("$._embedded.items[*].from").isNotEmpty())
				.andExpect(jsonPath("$._embedded.items[*].into").isNotEmpty())
				.andExpect(jsonPath("$._embedded.items[*].maps", hasItem(hasEntry(SUMMONERS_RIFT, true))))
				.andExpect(jsonPath("$._embedded.items[*].gold.purchasable", hasItem(true)))
				.andExpect(jsonPath("$._embedded.items[*]._links").exists())
				.andExpect(jsonPath("$._embedded.items[*]._links.from").doesNotExist())
				.andExpect(jsonPath("$._embedded.items[*]._links.into").doesNotExist())
				.andExpect(jsonPath("$._embedded.items[*]._links.maps").exists())
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists());
	}

	@Test
	public void getViktorOnly() throws Exception {
		itemsResponseSlice.setItems(itemsResponse.getItems().values().stream()
				.filter(item -> item.getRequiredChampion() != null &&
						item.getRequiredChampion().equals("Viktor"))
				.collect(Collectors.toMap(item -> String.valueOf(item.getId()), item -> item)));
		itemsRepository.save(itemsResponseSlice.getItems().values());

		mockMvc.perform(get(apiPath + "/items/viktor-only"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.items").exists())
				.andExpect(jsonPath("$._embedded.items[*].from").isNotEmpty())
				.andExpect(jsonPath("$._embedded.items[*].into").isNotEmpty())
				.andExpect(jsonPath("$._embedded.items[*].requiredChampion", hasItem("Viktor")))
				.andExpect(jsonPath("$._embedded.items[*].maps", hasItem(hasEntry(SUMMONERS_RIFT, true))))
				.andExpect(jsonPath("$._embedded.items[*]._links").exists())
				.andExpect(jsonPath("$._embedded.items[*]._links.from").exists())
				.andExpect(jsonPath("$._embedded.items[*]._links.into").exists())
				.andExpect(jsonPath("$._embedded.items[*]._links.maps").exists())
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists());
	}

	@Test
	public void getForTrollBuild() throws Exception {
		itemsResponseSlice.setItems(RandomizeUtil.getRandoms(
				itemsResponse.getItems().values(), DEFAULT_PAGE_SIZE).stream()
				.collect(Collectors.toMap(champion -> String.valueOf(champion.getId()), champion -> champion)));
		itemsRepository.save(itemsResponseSlice.getItems().values());

		mockMvc.perform(get(apiPath + "/items/for-troll-build")
				.param("mapId", SUMMONERS_RIFT))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.items").exists())
				.andExpect(jsonPath("$._embedded.items[*].from").isNotEmpty())
				.andExpect(jsonPath("$._embedded.items[*].into").isNotEmpty())
				.andExpect(jsonPath("$._embedded.items[*].maps", hasItem(hasEntry(SUMMONERS_RIFT, true))))
				.andExpect(jsonPath("$._embedded.items[*].gold.purchasable", hasItem(true)))
				.andExpect(jsonPath("$._embedded.items[*]._links").exists())
				.andExpect(jsonPath("$._embedded.items[*]._links.from").exists())
				.andExpect(jsonPath("$._embedded.items[*]._links.into").doesNotExist())
				.andExpect(jsonPath("$._embedded.items[*]._links.maps").exists())
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists());
	}

}