package com.drumonii.loltrollbuild.riot.api;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.image.SummonerSpellImage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import static com.drumonii.loltrollbuild.model.SummonerSpell.GameMode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.assertj.core.data.MapEntry.entry;

public class SummonerSpellResponseTest extends BaseSpringTestRunner {

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void summonerSpellsResponseFromRiotIsMapped() throws IOException {
		String responseBody = "{\"type\":\"summoner\",\"version\":\"5.22.3\",\"data\":{\"SummonerBoost\":{\"name\":" +
				"\"Cleanse\",\"description\":\"Removes all disables and summoner spell debuffs affecting your " +
				"champion and lowers the duration of incoming disables by 65% for 3 seconds.\",\"image\":{\"full\":" +
				"\"SummonerBoost.png\",\"sprite\":\"spell0.png\",\"group\":\"spell\",\"x\":48,\"y\":0,\"w\":48," +
				"\"h\":48},\"cooldown\":[210],\"summonerLevel\":6,\"id\":1,\"key\":\"SummonerBoost\",\"modes\":" +
				"[\"CLASSIC\",\"ODIN\",\"TUTORIAL\",\"ARAM\",\"ASCENSION\"]}}}";
		SummonerSpellsResponse spellsResponse = null;
		try {
			spellsResponse = objectMapper.readValue(responseBody, SummonerSpellsResponse.class);
		} catch (IOException e) {
			fail("Unable to marshal the Summoner Spells response.", e);
		}

		assertThat(spellsResponse).isNotNull();
		assertThat(spellsResponse).isEqualToComparingOnlyGivenFields(new SummonerSpellsResponse("summoner", "5.22.3"),
				"type", "version");
		assertThat(spellsResponse.getSummonerSpells()).hasSize(1);
		SummonerSpell summonerSpell = new SummonerSpell(1, "Cleanse", "Removes all disables and summoner spell " +
				"debuffs affecting your champion and lowers the duration of incoming disables by 65% for 3 seconds.",
				null, new SummonerSpellImage("SummonerBoost.png", "spell0.png", "spell", new byte[0], 48, 0, 48, 48),
				new HashSet<>(Arrays.asList(210)), new HashSet<>(Arrays.asList(CLASSIC, ODIN, TUTORIAL, ARAM,
				ASCENSION)));
		assertThat(spellsResponse.getSummonerSpells()).containsExactly(entry("SummonerBoost", summonerSpell));
	}

}
