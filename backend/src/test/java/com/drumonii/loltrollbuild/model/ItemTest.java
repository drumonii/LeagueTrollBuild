package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.builder.ItemBuilder;
import org.apache.commons.collections4.ListUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class ItemTest {

	@Test
	public void cardinality() {
		Item guinsoosRagebladeFromRiot = new ItemBuilder()
				.withId(3124)
				.withName("Guinsoo's Rageblade")
				.withMapEntries(new SimpleEntry<>(11, true))
				.build();
		Item guinsoosRagebladeFromDb = new ItemBuilder()
				.withId(3124)
				.withName("Guinsoo's Rageblade")
				.withMapEntries(new SimpleEntry<>(11, false))
				.build();

		Item hauntingGuiseFromRiot = new ItemBuilder()
				.withId(3136)
				.withName("Haunting Guise")
				.withMapEntries(new SimpleEntry<>(11, true))
				.build();
		Item hauntingGuiseFromDb = new ItemBuilder()
				.withId(3136)
				.withName("Haunting Guise")
				.withMapEntries(new SimpleEntry<>(11, true))
				.build();

		Item moonflairSpellbladeFromDb = new ItemBuilder()
				.withId(3170)
				.withName("Moonflair Spellblade")
				.withMapEntries(new SimpleEntry<>(11, true))
				.build();

		// Rageblade, Haunting Guise, and Moonflair
		List<Item> itemsFromDb = Arrays.asList(guinsoosRagebladeFromDb, hauntingGuiseFromDb, moonflairSpellbladeFromDb);

		Item sightStoneFromRiot = new ItemBuilder()
				.withId(2049)
				.withName("Sightstone")
				.withMapEntries(new SimpleEntry<>(11, true))
				.build();

		// Updated Rageblade, same Haunting Guise, "new" Sightstone, and no Moonflair
		List<Item> itemsFromRiot = Arrays.asList(guinsoosRagebladeFromRiot, hauntingGuiseFromRiot, sightStoneFromRiot);

		List<Item> deletedItems = ListUtils.subtract(itemsFromDb, itemsFromRiot);
		assertThat(deletedItems).hasSize(2);
		assertThat(deletedItems).containsOnly(guinsoosRagebladeFromDb, moonflairSpellbladeFromDb);

		List<Item> unmodifiedItems = ListUtils.intersection(itemsFromDb, itemsFromRiot);
		assertThat(unmodifiedItems).hasSize(1);
		assertThat(unmodifiedItems).containsOnly(hauntingGuiseFromDb);

		List<Item> itemsToUpdate = ListUtils.subtract(itemsFromRiot, itemsFromDb);
		assertThat(itemsToUpdate).hasSize(2);
		assertThat(itemsToUpdate).containsOnly(guinsoosRagebladeFromRiot, sightStoneFromRiot);
	}

}