package com.drumonii.loltrollbuild.api;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.test.api.WebMvcRestTest;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Collectors;

import static com.drumonii.loltrollbuild.util.GameMapUtil.SUMMONERS_RIFT_SID;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcRestTest(ItemsRestController.class)
public abstract class ItemsRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	protected ItemsRepository itemsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	@Value("${api.base-path}")
	private String apiPath;

	protected ItemsResponse itemsResponse;

	public abstract void before();

	@Test
	public void getItems() throws Exception {
		itemsResponse.setItems(itemsResponse.getItems().values().stream()
				.filter(item -> item.getRequiredChampion() != null)
				.collect(Collectors.toMap(item -> String.valueOf(item.getId()), item -> item)));
		itemsRepository.saveAll(itemsResponse.getItems().values());

		Item item = RandomizeUtil.getRandom(itemsResponse.getItems().values());

		// qbe
		mockMvc.perform(get("{apiPath}/items", apiPath))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with name
		mockMvc.perform(get("{apiPath}/items", apiPath)
				.param("name", item.getName().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with required champion
		mockMvc.perform(get("{apiPath}/items", apiPath)
				.param("requiredChampion", item.getRequiredChampion().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with maps
		mockMvc.perform(get("{apiPath}/items", apiPath)
				.param("maps[12]", Boolean.TRUE.toString()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*]").isNotEmpty());

		// qbe with no results
		mockMvc.perform(get("{apiPath}/items", apiPath)
				.param("name", "abcd1234"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(content().json("[]"));
	}

	@Test
	public void getItem() throws Exception {
		// find with non existing item Id
		mockMvc.perform(get("{apiPath}/items/{id}", apiPath, 0))
				.andExpect(status().isNotFound());

		Item sunfireCape = itemsRepository.save(itemsResponse.getItems().get("3068"));

		// find with existing item Id
		mockMvc.perform(get("{apiPath}/items/{id}", apiPath, sunfireCape.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
	}

	@Test
	public void getBoots() throws Exception {
		itemsResponse.setItems(itemsResponse.getItems().values().stream()
				.filter(item -> item.getFrom() != null && item.getFrom().contains(1001))
				.collect(Collectors.toMap(item -> String.valueOf(item.getId()), item -> item)));
		itemsRepository.saveAll(itemsResponse.getItems().values());

		mockMvc.perform(get("{apiPath}/items/boots", apiPath)
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
	public void getTrinkets() throws Exception {
		itemsResponse.setItems(itemsResponse.getItems().values().stream()
				.filter(item -> item.getName() != null && item.getName().contains("Trinket") ||
						item.getDescription() != null && item.getDescription().contains("Trinket"))
				.collect(Collectors.toMap(item -> String.valueOf(item.getId()), item -> item)));
		itemsRepository.saveAll(itemsResponse.getItems().values());

		mockMvc.perform(get("{apiPath}/items/trinkets", apiPath)
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
	public void getViktorOnly() throws Exception {
		itemsResponse.setItems(itemsResponse.getItems().values().stream()
				.filter(item -> item.getRequiredChampion() != null &&
						item.getRequiredChampion().equals("Viktor"))
				.collect(Collectors.toMap(item -> String.valueOf(item.getId()), item -> item)));
		itemsRepository.saveAll(itemsResponse.getItems().values());

		mockMvc.perform(get("{apiPath}/items/viktor-only", apiPath))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*].from").isNotEmpty())
				.andExpect(jsonPath("$.[*].into").isNotEmpty())
				.andExpect(jsonPath("$.[*].requiredChampion", hasItem("Viktor")))
				.andExpect(jsonPath("$.[*].maps", hasItem(hasEntry(SUMMONERS_RIFT_SID, true))));
	}

	@Test
	public void getForTrollBuild() throws Exception {
		itemsRepository.saveAll(itemsResponse.getItems().values());

		mockMvc.perform(get("{apiPath}/items/for-troll-build", apiPath)
				.param("mapId", SUMMONERS_RIFT_SID))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*].from").isNotEmpty())
				.andExpect(jsonPath("$.[*].into").isNotEmpty())
				.andExpect(jsonPath("$.[*].maps", hasItem(hasEntry(SUMMONERS_RIFT_SID, true))))
				.andExpect(jsonPath("$.[*].gold.purchasable", hasItem(true)));
	}

}
