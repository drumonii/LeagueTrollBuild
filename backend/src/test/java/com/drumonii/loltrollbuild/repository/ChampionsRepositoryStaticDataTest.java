package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

import static com.drumonii.loltrollbuild.config.Profiles.STATIC_DATA;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.fail;

@TestPropertySource(properties = { "riot.static-data.apiKey=API_KEY" })
@ActiveProfiles({ TESTING, STATIC_DATA })
public class ChampionsRepositoryStaticDataTest extends ChampionsRepositoryTest {

	@Override
	protected ChampionsResponse getChampionsResponse() {
		ClassPathResource championsJsonResource = new ClassPathResource("champions_static_data.json");
		try {
			return objectMapper.readValue(championsJsonResource.getFile(), ChampionsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Champions response.", e);
			return null;
		}
	}

}
