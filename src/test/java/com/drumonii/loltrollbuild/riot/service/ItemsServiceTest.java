package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Item;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class ItemsServiceTest extends BaseSpringTestRunner {

	@Autowired
	private RestTemplate restTemplate;

	private MockRestServiceServer mockServer;

	@Autowired
	private ItemsService itemsService;

	@Autowired
	@Qualifier("items")
	private UriComponents itemsUri;

	@Autowired
	@Qualifier("item")
	private UriComponentsBuilder itemUriBuilder;

	@Value("${riot.api.static-data.region}")
	private String region;

	@Before
	public void before() {
		super.before();

		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	public void getItems() throws Exception {
		mockServer.expect(requestTo(itemsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(itemsResponse), MediaType.APPLICATION_JSON_UTF8));

		List<Item> items = itemsService.getItems();
		mockServer.verify();

		assertThat(items).isNotEmpty();
	}

	@Test
	public void getItemsWithRestClientException() throws Exception {
		mockServer.expect(requestTo(itemsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		List<Item> items = itemsService.getItems();
		mockServer.verify();

		assertThat(items).isEmpty();
	}

	@Test
	public void getItem() throws Exception {
		Item bilgewaterCutlass = itemsResponse.getItems().get("3144");
		UriComponents championUri = itemUriBuilder.buildAndExpand(region, bilgewaterCutlass.getId());

		mockServer.expect(requestTo(championUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(bilgewaterCutlass), MediaType.APPLICATION_JSON_UTF8));

		Item item = itemsService.getItem(bilgewaterCutlass.getId());
		mockServer.verify();

		assertThat(item).isNotNull();
	}

	@Test
	public void getItemWithRestClientException() throws Exception {
		Item bilgewaterCutlass = itemsResponse.getItems().get("3144");
		UriComponents championUri = itemUriBuilder.buildAndExpand(region, bilgewaterCutlass.getId());

		mockServer.expect(requestTo(championUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		Item item = itemsService.getItem(bilgewaterCutlass.getId());
		mockServer.verify();

		assertThat(item).isNull();
	}

}