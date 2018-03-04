package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.fail;

@ActiveProfiles({ TESTING, DDRAGON })
public class MapsRepositoryDdragonTest extends MapsRepositoryTest {

	@Override
	protected MapsResponse getMapsResponse() {
		ClassPathResource mapsJsonResource = new ClassPathResource("maps_data_dragon.json");
		try {
			return objectMapper.readValue(mapsJsonResource.getFile(), MapsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Maps response.", e);
			return null;
		}
	}

}