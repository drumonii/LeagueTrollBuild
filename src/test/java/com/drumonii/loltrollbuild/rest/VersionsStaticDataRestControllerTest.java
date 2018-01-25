package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Before;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.drumonii.loltrollbuild.config.Profiles.STATIC_DATA;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.fail;

@TestPropertySource(properties = { "riot.static-data.apiKey=API_KEY" })
@ActiveProfiles({ TESTING, STATIC_DATA })
public class VersionsStaticDataRestControllerTest extends VersionsRestControllerTest {

	@Before
	public void before() {
		ClassPathResource versionsJson = new ClassPathResource("versions_static_data.json");
		try {
			versions = objectMapper.readValue(versionsJson.getFile(), new TypeReference<List<Version>>() {});
			versions.sort(Collections.reverseOrder());
		} catch (IOException e) {
			fail("Unable to unmarshal the Versions response.", e);
		}
		versionsRepository.save(versions);
	}

}