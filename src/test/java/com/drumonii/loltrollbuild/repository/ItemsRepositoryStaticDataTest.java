package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

import static com.drumonii.loltrollbuild.config.Profiles.STATIC_DATA;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.fail;

@TestPropertySource(properties = { "riot.static-data.apiKey=API_KEY" })
@ActiveProfiles({ TESTING, STATIC_DATA })
public class ItemsRepositoryStaticDataTest extends ItemsRepositoryTest {

	@Override
	protected ItemsResponse getItemsResponse() {
		ClassPathResource itemsJsonResource = new ClassPathResource("items_static_data.json");
		try {
			return objectMapper.readValue(itemsJsonResource.getFile(), ItemsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Items response.", e);
			return null;
		}
	}

}
