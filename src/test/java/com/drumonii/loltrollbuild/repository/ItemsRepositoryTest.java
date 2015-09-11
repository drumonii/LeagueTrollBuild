package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.ItemGold;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
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

	@Test
	public void trinkets() throws IOException {
		String responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"3345\":{\"id\":3345,\"name\":" +
				"\"Soul Anchor (Trinket)\",\"group\":\"RelicBase\",\"description\":\"<groupLimit>Limited to 1 " +
				"Trinket.</groupLimit><br><br><unique>Active:</unique> Consumes a charge to instantly revive at your " +
				"Summoner Platform and grants 125% Movement Speed that decays over 12 seconds.<br><br><i>Additional " +
				"charges are gained at levels 9 and 14.</i><br><br><font color='#BBFFFF'>(Max: 2 charges)</font></i>" +
				"<br><br>\",\"plaintext\":\"Consumes charge to revive champion.\",\"image\":{\"full\":\"3345.png\"," +
				"\"sprite\":\"item2.png\",\"group\":\"item\",\"x\":432,\"y\":0,\"w\":48,\"h\":48},\"gold\":{\"base\":" +
				"0,\"total\":0,\"sell\":0,\"purchasable\":false}}}}";
		Item soulAnchor = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3345");
		itemsRepository.save(soulAnchor);

		responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"3340\":{\"id\":3340,\"name\":" +
				"\"Warding Totem (Trinket)\",\"group\":\"RelicBase\",\"description\":\"<groupLimit>Limited to 1 " +
				"Trinket.</groupLimit><br><br><unique>Active:</unique> Places a <font color='#BBFFFF'>Stealth Ward" +
				"</font> that lasts 60 seconds (120 second cooldown).<br><br>At level 9, this ward's duration " +
				"increases to 120 seconds.<br><br>Limit 3 <font color='#BBFFFF'>Stealth Wards</font> on the map per " +
				"player.<br><br><i>(Trinkets cannot be used in the first 30 seconds of a game. Selling a Trinket " +
				"will disable Trinket use for 120 seconds).</i>\",\"plaintext\":\"Periodically place a Stealth " +
				"Ward\",\"into\":[\"3361\",\"3362\"],\"image\":{\"full\":\"3340.png\",\"sprite\":\"item2.png\"," +
				"\"group\":\"item\",\"x\":288,\"y\":0,\"w\":48,\"h\":48},\"gold\":{\"base\":0,\"total\":0,\"sell\":0," +
				"\"purchasable\":true}}}}";
		Item wardingTotem = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3340");
		itemsRepository.save(wardingTotem);

		responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"3364\":{\"id\":3364,\"name\":" +
				"\"Oracle's Lens (Trinket)\",\"group\":\"RelicBase\",\"description\":\"<groupLimit>Limited to 1 " +
				"Trinket.</groupLimit><levelLimit> *Level 9+ required to upgrade.</levelLimit><stats></stats><br>" +
				"<br><unique>UNIQUE Active:</unique> Reveals and disables nearby invisible traps and invisible wards " +
				"for 6 seconds in a medium radius and grants detection of nearby invisible units for 10 seconds " +
				"(75 second cooldown).<br><br><i>(Trinkets cannot be used in the first 30 seconds of a game. Selling " +
				"a Trinket will disable Trinket use for 120 seconds).</i>\",\"plaintext\":\"Disables nearby " +
				"invisible wards and trap and grants true sight briefly\",\"from\":[\"3341\"],\"image\":{\"full\":" +
				"\"3364.png\",\"sprite\":\"item2.png\",\"group\":\"item\",\"x\":144,\"y\":48,\"w\":48,\"h\":48}," +
				"\"gold\":{\"base\":250,\"total\":250,\"sell\":175,\"purchasable\":true}}}}";
		Item oraclesLens = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3364");
		itemsRepository.save(oraclesLens);

		List<Item> trinkets = itemsRepository.trinkets();
		assertThat(trinkets).isNotEmpty();
		assertThat(trinkets).extracting(Item::getGold).extracting(ItemGold::getTotal)
				.containsOnly(0);
		assertThat(trinkets).extracting(Item::getGold).extracting("purchasable", Boolean.class)
				.containsOnly(true);
		assertThat(trinkets).extracting(Item::getMaps)
				.extracting("1").containsNull();
		assertThat(trinkets).extracting(Item::getName)
				.have(new Condition<>(name -> name.contains("Trinket"), "Trinket"));
	}

	@Test
	public void viktorOnly() throws IOException {
		String responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"3196\":{\"id\":3196,\"name\":" +
				"\"The Hex Core mk-1\",\"description\":\"<stats>+3 Ability Power per level<br>+15 Mana per level" +
				"</stats><br><br><passive>UNIQUE Passive - Progress:</passive> Viktor can upgrade one of his basic " +
				"spells.\",\"plaintext\":\"Allows Viktor to improve an ability of his choice\",\"from\":[\"3200\"]," +
				"\"into\":[\"3197\"],\"image\":{\"full\":\"3196.png\",\"sprite\":\"item1.png\",\"group\":\"item\"," +
				"\"x\":48,\"y\":288,\"w\":48,\"h\":48},\"gold\":{\"base\":1000,\"total\":1000,\"sell\":700," +
				"\"purchasable\":true}}}}";
		Item hexCoreMk1 = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3196");
		itemsRepository.save(hexCoreMk1);

		responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"3198\":{\"id\":3198,\"name\":" +
				"\"Perfect Hex Core\",\"description\":\"<stats>+10 Ability Power per level<br>+25 Mana per level" +
				"</stats><br><br><passive>UNIQUE Passive - Glorious Evolution:</passive> Viktor has reached the " +
				"pinnacle of his power, upgrading Chaos Storm in addition to his basic spells.\",\"plaintext\":" +
				"\"Allows Viktor to improve an ability of his choice\",\"from\":[\"3197\"],\"image\":{\"full\":" +
				"\"3198.png\",\"sprite\":\"item1.png\",\"group\":\"item\",\"x\":144,\"y\":288,\"w\":48,\"h\":48}," +
				"\"gold\":{\"base\":1000,\"total\":3000,\"sell\":2100,\"purchasable\":true}}}}";
		Item perfectHexCore = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3198");
		itemsRepository.save(perfectHexCore);

		responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"3140\":{\"id\":3140,\"name\":" +
				"\"Quicksilver Sash\",\"description\":\"<stats>+30 Magic Resist</stats><br><br><active>UNIQUE Active " +
				"- Quicksilver:</active> Removes all debuffs (90 second cooldown).\",\"plaintext\":\"Activate to " +
				"remove all debuffs\",\"from\":[\"1033\"],\"into\":[\"3139\",\"3137\"],\"image\":{\"full\":" +
				"\"3140.png\",\"sprite\":\"item1.png\",\"group\":\"item\",\"x\":384,\"y\":96,\"w\":48,\"h\":48}," +
				"\"gold\":{\"base\":800,\"total\":1250,\"sell\":875,\"purchasable\":true}}}}";
		Item quickSilverFlash = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3140");
		itemsRepository.save(quickSilverFlash);

		responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"3135\":{\"id\":3135,\"name\":" +
				"\"Void Staff\",\"description\":\"<stats>+80 Ability Power</stats><br><br><unique>UNIQUE Passive:" +
				"</unique> Magic damage ignores 35% of the target's Magic Resist (applies before Magic Penetration)." +
				"\",\"plaintext\":\"Increases magic damage\",\"from\":[\"1026\",\"1052\"],\"image\":{\"full\":" +
				"\"3135.png\",\"sprite\":\"item1.png\",\"group\":\"item\",\"x\":192,\"y\":96,\"w\":48,\"h\":48}," +
				"\"gold\":{\"base\":1215,\"total\":2500,\"sell\":1750,\"purchasable\":true}}}}";
		Item voidStaff = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3135");
		itemsRepository.save(voidStaff);

		List<Item> viktorOnlyItems = itemsRepository.viktorOnly();
		assertThat(viktorOnlyItems).isNotEmpty();
		assertThat(viktorOnlyItems).extracting(Item::getName)
				.have(new Condition<>(name -> name.contains("Hex"), "Hex"));
		assertThat(viktorOnlyItems).extracting(Item::getDescription)
				.have(new Condition<>(name -> name.contains("Viktor"), "Viktor"));
	}

}
