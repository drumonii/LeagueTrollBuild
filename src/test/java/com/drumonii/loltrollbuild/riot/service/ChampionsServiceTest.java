package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Champion;
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

public class ChampionsServiceTest extends BaseSpringTestRunner {

	@Autowired
	private RestTemplate restTemplate;

	private MockRestServiceServer mockServer;

	@Autowired
	private ChampionsService championsService;

	@Autowired
	@Qualifier("champions")
	private UriComponents championsUri;

	@Autowired
	@Qualifier("champion")
	private UriComponentsBuilder championUriBuilder;

	@Value("${riot.api.static-data.region}")
	private String region;

	@Before
	public void before() {
		super.before();

		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	public void getChampions() throws Exception {
		mockServer.expect(requestTo(championsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(championsResponse), MediaType.APPLICATION_JSON_UTF8));

		List<Champion> champions = championsService.getChampions();
		mockServer.verify();

		assertThat(champions).isNotEmpty();
	}

	@Test
	public void getChampionsWithRestClientException() throws Exception {
		mockServer.expect(requestTo(championsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		List<Champion> champions = championsService.getChampions();
		mockServer.verify();

		assertThat(champions).isEmpty();
	}

	@Test
	public void getChampion() throws Exception {
		Champion trundle = championsResponse.getChampions().get("Trundle");
		UriComponents championUri = championUriBuilder.buildAndExpand(region, trundle.getId());

		mockServer.expect(requestTo(championUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(trundle), MediaType.APPLICATION_JSON_UTF8));

		Champion champion = championsService.getChampion(trundle.getId());
		mockServer.verify();

		assertThat(champion).isNotNull();
	}

	@Test
	public void getChampionWithRestClientException() throws Exception {
		Champion trundle = championsResponse.getChampions().get("Akali");
		UriComponents championUri = championUriBuilder.buildAndExpand(region, trundle.getId());

		mockServer.expect(requestTo(championUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		Champion champion = championsService.getChampion(trundle.getId());
		mockServer.verify();

		assertThat(champion).isNull();
	}

}