package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.config.RiotApiConfig;
import com.drumonii.loltrollbuild.model.Champion;
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
@RestClientTest(ChampionsService.class)
@Import(RiotApiConfig.class)
@EnableConfigurationProperties(RiotApiProperties.class)
@TestPropertySource(properties = "riot.static-data.apiKey=API_KEY")
@ActiveProfiles({ TESTING, STATIC_DATA })
public class ChampionsStaticDataServiceTest {

	@Autowired
	private ChampionsService championsService;

	@Autowired
	private MockRestServiceServer mockServer;

	@Autowired
	@Qualifier("champions")
	private UriComponents championsUri;

	@Autowired
	@Qualifier("champion")
	private UriComponentsBuilder championUriBuilder;

	@Value("${riot.static-data.region}")
	private String region;

	private String championsJson;

	@Before
	public void before() {
		ClassPathResource championsJsonResource = new ClassPathResource("champions_static_data.json");
		try {
			championsJson = new String(Files.readAllBytes(championsJsonResource.getFile().toPath()));
		} catch (IOException e) {
			fail("Unable to read the Champions JSON.", e);
		}
	}

	@Test
	public void getChampions() {
		mockServer.expect(requestTo(championsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(championsJson, MediaType.APPLICATION_JSON_UTF8));

		List<Champion> champions = championsService.getChampions();
		mockServer.verify();

		assertThat(champions).isNotEmpty();
	}

	@Test
	public void getChampionsWithRestClientException() {
		mockServer.expect(requestTo(championsUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		List<Champion> champions = championsService.getChampions();
		mockServer.verify();

		assertThat(champions).isEmpty();
	}

	@Test
	public void getChampion() {
		int trundleId = 48;
		String trundleJson =
				"{" + // simplified JSON for brevity
				"  \"id\": 48," +
				"  \"key\": \"Trundle\"," +
				"  \"name\": \"Trundle\"," +
				"  \"title\": \"the Troll King\"," +
				"  \"image\": {" +
				"    \"full\": \"Trundle.png\"," +
				"    \"sprite\": \"champion3.png\"," +
				"    \"group\": \"champion\"," +
				"    \"x\": 144," +
				"    \"y\": 96," +
				"    \"w\": 48," +
				"    \"h\": 48" +
				"  }," +
				"  \"tags\": [" +
				"    \"Fighter\"," +
				"    \"Tank\"" +
				"  ]," +
				"  \"partype\": \"Mana\"," +
				"  \"info\": {" +
				"    \"attack\": 7," +
				"    \"defense\": 6," +
				"    \"magic\": 2," +
				"    \"difficulty\": 5" +
				"  }," +
				"  \"spells\": []," +
				"  \"passive\": {" +
				"    \"name\": \"King's Tribute\"," +
				"    \"description\": \"When an enemy unit dies near Trundle, he heals for a percent of its maximum Health.\"," +
				"    \"sanitizedDescription\": \"When an enemy unit dies near Trundle, he heals for a percent of its maximum Health.\"," +
				"    \"image\": {" +
				"      \"full\": \"Trundle_Passive.png\"," +
				"      \"sprite\": \"passive3.png\"," +
				"      \"group\": \"passive\"," +
				"      \"x\": 144," +
				"      \"y\": 96," +
				"      \"w\": 48," +
				"      \"h\": 48" +
				"    }" +
				"  }" +
				"}";

		UriComponents championUri = championUriBuilder.buildAndExpand(region, trundleId);

		mockServer.expect(requestTo(championUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(trundleJson, MediaType.APPLICATION_JSON_UTF8));

		Champion champion = championsService.getChampion(trundleId);
		mockServer.verify();

		assertThat(champion).isNotNull();
	}

	@Test
	public void getChampionWithRestClientException() {
		int akaliId = 84;

		UriComponents championUri = championUriBuilder.buildAndExpand(region, akaliId);

		mockServer.expect(requestTo(championUri.toString()))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withServerError());

		Champion champion = championsService.getChampion(akaliId);
		mockServer.verify();

		assertThat(champion).isNull();
	}

}