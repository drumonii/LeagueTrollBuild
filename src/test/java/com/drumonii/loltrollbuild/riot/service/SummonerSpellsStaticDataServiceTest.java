package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.config.RiotApiConfig;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static com.drumonii.loltrollbuild.config.Profiles.STATIC_DATA;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(SummonerSpellsService.class)
@Import(RiotApiConfig.class)
@EnableConfigurationProperties(RiotApiProperties.class)
@TestPropertySource(properties = "riot.static-data.apiKey=API_KEY")
@ActiveProfiles({ TESTING, STATIC_DATA })
public class SummonerSpellsStaticDataServiceTest {

	@Autowired
	private SummonerSpellsService summonerSpellsService;

	@Autowired
	private MockRestServiceServer mockServer;

	@Autowired
	@Qualifier("summonerSpells")
	private UriComponents summonerSpellsUri;

	@Autowired
	@Qualifier("summonerSpell")
	private UriComponentsBuilder summonerSpellBuilder;

	@Value("${riot.static-data.region}")
	private String region;

	private String summonerSpellsJson;

	@Before
	public void before() {
		ClassPathResource summonerSpellsJsonResource = new ClassPathResource("summoners_static_data.json");
		try {
			summonerSpellsJson = new String(Files.readAllBytes(summonerSpellsJsonResource.getFile().toPath()));
		} catch (IOException e) {
			fail("Unable to read the Summoner Spells JSON.", e);
		}
	}

	@Test
	public void getSummonerSpells() {
		mockServer.expect(requestTo(summonerSpellsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(summonerSpellsJson, MediaType.APPLICATION_JSON_UTF8));

		List<SummonerSpell> summonerSpells = summonerSpellsService.getSummonerSpells();
		mockServer.verify();

		assertThat(summonerSpells).isNotEmpty();
	}

	@Test
	public void getSummonerSpellsWithRestClientException() {
		mockServer.expect(requestTo(summonerSpellsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		List<SummonerSpell> summonerSpells = summonerSpellsService.getSummonerSpells();
		mockServer.verify();

		assertThat(summonerSpells).isEmpty();
	}

	@Test
	public void getSummonerSpell() {
		int smiteId = 11;
		String smiteJson =
				"{" +
				"  \"name\": \"Smite\"," +
				"  \"description\": \"Deals 390-1000 true damage (depending on champion level) to target epic, large, or medium monster or enemy minion. Restores Health based on your maximum life when used against monsters.\"," +
				"  \"image\": {" +
				"    \"full\": \"SummonerSmite.png\"," +
				"    \"sprite\": \"spell0.png\"," +
				"    \"group\": \"spell\"," +
				"    \"x\": 192," +
				"    \"y\": 48," +
				"    \"w\": 48," +
				"    \"h\": 48" +
				"  }," +
				"  \"cooldown\": [" +
				"    15" +
				"  ]," +
				"  \"summonerLevel\": 9," +
				"  \"id\": 11," +
				"  \"key\": \"SummonerSmite\"," +
				"  \"modes\": [" +
				"    \"CLASSIC\"," +
				"    \"TUTORIAL\"," +
				"    \"FIRSTBLOOD\"," +
				"    \"URF\"," +
				"    \"ARSR\"," +
				"    \"DOOMBOTSTEEMO\"" +
				"  ]" +
				"}";

		UriComponents summonerSpellUri = summonerSpellBuilder.buildAndExpand(region, smiteId);

		mockServer.expect(requestTo(summonerSpellUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(smiteJson, MediaType.APPLICATION_JSON_UTF8));

		SummonerSpell summonerSpell = summonerSpellsService.getSummonerSpell(smiteId);
		mockServer.verify();

		assertThat(summonerSpell).isNotNull();
	}

	@Test
	public void getSummonerSpellWithRestClientException() {
		int smiteId = 11;

		UriComponents summonerSpellUri = summonerSpellBuilder.buildAndExpand(region, smiteId);

		mockServer.expect(requestTo(summonerSpellUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		SummonerSpell summonerSpell = summonerSpellsService.getSummonerSpell(smiteId);
		mockServer.verify();

		assertThat(summonerSpell).isNull();
	}

}