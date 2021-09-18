package com.drumonii.loltrollbuild.batch.versions;

import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.ActiveProfiles;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;

@ActiveProfiles({ TESTING })
class VersionsDdragonRetrievalJobConfigTest extends VersionsRetrievalJobConfigTest {

	@BeforeEach
	public void beforeEach() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);

		versions = jsonTestFilesUtil.getVersions();
		latestVersion = versions.get(0);
	}

}
