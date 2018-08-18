package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.ItemGold;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.test.repository.RepositoryTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.iterable.Extractor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Objects;
import java.util.SortedMap;
import java.util.function.Consumer;

import static com.drumonii.loltrollbuild.util.GameMapUtil.SUMMONERS_RIFT_ID;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@RepositoryTest
public abstract class ItemsRepositoryTest {

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	private ItemsResponse itemsResponse;

	protected abstract ItemsResponse getItemsResponse();

	@Before
	public void before() {
		itemsResponse = getItemsResponse();
		itemsRepository.saveAll(itemsResponse.getItems().values());
	}

	@Test
	public void boots() {
		Item bootsOfSpeed = itemsResponse.getItems().get("1001");

		List<Item> boots = itemsRepository.boots(SUMMONERS_RIFT_ID);
		assertThat(boots).isNotEmpty();
		assertThat(boots).doesNotHaveDuplicates();
		assertThat(boots).doesNotContain(bootsOfSpeed);
		assertThat(boots).flatExtracting(Item::getFrom)
				.contains(1001);
		assertThat(boots).extracting(Item::getDescription).allSatisfy((Consumer<String>) description -> {
			assertThat(description).isNotNull();
			assertThat(description).contains("Movement");
		});
		assertThat(boots).extracting(Item::getMaps)
				.extracting((Extractor<SortedMap<Integer, Boolean>, Object>) input -> input.get(SUMMONERS_RIFT_ID))
				.containsOnly(true);
	}

	@Test
	public void trinkets() {
		List<Item> trinkets = itemsRepository.trinkets(SUMMONERS_RIFT_ID);
		assertThat(trinkets).isNotEmpty();
		assertThat(trinkets).doesNotHaveDuplicates();
		assertThat(trinkets).extracting(Item::getGold).extracting(ItemGold::getTotal)
				.containsOnly(0);
		assertThat(trinkets).extracting(Item::getGold).extracting("purchasable", Boolean.class)
				.containsOnly(true);
		assertThat(trinkets).extracting(Item::getMaps)
				.extracting((Extractor<SortedMap<Integer, Boolean>, Object>) input -> input.get(SUMMONERS_RIFT_ID))
				.containsOnly(true);
		assertThat(trinkets).extracting(Item::getDescription).allSatisfy((Consumer<String>) description -> {
			assertThat(description).isNotNull();
			assertThat(description).contains("Trinket");
		});
	}

	@Test
	public void viktorOnly() {
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
				.extracting((Extractor<SortedMap<Integer, Boolean>, Object>) input -> input.get(SUMMONERS_RIFT_ID))
				.contains(true);
	}

	@Test
	public void forTrollBuild() {
		List<Item> forTrollBuild = itemsRepository.forTrollBuild(SUMMONERS_RIFT_ID);
		assertThat(forTrollBuild).isNotEmpty();
		assertThat(forTrollBuild).doesNotHaveDuplicates();
		assertThat(forTrollBuild).extracting(Item::getMaps)
				.extracting((Extractor<SortedMap<Integer, Boolean>, Object>) input -> input.get(SUMMONERS_RIFT_ID))
				.contains(true);
		assertThat(forTrollBuild).extracting(Item::getGold)
				.extracting("purchasable", Boolean.class)
				.containsOnly(true);
		assertThat(forTrollBuild).extracting(Item::getConsumed)
				.containsNull();
		assertThat(forTrollBuild).filteredOn(item -> item.getInto() != null).flatExtracting(Item::getInto)
				.isEmpty();
		assertThat(forTrollBuild).doesNotContain(itemsResponse.getItems().get("1001"));
		assertThat(forTrollBuild).extracting(Item::getDescription).allSatisfy((Consumer<String>) description -> {
			assertThat(description).isNotNull();
			assertThat(description).doesNotContain("Movement");
			assertThat(description).doesNotContain("Potion");
			assertThat(description).doesNotContain("Trinket");
		});
		assertThat(forTrollBuild).extracting(Item::getRequiredChampion).allSatisfy((Consumer<String>) requiredChampion -> {
			assertThat(requiredChampion).isNull();
		});
		assertThat(forTrollBuild).extracting(Item::getRequiredAlly).allSatisfy((Consumer<String>) requiredAlly -> {
			assertThat(requiredAlly).isNull();
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
		assertThat(forTrollBuild).extracting(Item::getGroup).filteredOn(Objects::nonNull)
				.allSatisfy((Consumer<String>) group -> {
					assertThat(group).doesNotContain("FlaskGroup");
					assertThat(group).doesNotContain("RelicBase");
		});
	}

}
