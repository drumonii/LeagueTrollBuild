package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;

@ActiveProfiles({ TESTING, DDRAGON })
public class VersionsRepositoryDdragonTest extends VersionsRepositoryTest {

	@Override protected List<Version> getVersions() {
		JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);
		return jsonTestFilesUtil.getVersions();
	}

}