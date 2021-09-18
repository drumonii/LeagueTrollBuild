package com.drumonii.loltrollbuild.api;

import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.ActiveProfiles;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;

@ActiveProfiles({ TESTING })
class VersionsDdragonRestControllerTest extends VersionsRestControllerTest {

	@BeforeEach
	protected void beforeEach() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);

		versions = jsonTestFilesUtil.getVersions();
		versionsRepository.saveAll(versions);
	}

}
