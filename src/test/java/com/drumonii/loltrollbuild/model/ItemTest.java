package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemTest extends BaseSpringTestRunner {

	@Test
	public void equals() throws IOException {
		Item orbOfWinterFromRiot = itemsResponse.getItems().get("3112");
		itemsRepository.save(orbOfWinterFromRiot);

		Item orbOfWinterFromDb = itemsRepository.findOne(orbOfWinterFromRiot.getId());
		assertThat(orbOfWinterFromRiot).isEqualTo(orbOfWinterFromDb);

		Item faerieCharmFromRiot = itemsResponse.getItems().get("1004");
		itemsRepository.save(faerieCharmFromRiot);

		Item faerieCharmFromDb = itemsRepository.findOne(faerieCharmFromRiot.getId());
		assertThat(faerieCharmFromRiot).isEqualTo(faerieCharmFromDb);

		Item righteousGloryFromRiot = itemsResponse.getItems().get("3800");
		itemsRepository.save(righteousGloryFromRiot);

		Item righteousGloryFromDb = itemsRepository.findOne(righteousGloryFromRiot.getId());
		assertThat(righteousGloryFromRiot).isEqualTo(righteousGloryFromDb);

		righteousGloryFromRiot.setGold(new ItemGold(0, RandomUtils.nextInt(500, 1001), RandomUtils.nextInt(2000, 3001),
				RandomUtils.nextInt(1000, 2001), true, null));
		assertThat(righteousGloryFromRiot).isNotEqualTo(righteousGloryFromDb);
	}

	@Test
	public void cardinality() throws IOException {
		Item guinsoosRagebladeFromRiot = itemsResponse.getItems().get("3124");
		Item guinsoosRagebladeFromDb = itemsRepository.save(guinsoosRagebladeFromRiot);
		guinsoosRagebladeFromRiot.setGroup("NEW_GROUP");

		Item hauntingGuiseFromRiot = itemsResponse.getItems().get("3136");
		Item hauntingGuiseFromDb = itemsRepository.save(hauntingGuiseFromRiot);

		Item moonflairSpellbladeFromRiot = itemsResponse.getItems().get("3170");
		Item moonflairSpellbladeFromDb = itemsRepository.save(moonflairSpellbladeFromRiot);

		// Rageblade, Haunting Guise, and Moonflair
		List<Item> itemsFromDb = Arrays.asList(guinsoosRagebladeFromDb, hauntingGuiseFromDb, moonflairSpellbladeFromDb);

		Item giantsBeltFromRiot = itemsResponse.getItems().get("1011");

		// Updated Rageblade, same Moonflair, "new" Giant's Belt, and no Haunting Guise
		List<Item> itemsFromRiot = Arrays.asList(guinsoosRagebladeFromRiot, moonflairSpellbladeFromRiot,
				giantsBeltFromRiot);

		List<Item> deletedItems = ListUtils.subtract(itemsFromDb, itemsFromRiot);
		assertThat(deletedItems).hasSize(2);
		assertThat(deletedItems).containsOnly(guinsoosRagebladeFromDb, hauntingGuiseFromDb);

		List<Item> unmodifiedItems = ListUtils.intersection(itemsFromDb, itemsFromRiot);
		assertThat(unmodifiedItems).hasSize(1);
		assertThat(unmodifiedItems).containsOnly(moonflairSpellbladeFromDb);

		List<Item> itemsToUpdate = ListUtils.subtract(itemsFromRiot, itemsFromDb);
		assertThat(itemsToUpdate).hasSize(2);
		assertThat(itemsToUpdate).containsOnly(guinsoosRagebladeFromRiot, giantsBeltFromRiot);
	}

}