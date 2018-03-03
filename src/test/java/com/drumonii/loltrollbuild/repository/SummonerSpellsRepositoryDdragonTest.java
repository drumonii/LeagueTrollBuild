package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.fail;

@ActiveProfiles({ TESTING, DDRAGON })
public class SummonerSpellsRepositoryDdragonTest extends SummonerSpellsRepositoryTest {

	@Override
	protected SummonerSpellsResponse getSummonerSpellsResponse() {
		ClassPathResource summonerSpellsJsonResource = new ClassPathResource("summoners_data_dragon.json");
		try {
			return objectMapper.readValue(summonerSpellsJsonResource.getFile(), SummonerSpellsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Summoner Spells response.", e);
			return null;
		}
	}

}
