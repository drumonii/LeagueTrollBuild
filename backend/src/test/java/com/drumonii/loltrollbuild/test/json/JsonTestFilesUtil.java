package com.drumonii.loltrollbuild.test.json;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.fail;

/**
 * Test utility for getting the Riot responses from the JSON test files.
 */
public class JsonTestFilesUtil {

    private final ObjectMapper objectMapper;

    public JsonTestFilesUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public static String getItemsJson() {
        ClassPathResource summonerSpellsJsonResource = new ClassPathResource("items_data_dragon.json");
        try {
            return new String(Files.readAllBytes(summonerSpellsJsonResource.getFile().toPath()));
        } catch (IOException e) {
            fail("Unable to read the Items JSON.", e);
            return "";
        }
    }

    public ItemsResponse getItemsResponse() {
        try {
            return objectMapper.readValue(getItemsJson(), ItemsResponse.class);
        } catch (IOException e) {
            fail("Unable to unmarshal the Items response.", e);
            return new ItemsResponse();
        }
    }

    public static String getChampionsJson() {
        ClassPathResource summonerSpellsJsonResource = new ClassPathResource("champions_data_dragon.json");
        try {
            return new String(Files.readAllBytes(summonerSpellsJsonResource.getFile().toPath()));
        } catch (IOException e) {
            fail("Unable to read the Champions JSON.", e);
            return "";
        }
    }

    public ChampionsResponse getChampionsResponse() {
        try {
            return objectMapper.readValue(getChampionsJson(), ChampionsResponse.class);
        } catch (IOException e) {
            fail("Unable to unmarshal the Champions response.", e);
            return new ChampionsResponse();
        }
    }

    public ChampionsResponse getFullChampionsResponse() {
        ChampionsResponse championsResponse = getChampionsResponse();

        ChampionsResponse fullChampionsResponse = new ChampionsResponse();
        fullChampionsResponse.setType("champion");
        fullChampionsResponse.setVersion(championsResponse.getVersion());

        for (Champion champion : championsResponse.getChampions().values()) {
            ChampionsResponse championResponse = getChampionResponse(champion.getKey());
            fullChampionsResponse.getChampions()
                    .put(champion.getKey(), championResponse.getChampions().get(champion.getKey()));
        }

        return fullChampionsResponse;
    }

    public static String getChampionJson(String championKey) {
        String jsonFilePath = championKey + "_data_dragon.json";

        ClassPathResource summonerSpellsJsonResource = new ClassPathResource(jsonFilePath);
        try {
            return new String(Files.readAllBytes(summonerSpellsJsonResource.getFile().toPath()));
        } catch (IOException e) {
            fail("Unable to read the Champion JSON.", e);
            return "";
        }
    }

    public ChampionsResponse getChampionResponse(String championKey) {
        try {
            return objectMapper.readValue(getChampionJson(championKey), ChampionsResponse.class);
        } catch (IOException e) {
            fail("Unable to unmarshal the Champion response.", e);
            return new ChampionsResponse();
        }
    }

    public static String getMapsJson() {
        ClassPathResource summonerSpellsJsonResource = new ClassPathResource("maps_data_dragon.json");
        try {
            return new String(Files.readAllBytes(summonerSpellsJsonResource.getFile().toPath()));
        } catch (IOException e) {
            fail("Unable to read the Maps JSON.", e);
            return "";
        }
    }

    public MapsResponse getMapsResponse() {
        try {
            return objectMapper.readValue(getMapsJson(), MapsResponse.class);
        } catch (IOException e) {
            fail("Unable to unmarshal the Maps response.", e);
            return new MapsResponse();
        }
    }

    public static String getSummonerSpellsJson() {
        ClassPathResource summonerSpellsJsonResource = new ClassPathResource("summoners_data_dragon.json");
        try {
            return new String(Files.readAllBytes(summonerSpellsJsonResource.getFile().toPath()));
        } catch (IOException e) {
            fail("Unable to read the Summoner Spells JSON.", e);
            return "";
        }
    }

    public SummonerSpellsResponse getSummonerSpellsResponse() {
        try {
            return objectMapper.readValue(getSummonerSpellsJson(), SummonerSpellsResponse.class);
        } catch (IOException e) {
            fail("Unable to unmarshal the Summoner Spells response.", e);
            return new SummonerSpellsResponse();
        }
    }

    public static String getVersionsJson() {
        ClassPathResource versionsJsonResource = new ClassPathResource("versions_data_dragon.json");
        try {
            return new String(Files.readAllBytes(versionsJsonResource.getFile().toPath()));
        } catch (IOException e) {
            fail("Unable to read the Versions JSON.", e);
            return "";
        }
    }

    public List<Version> getVersions() {
        try {
            List<Version> versions = objectMapper.readValue(getVersionsJson(), new TypeReference<>() {});
            versions.sort(Collections.reverseOrder());
            return versions;
        } catch (IOException e) {
            fail("Unable to unmarshal the Versions response.", e);
            return new ArrayList<>();
        }
    }

}
