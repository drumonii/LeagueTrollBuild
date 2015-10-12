package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

public class SummonerSpellTest extends BaseSpringTestRunner {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@After
	public void after() {
		summonerSpellsRepository.deleteAll();
	}

	@Test
	public void equals() throws IOException {
		String responseBody = "{\"type\":\"summoner\",\"version\":\"5.16.1\",\"data\":{\"SummonerSnowball\":" +
				"{\"name\":\"Mark\",\"description\":\"Throw a snowball in a straight line at your enemies. If it " +
				"hits an enemy, they become marked and your champion can quickly travel to the marked target as a " +
				"follow up.\",\"image\":{\"full\":\"SummonerSnowball.png\",\"sprite\":\"spell13.png\",\"group\":" +
				"\"spell\",\"x\":384,\"y\":96,\"w\":48,\"h\":48},\"cooldown\":[40],\"summonerLevel\":1,\"id\":32," +
				"\"key\":\"SummonerSnowball\",\"modes\":[\"ARAM\"]}}}";
		SummonerSpell markFromRiot = objectMapper.readValue(responseBody, SummonerSpellsResponse.class)
				.getSummonerSpells().get("SummonerSnowball");
		summonerSpellsRepository.save(markFromRiot);

		SummonerSpell markFromDb = summonerSpellsRepository.findOne(markFromRiot.getId());
		assertThat(markFromRiot).isEqualTo(markFromDb);

		markFromRiot.setModes(new HashSet<>(Arrays.asList("ARAM", "CLASSIC")));
		markFromRiot.setCooldown(new HashSet<>(Arrays.asList(50)));
		assertThat(markFromRiot).isNotEqualTo(markFromDb);
	}

}