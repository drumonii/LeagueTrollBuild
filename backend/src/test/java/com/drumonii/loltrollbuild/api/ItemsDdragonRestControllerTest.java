package com.drumonii.loltrollbuild.api;

import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import org.junit.Before;
import org.springframework.test.context.ActiveProfiles;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;

@ActiveProfiles({ TESTING, DDRAGON })
public class ItemsDdragonRestControllerTest extends ItemsRestControllerTest {

	@Before
	public void before() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);

		itemsResponse = jsonTestFilesUtil.getItemsResponse();
	}

}
