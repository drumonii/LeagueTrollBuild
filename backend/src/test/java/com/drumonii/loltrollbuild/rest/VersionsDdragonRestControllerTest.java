package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import org.junit.Before;
import org.springframework.test.context.ActiveProfiles;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;

@ActiveProfiles({ TESTING, DDRAGON })
public class VersionsDdragonRestControllerTest extends VersionsRestControllerTest {

	@Before
	public void before() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);

		versions = jsonTestFilesUtil.getVersions();
		versionsRepository.saveAll(versions);
	}

}