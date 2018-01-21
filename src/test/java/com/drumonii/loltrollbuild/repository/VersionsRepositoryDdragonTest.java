package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.fail;

@ActiveProfiles({ TESTING, DDRAGON })
public class VersionsRepositoryDdragonTest extends VersionsRepositoryTest {

	@Override protected List<Version> getVersions() {
		ClassPathResource versionsJson = new ClassPathResource("versions_data_dragon.json");
		try {
			return objectMapper.readValue(versionsJson.getFile(), new TypeReference<List<Version>>() {});
		} catch (IOException e) {
			fail("Unable to unmarshal the Versions response.", e);
			return null;
		}
	}

}