package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import org.springframework.test.context.ActiveProfiles;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;

@ActiveProfiles({ TESTING, DDRAGON })
class ItemsRepositoryDdragonTest extends ItemsRepositoryTest {

	@Override
	protected ItemsResponse getItemsResponse() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);
		return jsonTestFilesUtil.getItemsResponse();
	}

}
