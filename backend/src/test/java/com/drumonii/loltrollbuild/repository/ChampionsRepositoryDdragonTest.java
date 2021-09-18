package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import org.springframework.test.context.ActiveProfiles;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;

@ActiveProfiles({ TESTING })
class ChampionsRepositoryDdragonTest extends ChampionsRepositoryTest {

	@Override
	protected ChampionsResponse getChampionsResponse() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);
		return jsonTestFilesUtil.getFullChampionsResponse();
	}

}
