package com.drumonii.loltrollbuild.api;

import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.ActiveProfiles;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;

@ActiveProfiles({ TESTING })
class ChampionsDdragonRestControllerTest extends ChampionsRestControllerTest {

	@BeforeEach
	protected void beforeEach() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);

		championsResponse = jsonTestFilesUtil.getFullChampionsResponse();
		championsRepository.saveAll(championsResponse.getChampions().values());

		ItemsResponse itemsResponse = jsonTestFilesUtil.getItemsResponse();
		itemsRepository.saveAll(itemsResponse.getItems().values());

		SummonerSpellsResponse summonerSpellsResponse = jsonTestFilesUtil.getSummonerSpellsResponse();
		summonerSpellsRepository.saveAll(summonerSpellsResponse.getSummonerSpells().values());
	}

}
