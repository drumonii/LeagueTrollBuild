package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import org.junit.Before;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.fail;

@ActiveProfiles({ TESTING, DDRAGON })
public class ChampionsDdragonRestControllerTest extends ChampionsRestControllerTest {

	@Before
	public void before() {
		ClassPathResource championsJsonResource = new ClassPathResource("champion_data_dragon.json");
		try {
			championsResponse = objectMapper.readValue(championsJsonResource.getFile(), ChampionsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Champions response.", e);
		}
		championsRepository.save(championsResponse.getChampions().values());
	}

}