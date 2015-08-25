package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Champion;
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

public class ChampionsRetrievalTest extends BaseSpringTestRunner {

	@Autowired
	private RestTemplate restTemplate;

	private MockRestServiceServer mockServer;

	@Before
	public void before() {
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	public void champions() throws Exception {
		mockServer.expect(requestTo("/riot/champions")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess("[{\"id\":412,\"key\":\"Thresh\",\"name\":\"Thresh\",\"title\":" +
						"\"the Chain Warden\",\"image\":{\"full\":\"Thresh.png\",\"sprite\":\"champion3.png\"," +
						"\"group\":\"champion\",\"x\":336,\"y\":0,\"w\":48,\"h\":48},\"tags\":[\"Support\"," +
						"\"Fighter\"],\"partype\":\"Mana\"}]", MediaType.APPLICATION_JSON));
		List<Champion> champions = Arrays.asList(restTemplate.getForObject("/riot/champions", Champion[].class));

		mockServer.verify();
		assertThat(champions).isNotEmpty();
	}

}