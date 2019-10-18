package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.SummonerSpell.GameMode;
import com.drumonii.loltrollbuild.model.builder.SummonerSpellBuilder;
import org.apache.commons.collections4.ListUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SummonerSpellTest {

	@Test
	void cardinality() {
		SummonerSpell clarityFromRiot = new SummonerSpellBuilder()
				.withId(13)
				.withKey("SummonerMana")
				.withName("Clarity")
				.withDescription("Restores 50% of your champion's maximum Mana. Also restores allies for 25% of their maximum Mana.")
				.withCooldown(240)
				.withModes(GameMode.ARAM)
				.build();
		SummonerSpell clarityFromDb = new SummonerSpellBuilder()
				.withId(13)
				.withKey("SummonerMana")
				.withName("Clarity")
				.withDescription("Restores 50% of your champion's maximum Mana. Also restores allies for 25% of their maximum Mana.")
				.withCooldown(240)
				.withModes(GameMode.ARAM, GameMode.CLASSIC)
				.build();

		SummonerSpell teleportFromRiot = new SummonerSpellBuilder()
				.withId(12)
				.withKey("SummonerTeleport")
				.withName("Teleport")
				.withDescription("After channeling for 4.5 seconds, teleports your champion to target allied structure, minion, or ward.")
				.withCooldown(210)
				.withModes(GameMode.ARAM, GameMode.CLASSIC, GameMode.TUTORIAL)
				.build();
		SummonerSpell teleportFromDb = new SummonerSpellBuilder()
				.withId(12)
				.withKey("SummonerTeleport")
				.withName("Teleport")
				.withDescription("After channeling for 4.5 seconds, teleports your champion to target allied structure, minion, or ward.")
				.withCooldown(210)
				.withModes(GameMode.ARAM, GameMode.CLASSIC, GameMode.TUTORIAL)
				.build();

		SummonerSpell smiteFromDb = new SummonerSpellBuilder()
				.withId(11)
				.withKey("SummonerSmite")
				.withName("Smite")
				.withDescription("Deals 390-1000 true damage (depending on champion level) to target epic, large, or medium monster or enemy minion. Restores Health based on your maximum life when used against monsters.")
				.withCooldown(15)
				.withModes(GameMode.ARAM, GameMode.CLASSIC, GameMode.TUTORIAL)
				.build();

		// Clarity, Teleport, and Smite
		List<SummonerSpell> summonerSpellsFromDb = Arrays.asList(clarityFromDb, teleportFromDb, smiteFromDb);

		SummonerSpell barrierFromRiot = new SummonerSpellBuilder()
				.withId(21)
				.withKey("SummonerBarrier")
				.withName("Barrier")
				.withDescription("Shields your champion from 115-455 damage (depending on champion level) for 2 seconds.")
				.withCooldown(180)
				.withModes(GameMode.ARAM, GameMode.CLASSIC, GameMode.TUTORIAL)
				.build();

		// Updated Clarity, same Teleport, "new" Barrier, and no Smite
		List<SummonerSpell> summonerSpellsFromRiot = Arrays.asList(clarityFromRiot, teleportFromRiot, barrierFromRiot);

		List<SummonerSpell> deletedSummonerSpells = ListUtils.subtract(summonerSpellsFromDb, summonerSpellsFromRiot);
		assertThat(deletedSummonerSpells).hasSize(2);
		assertThat(deletedSummonerSpells).containsOnly(clarityFromDb, smiteFromDb);

		List<SummonerSpell> unmodifiedSummonerSpells = ListUtils.intersection(summonerSpellsFromDb,
				summonerSpellsFromRiot);
		assertThat(unmodifiedSummonerSpells).hasSize(1);
		assertThat(unmodifiedSummonerSpells).containsOnly(teleportFromDb);

		List<SummonerSpell> summonerSpellsToUpdate = ListUtils.subtract(summonerSpellsFromRiot, summonerSpellsFromDb);
		assertThat(summonerSpellsToUpdate).hasSize(2);
		assertThat(summonerSpellsToUpdate).containsOnly(clarityFromRiot, barrierFromRiot);
	}

}
