package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.MediaTypes;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ItemsRestControllerTest extends BaseSpringTestRunner {

	@Value("${spring.data.rest.base-path}")
	private String apiPath;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ItemsRepository itemsRepository;

	@After
	public void after() {
		itemsRepository.deleteAll();
	}

	@Test
	public void getItems() throws Exception {
		String responseBody = "{\"type\":\"item\",\"version\":\"6.13.1\",\"data\":{\"3901\":{\"id\":3901,\"name\":" +
				"\"Fire at Will\",\"group\":\"GangplankRUpgrade01\",\"description\":\"Requires 500 Silver Serpents." +
				"<br><br><unique>UNIQUE Passive:</unique> Cannon Barrage fires at an increasing rate over time " +
				"(additional 6 waves over the duration).\",\"plaintext\":\"Cannon Barrage gains extra waves\"," +
				"\"consumed\":true,\"requiredChampion\":\"Gangplank\",\"maps\":{\"1\":false,\"8\":true,\"10\":true," +
				"\"11\":true,\"12\":true,\"14\":false},\"image\":{\"full\":\"3901.png\",\"sprite\":\"item2.png\"," +
				"\"group\":\"item\",\"x\":0,\"y\":96,\"w\":48,\"h\":48},\"gold\":{\"base\":0,\"total\":0,\"sell\":0," +
				"\"purchasable\":true}}}}";
		Item fireAtWill = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3901");
		itemsRepository.save(fireAtWill);

		// qbe with name
		mockMvc.perform(get(apiPath + "/items")
				.param("name", "fire"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON))
				.andExpect(jsonPath("$._embedded.items", hasSize(1)))
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$.page").exists());

		// qbe with group
		mockMvc.perform(get(apiPath + "/items")
				.param("group", "upgrade"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON))
				.andExpect(jsonPath("$._embedded.items", hasSize(1)))
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$.page").exists());

		// qbe with name
		mockMvc.perform(get(apiPath + "/items")
				.param("requiredChampion", "gang"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON))
				.andExpect(jsonPath("$._embedded.items", hasSize(1)))
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$.page").exists());

		// qbe with no results
		mockMvc.perform(get(apiPath + "/items")
				.param("name", "notfound"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON))
				.andExpect(jsonPath("$._embedded").doesNotExist())
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$.page").exists());
	}

	@Test
	public void getBoots() throws Exception {
		mockMvc.perform(get(apiPath + "/items/boots")
				.param("mapId", SUMMONERS_RIFT))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON))
				.andExpect(jsonPath("$._links").exists());
	}

	@Test
	public void getTrinkets() throws Exception {
		mockMvc.perform(get(apiPath + "/items/trinkets")
				.param("mapId", SUMMONERS_RIFT))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON))
				.andExpect(jsonPath("$._links").exists());
	}

	@Test
	public void getViktorOnly() throws Exception {
		mockMvc.perform(get(apiPath + "/items/viktor-only"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON))
				.andExpect(jsonPath("$._links").exists());
	}

	@Test
	public void getForTrollBuild() throws Exception {
		mockMvc.perform(get(apiPath + "/items/for-troll-build")
				.param("mapId", SUMMONERS_RIFT))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON))
				.andExpect(jsonPath("$._links").exists());
	}

}