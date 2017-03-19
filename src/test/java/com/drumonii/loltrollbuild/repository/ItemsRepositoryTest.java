package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.ItemGold;
import org.assertj.core.api.iterable.Extractor;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.SortedMap;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemsRepositoryTest extends BaseSpringTestRunner {

	@Before
	public void before() {
		super.before();

		itemsRepository.save(itemsResponse.getItems().values());
	}

	@Test
	public void boots() throws IOException {
		Item bootsOfSpeed = itemsResponse.getItems().get("1001");

		List<Item> boots = itemsRepository.boots(Integer.valueOf(SUMMONERS_RIFT));
		assertThat(boots).isNotEmpty();
		assertThat(boots).doesNotHaveDuplicates();
		assertThat(boots).doesNotContain(bootsOfSpeed);
		assertThat(boots).flatExtracting(Item::getFrom)
				.contains("1001");
		assertThat(boots).extracting(Item::getDescription).allSatisfy((Consumer<String>) description -> {
			assertThat(description).isNotNull();
			assertThat(description).contains("Movement");
		});
		assertThat(boots).extracting(Item::getMaps)
				.extracting((Extractor<SortedMap<Integer, Boolean>, Object>) input ->
						input.get(Integer.valueOf(SUMMONERS_RIFT)))
				.contains(true);
	}

	@Test
	public void trinkets() throws IOException {
		List<Item> trinkets = itemsRepository.trinkets(Integer.valueOf(SUMMONERS_RIFT));
		assertThat(trinkets).isNotEmpty();
		assertThat(trinkets).doesNotHaveDuplicates();
		assertThat(trinkets).extracting(Item::getGold).extracting(ItemGold::getTotal)
				.containsOnly(0);
		assertThat(trinkets).extracting(Item::getGold).extracting("purchasable", Boolean.class)
				.containsOnly(true);
		assertThat(trinkets).extracting(Item::getMaps)
				.extracting((Extractor<SortedMap<Integer, Boolean>, Object>) input ->
						input.get(Integer.valueOf(SUMMONERS_RIFT)))
				.contains(true);
		assertThat(trinkets).extracting(Item::getDescription).allSatisfy((Consumer<String>) description -> {
			assertThat(description).isNotNull();
			assertThat(description).contains("Trinket");
		});
	}

	@Test
	public void viktorOnly() throws IOException {
		List<Item> viktorOnlyItems = itemsRepository.viktorOnly();
		assertThat(viktorOnlyItems).isNotEmpty();
		assertThat(viktorOnlyItems).doesNotHaveDuplicates();
		assertThat(viktorOnlyItems).extracting(Item::getDescription).allSatisfy((Consumer<String>) description -> {
			assertThat(description).isNotNull();
			assertThat(description).contains("Viktor");
		});
		assertThat(viktorOnlyItems).extracting(Item::getRequiredChampion).allSatisfy((Consumer<String>) description -> {
			assertThat(description).isNotNull();
			assertThat(description).contains("Viktor");
		});
		assertThat(viktorOnlyItems).extracting(Item::getMaps)
				.extracting((Extractor<SortedMap<Integer, Boolean>, Object>) input ->
						input.get(Integer.valueOf(SUMMONERS_RIFT)))
				.contains(true);
	}

	@Test
	public void forTrollBuild() throws IOException {
		List<Item> forTrollBuild = itemsRepository.forTrollBuild(Integer.valueOf(SUMMONERS_RIFT));
		assertThat(forTrollBuild).isNotEmpty();
		assertThat(forTrollBuild).doesNotHaveDuplicates();
		assertThat(forTrollBuild).extracting(Item::getMaps)
				.extracting((Extractor<SortedMap<Integer, Boolean>, Object>) input ->
						input.get(Integer.valueOf(SUMMONERS_RIFT)))
				.contains(true);
		assertThat(forTrollBuild).extracting(Item::getGold)
				.extracting("purchasable", Boolean.class)
				.containsOnly(true);
		assertThat(forTrollBuild).extracting(Item::getConsumed)
				.containsNull();
		assertThat(forTrollBuild).flatExtracting(Item::getInto)
				.isEmpty();
		assertThat(forTrollBuild).doesNotContain(itemsResponse.getItems().get("1001"));
		assertThat(forTrollBuild).extracting(Item::getDescription).allSatisfy((Consumer<String>) description -> {
			assertThat(description).isNotNull();
			assertThat(description).doesNotContain("Movement");
			assertThat(description).doesNotContain("Potion");
			assertThat(description).doesNotContain("Trinket");
		});
		assertThat(forTrollBuild).extracting(Item::getName).allSatisfy((Consumer<String>) name -> {
			assertThat(name).isNotNull();
			assertThat(name).doesNotContain("Movement");
			assertThat(name).doesNotContain("Potion");
			assertThat(name).doesNotContain("Trinket");
			assertThat(name).doesNotContain("Viktor");
			assertThat(name).doesNotContain("Flask");
			assertThat(name).doesNotContain("Enchantment");
			assertThat(name).doesNotContain("Doran");
			assertThat(name).doesNotContain("Quick");
		});
		assertThat(forTrollBuild).extracting(Item::getGroup).allSatisfy((Consumer<String>) group -> {
			if (group != null) {
				assertThat(group).doesNotContain("FlaskGroup");
				assertThat(group).doesNotContain("RelicBase");
			}
		});
	}

}
