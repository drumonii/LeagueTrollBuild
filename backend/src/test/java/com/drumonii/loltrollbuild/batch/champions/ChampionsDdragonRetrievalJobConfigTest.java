package com.drumonii.loltrollbuild.batch.champions;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Before;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.fail;

@ActiveProfiles({ TESTING, DDRAGON })
public class ChampionsDdragonRetrievalJobConfigTest extends ChampionsRetrievalJobConfigTest {

	@Before
	public void before() {
		ClassPathResource championsJsonResource = new ClassPathResource("champion_data_dragon.json");
		try {
			championsResponse = objectMapper.readValue(championsJsonResource.getFile(), ChampionsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Champions response.", e);
		}
		ClassPathResource versionsJson = new ClassPathResource("versions_data_dragon.json");
		try {
			List<Version> versions = objectMapper.readValue(versionsJson.getFile(), new TypeReference<List<Version>>() {});
			versions.sort(Collections.reverseOrder());
			latestVersion = versions.get(0);
		} catch (IOException e) {
			fail("Unable to unmarshal the Versions response.", e);
		}
	}

}