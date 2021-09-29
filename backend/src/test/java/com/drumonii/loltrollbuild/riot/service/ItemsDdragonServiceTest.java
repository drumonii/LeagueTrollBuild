package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.test.api.service.AbstractDdragonServiceTests;
import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RestClientTest(ItemsService.class)
class ItemsDdragonServiceTest extends AbstractDdragonServiceTests {

	@Autowired
	private ItemsService itemsService;

	private String itemsJson;
	private Version latestVersion;

	@BeforeEach
	void beforeEach() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);

		itemsJson = JsonTestFilesUtil.getItemsJson();

		List<Version> versions = jsonTestFilesUtil.getVersions();
		latestVersion = versions.get(0);
	}

	@Nested
	@DisplayName("getItems")
	class GetItems {

		@Test
		void fromVersion() throws Exception {
			MockResponse itemsMockResponse = new MockResponse()
					.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.setBody(itemsJson);

			mockWebServer.enqueue(itemsMockResponse);

			List<Item> items = itemsService.getItems(latestVersion);
			assertThat(items).isNotEmpty();

			RecordedRequest recordedRequest = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
			assertThat(recordedRequest).satisfies(new ItemsRecordedRequest());
		}

		@Test
		void fromVersionWithRestClientException() throws Exception {
			MockResponse itemsMockResponse = new MockResponse()
					.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

			mockWebServer.enqueue(itemsMockResponse);

			List<Item> items = itemsService.getItems(latestVersion);
			assertThat(items).isEmpty();

			RecordedRequest recordedRequest = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
			assertThat(recordedRequest).satisfies(new ItemsRecordedRequest());
		}

	}

	private class ItemsRecordedRequest extends RiotApiRecordedRequest {

		@Override
		public void accept(RecordedRequest recordedRequest) {
			super.accept(recordedRequest);

			assertThat(recordedRequest.getPath()).as("Path")
					.isEqualTo(UriComponentsBuilder.fromPath(riotApiProperties.getDdragon().getItems())
							.buildAndExpand(latestVersion.getPatch(), riotApiProperties.getDdragon().getLocale())
							.toString());
		}

	}

}
