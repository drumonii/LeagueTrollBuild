package com.drumonii.loltrollbuild.api;

import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import org.junit.Before;
import org.springframework.test.context.ActiveProfiles;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;

@ActiveProfiles({ TESTING, DDRAGON })
public class ImagesDdragonRestControllerTest extends ImagesRestControllerTest {

	@Before
	public void before() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);

		championsResponse = jsonTestFilesUtil.getFullChampionsResponse();
		itemsResponse = jsonTestFilesUtil.getItemsResponse();
		mapsResponse = jsonTestFilesUtil.getMapsResponse();
		summonerSpellsResponse = jsonTestFilesUtil.getSummonerSpellsResponse();
	}

}
