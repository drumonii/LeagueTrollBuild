package com.drumonii.loltrollbuild.api;

import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.ActiveProfiles;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;

@ActiveProfiles({ TESTING })
class SummonerSpellsDdragonRestControllerTest extends SummonerSpellsRestControllerTest {

	@BeforeEach
	protected void beforeEach() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);

		summonerSpellsResponse = jsonTestFilesUtil.getSummonerSpellsResponse();
		summonerSpellsRepository.saveAll(summonerSpellsResponse.getSummonerSpells().values());
	}

}
