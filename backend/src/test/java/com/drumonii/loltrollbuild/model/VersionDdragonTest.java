package com.drumonii.loltrollbuild.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@JsonTest
class VersionDdragonTest {

	@Autowired
	private JacksonTester<Version> jacksonTester;

	@Test
	void lolPatchStyle() {
		Version version = Version.patch("lolpatch_7.20");

		assertThat(version.getPatch()).isEqualTo("7.20.0");
		assertThat(version.getMajor()).isEqualTo(7);
		assertThat(version.getMinor()).isEqualTo(20);
		assertThat(version.getRevision()).isEqualTo(0);
	}

	@Test
	void serializesIntoJson() {
		Version version = Version.patch("lolpatch_6.24");

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

	@MethodSource("jsons")
	@ParameterizedTest
	void deserializesFromJson(String json) {
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

	static Stream<String> jsons() {
		return Stream.of(
				"""
                { "patch": "5.24.2", "major": 5, "minor": 24, "revision": 2 }
                """,
				"\"5.24.2\""
		);
	}

}
