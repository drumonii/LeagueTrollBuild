package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

import static com.drumonii.loltrollbuild.config.Profiles.STATIC_DATA;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;

@TestPropertySource(properties = { "riot.static-data.apiKey=API_KEY" })
@ActiveProfiles({ TESTING, STATIC_DATA })
public class ImagesStaticDataRestControllerTest extends ImagesRestControllerTest {

	@Before
	public void before() {
		ClassPathResource championsJsonResource = new ClassPathResource("champions_static_data.json");
		try {
			championsResponse = objectMapper.readValue(championsJsonResource.getFile(), ChampionsResponse.class);
		} catch (IOException e) {
			Assertions.fail("Unable to unmarshal the Champions response.", e);
		}
		ClassPathResource itemsJsonResource = new ClassPathResource("items_static_data.json");
		try {
			itemsResponse = objectMapper.readValue(itemsJsonResource.getFile(), ItemsResponse.class);
		} catch (IOException e) {
			Assertions.fail("Unable to unmarshal the Items response.", e);
		}
		ClassPathResource mapsJsonResource = new ClassPathResource("maps_static_data.json");
		try {
			mapsResponse = objectMapper.readValue(mapsJsonResource.getFile(), MapsResponse.class);
		} catch (IOException e) {
			Assertions.fail("Unable to unmarshal the Maps response.", e);
		}
		ClassPathResource summonerSpellsJson = new ClassPathResource("summoners_static_data.json");
		try {
			summonerSpellsResponse = objectMapper.readValue(summonerSpellsJson.getFile(), SummonerSpellsResponse.class);
		} catch (IOException e) {
			Assertions.fail("Unable to unmarshal the Summoner Spells response.", e);
		}
	}

}