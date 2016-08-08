package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ItemsRestControllerTest extends BaseSpringTestRunner {

	@Value("${spring.data.rest.base-path}")
	private String apiPath;

	@Autowired
	private ItemsRepository itemsRepository;

	private ItemsResponse itemsResponseSlice;

	@Before
	public void before() {
		super.before();

		itemsResponseSlice = new ItemsResponse();
		itemsResponseSlice.setType(itemsResponse.getType());
		itemsResponseSlice.setVersion(itemsResponse.getVersion());
		itemsResponseSlice.setItems(itemsResponse.getItems().values().stream()
				.filter(item -> item.getGroup() != null && item.getRequiredChampion() != null)
				.collect(Collectors.toMap(item -> String.valueOf(item.getId()), item -> item)));
		itemsRepository.save(itemsResponseSlice.getItems().values());
	}

	@After
	public void after() {
		itemsRepository.deleteAll();
	}

	@Test
	public void getItems() throws Exception {
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
				.andExpect(jsonPath("$._embedded.items", hasSize(1)))
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(20)))
				.andExpect(jsonPath("$.page.totalElements", is(1)))
				.andExpect(jsonPath("$.page.totalPages", is(1)))
				.andExpect(jsonPath("$.page.number", is(0)));

		// qbe with group
		mockMvc.perform(get(apiPath + "/items")
				.param("group", item.getGroup().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.items", hasSize(
						(int) itemsResponseSlice.getItems().values().stream()
								.filter(i -> i.getGroup().equals(item.getGroup()))
								.count())))
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(20)))
				.andExpect(jsonPath("$.page.totalElements", is(
						(int) itemsResponseSlice.getItems().values().stream()
								.filter(i -> i.getGroup().equals(item.getGroup()))
								.count())))
				.andExpect(jsonPath("$.page.totalPages", is(
						(int) Math.ceil((double) itemsResponseSlice.getItems().values().stream()
								.filter(i -> i.getGroup().equals(item.getGroup()))
								.count() / (double) 20))))
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
		mockMvc.perform(get(apiPath + "/items/boots")
				.param("mapId", SUMMONERS_RIFT))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._links").exists());
	}

	@Test
	public void getTrinkets() throws Exception {
		mockMvc.perform(get(apiPath + "/items/trinkets")
				.param("mapId", SUMMONERS_RIFT))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._links").exists());
	}

	@Test
	public void getViktorOnly() throws Exception {
		mockMvc.perform(get(apiPath + "/items/viktor-only"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._links").exists());
	}

	@Test
	public void getForTrollBuild() throws Exception {
		mockMvc.perform(get(apiPath + "/items/for-troll-build")
				.param("mapId", SUMMONERS_RIFT))
				.andExpect(status().isOk())
				.andExpect(content().contentType(HAL_JSON_UTF8))
				.andExpect(jsonPath("$._links").exists());
	}

}