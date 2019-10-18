package com.drumonii.loltrollbuild.api;

import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.ActiveProfiles;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;

@ActiveProfiles({ TESTING, DDRAGON })
class MapsDdragonRestControllerTest extends MapsRestControllerTest {

	@BeforeEach
	protected void beforeEach() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);

		mapsResponse = jsonTestFilesUtil.getMapsResponse();
		mapsRepository.saveAll(mapsResponse.getMaps().values());
	}

}
