package com.drumonii.loltrollbuild.batch.items;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;

@ActiveProfiles({ TESTING })
class ItemsDdragonRetrievalJobConfigTest extends ItemsRetrievalJobConfigTest {

	@BeforeEach
	public void beforeEach() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);

		itemsResponse = jsonTestFilesUtil.getItemsResponse();
		List<Version> versions = jsonTestFilesUtil.getVersions();
		latestVersion = versions.get(0);
	}

}
