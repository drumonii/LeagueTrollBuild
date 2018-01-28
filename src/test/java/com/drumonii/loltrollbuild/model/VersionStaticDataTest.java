package com.drumonii.loltrollbuild.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static com.drumonii.loltrollbuild.config.Profiles.STATIC_DATA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(SpringRunner.class)
@JsonTest
@TestPropertySource(properties = "riot.static-data.apiKey=API_KEY")
@ActiveProfiles({ STATIC_DATA })
public class VersionStaticDataTest {

	@Autowired
	private JacksonTester<Version> jacksonTester;

	@Test
	public void serializesIntoJson() {
		Version version = new Version("6.24.1");

		JsonContent<Version> jsonContent = null;
		try {
			jsonContent = jacksonTester.write(version);
		} catch (IOException e) {
			fail("Unable to serialize Version into JSON", e);
		}

		assertThat(jsonContent).isNotNull();
		assertThat(jsonContent).hasJsonPathStringValue("$.patch");
		assertThat(jsonContent).hasJsonPathNumberValue("$.major");
		assertThat(jsonContent).hasJsonPathNumberValue("$.minor");
		assertThat(jsonContent).hasJsonPathNumberValue("$.revision");
	}

	@Test
	public void deserializesFromJson() {
		String json =
				"{" +
				"  \"patch\": \"5.24.2\"," +
				"  \"major\": 5," +
				"  \"minor\": 24," +
				"  \"revision\": 2" +
				"}";

		ObjectContent<Version> version = null;
		try {
			version = jacksonTester.parse(json);
		} catch (IOException e) {
			fail("Unable to deserialize Version from JSON", e);
		}

		assertThat(version).isNotNull();
		assertThat(version.getObject()).isNotNull();
		assertThat(version.getObject().getPatch()).isEqualTo("5.24.2");
		assertThat(version.getObject().getMajor()).isEqualTo(5);
		assertThat(version.getObject().getMinor()).isEqualTo(24);
		assertThat(version.getObject().getRevision()).isEqualTo(2);
	}

}