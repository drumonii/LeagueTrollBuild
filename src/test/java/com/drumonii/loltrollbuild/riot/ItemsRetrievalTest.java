package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ItemsRetrievalTest extends BaseSpringTestRunner {

	private static final int MAX_RESPONSE_SIZE = 10;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("items")
	private UriComponents itemsUri;

	@Autowired
	@Qualifier("item")
	private UriComponentsBuilder itemUriBuilder;

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	@Autowired
	private ItemsRepository itemsRepository;

	private MockRestServiceServer mockServer;

	private ItemsResponse itemsResponseSlice;
	private String itemsResponseBody;

	private String itemResponseBody;
	private UriComponents itemUri;
	private Item lichBane;

	private String versionsResponseBody;

	@Before
	public void before() {
		super.before();

		mockServer = MockRestServiceServer.createServer(restTemplate);

		// Create a random "slice" of ItemsResponse with size of MAX_RESPONSE_SIZE
		itemsResponseSlice = new ItemsResponse();
		itemsResponseSlice.setType(championsResponse.getType());
		itemsResponseSlice.setVersion(championsResponse.getVersion());
		itemsResponseSlice.setItems(RandomizeUtil.getRandoms(
				itemsResponse.getItems().values(), MAX_RESPONSE_SIZE).stream()
				.collect(Collectors.toMap(item -> String.valueOf(item.getId()), item -> item)));

		try {
			itemsResponseBody = objectMapper.writeValueAsString(itemsResponseSlice);
		} catch (JsonProcessingException e) {
			fail("Unable to marshal the Items response.", e);
		}

		lichBane = itemsResponse.getItems().get("3100");
		try {
			itemResponseBody = objectMapper.writeValueAsString(lichBane);
		} catch (JsonProcessingException e) {
			fail("Unable to marshal the Item.", e);
		}
		itemUri = itemUriBuilder.buildAndExpand("na", lichBane.getId());

		try {
			versionsResponseBody = objectMapper.writeValueAsString(versions);
		} catch (JsonProcessingException e) {
			fail("Unable to marshal the Versions.", e);
		}
	}

	@After
	public void after() {
		itemsRepository.deleteAll();
	}

	@Test
	public void items() throws Exception {
		mockServer.expect(requestTo(itemsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(get("/riot/items").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(itemsResponseSlice.getItems().values())));
		mockServer.verify();
	}

	@Test
	public void saveItems() throws Exception {
		mockServer.expect(requestTo(itemsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/items").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(itemsResponseSlice.getItems().values())));
		mockServer.verify();

		assertThat(itemsRepository.findAll())
				.containsOnlyElementsOf(itemsResponseSlice.getItems().values());
	}

	@Test
	public void saveDifferenceOfItems() throws Exception {
		mockServer.expect(requestTo(itemsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		List<Item> champions = itemsRepository.save(itemsResponseSlice.getItems().values());

		mockMvc.perform(post("/riot/items").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
		mockServer.verify();

		assertThat(itemsRepository.findAll()).containsOnlyElementsOf(champions);
	}

	@Test
	public void saveDifferenceOfItemsWithDeleted() throws Exception {
		List<Item> items = itemsRepository.save(itemsResponseSlice.getItems().values());
		Item itemToDelete = RandomizeUtil.getRandom(items);
		itemsResponseSlice.getItems().remove(String.valueOf(itemToDelete.getId()));

		mockServer.expect(requestTo(itemsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(itemsResponseSlice),
						MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/items").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
		mockServer.verify();

		assertThat(itemsRepository.findOne(itemToDelete.getId())).isNull();
	}

	@Test
	public void saveItemsWithTruncate() throws Exception {
		mockServer.expect(requestTo(itemsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/items?truncate=true").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(itemsResponseSlice.getItems().values())));
		mockServer.verify();

		assertThat(itemsRepository.findAll())
				.containsOnlyElementsOf(itemsResponseSlice.getItems().values());
	}

	@Test
	public void item() throws Exception {
		mockServer.expect(requestTo(itemUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(get("/riot/items/{id}", lichBane.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(lichBane)));
		mockServer.verify();
	}

	@Test
	public void itemNotFound() throws Exception {
		mockServer.expect(requestTo(itemUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.NOT_FOUND));

		mockMvc.perform(get("/riot/items/{id}", lichBane.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isNotFound());
		mockServer.verify();
	}

	@Test
	public void saveItem() throws Exception {
		mockServer.expect(requestTo(itemUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/items/{id}", lichBane.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(lichBane)));
		mockServer.verify();

		assertThat(itemsRepository.findOne(lichBane.getId())).isNotNull();
	}

	@Test
	public void saveItemNotFound() throws Exception {
		mockServer.expect(requestTo(itemUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.NOT_FOUND));

		mockMvc.perform(post("/riot/items/{id}", lichBane.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isNotFound());
		mockServer.verify();
	}

	@Test
	public void saveItemWithOverwrite() throws Exception {
		itemsRepository.save(lichBane);
		Item newLichBane = objectMapper.readValue(itemResponseBody, Item.class);
		newLichBane.setName("New Lich Bane");

		mockServer.expect(requestTo(itemUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(newLichBane), MediaType.APPLICATION_JSON_UTF8));

		mockServer.expect(once(), requestTo(versionsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/items/{id}", lichBane.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(newLichBane)));
		mockServer.verify();

		assertThat(itemsRepository.findOne(newLichBane.getId())).isNotNull()
				.isEqualTo(newLichBane);
	}

}
