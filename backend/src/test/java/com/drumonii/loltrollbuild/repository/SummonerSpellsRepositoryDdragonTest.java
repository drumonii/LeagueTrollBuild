package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import org.springframework.test.context.ActiveProfiles;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;

@ActiveProfiles({ TESTING, DDRAGON })
public class SummonerSpellsRepositoryDdragonTest extends SummonerSpellsRepositoryTest {

	@Override
	protected SummonerSpellsResponse getSummonerSpellsResponse() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);
		return jsonTestFilesUtil.getSummonerSpellsResponse();
	}

}
