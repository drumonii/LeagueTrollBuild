package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.SummonerSpell;
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

public class SummonerSpellsRetrievalTest extends BaseSpringTestRunner {

	@Autowired
	private RestTemplate restTemplate;

	private MockRestServiceServer mockServer;

	@Before
	public void before() {
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	public void summonerSpells() {
		mockServer.expect(requestTo("/riot/summoner-spells")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess("[{\"id\":1,\"name\":\"Cleanse\",\"description\":\"Removes all disables and " +
						"summoner spell debuffs affecting your champion and lowers the duration of incoming disables " +
						"by 65% for 3 seconds.\",\"image\":{\"full\":\"SummonerBoost.png\",\"sprite\":\"spell0.png\"," +
						"\"group\":\"spell\",\"x\":48,\"y\":0,\"w\":48,\"h\":48},\"cooldown\":[210],\"modes\":" +
						"[\"CLASSIC\",\"ODIN\",\"TUTORIAL\",\"ARAM\",\"ASCENSION\"]}]", MediaType.APPLICATION_JSON));
		List<SummonerSpell> summonerSpells = Arrays.asList(restTemplate.getForObject("/riot/summoner-spells",
				SummonerSpell[].class));

		mockServer.verify();
		assertThat(summonerSpells).isNotEmpty();
	}

}
