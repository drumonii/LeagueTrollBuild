package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import org.springframework.test.context.ActiveProfiles;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;

@ActiveProfiles({ TESTING })
class MapsRepositoryDdragonTest extends MapsRepositoryTest {

	@Override
	protected MapsResponse getMapsResponse() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);
		return jsonTestFilesUtil.getMapsResponse();
	}

}
