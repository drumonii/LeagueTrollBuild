package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.config.RiotApiConfig;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static com.drumonii.loltrollbuild.config.Profiles.STATIC_DATA;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(ItemsService.class)
@Import(RiotApiConfig.class)
@EnableConfigurationProperties(RiotApiProperties.class)
@TestPropertySource(properties = "riot.static-data.apiKey=API_KEY")
@ActiveProfiles({ TESTING, STATIC_DATA })
public class ItemsStaticDataServiceTest {

	@Autowired
	private ItemsService itemsService;

	@Autowired
	private MockRestServiceServer mockServer;

	@Autowired
	@Qualifier("items")
	private UriComponents itemsUri;

	@Autowired
	@Qualifier("item")
	private UriComponentsBuilder itemUriBuilder;

	@Value("${riot.static-data.region}")
	private String region;

	private String itemsJson;

	@Before
	public void before() {
		ClassPathResource itemsJsonResource = new ClassPathResource("items_static_data.json");
		try {
			itemsJson = new String(Files.readAllBytes(itemsJsonResource.getFile().toPath()));
		} catch (IOException e) {
			fail("Unable to read the Items JSON.", e);
		}
	}

	@Test
	public void getItems() {
		mockServer.expect(requestTo(itemsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemsJson, MediaType.APPLICATION_JSON_UTF8));

		List<Item> items = itemsService.getItems();
		mockServer.verify();

		assertThat(items).isNotEmpty();
	}

	@Test
	public void getItemsWithRestClientException() {
		mockServer.expect(requestTo(itemsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		List<Item> items = itemsService.getItems();
		mockServer.verify();

		assertThat(items).isEmpty();
	}

	@Test
	public void getItem() {
		int bilgewaterCutlassId = 3144;
		String bilgewaterCutlassJson =
				"{" +
				"  \"id\": 3144," +
				"  \"name\": \"Bilgewater Cutlass\"," +
				"  \"description\": \"<stats>+25 Attack Damage<br>+10% Life Steal</stats><br><br><active>UNIQUE Active:</active> Deals 100 magic damage and slows the target champion's Movement Speed by 25% for 2 seconds (90 second cooldown).\"," +
				"  \"image\": {" +
				"    \"full\": \"3144.png\"," +
				"    \"sprite\": \"item1.png\"," +
				"    \"group\": \"item\"," +
				"    \"x\": 0," +
				"    \"y\": 336," +
				"    \"w\": 48," +
				"    \"h\": 48" +
				"  }," +
				"  \"gold\": {" +
				"    \"base\": 250," +
				"    \"total\": 1500," +
				"    \"sell\": 1050," +
				"    \"purchasable\": true" +
				"  }," +
				"  \"plaintext\": \"Activate to deal magic damage and slow target champion\"," +
				"  \"from\": [" +
				"    \"1053\"," +
				"    \"1036\"" +
				"  ]," +
				"  \"into\": [" +
				"    \"3146\"," +
				"    \"3153\"" +
				"  ]," +
				"  \"maps\": {" +
				"    \"8\": true," +
				"    \"10\": true," +
				"    \"11\": true," +
				"    \"12\": true," +
				"    \"14\": false," +
				"    \"16\": false," +
				"    \"18\": true," +
				"    \"19\": true" +
				"  }" +
				"}";

		UriComponents championUri = itemUriBuilder.buildAndExpand(region, bilgewaterCutlassId);

		mockServer.expect(requestTo(championUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(bilgewaterCutlassJson, MediaType.APPLICATION_JSON_UTF8));

		Item item = itemsService.getItem(bilgewaterCutlassId);
		mockServer.verify();

		assertThat(item).isNotNull();
	}

	@Test
	public void getItemWithRestClientException() {
		int bilgewaterCutlassId = 3144;

		UriComponents championUri = itemUriBuilder.buildAndExpand(region, bilgewaterCutlassId);

		mockServer.expect(requestTo(championUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		Item item = itemsService.getItem(bilgewaterCutlassId);
		mockServer.verify();

		assertThat(item).isNull();
	}

}