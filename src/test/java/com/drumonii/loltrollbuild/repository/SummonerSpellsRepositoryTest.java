package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

public class SummonerSpellsRepositoryTest extends BaseSpringTestRunner {

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void crudOperations() throws IOException {
		String responseBody = "{\"type\":\"summoner\",\"version\":\"5.16.1\",\"data\":{\"SummonerTest\":{\"name\":" +
				"\"Test\",\"description\":\"Test description. Very cool Summoner Spell. Wow.\",\"image\":{\"full\":" +
				"\"SummonerTest.png\",\"sprite\":\"spell1.png\",\"group\":\"spell\",\"x\":48,\"y\":0,\"w\":48,\"h\"" +
				":48},\"cooldown\":[210.0],\"summonerLevel\":6,\"id\":10001,\"key\":\"SummonerTest\",\"modes\":" +
				"[\"CLASSIC\", \"ARAM\"]}}}";
		SummonerSpellsResponse spellsResponse = objectMapper.readValue(responseBody, SummonerSpellsResponse.class);

		SummonerSpell unmarshalledSummonerSpell = spellsResponse.getSummonerSpells().get("SummonerTest");

		// Create
		summonerSpellsRepository.save(unmarshalledSummonerSpell);

		// Select
		SummonerSpell summonerSpellFromDb = summonerSpellsRepository.findOne(10001);
		assertThat(summonerSpellFromDb).isNotNull();
		assertThat(summonerSpellFromDb).isEqualToIgnoringNullFields(unmarshalledSummonerSpell);

		// Update
		summonerSpellFromDb.setModes(new HashSet<>(Arrays.asList("CLASSIC")));
		summonerSpellsRepository.save(summonerSpellFromDb);
		summonerSpellFromDb = summonerSpellsRepository.findOne(10001);
		assertThat(summonerSpellFromDb.getModes()).isEqualTo(new HashSet<>(Arrays.asList("CLASSIC")));

		// Delete
		summonerSpellsRepository.delete(summonerSpellFromDb);
		assertThat(summonerSpellsRepository.findOne(10001)).isNull();
	}

}
