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
class ItemsResponseDdragonTest {

    @Autowired
    private JacksonTester<ItemsResponse> jacksonTester;

    @Test
    void serializesIntoJson() {
        ItemsResponse response = new ItemsResponse();
        response.setItems(new LinkedHashMap<>());
        response.setType("item");
        response.setVersion("8.16.1");

        JsonContent<ItemsResponse> jsonContent = null;
        try {
            jsonContent = jacksonTester.write(response);
        } catch (IOException e) {
            fail("Unable to serialize Items Response into JSON", e);
        }

        assertThat(jsonContent).hasJsonPathStringValue("$.type");
        assertThat(jsonContent).hasJsonPathStringValue("$.version");
        assertThat(jsonContent).hasJsonPathMapValue("$.data");
    }

    @Test
    void deserializesFromJson() {
        String json =
                "{" +
                "  \"type\": \"item\"," +
                "  \"version\": \"8.16.1\"," +
                "  \"data\": {}" +
                "}";

        ObjectContent<ItemsResponse> response = null;
        try {
            response = jacksonTester.parse(json);
        } catch (IOException e) {
            fail("Unable to deserialize Items Response from JSON", e);
        }

        assertThat(response.getObject()).isNotNull();
        assertThat(response.getObject().getType()).isEqualTo("item");
        assertThat(response.getObject().getVersion()).isEqualTo("8.16.1");
        assertThat(response.getObject().getItems()).isEmpty();
    }

}
