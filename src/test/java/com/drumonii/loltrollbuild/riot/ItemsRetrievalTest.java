package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ItemsRetrievalTest extends BaseSpringTestRunner {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	@Qualifier("items")
	private UriComponents itemsUri;

	@Autowired
	private ItemsRepository itemsRepository;

	private MockRestServiceServer mockServer;
	private ItemsResponse itemsResponse;

	@Before
	public void before() {
		super.before();
		// Only first request is handled. See: http://stackoverflow.com/q/30713734
		mockServer = MockRestServiceServer.createServer(restTemplate);
		String responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"3711\":{\"id\":3711,\"name\":" +
				"\"Poacher's Knife\",\"group\":\"JungleItems\",\"description\":\"<stats>+30 Bonus Gold per Large " +
				"Monster Kill</stats><br><passive>Passive - Scavenging Smite:</passive> When you Smite a large " +
				"monster in the enemy jungle, you gain half a charge of Smite. If you kill that monster, you gain " +
				"+20 bonus Gold, and you gain 175% increased Movement Speed decaying over 2 seconds.<br><br><passive>" +
				"Passive - Jungler:</passive> Deal 45 additional magic damage to monsters over 2 seconds and gain 10 " +
				"Health Regen and 5 Mana Regen per second while under attack from neutral monsters.<br><br>" +
				"<groupLimit>Limited to 1 Jungle item</groupLimit>\",\"plaintext\":\"Makes your Smite give extra " +
				"gold from the enemy jungle\",\"from\":[\"1039\"],\"into\":[\"3719\",\"3720\",\"3721\",\"3722\"]," +
				"\"image\":{\"full\":\"3711.png\",\"sprite\":\"item2.png\",\"group\":\"item\",\"x\":432,\"y\":192," +
				"\"w\":48,\"h\":48},\"gold\":{\"base\":450,\"total\":850,\"sell\":595,\"purchasable\":true}}}}";
		mockServer.expect(requestTo(itemsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));
		try {
			itemsResponse = objectMapper.readValue(responseBody, ItemsResponse.class);
		} catch (IOException e) {}
		restTemplate = mock(RestTemplate.class);
		when(restTemplate.getForObject(itemsUri.toString(), ItemsResponse.class))
				.thenReturn(itemsResponse);
	}

	@After
	public void after() {
		itemsRepository.deleteAll();
	}

	@Test
	public void items() throws Exception {
		mockMvc.perform(get("/riot/items"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(
						Arrays.asList(itemsResponse.getItems().get("3711")))));
		mockServer.verify();
	}

	@Test
	public void saveItems() throws Exception {
		mockMvc.perform(post("/riot/items"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(
						Arrays.asList(itemsResponse.getItems().get("3711")))));
		mockServer.verify();

		assertThat(itemsRepository.findOne(3711)).isNotNull();
	}

	@Test
	public void saveDifferenceOfItems() throws Exception {
		itemsRepository.save(itemsResponse.getItems().get("3711"));
		mockMvc.perform(post("/riot/items"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
		mockServer.verify();

		assertThat(itemsRepository.findOne(3711)).isNotNull();
	}

	@Test
	public void saveItemsWithTruncate() throws Exception {
		String responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"3089\":{\"id\":3089,\"name\":" +
				"\"Rabadon's Deathcap\",\"description\":\"<stats>+120 Ability Power  </stats><br><br><unique>UNIQUE " +
				"Passive:</unique> Increases Ability Power by 35%.\",\"plaintext\":\"Massively increases Ability " +
				"Power\",\"from\":[\"1026\",\"1058\",\"1052\"],\"image\":{\"full\":\"3089.png\",\"sprite\":" +
				"\"item0.png\",\"group\":\"item\",\"x\":384,\"y\":432,\"w\":48,\"h\":48},\"gold\":{\"base\":965," +
				"\"total\":3500,\"sell\":2450,\"purchasable\":true}}}}";
		ItemsResponse newItemsResponse = objectMapper.readValue(responseBody, ItemsResponse.class);
		itemsRepository.save(newItemsResponse.getItems().get("3089"));

		mockMvc.perform(post("/riot/items?truncate=true"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(
						Arrays.asList(itemsResponse.getItems().get("3711")))));
		mockServer.verify();

		assertThat(itemsRepository.findOne(3089)).isNull();
		assertThat(itemsRepository.findOne(3711)).isNotNull();
	}

}
