package com.drumonii.loltrollbuild.batch.maps;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
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
public class MapsDdragonRetrievalJobConfigTest extends MapsRetrievalJobConfigTest {

	@Before
	public void before() {
		ClassPathResource mapsJsonResource = new ClassPathResource("maps_data_dragon.json");
		try {
			mapsResponse = objectMapper.readValue(mapsJsonResource.getFile(), MapsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Maps response.", e);
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