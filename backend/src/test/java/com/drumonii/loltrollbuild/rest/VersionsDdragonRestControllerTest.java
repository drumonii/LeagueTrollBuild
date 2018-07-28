package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Before;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.fail;

@ActiveProfiles({ TESTING, DDRAGON })
public class VersionsDdragonRestControllerTest extends VersionsRestControllerTest {

	@Before
	public void before() {
		ClassPathResource versionsJson = new ClassPathResource("versions_data_dragon.json");
		try {
			versions = objectMapper.readValue(versionsJson.getFile(), new TypeReference<List<Version>>() {});
			versions.sort(Collections.reverseOrder());
		} catch (IOException e) {
			fail("Unable to unmarshal the Versions response.", e);
		}
		versionsRepository.saveAll(versions);
	}

}