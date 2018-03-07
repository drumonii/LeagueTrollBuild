package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.rest.processor.ItemResourceProcessor;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.test.rest.WebMvcRestTest;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Collectors;

import static com.drumonii.loltrollbuild.rest.ItemsRestController.PAGE_SIZE;
import static com.drumonii.loltrollbuild.util.GameMapUtil.SUMMONERS_RIFT_SID;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcRestTest(controllers = ItemsRestController.class,
		includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ItemResourceProcessor.class))
public abstract class ItemsRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	protected ItemsRepository itemsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	@Value("${spring.data.rest.base-path}")
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
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.items").exists())
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(PAGE_SIZE)))
				.andExpect(jsonPath("$.page.totalElements").exists())
				.andExpect(jsonPath("$.page.totalPages").exists())
				.andExpect(jsonPath("$.page.number", is(0)));

		// qbe with name
		mockMvc.perform(get("{apiPath}/items", apiPath)
				.param("name", item.getName().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.items").exists())
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(PAGE_SIZE)))
				.andExpect(jsonPath("$.page.totalElements").exists())
				.andExpect(jsonPath("$.page.totalPages").exists())
				.andExpect(jsonPath("$.page.number").exists());

		// qbe with required champion
		mockMvc.perform(get("{apiPath}/items", apiPath)
				.param("requiredChampion", item.getRequiredChampion().toLowerCase()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.items").exists())
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(PAGE_SIZE)))
				.andExpect(jsonPath("$.page.totalElements").exists())
				.andExpect(jsonPath("$.page.totalPages").exists())
				.andExpect(jsonPath("$.page.number", is(0)));

		// qbe with maps
		mockMvc.perform(get("{apiPath}/items", apiPath)
				.param("maps[12]", Boolean.TRUE.toString()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.items").exists())
				.andExpect(jsonPath("$._links").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page").exists())
				.andExpect(jsonPath("$.page.size", is(PAGE_SIZE)))
				.andExpect(jsonPath("$.page.totalElements").exists())
				.andExpect(jsonPath("$.page.totalPages").exists())
				.andExpect(jsonPath("$.page.number", is(0)));

		// qbe with no results
		mockMvc.perform(get("{apiPath}/items", apiPath)
				.param("name", "abcd1234"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded").doesNotExist())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.self.href").exists())
				.andExpect(jsonPath("$.page.size", is(PAGE_SIZE)))
				.andExpect(jsonPath("$.page.totalElements").exists())
				.andExpect(jsonPath("$.page.totalPages").exists())
				.andExpect(jsonPath("$.page.number").exists());
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
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.items").exists())
				.andExpect(jsonPath("$._embedded.items[*].group").exists())
				.andExpect(jsonPath("$._embedded.items[*].from").isNotEmpty())
				.andExpect(jsonPath("$._embedded.items[*].into").exists())
				.andExpect(jsonPath("$._embedded.items[*].maps", hasItem(hasEntry(SUMMONERS_RIFT_SID, true))))
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
		itemsResponse.setItems(itemsResponse.getItems().values().stream()
				.filter(item -> item.getName() != null && item.getName().contains("Trinket") &&
						item.getDescription() != null && item.getDescription().contains("Trinket"))
				.collect(Collectors.toMap(item -> String.valueOf(item.getId()), item -> item)));
		itemsRepository.saveAll(itemsResponse.getItems().values());

		mockMvc.perform(get("{apiPath}/items/trinkets", apiPath)
				.param("mapId", SUMMONERS_RIFT_SID))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.items").exists())
				.andExpect(jsonPath("$._embedded.items[*].group").exists())
				.andExpect(jsonPath("$._embedded.items[*].from").isNotEmpty())
				.andExpect(jsonPath("$._embedded.items[*].into").isNotEmpty())
				.andExpect(jsonPath("$._embedded.items[*].maps", hasItem(hasEntry(SUMMONERS_RIFT_SID, true))))
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
		itemsResponse.setItems(itemsResponse.getItems().values().stream()
				.filter(item -> item.getRequiredChampion() != null &&
						item.getRequiredChampion().equals("Viktor"))
				.collect(Collectors.toMap(item -> String.valueOf(item.getId()), item -> item)));
		itemsRepository.saveAll(itemsResponse.getItems().values());

		mockMvc.perform(get("{apiPath}/items/viktor-only", apiPath))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.items").exists())
				.andExpect(jsonPath("$._embedded.items[*].from").isNotEmpty())
				.andExpect(jsonPath("$._embedded.items[*].into").isNotEmpty())
				.andExpect(jsonPath("$._embedded.items[*].requiredChampion", hasItem("Viktor")))
				.andExpect(jsonPath("$._embedded.items[*].maps", hasItem(hasEntry(SUMMONERS_RIFT_SID, true))))
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
		itemsRepository.saveAll(itemsResponse.getItems().values());

		mockMvc.perform(get("{apiPath}/items/for-troll-build", apiPath)
				.param("mapId", SUMMONERS_RIFT_SID))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8))
				.andExpect(jsonPath("$._embedded.items").exists())
				.andExpect(jsonPath("$._embedded.items[*].from").isNotEmpty())
				.andExpect(jsonPath("$._embedded.items[*].into").isNotEmpty())
				.andExpect(jsonPath("$._embedded.items[*].maps", hasItem(hasEntry(SUMMONERS_RIFT_SID, true))))
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