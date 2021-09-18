package com.drumonii.loltrollbuild.riot.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import java.io.IOException;
import java.util.LinkedHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

@JsonTest
class MapsResponseDdragonTest {

    @Autowired
    private JacksonTester<MapsResponse> jacksonTester;

    @Test
    void serializesIntoJson() {
        MapsResponse response = new MapsResponse();
        response.setMaps(new LinkedHashMap<>());
        response.setType("map");
        response.setVersion("8.16.1");

        JsonContent<MapsResponse> jsonContent = null;
        try {
            jsonContent = jacksonTester.write(response);
        } catch (IOException e) {
            fail("Unable to serialize Maps Response into JSON", e);
        }

        assertThat(jsonContent).hasJsonPathStringValue("$.type");
        assertThat(jsonContent).hasJsonPathStringValue("$.version");
        assertThat(jsonContent).hasJsonPathMapValue("$.data");
    }

    @Test
    void deserializesFromJson() {
        String json =
                "{" +
                "  \"type\": \"map\"," +
                "  \"version\": \"8.16.1\"," +
                "  \"data\": {}" +
                "}";

        ObjectContent<MapsResponse> response = null;
        try {
            response = jacksonTester.parse(json);
        } catch (IOException e) {
            fail("Unable to deserialize Maps Response from JSON", e);
        }

        assertThat(response.getObject()).isNotNull();
        assertThat(response.getObject().getType()).isEqualTo("map");
        assertThat(response.getObject().getVersion()).isEqualTo("8.16.1");
        assertThat(response.getObject().getMaps()).isEmpty();
    }

}
