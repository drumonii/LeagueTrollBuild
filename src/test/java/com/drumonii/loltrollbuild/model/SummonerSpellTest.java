package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.ListUtils;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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

	@Test
	public void cardinality() throws IOException {
		String responseBody = "{\"type\":\"summoner\",\"version\":\"5.21.1\",\"data\":{\"SummonerMana\":{\"name\":" +
				"\"Clarity\",\"description\":\"Restores 40% of your champion's maximum Mana. Also restores allies " +
				"for 40% of their maximum Mana\",\"image\":{\"full\":\"SummonerMana.png\",\"sprite\":\"spell0.png\"," +
				"\"group\":\"spell\",\"x\":384,\"y\":0,\"w\":48,\"h\":48},\"cooldown\":[180],\"summonerLevel\":1," +
				"\"id\":13,\"key\":\"SummonerMana\",\"modes\":[\"CLASSIC\",\"ODIN\",\"TUTORIAL\",\"ARAM\"," +
				"\"ASCENSION\"]}}}";
		SummonerSpell clarityFromRiot = objectMapper.readValue(responseBody, SummonerSpellsResponse.class)
				.getSummonerSpells().get("SummonerMana");
		summonerSpellsRepository.save(clarityFromRiot);
		SummonerSpell clarityFromDb = summonerSpellsRepository.findOne(clarityFromRiot.getId());

		responseBody = "{\"type\":\"summoner\",\"version\":\"5.24.2\",\"data\":{\"SummonerTeleport\":{\"name\":" +
				"\"Teleport\",\"description\":\"After channeling for 3.5 seconds, teleports your champion to target " +
				"allied structure, minion, or ward.\",\"image\":{\"full\":\"SummonerTeleport.png\",\"sprite\":" +
				"\"spell0.png\",\"group\":\"spell\",\"x\":144,\"y\":48,\"w\":48,\"h\":48},\"cooldown\":[300]," +
				"\"summonerLevel\":6,\"id\":12,\"key\":\"SummonerTeleport\",\"modes\":[\"CLASSIC\",\"TUTORIAL\"]}}}";
		SummonerSpell teleportFromRiot = objectMapper.readValue(responseBody, SummonerSpellsResponse.class)
				.getSummonerSpells().get("SummonerTeleport");
		summonerSpellsRepository.save(teleportFromRiot);
		SummonerSpell teleportFromDb = summonerSpellsRepository.findOne(teleportFromRiot.getId());

		responseBody = "{\"type\":\"summoner\",\"version\":\"5.24.2\",\"data\":{\"SummonerClairvoyance\":{\"name\":" +
				"\"Clairvoyance\",\"description\":\"Reveals a small area of the map for your team for 5 seconds.\"," +
				"\"image\":{\"full\":\"SummonerClairvoyance.png\",\"sprite\":\"spell0.png\",\"group\":\"spell\"," +
				"\"x\":96,\"y\":0,\"w\":48,\"h\":48},\"cooldown\":[55],\"summonerLevel\":8,\"id\":2,\"key\":" +
				"\"SummonerClairvoyance\",\"modes\":[\"ODIN\",\"TUTORIAL\",\"ASCENSION\"]}}}";
		SummonerSpell clairvoyanceFromRiot = objectMapper.readValue(responseBody, SummonerSpellsResponse.class)
				.getSummonerSpells().get("SummonerClairvoyance");
		summonerSpellsRepository.save(clairvoyanceFromRiot);
		SummonerSpell clairvoyanceFromDb = summonerSpellsRepository.findOne(clairvoyanceFromRiot.getId());

		// Clarity, Teleport, and Clairvoyance
		List<SummonerSpell> summonerSpellsFromDb = Arrays.asList(clarityFromDb, teleportFromDb, clairvoyanceFromDb);

		responseBody = "{\"type\":\"summoner\",\"version\":\"5.24.2\",\"data\":{\"SummonerMana\":{\"name\":" +
				"\"Clarity\",\"description\":\"Restores 40% of your champion's maximum Mana. Also restores allies " +
				"for 40% of their maximum Mana\",\"image\":{\"full\":\"SummonerMana.png\",\"sprite\":\"spell0.png\"," +
				"\"group\":\"spell\",\"x\":384,\"y\":0,\"w\":48,\"h\":48},\"cooldown\":[180],\"summonerLevel\":1," +
				"\"id\":13,\"key\":\"SummonerMana\",\"modes\":[\"ODIN\",\"TUTORIAL\",\"ARAM\",\"ASCENSION\"]}}}";
		clarityFromRiot = objectMapper.readValue(responseBody, SummonerSpellsResponse.class).getSummonerSpells()
				.get("SummonerMana");

		responseBody = "{\"type\":\"summoner\",\"version\":\"5.24.2\",\"data\":{\"SummonerBarrier\":{\"name\":" +
				"\"Barrier\",\"description\":\"Shields your champion for 115-455 (depending on champion level) for 2 " +
				"seconds.\",\"image\":{\"full\":\"SummonerBarrier.png\",\"sprite\":\"spell0.png\",\"group\":" +
				"\"spell\",\"x\":0,\"y\":0,\"w\":48,\"h\":48},\"cooldown\":[210],\"summonerLevel\":4,\"id\":21," +
				"\"key\":\"SummonerBarrier\",\"modes\":[\"ARAM\",\"CLASSIC\",\"TUTORIAL\",\"ODIN\",\"ASCENSION\"]}}}";
		SummonerSpell barrierFromRiot = objectMapper.readValue(responseBody, SummonerSpellsResponse.class)
				.getSummonerSpells().get("SummonerBarrier");

		// Updated Clarity, same Teleport, "new" Barrier, and no Clairvoyance
		List<SummonerSpell> summonerSpellsFromRiot = Arrays.asList(clarityFromRiot, teleportFromRiot, barrierFromRiot);

		List<SummonerSpell> deletedSummonerSpells = ListUtils.subtract(summonerSpellsFromDb, summonerSpellsFromRiot);
		assertThat(deletedSummonerSpells).hasSize(2);
		assertThat(deletedSummonerSpells).containsOnly(clarityFromDb, clairvoyanceFromDb);

		List<SummonerSpell> unmodifiedSummonerSpells = ListUtils.intersection(summonerSpellsFromDb,
				summonerSpellsFromRiot);
		assertThat(unmodifiedSummonerSpells).hasSize(1);
		assertThat(unmodifiedSummonerSpells).containsOnly(teleportFromDb);

		List<SummonerSpell> summonerSpellsToUpdate = ListUtils.subtract(summonerSpellsFromRiot, summonerSpellsFromDb);
		assertThat(summonerSpellsToUpdate).hasSize(2);
		assertThat(summonerSpellsToUpdate).containsOnly(clarityFromRiot, barrierFromRiot);
	}

}