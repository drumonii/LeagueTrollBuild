package com.drumonii.loltrollbuild.api;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.test.api.WebMvcRestTest;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Collectors;

import static com.drumonii.loltrollbuild.util.GameMapUtil.SUMMONERS_RIFT_SID;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcRestTest(ItemsRestController.class)
abstract class ItemsRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	protected ItemsRepository itemsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	protected ItemsResponse itemsResponse;

	protected abstract void beforeEach();

	@Test
	void getItems() throws Exception {
		itemsResponse.setItems(itemsResponse.getItems().values().stream()
				.filter(item -> item.getRequiredChampion() != null)
				.collect(Collectors.toMap(item -> String.valueOf(item.getId()), item -> item)));
		itemsRepository.saveAll(itemsResponse.getItems().values());

		Item item = RandomizeUtil.getRandom(itemsResponse.getItems().values());

		// qbe
		mockMvc.perform(get("/api/items"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with name
		mockMvc.perform(get("/api/items")
				.param("name", item.getName().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$..name", everyItem(is(item.getName()))));

		// qbe with required champion
		mockMvc.perform(get("/api/items")
				.param("requiredChampion", item.getRequiredChampion().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$..requiredChampion", everyItem(is(item.getRequiredChampion()))));

		// qbe with maps
		mockMvc.perform(get("/api/items")
				.param("maps[12]", Boolean.TRUE.toString()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$..maps[\"12\"]", everyItem(is(true))));

		// qbe with no results
		mockMvc.perform(get("/api/items")
				.param("name", "abcd1234"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(content().json("[]"));
	}

	@Test
	void getItem() throws Exception {
		// find with non existing item Id
		mockMvc.perform(get("/api/items/{id}", 0))
				.andExpect(status().isNotFound());

		Item sunfireCape = itemsRepository.save(itemsResponse.getItems().get("3068"));

		// find with existing item Id
		mockMvc.perform(get("/api/items/{id}", sunfireCape.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
	}

	@Test
	void getBoots() throws Exception {
		itemsResponse.setItems(itemsResponse.getItems().values().stream()
				.filter(item -> item.getFrom() != null && item.getFrom().contains(1001))
				.collect(Collectors.toMap(item -> String.valueOf(item.getId()), item -> item)));
		itemsRepository.saveAll(itemsResponse.getItems().values());

		mockMvc.perform(get("/api/items/boots")
				.param("mapId", SUMMONERS_RIFT_SID))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*].group").exists())
				.andExpect(jsonPath("$.[*].from").isNotEmpty())
				.andExpect(jsonPath("$.[*].into").exists())
				.andExpect(jsonPath("$.[*].maps", hasItem(hasEntry(SUMMONERS_RIFT_SID, true))))
				.andExpect(jsonPath("$.[*].gold.purchasable", hasItem(true)));
	}

	@Test
	void getTrinkets() throws Exception {
		itemsResponse.setItems(itemsResponse.getItems().values().stream()
				.filter(item -> item.getName() != null && item.getName().contains("Trinket") ||
						item.getDescription() != null && item.getDescription().contains("Trinket"))
				.collect(Collectors.toMap(item -> String.valueOf(item.getId()), item -> item)));
		itemsRepository.saveAll(itemsResponse.getItems().values());

		mockMvc.perform(get("/api/items/trinkets")
				.param("mapId", SUMMONERS_RIFT_SID))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*].group").exists())
				.andExpect(jsonPath("$.[*].from").isNotEmpty())
				.andExpect(jsonPath("$.[*].into").isNotEmpty())
				.andExpect(jsonPath("$.[*].maps", hasItem(hasEntry(SUMMONERS_RIFT_SID, true))))
				.andExpect(jsonPath("$.[*].gold.purchasable", hasItem(true)));
	}

	@Test
	void getViktorOnly() throws Exception {
		itemsResponse.setItems(itemsResponse.getItems().values().stream()
				.filter(item -> item.getRequiredChampion() != null &&
						item.getRequiredChampion().equals("Viktor"))
				.collect(Collectors.toMap(item -> String.valueOf(item.getId()), item -> item)));
		itemsRepository.saveAll(itemsResponse.getItems().values());

		mockMvc.perform(get("/api/items/viktor-only"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*].from").isNotEmpty())
				.andExpect(jsonPath("$.[*].into").isNotEmpty())
				.andExpect(jsonPath("$.[*].requiredChampion", hasItem("Viktor")))
				.andExpect(jsonPath("$.[*].maps", hasItem(hasEntry(SUMMONERS_RIFT_SID, true))));
	}

	@Test
	void getForTrollBuild() throws Exception {
		itemsRepository.saveAll(itemsResponse.getItems().values());

		mockMvc.perform(get("/api/items/for-troll-build")
				.param("mapId", SUMMONERS_RIFT_SID))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*].from").isNotEmpty())
				.andExpect(jsonPath("$.[*].into").isNotEmpty())
				.andExpect(jsonPath("$.[*].maps", hasItem(hasEntry(SUMMONERS_RIFT_SID, true))))
				.andExpect(jsonPath("$.[*].gold.purchasable", hasItem(true)));
	}

}
