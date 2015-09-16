package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Condition;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SummonerSpellsRepositoryTest extends BaseSpringTestRunner {

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@After
	public void after() {
		summonerSpellsRepository.deleteAll();
	}

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

	@Test
	public void forTrollBuild() throws IOException {
		String responseBody = "{\"type\":\"summoner\",\"version\":\"5.16.1\",\"data\":{\"SummonerSnowball\":" +
				"{\"name\":\"Mark\",\"description\":\"Throw a snowball in a straight line at your enemies. If it " +
				"hits an enemy, they become marked and your champion can quickly travel to the marked target as a " +
				"follow up.\",\"image\":{\"full\":\"SummonerSnowball.png\",\"sprite\":\"spell13.png\",\"group\":" +
				"\"spell\",\"x\":384,\"y\":96,\"w\":48,\"h\":48},\"cooldown\":[40],\"summonerLevel\":1,\"id\":32," +
				"\"key\":\"SummonerSnowball\",\"modes\":[\"ARAM\"]}}}";
		SummonerSpell mark = objectMapper.readValue(responseBody, SummonerSpellsResponse.class)
				.getSummonerSpells().get("SummonerSnowball");
		summonerSpellsRepository.save(mark);

		responseBody = "{\"type\":\"summoner\",\"version\":\"5.16.1\",\"data\":{\"SummonerSmite\":{\"name\":" +
				"\"Smite\",\"description\":\"Deals 390-1000 true damage (depending on champion level) to target epic " +
				"or large monster or enemy minion.\",\"image\":{\"full\":\"SummonerSmite.png\",\"sprite\":" +
				"\"spell0.png\",\"group\":\"spell\",\"x\":96,\"y\":48,\"w\":48,\"h\":48},\"cooldown\":[90]," +
				"\"summonerLevel\":10,\"id\":11,\"key\":\"SummonerSmite\",\"modes\":[\"CLASSIC\",\"TUTORIAL\"]}}}";
		SummonerSpell smite = objectMapper.readValue(responseBody, SummonerSpellsResponse.class)
				.getSummonerSpells().get("SummonerSmite");
		summonerSpellsRepository.save(smite);

		responseBody = "{\"type\":\"summoner\",\"version\":\"5.16.1\",\"data\":{\"SummonerExhaust\":{\"name\":" +
				"\"Exhaust\",\"description\":\"Exhausts target enemy champion, reducing their Movement Speed and " +
				"Attack Speed by 30%, their Armor and Magic Resist by 10, and their damage dealt by 40% for 2.5 " +
				"seconds.\",\"image\":{\"full\":\"SummonerExhaust.png\",\"sprite\":\"spell0.png\",\"group\":" +
				"\"spell\",\"x\":192,\"y\":0,\"w\":48,\"h\":48},\"cooldown\":[210],\"summonerLevel\":4,\"id\":3," +
				"\"key\":\"SummonerExhaust\",\"modes\":[\"CLASSIC\",\"ODIN\",\"TUTORIAL\",\"ARAM\",\"ASCENSION\"]}}}";
		SummonerSpell exhaust = objectMapper.readValue(responseBody, SummonerSpellsResponse.class)
				.getSummonerSpells().get("SummonerExhaust");
		summonerSpellsRepository.save(exhaust);

		List<SummonerSpell> forTrollBuild = summonerSpellsRepository.forTrollBuild();
		assertThat(forTrollBuild).extracting(SummonerSpell::getModes)
				.have(new Condition<>(mode -> mode.contains("CLASSIC"), "CLASSIC"));
	}

}
