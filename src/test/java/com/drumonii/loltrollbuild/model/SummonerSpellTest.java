package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import org.apache.commons.collections4.ListUtils;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static com.drumonii.loltrollbuild.model.SummonerSpell.GameMode.ARAM;
import static com.drumonii.loltrollbuild.model.SummonerSpell.GameMode.CLASSIC;
import static org.assertj.core.api.Assertions.assertThat;

public class SummonerSpellTest extends BaseSpringTestRunner {

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@After
	public void after() {
		summonerSpellsRepository.deleteAll();
	}

	@Test
	public void equals() throws IOException {
		SummonerSpell markFromRiot = summonerSpellsResponse.getSummonerSpells().get("SummonerSnowball");
		summonerSpellsRepository.save(markFromRiot);

		SummonerSpell markFromDb = summonerSpellsRepository.findOne(markFromRiot.getId());
		assertThat(markFromRiot).isEqualTo(markFromDb);

		markFromRiot.setModes(new HashSet<>(Arrays.asList(ARAM, CLASSIC)));
		markFromRiot.setCooldown(new HashSet<>(Arrays.asList(50)));
		assertThat(markFromRiot).isNotEqualTo(markFromDb);
	}

	@Test
	public void cardinality() throws IOException {
		SummonerSpell clarityFromRiot = summonerSpellsResponse.getSummonerSpells().get("SummonerMana");
		SummonerSpell clarityFromDb = summonerSpellsRepository.save(clarityFromRiot);
		clarityFromRiot.setDescription("NEW_DESCRIPTION");

		SummonerSpell teleportFromRiot = summonerSpellsResponse.getSummonerSpells().get("SummonerTeleport");
		SummonerSpell teleportFromDb = summonerSpellsRepository.save(teleportFromRiot);

		SummonerSpell clairvoyanceFromRiot = summonerSpellsResponse.getSummonerSpells().get("SummonerClairvoyance");
		SummonerSpell clairvoyanceFromDb = summonerSpellsRepository.save(clairvoyanceFromRiot);

		// Clarity, Teleport, and Clairvoyance
		List<SummonerSpell> summonerSpellsFromDb = Arrays.asList(clarityFromDb, teleportFromDb, clairvoyanceFromDb);

		SummonerSpell barrierFromRiot = summonerSpellsResponse.getSummonerSpells().get("SummonerBarrier");

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