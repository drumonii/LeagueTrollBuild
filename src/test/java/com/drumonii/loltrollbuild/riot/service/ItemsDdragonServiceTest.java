package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.config.RiotApiConfig;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.client.ExpectedCount.never;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest({ ItemsService.class, VersionsService.class })
@Import(RiotApiConfig.class)
@EnableConfigurationProperties(RiotApiProperties.class)
@ActiveProfiles({ TESTING, DDRAGON })
public class ItemsDdragonServiceTest {

	@Autowired
	private ItemsService itemsService;

	@Autowired
	private MockRestServiceServer mockServer;

	@Autowired
	@Qualifier("items")
	private UriComponentsBuilder itemsUri;

	@Autowired
	@Qualifier("versions")
	private UriComponents versionsUri;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${riot.ddragon.locale}")
	private String locale;

	private String itemsJson;
	private String versionsJson;

	private Version latestVersion;

	@Before
	public void before() {
		ClassPathResource itemsJsonResource = new ClassPathResource("items_data_dragon.json");
		try {
			itemsJson = new String(Files.readAllBytes(itemsJsonResource.getFile().toPath()));
		} catch (IOException e) {
			fail("Unable to read the Items JSON.", e);
		}
		ClassPathResource versionsJsonResource = new ClassPathResource("versions_data_dragon.json");
		try {
			versionsJson = new String(Files.readAllBytes(versionsJsonResource.getFile().toPath()));
		} catch (IOException e) {
			fail("Unable to read the Versions JSON.", e);
		}
		try {
			List<Version> versions = objectMapper.readValue(versionsJson, new TypeReference<List<Version>>() {});
			latestVersion = versions.get(0);
		} catch (IOException e) {
			fail("Unable to unmarshal the Versions response.", e);
		}
	}

	@Test
	public void getItems() {
		mockServer.expect(requestTo(versionsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsJson, MediaType.parseMediaType("text/json;charset=UTF-8")));

		mockServer.expect(requestTo(itemsUri.buildAndExpand(latestVersion.getPatch(), locale).toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemsJson, MediaType.APPLICATION_JSON_UTF8));

		List<Item> items = itemsService.getItems();
		mockServer.verify();

		assertThat(items).isNotEmpty();
	}

	@Test
	public void getItemsWithRestClientException() {
		mockServer.expect(requestTo(versionsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsJson, MediaType.parseMediaType("text/json;charset=UTF-8")));

		mockServer.expect(requestTo(itemsUri.buildAndExpand(latestVersion.getPatch(), locale).toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		List<Item> items = itemsService.getItems();
		mockServer.verify();

		assertThat(items).isEmpty();
	}

	@Test
	public void getItemsWithRestClientExceptionFromVersions() {
		mockServer.expect(requestTo(versionsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		mockServer.expect(never(), requestTo(itemsUri.buildAndExpand(latestVersion.getPatch(), locale).toString()))
				.andExpect(method(HttpMethod.GET));

		List<Item> items = itemsService.getItems();
		mockServer.verify();

		assertThat(items).isEmpty();
	}

	@Test
	public void getItem() {
		int bilgewaterCutlassId = 3144;

		mockServer.expect(requestTo(versionsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsJson, MediaType.parseMediaType("text/json;charset=UTF-8")));

		mockServer.expect(requestTo(itemsUri.buildAndExpand(latestVersion.getPatch(), locale).toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(itemsJson, MediaType.APPLICATION_JSON_UTF8));

		Item item = itemsService.getItem(bilgewaterCutlassId);
		mockServer.verify();

		assertThat(item).isNotNull();
	}

	@Test
	public void getItemWithRestClientException() {
		int bilgewaterCutlassId = 3144;

		mockServer.expect(requestTo(versionsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(versionsJson, MediaType.parseMediaType("text/json;charset=UTF-8")));

		mockServer.expect(requestTo(itemsUri.buildAndExpand(latestVersion.getPatch(), locale).toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		Item item = itemsService.getItem(bilgewaterCutlassId);
		mockServer.verify();

		assertThat(item).isNull();
	}

	@Test
	public void getItemWithRestClientExceptionFromVersions() {
		int bilgewaterCutlassId = 3144;

		mockServer.expect(requestTo(versionsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		mockServer.expect(never(), requestTo(itemsUri.buildAndExpand(latestVersion.getPatch(), locale).toString()))
				.andExpect(method(HttpMethod.GET));

		Item item = itemsService.getItem(bilgewaterCutlassId);
		mockServer.verify();

		assertThat(item).isNull();
	}

}