package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import com.drumonii.loltrollbuild.riot.api.ImageSaver;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
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

	@Mock
	private UriComponentsBuilder itemsImgBuilder;

	@Mock
	private UriComponents itemsImgUri;

	@Mock
	private ImageSaver imageSaver;

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private VersionsRepository versionsRepository;

	private MockRestServiceServer mockServer;

	private String itemsResponseBody;
	private Item poachersKnife;

	private String itemResponseBody;
	private UriComponents itemUri;
	private Item lichBane;

	@Before
	public void before() {
		super.before();
		MockitoAnnotations.initMocks(this);

		// Only first request is handled. See: http://stackoverflow.com/q/30713734
		mockServer = MockRestServiceServer.createServer(restTemplate);
		itemsResponseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"3711\":{\"id\":3711,\"name\":" +
				"\"Poacher's Knife\",\"group\":\"JungleItems\",\"description\":\"<stats>+30 Bonus Gold per Large " +
				"Monster Kill</stats><br><passive>Passive - Scavenging Smite:</passive> When you Smite a large " +
				"monster in the enemy jungle, you gain half a charge of Smite. If you kill that monster, you gain " +
				"+20 bonus Gold, and you gain 175% increased Movement Speed decaying over 2 seconds.<br><br><passive>" +
				"Passive - Jungler:</passive> Deal 45 additional magic damage to monsters over 2 seconds and gain 10 " +
				"Health Regen and 5 Mana Regen per second while under attack from neutral monsters.<br><br>" +
				"<groupLimit>Limited to 1 Jungle item</groupLimit>\",\"plaintext\":\"Makes your Smite give extra " +
				"gold from the enemy jungle\",\"from\":[\"1039\"],\"into\":[\"3719\",\"3720\",\"3721\",\"3722\"]," +
				"\"maps\":{\"1\":false,\"8\":false,\"10\":true,\"11\":true,\"12\":false,\"14\":false},\"image\":" +
				"{\"full\":\"3711.png\",\"sprite\":\"item2.png\",\"group\":\"item\",\"x\":432,\"y\":192,\"w\":48," +
				"\"h\":48},\"gold\":{\"base\":450,\"total\":850,\"sell\":595,\"purchasable\":true}}}}";
		try {
			poachersKnife = objectMapper.readValue(itemsResponseBody, ItemsResponse.class).getItems().get("3711");
		} catch (IOException e) {
			fail("Unable to unmarshal the Items response.");
		}

		itemResponseBody = "{\"id\":3100,\"name\":\"Lich Bane\",\"description\":\"<stats>+80 Ability Power<br>+5% " +
				"Movement Speed<br><mana>+250 Mana</mana></stats><br><br><unique>UNIQUE Passive - Spellblade:" +
				"</unique> After using an ability, the next basic attack deals 75% Base Attack Damage (+50% of " +
				"Ability Power) bonus magic damage on hit (1.5 second cooldown).<br><br><i>(Unique Passives with the " +
				"same name don't stack.)</i>\",\"plaintext\":\"Grants a bonus to next attack after spell cast\"," +
				"\"from\":[\"3057\",\"3113\"],\"maps\":{\"1\":false,\"8\":true,\"10\":true,\"11\":true,\"12\":true," +
				"\"14\":false},\"image\":{\"full\":\"3100.png\",\"sprite\":\"item1.png\",\"group\":\"item\"," +
				"\"x\":288,\"y\":0,\"w\":48,\"h\":48},\"gold\":{\"base\":950,\"total\":3000,\"sell\":2100," +
				"\"purchasable\":true}}";
		try {
			lichBane = objectMapper.readValue(itemResponseBody, Item.class);
			itemUri = itemUriBuilder.buildAndExpand("na", lichBane.getId());
		} catch (IOException e) {
			fail("Unable to unmarshal the Item by ID response.");
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
				.andRespond(withSuccess(itemsResponseBody, MediaType.APPLICATION_JSON));

		mockMvc.perform(get("/riot/items").with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(poachersKnife))));
		mockServer.verify();
	}

	@Test
	public void saveItems() throws Exception {
		mockServer.expect(requestTo(itemsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemsResponseBody, MediaType.APPLICATION_JSON));

		when(itemsImgBuilder.buildAndExpand(anyString(), anyString()))
				.thenReturn(itemsImgUri);
		when(itemsImgUri.toUriString())
				.thenReturn(anyString());
		when(imageSaver.copyImageFromURL(any(Image.class), itemsImgBuilder))
				.thenReturn(1);

		mockMvc.perform(post("/riot/items").with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(poachersKnife))));
		mockServer.verify();

		assertThat(itemsRepository.findOne(3711)).isNotNull();
	}

	@Test
	public void saveItemsNoPatchVersion() throws Exception {
		mockServer.expect(requestTo(itemsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemsResponseBody, MediaType.APPLICATION_JSON));

		versionsRepository.deleteAll();

		mockMvc.perform(post("/riot/items").with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isNotFound());
		mockServer.verify();
	}

	@Test
	public void saveDifferenceOfItems() throws Exception {
		mockServer.expect(requestTo(itemsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemsResponseBody, MediaType.APPLICATION_JSON));
		itemsRepository.save(poachersKnife);

		when(itemsImgBuilder.buildAndExpand(anyString(), anyString()))
				.thenReturn(itemsImgUri);
		when(itemsImgUri.toUriString())
				.thenReturn(anyString());
		when(imageSaver.copyImageFromURL(any(Image.class), itemsImgBuilder))
				.thenReturn(1);

		mockMvc.perform(post("/riot/items").with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json("[]"));
		mockServer.verify();

		assertThat(itemsRepository.findOne(poachersKnife.getId())).isNotNull();
	}

	@Test
	public void saveItemsWithTruncate() throws Exception {
		String responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"3089\":{\"id\":3089,\"name\":" +
				"\"Rabadon's Deathcap\",\"description\":\"<stats>+120 Ability Power  </stats><br><br><unique>UNIQUE " +
				"Passive:</unique> Increases Ability Power by 35%.\",\"plaintext\":\"Massively increases Ability " +
				"Power\",\"from\":[\"1026\",\"1058\",\"1052\"],\"image\":{\"full\":\"3089.png\",\"sprite\":" +
				"\"item0.png\",\"group\":\"item\",\"x\":384,\"y\":432,\"w\":48,\"h\":48},\"gold\":{\"base\":965," +
				"\"total\":3500,\"sell\":2450,\"purchasable\":true}}}}";
		Item deathCap = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3089");
		itemsRepository.save(deathCap);

		mockServer.expect(requestTo(itemsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemsResponseBody, MediaType.APPLICATION_JSON));

		when(itemsImgBuilder.buildAndExpand(anyString(), anyString()))
				.thenReturn(itemsImgUri);
		when(itemsImgUri.toUriString())
				.thenReturn(anyString());
		when(imageSaver.copyImageFromURL(any(Image.class), itemsImgBuilder))
				.thenReturn(1);

		mockMvc.perform(post("/riot/items?truncate=true").with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(poachersKnife))));
		mockServer.verify();

		assertThat(itemsRepository.findOne(deathCap.getId())).isNull();
		assertThat(itemsRepository.findOne(poachersKnife.getId())).isNotNull();
	}

	@Test
	public void item() throws Exception {
		mockServer.expect(requestTo(itemUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemResponseBody, MediaType.APPLICATION_JSON));

		mockMvc.perform(get("/riot/items/{id}", lichBane.getId()).with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(lichBane)));
		mockServer.verify();
	}

	@Test
	public void itemNotFound() throws Exception {
		mockServer.expect(requestTo(itemUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.NOT_FOUND));

		mockMvc.perform(get("/riot/items/{id}", lichBane.getId()).with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isNotFound());
		mockServer.verify();
	}

	@Test
	public void saveItem() throws Exception {
		mockServer.expect(requestTo(itemUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemResponseBody, MediaType.APPLICATION_JSON));

		when(itemsImgBuilder.buildAndExpand(anyString(), anyString()))
				.thenReturn(itemsImgUri);
		when(itemsImgUri.toUriString())
				.thenReturn(anyString());
		when(imageSaver.copyImageFromURL(any(Image.class), itemsImgBuilder))
				.thenReturn(1);

		mockMvc.perform(post("/riot/items/{id}", lichBane.getId()).with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(lichBane)));
		mockServer.verify();

		assertThat(itemsRepository.findOne(lichBane.getId())).isNotNull();
	}

	@Test
	public void saveItemNotFound() throws Exception {
		mockServer.expect(requestTo(itemUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.NOT_FOUND));

		mockMvc.perform(post("/riot/items/{id}", lichBane.getId()).with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isNotFound());
		mockServer.verify();
	}

	@Test
	public void saveItemNoPatchVersion() throws Exception {
		mockServer.expect(requestTo(itemUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemResponseBody, MediaType.APPLICATION_JSON));

		versionsRepository.deleteAll();

		mockMvc.perform(post("/riot/items/{id}", lichBane.getId()).with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isNotFound());
		mockServer.verify();
	}

	@Test
	public void saveItemWithOverwrite() throws Exception {
		Item newLichBane = objectMapper.readValue(itemResponseBody, Item.class);
		newLichBane.setName("New Lich Bane");
		itemsRepository.save(newLichBane);

		mockServer.expect(requestTo(itemUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemResponseBody, MediaType.APPLICATION_JSON));

		when(itemsImgBuilder.buildAndExpand(anyString(), anyString()))
				.thenReturn(itemsImgUri);
		when(itemsImgUri.toUriString())
				.thenReturn(anyString());
		when(imageSaver.copyImageFromURL(any(Image.class), itemsImgBuilder))
				.thenReturn(1);

		mockMvc.perform(post("/riot/items/{id}", lichBane.getId()).with(csrf()).session(mockHttpSession("admin")))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(objectMapper.writeValueAsString(lichBane)));
		mockServer.verify();

		assertThat(itemsRepository.findOne(lichBane.getId())).isNotNull();
		assertThat(itemsRepository.findOne(lichBane.getId()).getName()).isEqualTo(lichBane.getName());
	}

}
