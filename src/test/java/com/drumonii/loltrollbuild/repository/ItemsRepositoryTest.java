package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Condition;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemsRepositoryTest extends BaseSpringTestRunner {

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@After
	public void after() {
		itemsRepository.deleteAll();
	}

	@Test
	public void crudOperations() throws IOException {
		String responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"10001\":{\"id\":10001,\"name\":" +
				"\"Test Item\",\"group\":\"TestItems\",\"description\":\"Really concise item description\"," +
				"\"plaintext\":\"Ultra awesome test item bro\",\"from\":[\"9998\", \"9999\"],\"into\":[\"10002\"," +
				"\"10003\"],\"image\":{\"full\":\"10001.png\",\"sprite\":\"item0.png\",\"group\":\"item\",\"x\":432," +
				"\"y\":192,\"w\":48,\"h\":48},\"gold\":{\"base\":150,\"total\":1500,\"sell\":875,\"purchasable\":" +
				"true}}}}";
		ItemsResponse itemsResponse = objectMapper.readValue(responseBody, ItemsResponse.class);

		Item unmarshalledItem = itemsResponse.getItems().get("10001");

		// Create
		itemsRepository.save(unmarshalledItem);

		// Select
		Item itemFromDb = itemsRepository.findOne(10001);
		assertThat(itemFromDb).isNotNull();
		itemFromDb.setMaps(null); // It's empty and not null from the db
		assertThat(itemFromDb).isEqualToIgnoringGivenFields(unmarshalledItem, "from", "gold");

		// Update
		itemFromDb.setConsumed(true);
		itemsRepository.save(itemFromDb);
		itemFromDb = itemsRepository.findOne(10001);
		assertThat(itemFromDb.getConsumed()).isTrue();

		// Delete
		itemsRepository.delete(itemFromDb);
		assertThat(itemsRepository.findOne(10001)).isNull();
	}

	@Test
	public void items() throws IOException {
		String responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"1001\":{\"id\":1001,\"name\":" +
				"\"Boots of Speed\",\"group\":\"BootsNormal\",\"description\":\"<groupLimit>Limited to 1." +
				"</groupLimit><br><br><unique>UNIQUE Passive - Enhanced Movement:</unique> +25 Movement Speed<br><br>" +
				"<i>(Unique Passives with the same name don't stack.)</i>\",\"plaintext\":\"Slightly increases " +
				"Movement Speed\",\"into\":[\"3006\",\"3047\",\"3020\",\"3158\",\"3111\",\"3117\",\"3009\"]," +
				"\"image\":{\"full\":\"1001.png\",\"sprite\":\"item0.png\",\"group\":\"item\",\"x\":0,\"y\":0,\"w\":" +
				"48,\"h\":48},\"gold\":{\"base\":325,\"total\":325,\"sell\":227,\"purchasable\":true}}}}";
		Item bootsOfSpeed = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("1001");
		itemsRepository.save(bootsOfSpeed);

		responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"3111\":{\"id\":3111,\"name\":" +
				"\"Mercury's Treads\",\"description\":\"<stats>+25 Magic Resist</stats><br><br><unique>UNIQUE " +
				"Passive - Enhanced Movement:</unique> +45 Movement Speed<br><unique>UNIQUE Passive - Tenacity:" +
				"</unique> Reduces the duration of stuns, slows, taunts, fears, silences, blinds, polymorphs, and " +
				"immobilizes by 35%.<br><br><i>(Unique Passives with the same name don't stack.)</i>\",\"plaintext\":" +
				"\"Increases Movement Speed and reduces duration of disabling effects\",\"from\":[\"1001\",\"1033\"]," +
				"\"into\":[\"1321\",\"1323\",\"1320\",\"1322\",\"1324\",\"1339\"],\"image\":{\"full\":\"3111.png\"," +
				"\"sprite\":\"item1.png\",\"group\":\"item\",\"x\":192,\"y\":48,\"w\":48,\"h\":48},\"gold\":" +
				"{\"base\":425,\"total\":1200,\"sell\":840,\"purchasable\":true}}}}";
		Item mercTreads = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3111");
		itemsRepository.save(mercTreads);

		responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"1319\":{\"id\":1319,\"name\":" +
				"\"Enchantment: Homeguard\",\"group\":\"BootsHomeguard\",\"description\":\"<groupLimit>Limited to 1 " +
				"of each enchantment type.</groupLimit><br>Enchants boots to have Homeguard bonus.<br><br><unique>" +
				"UNIQUE Passive - Homeguard:</unique> Visiting the shop vastly increases Health and Mana " +
				"Regeneration and grants 200% bonus Movement Speed that decays over 8 seconds. Bonus Movement Speed " +
				"and regeneration are disabled for 6 seconds upon dealing or taking damage.<br><br><i>(Unique " +
				"Passives with the same name don't stack.)</i>\",\"from\":[\"3047\"],\"image\":{\"full\":" +
				"\"1319.png\",\"sprite\":\"item2.png\",\"group\":\"item\",\"x\":432,\"y\":384,\"w\":48,\"h\":48}," +
				"\"gold\":{\"base\":475,\"total\":1475,\"sell\":1033,\"purchasable\":true}}}}";
		Item homeguard = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("1319");
		itemsRepository.save(homeguard);

		responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"3047\":{\"id\":3047,\"name\":" +
				"\"Ninja Tabi\",\"description\":\"<stats>+25 Armor</stats><br><br><unique>UNIQUE Passive:</unique> " +
				"Blocks 10% of the damage from basic attacks.<br><unique>UNIQUE Passive - Enhanced Movement:" +
				"</unique> +45 Movement Speed<br><br><i>(Unique Passives with the same name don't stack.)</i>\"," +
				"\"plaintext\":\"Enhances Movement Speed and reduces incoming basic attack damage\",\"from\":" +
				"[\"1001\",\"1029\"],\"into\":[\"1316\",\"1318\",\"1315\",\"1317\",\"1319\",\"1338\"],\"image\":" +
				"{\"full\":\"3047.png\",\"sprite\":\"item0.png\",\"group\":\"item\",\"x\":192,\"y\":336,\"w\":48," +
				"\"h\":48},\"gold\":{\"base\":375,\"total\":1000,\"sell\":700,\"purchasable\":true}}}}";
		Item ninjaTabi = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3047");
		itemsRepository.save(ninjaTabi);

		List<Item> boots = itemsRepository.boots();
		assertThat(boots).isNotEmpty();
		assertThat(boots).flatExtracting(Item::getFrom)
				.contains("1001");
		assertThat(boots).extracting(Item::getName)
				.doesNotHave(new Condition<>(name -> name.contains("Enchantment"), "non enchanted"));
		assertThat(boots).extracting(Item::getDescription)
				.have(new Condition<>(descr -> descr.contains("Enhanced Movement"), "movement"));
	}

}
