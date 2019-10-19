package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.ItemGold;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.test.repository.RepositoryTest;
import com.drumonii.loltrollbuild.util.GameMapUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.drumonii.loltrollbuild.util.GameMapUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
abstract class ItemsRepositoryTest {

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	private ItemsResponse itemsResponse;

	protected abstract ItemsResponse getItemsResponse();

	static Stream<Integer> maps() {
		return Stream.of(TWISTED_TREELINE_ID, SUMMONERS_RIFT_ID, HOWLING_ABYSS_ID);
	}

	@BeforeEach
	void beforeEach() {
		itemsResponse = getItemsResponse();
		itemsRepository.saveAll(itemsResponse.getItems().values());
	}

	@ParameterizedTest(name = "map id=''{0}''")
	@MethodSource("maps")
	void boots(Integer map) {
		Item bootsOfSpeed = itemsResponse.getItems().get("1001");

		List<Item> boots = itemsRepository.boots(map);
		assertThat(boots).as("Boots should be empty for map: " + GameMapUtil.getNameFromId(map) + "(" + map + ")")
				.isNotEmpty();
		assertThat(boots).doesNotHaveDuplicates();
		assertThat(boots).doesNotContain(bootsOfSpeed);
		assertThat(boots).flatExtracting(Item::getFrom)
				.contains(1001);
		assertThat(boots).extracting(Item::getDescription).allSatisfy(description -> {
			assertThat(description).isNotNull();
			assertThat(description).contains("Movement");
		});
		assertThat(boots).extracting(Item::getMaps)
				.extracting(input -> input.get(map))
				.containsOnly(true);
	}

	@ParameterizedTest(name = "map id=''{0}''")
	@MethodSource("maps")
	void trinkets(Integer map) {
		List<Item> trinkets = itemsRepository.trinkets(map);
		assertThat(trinkets).isNotEmpty();
		assertThat(trinkets).doesNotHaveDuplicates();
		if (map == TWISTED_TREELINE_ID) {
			assertThat(trinkets).extracting(Item::getName).containsOnly("Arcane Sweeper");
		} else if (map == HOWLING_ABYSS_ID) {
			assertThat(trinkets).extracting(Item::getName).containsOnly("Poro-Snax");
		} else {
			assertThat(trinkets).extracting(Item::getGold).extracting(ItemGold::getTotal)
					.containsOnly(0);
			assertThat(trinkets).extracting(Item::getGold).extracting("purchasable", Boolean.class)
					.containsOnly(true);
			assertThat(trinkets).extracting(Item::getTags).allSatisfy(tags -> {
				assertThat(tags).contains("Trinket");
			});
			assertThat(trinkets).extracting(Item::getMaps)
					.extracting(input -> input.get(map))
					.containsOnly(true);
			assertThat(trinkets).extracting(Item::getDescription).allSatisfy(description -> {
				assertThat(description).isNotNull();
				assertThat(description).contains("Trinket");
			});
		}
	}

	@Test
	void viktorOnly() {
		List<Item> viktorOnlyItems = itemsRepository.viktorOnly();
		assertThat(viktorOnlyItems).isNotEmpty();
		assertThat(viktorOnlyItems).doesNotHaveDuplicates();
		assertThat(viktorOnlyItems).extracting(Item::getDescription).allSatisfy(description -> {
			assertThat(description).isNotNull();
			assertThat(description).contains("Viktor");
		});
		assertThat(viktorOnlyItems).extracting(Item::getRequiredChampion).allSatisfy(description -> {
			assertThat(description).isNotNull();
			assertThat(description).contains("Viktor");
		});
		assertThat(viktorOnlyItems).extracting(Item::getMaps)
				.extracting(input -> input.get(SUMMONERS_RIFT_ID))
				.contains(true);
	}

	@ParameterizedTest(name = "map id=''{0}''")
	@MethodSource("maps")
	void forTrollBuild(Integer map) {
		List<Item> forTrollBuild = itemsRepository.forTrollBuild(map);
		assertThat(forTrollBuild).as("Items for Troll Build should not be empty for map: " + GameMapUtil.getNameFromId(map) + "(" + map + ")")
				.isNotEmpty();
		assertThat(forTrollBuild).doesNotHaveDuplicates();
		assertThat(forTrollBuild).extracting(Item::getMaps)
				.extracting(input -> input.get(map))
				.contains(true);
		assertThat(forTrollBuild).extracting(Item::getGold)
				.extracting("purchasable", Boolean.class)
				.containsOnly(true);
		assertThat(forTrollBuild).extracting(Item::getConsumed)
				.containsNull();
		assertThat(forTrollBuild).filteredOn(item -> item.getInto() != null).flatExtracting(Item::getInto)
				.isEmpty();
		assertThat(forTrollBuild).doesNotContain(itemsResponse.getItems().get("1001"));
		assertThat(forTrollBuild).extracting(Item::getDescription).allSatisfy(description -> {
			assertThat(description).isNotNull();
			assertThat(description).doesNotContain("Movement");
			assertThat(description).doesNotContain("Potion");
			assertThat(description).doesNotContain("Trinket");
		});
		assertThat(forTrollBuild).extracting(Item::getRequiredChampion).allSatisfy(requiredChampion -> {
			assertThat(requiredChampion).isNull();
		});
		assertThat(forTrollBuild).extracting(Item::getRequiredAlly).allSatisfy(requiredAlly -> {
			assertThat(requiredAlly).isNull();
		});
		assertThat(forTrollBuild).extracting(Item::getName).allSatisfy(name -> {
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
		assertThat(forTrollBuild).extracting(Item::getGroup)
				.filteredOn(Objects::nonNull)
				.allSatisfy(group -> {
					assertThat(group).doesNotContain("FlaskGroup");
					assertThat(group).doesNotContain("RelicBase");
				});
	}

}
