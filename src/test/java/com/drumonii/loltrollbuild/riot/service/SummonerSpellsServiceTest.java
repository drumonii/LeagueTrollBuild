package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.SummonerSpell;
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

public class SummonerSpellsServiceTest extends BaseSpringTestRunner {

	@Autowired
	private RestTemplate restTemplate;

	private MockRestServiceServer mockServer;

	@Autowired
	private SummonerSpellsService summonerSpellsService;

	@Autowired
	@Qualifier("summonerSpells")
	private UriComponents summonerSpellsUri;

	@Autowired
	@Qualifier("summonerSpell")
	private UriComponentsBuilder summonerSpellBuilder;

	@Value("${riot.api.static-data.region}")
	private String region;

	@Before
	public void before() {
		super.before();

		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	public void getSummonerSpells() throws Exception {
		mockServer.expect(requestTo(summonerSpellsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(summonerSpellsResponse), MediaType.APPLICATION_JSON_UTF8));

		List<SummonerSpell> summonerSpells = summonerSpellsService.getSummonerSpells();
		mockServer.verify();

		assertThat(summonerSpells).isNotEmpty();
	}

	@Test
	public void getSummonerSpellsWithRestClientException() throws Exception {
		mockServer.expect(requestTo(summonerSpellsUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		List<SummonerSpell> summonerSpells = summonerSpellsService.getSummonerSpells();
		mockServer.verify();

		assertThat(summonerSpells).isEmpty();
	}

	@Test
	public void getSummonerSpell() throws Exception {
		SummonerSpell smite = summonerSpellsResponse.getSummonerSpells().get("SummonerSmite");
		UriComponents summonerSpellUri = summonerSpellBuilder.buildAndExpand(region, smite.getId());

		mockServer.expect(requestTo(summonerSpellUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(objectMapper.writeValueAsString(smite), MediaType.APPLICATION_JSON_UTF8));

		SummonerSpell summonerSpell = summonerSpellsService.getSummonerSpell(smite.getId());
		mockServer.verify();

		assertThat(summonerSpell).isNotNull();
	}

	@Test
	public void getSummonerSpellWithRestClientException() throws Exception {
		SummonerSpell smite = summonerSpellsResponse.getSummonerSpells().get("SummonerSmite");
		UriComponents summonerSpellUri = summonerSpellBuilder.buildAndExpand(region, smite.getId());

		mockServer.expect(requestTo(summonerSpellUri.toString())).andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		SummonerSpell summonerSpell = summonerSpellsService.getSummonerSpell(smite.getId());
		mockServer.verify();

		assertThat(summonerSpell).isNull();
	}

}