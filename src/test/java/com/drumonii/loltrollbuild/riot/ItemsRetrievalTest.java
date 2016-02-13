package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
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
	@Qualifier("item")
	private UriComponentsBuilder itemUriBuilder;

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private VersionsRepository versionsRepository;

	private MockRestServiceServer mockServer;

	private String itemsResponseBody;
	private Item trackersKnife;

	private String itemResponseBody;
	private UriComponents itemUri;
	private Item lichBane;

	@Before
	public void before() {
		super.before();

		// Only first request is handled. See: http://stackoverflow.com/q/30713734
		mockServer = MockRestServiceServer.createServer(restTemplate);
		itemsResponseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"3711\":{\"id\":3711,\"name\":" +
				"\"Tracker's Knife\",\"group\":\"JungleItems\",\"description\":\"<groupLimit>Limited to 1 Jungle item" +
				"</groupLimit><br><br><stats>+10% Life Steal vs. Monsters<br>+150% Mana Regeneration while in Jungle" +
				"</stats><br><br><unique>UNIQUE Passive - Tooth / Nail:</unique> Basic attacks deal 20 bonus damage " +
				"vs. monsters. Damaging a monster steals 30 Health over 5 seconds. Killing a Large Monster grants +30" +
				" bonus experience.<br><active>UNIQUE Active - Warding (Minor):</active> Consumes a charge to place a" +
				" <font color='#BBFFFF'>Stealth Ward</font> that reveals the surrounding area for 150 seconds.  Holds" +
				" up to 2 charges which refill upon visiting the shop. <br><br><rules>(A player may only have 3 <font" +
				" color='#BBFFFF'>Stealth Wards</font> on the map at one time. Unique Passives with the same name " +
				"don't stack.)</rules>\",\"plaintext\":\"Makes your Smite give extra gold from the enemy jungle\"," +
				"\"from\":[\"1039\",\"1041\"],\"into\":[\"1408\",\"1409\",\"1410\",\"1411\"],\"maps\":{\"1\":false," +
				"\"8\":false,\"10\":false,\"11\":true,\"12\":false,\"14\":false},\"image\":{\"full\":\"3711.png\"," +
				"\"sprite\":\"item2.png\",\"group\":\"item\",\"x\":432,\"y\":192,\"w\":48,\"h\":48},\"gold\":" +
				"{\"base\":350,\"total\":1050,\"sell\":735,\"purchasable\":true}}}}";
		try {
			trackersKnife = objectMapper.readValue(itemsResponseBody, ItemsResponse.class).getItems().get("3711");
		} catch (IOException e) {
			fail("Unable to unmarshal the Items response.", e);
		}

		itemResponseBody = "{\"id\":3100,\"name\":\"Lich Bane\",\"description\":\"<stats>+80 Ability Power<br>+7% " +
				"Movement Speed<br>+10% Cooldown Reduction<br><mana>+250 Mana</mana></stats><br><br><unique>UNIQUE " +
				"Passive - Spellblade:</unique> After using an ability, the next basic attack deals 75% Base Attack " +
				"Damage (+50% of Ability Power) bonus magic damage on hit (1.5 second cooldown).\",\"plaintext\":" +
				"\"Grants a bonus to next attack after spell cast\",\"from\":[\"3057\",\"3113\",\"1026\"],\"maps\":" +
				"{\"1\":false,\"8\":true,\"10\":true,\"11\":true,\"12\":true,\"14\":false},\"image\":{\"full\":" +
				"\"3100.png\",\"sprite\":\"item1.png\",\"group\":\"item\",\"x\":288,\"y\":0,\"w\":48,\"h\":48}," +
				"\"gold\":{\"base\":450,\"total\":3200,\"sell\":2240,\"purchasable\":true}}";
		try {
			lichBane = objectMapper.readValue(itemResponseBody, Item.class);
			itemUri = itemUriBuilder.buildAndExpand("na", lichBane.getId());
		} catch (IOException e) {
			fail("Unable to unmarshal the Item by ID response.", e);
		}

		versionsRepository.save(new Version("latest patch version"));
	}

	@After
	public void after() {
		itemsRepository.deleteAll();
		versionsRepository.deleteAll();
	}

	@Test
	public void items() throws Exception {
		mockServer.expect(requestTo(itemsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(get("/riot/items").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(trackersKnife))));
		mockServer.verify();
	}

	@Test
	public void saveItems() throws Exception {
		mockServer.expect(requestTo(itemsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/items").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(trackersKnife))));
		mockServer.verify();

		assertThat(itemsRepository.findOne(trackersKnife.getId())).isNotNull();
	}

	@Test
	public void saveItemsNoPatchVersion() throws Exception {
		mockServer.expect(requestTo(itemsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		versionsRepository.deleteAll();

		mockMvc.perform(post("/riot/items").with(adminUser()).with(csrf()))
				.andExpect(status().isNotFound());
		mockServer.verify();
	}

	@Test
	public void saveDifferenceOfItems() throws Exception {
		mockServer.expect(requestTo(itemsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemsResponseBody, MediaType.APPLICATION_JSON_UTF8));
		itemsRepository.save(trackersKnife);

		mockMvc.perform(post("/riot/items").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
		mockServer.verify();

		assertThat(itemsRepository.findOne(trackersKnife.getId())).isNotNull();
	}

	@Test
	public void saveDifferenceOfItemsWithDeleted() throws Exception {
		mockServer.expect(requestTo(itemsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemsResponseBody, MediaType.APPLICATION_JSON_UTF8));
		itemsRepository.save(trackersKnife);
		itemsRepository.save(lichBane);

		mockMvc.perform(post("/riot/items").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
		mockServer.verify();

		assertThat(itemsRepository.findOne(trackersKnife.getId())).isNotNull();
		assertThat(itemsRepository.findOne(lichBane.getId())).isNull();
	}

	@Test
	public void saveItemsWithTruncate() throws Exception {
		String responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"3089\":{\"id\":3089,\"name\":" +
				"\"Rabadon's Deathcap\",\"description\":\"<stats>+120 Ability Power  </stats><br><br><unique>UNIQUE " +
				"Passive:</unique> Increases Ability Power by 35%.\",\"plaintext\":\"Massively increases Ability " +
				"Power\",\"from\":[\"1026\",\"1058\",\"1052\"],\"maps\":{\"1\":false,\"8\":false,\"10\":false,\"11\":" +
				"true,\"12\":true,\"14\":false},\"image\":{\"full\":\"3089.png\",\"sprite\":\"item0.png\",\"group\":" +
				"\"item\",\"x\":384,\"y\":432,\"w\":48,\"h\":48},\"gold\":{\"base\":1265,\"total\":3800,\"sell\":" +
				"2660,\"purchasable\":true}}}}";
		Item deathCap = null;
		try {
			deathCap = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3089");
		} catch (IOException e) {
			fail("Unable to unmarshal the Item by ID response.", e);
		}
		itemsRepository.save(deathCap);

		mockServer.expect(requestTo(itemsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemsResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/items?truncate=true").with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(trackersKnife))));
		mockServer.verify();

		assertThat(itemsRepository.findOne(deathCap.getId())).isNull();
		assertThat(itemsRepository.findOne(trackersKnife.getId())).isNotNull();
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
	public void saveItemNoPatchVersion() throws Exception {
		mockServer.expect(requestTo(itemUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemResponseBody, MediaType.APPLICATION_JSON_UTF8));

		versionsRepository.deleteAll();

		mockMvc.perform(post("/riot/items/{id}", lichBane.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isNotFound());
		mockServer.verify();
	}

	@Test
	public void saveItemWithOverwrite() throws Exception {
		Item newLichBane = objectMapper.readValue(itemResponseBody, Item.class);
		newLichBane.setName("New Lich Bane");
		itemsRepository.save(newLichBane);

		mockServer.expect(requestTo(itemUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemResponseBody, MediaType.APPLICATION_JSON_UTF8));

		mockMvc.perform(post("/riot/items/{id}", lichBane.getId()).with(adminUser()).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(lichBane)));
		mockServer.verify();

		assertThat(itemsRepository.findOne(lichBane.getId())).isNotNull();
		assertThat(itemsRepository.findOne(lichBane.getId()).getName()).isEqualTo(lichBane.getName());
	}

}
