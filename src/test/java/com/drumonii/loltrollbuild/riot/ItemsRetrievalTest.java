package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Item;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class ItemsRetrievalTest extends BaseSpringTestRunner {

	@Autowired
	private RestTemplate restTemplate;

	private MockRestServiceServer mockServer;

	@Before
	public void before() {
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	public void items() {
		mockServer.expect(requestTo("/riot/items")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess("[{\"id\":3725,\"name\":\"Enchantment: Cinderhulk\",\"group\":\"JungleItems\"" +
						",\"consumed\":null,\"description\":\"<stats>+400 Health<br>+15% Bonus Health</stats><br><br>" +
						"<unique>UNIQUE Passive - Immolate:</unique> Deals 15 (+0.6 per champion level) magic damage " +
						"a second to nearby enemies. Deals 100% bonus damage to monsters. \",\"from\":[\"3713\"," +
						"\"3751\"],\"into\":null,\"maps\":null,\"image\":{\"full\":\"3725.png\",\"sprite\":" +
						"\"item2.png\",\"group\":\"item\",\"x\":96,\"y\":288,\"w\":48,\"h\":48},\"gold\":{\"base\":" +
						"400,\"total\":2250,\"sell\":1575,\"purchasable\":true}}]", MediaType.APPLICATION_JSON));
		List<Item> items = Arrays.asList(restTemplate.getForObject("/riot/items", Item[].class));

		mockServer.verify();
		assertThat(items).isNotEmpty();
	}

}
