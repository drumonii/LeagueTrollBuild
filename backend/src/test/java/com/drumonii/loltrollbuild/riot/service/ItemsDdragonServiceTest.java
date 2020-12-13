package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.config.RiotApiConfig;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.ExpectedCount.never;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest({ ItemsService.class, VersionsService.class })
@Import(RiotApiConfig.class)
@EnableConfigurationProperties(RiotApiProperties.class)
@ActiveProfiles({ TESTING, DDRAGON })
class ItemsDdragonServiceTest {

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

	@BeforeEach
	void beforeEach() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);

		itemsJson = JsonTestFilesUtil.getItemsJson();
		versionsJson = JsonTestFilesUtil.getVersionsJson();
		List<Version> versions = jsonTestFilesUtil.getVersions();
		latestVersion = versions.get(0);
	}

	@AfterEach
	void afterEach() {
		mockServer.reset();
	}

	@Nested
	@DisplayName("getItems")
	class GetItems {

		@Test
		void fromVersion() {
			mockServer.expect(requestTo(itemsUri.buildAndExpand(latestVersion.getPatch(), locale).toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess(itemsJson, MediaType.APPLICATION_JSON));

			List<Item> items = itemsService.getItems(latestVersion);
			mockServer.verify();

			assertThat(items).isNotEmpty();
		}

		@Test
		void fromVersionWithRestClientException() {
			mockServer.expect(requestTo(itemsUri.buildAndExpand(latestVersion.getPatch(), locale).toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withServerError());

			List<Item> items = itemsService.getItems(latestVersion);
			mockServer.verify();

			assertThat(items).isEmpty();
		}

	}

	@Nested
	@DisplayName("getItem")
	class GetItem {

		@Test
		void fromItemId() {
			int bamisCinderId = 6660;

			mockServer.expect(requestTo(versionsUri.toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess(versionsJson, MediaType.parseMediaType("text/json;charset=UTF-8")));

			mockServer.expect(requestTo(itemsUri.buildAndExpand(latestVersion.getPatch(), locale).toString()))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess(itemsJson, MediaType.APPLICATION_JSON));

			Item item = itemsService.getItem(bamisCinderId);
			mockServer.verify();

			assertThat(item).isNotNull();
		}

		@Test
		void fromItemIdWithRestClientException() {
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
		void fromItemIdWithRestClientExceptionFromVersions() {
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

}
