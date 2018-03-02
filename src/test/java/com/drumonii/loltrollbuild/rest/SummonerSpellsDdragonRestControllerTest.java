package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import org.junit.Before;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.fail;

@ActiveProfiles({ TESTING, DDRAGON })
public class SummonerSpellsDdragonRestControllerTest extends SummonerSpellsRestControllerTest {

	@Before
	public void before() {
		ClassPathResource summonerSpellsJson = new ClassPathResource("summoners_data_dragon.json");
		try {
			summonerSpellsResponse = objectMapper.readValue(summonerSpellsJson.getFile(), SummonerSpellsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Summoner Spells response.", e);
		}
		summonerSpellsRepository.saveAll(summonerSpellsResponse.getSummonerSpells().values());
	}

}