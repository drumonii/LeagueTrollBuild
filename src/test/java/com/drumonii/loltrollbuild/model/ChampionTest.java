package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.builder.ChampionBuilder;
import org.apache.commons.collections4.ListUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class ChampionTest {

	@Test
	public void cardinality() {
		Champion gnarFromRiot = new ChampionBuilder()
				.withId(150)
				.withKey("Gnar")
				.withName("Gnar")
				.withTitle("the Missing Link")
				.withTags("Fighter", "Tank", "NEW_TAG")
				.build();
		Champion gnarFromDb = new ChampionBuilder()
				.withId(150)
				.withKey("Gnar")
				.withName("Gnar")
				.withTitle("the Missing Link")
				.withTags("Fighter", "Tank")
				.build();

		Champion gravesFromRiot = new ChampionBuilder()
				.withId(104)
				.withKey("Graves")
				.withName("Graves")
				.withTitle("the Outlaw")
				.withTags("Marksman")
				.build();
		Champion gravesFromDb = new ChampionBuilder()
				.withId(104)
				.withKey("Graves")
				.withName("Graves")
				.withTitle("the Outlaw")
				.withTags("Marksman")
				.build();

		Champion evelynnFromDb = new ChampionBuilder()
				.withId(28)
				.withKey("Evelynn")
				.withName("Evelynn")
				.withTitle("Agony's Embrace")
				.withTags("Assassin", "Mage")
				.build();

		// Gnar, Illaoi, and Graves
		List<Champion> championsFromDb = Arrays.asList(gnarFromDb, gravesFromDb, evelynnFromDb);

		Champion illaoiFromRiot = new ChampionBuilder()
				.withId(420)
				.withKey("Illaoi")
				.withName("Illaoi")
				.withTitle("the Kraken Priestess")
				.withTags("Fighter", "Tank")
				.build();

		// Updated Gnar, same Graves, "new" Illaoi, and no Evelynn
		List<Champion> championsFromRiot = Arrays.asList(gnarFromRiot, gravesFromRiot, illaoiFromRiot);

		List<Champion> deletedChampions = ListUtils.subtract(championsFromDb, championsFromRiot);
		assertThat(deletedChampions).hasSize(2);
		assertThat(deletedChampions).containsOnly(gnarFromDb, evelynnFromDb);

		List<Champion> unmodifiedChampions = ListUtils.intersection(championsFromDb, championsFromRiot);
		assertThat(unmodifiedChampions).hasSize(1);
		assertThat(unmodifiedChampions).containsOnly(gravesFromDb);

		List<Champion> championsToUpdate = ListUtils.subtract(championsFromRiot, championsFromDb);
		assertThat(championsToUpdate).hasSize(2);
		assertThat(championsToUpdate).containsOnly(gnarFromRiot, illaoiFromRiot);
	}

}