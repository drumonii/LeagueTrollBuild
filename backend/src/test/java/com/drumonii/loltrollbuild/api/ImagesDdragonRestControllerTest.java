package com.drumonii.loltrollbuild.api;

import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.ActiveProfiles;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;

@ActiveProfiles({ TESTING })
class ImagesDdragonRestControllerTest extends ImagesRestControllerTest {

	@BeforeEach
	protected void beforeEach() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);

		championsResponse = jsonTestFilesUtil.getFullChampionsResponse();
		itemsResponse = jsonTestFilesUtil.getItemsResponse();
		mapsResponse = jsonTestFilesUtil.getMapsResponse();
		summonerSpellsResponse = jsonTestFilesUtil.getSummonerSpellsResponse();
	}

}
