package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.Build;
import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import org.junit.Before;
import org.springframework.test.context.ActiveProfiles;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static com.drumonii.loltrollbuild.util.GameMapUtil.SUMMONERS_RIFT_SID;

@ActiveProfiles({ TESTING, DDRAGON })
public class BuildsDdragonRestControllerTest extends BuildsRestControllerTest {

	@Before
	public void before() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);

		championsResponse = jsonTestFilesUtil.getFullChampionsResponse();
		itemsResponse = jsonTestFilesUtil.getItemsResponse();
		mapsResponse = jsonTestFilesUtil.getMapsResponse();
		summonerSpellsResponse = jsonTestFilesUtil.getSummonerSpellsResponse();

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
		build.setTrinketId(itemsResponse.getItems().get("3364").getId());
		build.setMapId(mapsResponse.getMaps().get(SUMMONERS_RIFT_SID).getMapId());
		buildsRepository.save(build);
	}

}