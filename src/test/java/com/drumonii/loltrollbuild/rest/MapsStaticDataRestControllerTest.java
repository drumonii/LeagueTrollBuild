package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.riot.api.MapsResponse;
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
public class MapsStaticDataRestControllerTest extends MapsRestControllerTest {

	@Before
	public void before() {
		ClassPathResource mapsJsonResource = new ClassPathResource("maps_static_data.json");
		try {
			mapsResponse = objectMapper.readValue(mapsJsonResource.getFile(), MapsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Maps response.", e);
		}
		mapsRepository.save(mapsResponse.getMaps().values());
	}

}