package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import org.junit.Before;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

import static com.drumonii.loltrollbuild.config.Profiles.STATIC_DATA;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.fail;

@TestPropertySource(properties = { "riot.static-data.apiKey=API_KEY" })
@ActiveProfiles({ TESTING, STATIC_DATA })
public class SummonerSpellsStaticDataRestControllerTest extends SummonerSpellsRestControllerTest {

	@Before
	public void before() {
		ClassPathResource summonerSpellsJson = new ClassPathResource("summoners_static_data.json");
		try {
			summonerSpellsResponse = objectMapper.readValue(summonerSpellsJson.getFile(), SummonerSpellsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Summoner Spells response.", e);
		}
		summonerSpellsRepository.saveAll(summonerSpellsResponse.getSummonerSpells().values());
	}

}