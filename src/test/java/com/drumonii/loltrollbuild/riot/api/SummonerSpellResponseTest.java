package com.drumonii.loltrollbuild.riot.api;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Image;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.StrictAssertions.entry;

public class SummonerSpellResponseTest extends BaseSpringTestRunner {

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void summonerSpellsResponseFromRiotIsMapped() throws IOException {
		String responseBody = "{\"type\":\"summoner\",\"version\":\"5.16.1\",\"data\":{\"SummonerBoost\":{\"name\":" +
				"\"Cleanse\",\"description\":\"Removes all disables and summoner spell debuffs affecting your " +
				"champion and lowers the duration of incoming disables by 65% for 3 seconds.\",\"image\":{\"full\":" +
				"\"SummonerBoost.png\",\"sprite\":\"spell0.png\",\"group\":\"spell\",\"x\":48,\"y\":0,\"w\":48,\"h\"" +
				":48},\"cooldown\":[210.0],\"summonerLevel\":6,\"id\":1,\"key\":\"SummonerBoost\",\"modes\":" +
				"[\"CLASSIC\",\"ODIN\",\"TUTORIAL\",\"ARAM\",\"ASCENSION\"]}}}";

		SummonerSpellsResponse spellsResponse = objectMapper.readValue(responseBody, SummonerSpellsResponse.class);
		assertThat(spellsResponse).isNotNull();
		assertThat(spellsResponse).isEqualToComparingOnlyGivenFields(new SummonerSpellsResponse("summoner", "5.16.1"),
				"type", "version");
		assertThat(spellsResponse.getSummonerSpells()).hasSize(1);
		SummonerSpell summonerSpell = new SummonerSpell(1, "Cleanse", "Removes all disables and summoner spell " +
				"debuffs affecting your champion and lowers the duration of incoming disables by 65% for 3 seconds.",
				new Image("SummonerBoost.png", "spell0.png", "spell", 48, 0, 48, 48), Arrays.asList(210),
				Arrays.asList("CLASSIC", "ODIN", "TUTORIAL", "ARAM", "ASCENSION"));
		assertThat(spellsResponse.getSummonerSpells()).containsExactly(entry("SummonerBoost", summonerSpell));
	}

}
