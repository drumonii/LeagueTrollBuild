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
class SummonerSpellsResponseDdragonTest {

    @Autowired
    private JacksonTester<SummonerSpellsResponse> jacksonTester;

    @Test
    void serializesIntoJson() {
        SummonerSpellsResponse response = new SummonerSpellsResponse();
        response.setSummonerSpells(new LinkedHashMap<>());
        response.setType("summoner");
        response.setVersion("8.16.1");

        JsonContent<SummonerSpellsResponse> jsonContent = null;
        try {
            jsonContent = jacksonTester.write(response);
        } catch (IOException e) {
            fail("Unable to serialize Summoner Spells Response into JSON", e);
        }

        assertThat(jsonContent).hasJsonPathStringValue("$.type");
        assertThat(jsonContent).hasJsonPathStringValue("$.version");
        assertThat(jsonContent).hasJsonPathMapValue("$.data");
    }

    @Test
    void deserializesFromJson() {
        String json =
                """
                {
                  "type": "summoner",
                  "version": "8.16.1",
                  "data": {}
                }
                """;

        ObjectContent<SummonerSpellsResponse> response = null;
        try {
            response = jacksonTester.parse(json);
        } catch (IOException e) {
            fail("Unable to deserialize Summoner Spells Response from JSON", e);
        }

        assertThat(response.getObject()).isNotNull();
        assertThat(response.getObject().getType()).isEqualTo("summoner");
        assertThat(response.getObject().getVersion()).isEqualTo("8.16.1");
        assertThat(response.getObject().getSummonerSpells()).isEmpty();
    }

}
