package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.Build;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import org.junit.Before;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

import static com.drumonii.loltrollbuild.config.Profiles.STATIC_DATA;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static com.drumonii.loltrollbuild.util.GameMapUtil.SUMMONERS_RIFT_SID;
import static org.assertj.core.api.Assertions.fail;

@TestPropertySource(properties = { "riot.static-data.apiKey=API_KEY" })
@ActiveProfiles({ TESTING, STATIC_DATA })
public class BuildsStaticDataRestControllerTest extends BuildsRestControllerTest {

	@Before
	public void before() {
		ClassPathResource championsJsonResource = new ClassPathResource("champions_static_data.json");
		try {
			championsResponse = objectMapper.readValue(championsJsonResource.getFile(), ChampionsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Champions response.", e);
		}
		ClassPathResource itemsJsonResource = new ClassPathResource("items_static_data.json");
		try {
			itemsResponse = objectMapper.readValue(itemsJsonResource.getFile(), ItemsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Items response.", e);
		}
		ClassPathResource mapsJsonResource = new ClassPathResource("maps_static_data.json");
		try {
			mapsResponse = objectMapper.readValue(mapsJsonResource.getFile(), MapsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Maps response.", e);
		}
		ClassPathResource summonerSpellsJson = new ClassPathResource("summoners_static_data.json");
		try {
			summonerSpellsResponse = objectMapper.readValue(summonerSpellsJson.getFile(), SummonerSpellsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Summoner Spells response.", e);
		}

		Build build = new Build();
		build.setChampionId(championsResponse.getChampions().get("Nasus").getId());
		build.setItem1Id(itemsResponse.getItems().get("3006").getId());
		build.setItem2Id(itemsResponse.getItems().get("3285").getId());
		build.setItem3Id(itemsResponse.getItems().get("3102").getId());
		build.setItem4Id(itemsResponse.getItems().get("3083").getId());
		build.setItem5Id(itemsResponse.getItems().get("3001").getId());
		build.setItem6Id(itemsResponse.getItems().get("3157").getId());
		build.setSummonerSpell1Id(summonerSpellsResponse.getSummonerSpells().get("SummonerTeleport").getId());
		build.setSummonerSpell2Id(summonerSpellsResponse.getSummonerSpells().get("SummonerHaste").getId());
		build.setTrinketId(itemsResponse.getItems().get("3341").getId());
		build.setMapId(mapsResponse.getMaps().get(SUMMONERS_RIFT_SID).getMapId());
		buildsRepository.save(build);
	}

}