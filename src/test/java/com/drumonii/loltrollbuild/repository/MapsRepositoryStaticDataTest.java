package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

import static com.drumonii.loltrollbuild.config.Profiles.STATIC_DATA;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.fail;

@TestPropertySource(properties = { "riot.static-data.apiKey=API_KEY" })
@ActiveProfiles({ TESTING, STATIC_DATA })
public class MapsRepositoryStaticDataTest extends MapsRepositoryTest {

	@Override
	protected MapsResponse getMapsResponse() {
		ClassPathResource mapsJsonResource = new ClassPathResource("maps_static_data.json");
		try {
			return objectMapper.readValue(mapsJsonResource.getFile(), MapsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Maps response.", e);
			return null;
		}
	}

}