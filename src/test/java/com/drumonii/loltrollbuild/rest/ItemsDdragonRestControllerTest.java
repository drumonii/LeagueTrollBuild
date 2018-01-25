package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import org.junit.Before;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.fail;

@ActiveProfiles({ TESTING, DDRAGON })
public class ItemsDdragonRestControllerTest extends ItemsRestControllerTest {

	@Before
	public void before() {
		ClassPathResource itemsJsonResource = new ClassPathResource("items_data_dragon.json");
		try {
			itemsResponse = objectMapper.readValue(itemsJsonResource.getFile(), ItemsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Items response.", e);
		}
	}

}