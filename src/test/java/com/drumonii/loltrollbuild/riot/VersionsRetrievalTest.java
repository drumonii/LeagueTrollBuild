package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
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

public class VersionsRetrievalTest extends BaseSpringTestRunner {

	@Autowired
	private RestTemplate restTemplate;

	private MockRestServiceServer mockServer;

	@Before
	public void before() {
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	public void versions() throws Exception {
		mockServer.expect(requestTo("/riot/versions")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess("[\"5.16.1\",\"5.15.1\",\"5.14.1\",\"5.13.1\",\"5.12.1\",\"5.11.1\"," +
						"\"5.10.1\",\"5.9.1\",\"5.8.1\",\"5.7.2\",\"5.7.1\",\"5.6.2\",\"5.6.1\",\"5.5.3\",\n" +
						"\"5.5.2\",\"5.5.1\",\"5.4.1\",\"5.3.1\",\"5.2.2\",\"5.2.1\",\"5.1.2\",\"5.1.1\"]",
						MediaType.APPLICATION_JSON));
		List<String> versions = Arrays.asList(restTemplate.getForObject("/riot/versions", String[].class));

		mockServer.verify();
		assertThat(versions).isNotEmpty();
	}

}
