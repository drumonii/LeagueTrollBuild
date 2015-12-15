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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.Sort.Direction.ASC;

public class ItemsRepositoryTest extends BaseSpringTestRunner {

	private static final String SUMMONERS_RIFT = "11";

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
		assertThat(itemsRepository.save(unmarshalledItem)).isNotNull();

		// Select
		Item itemFromDb = itemsRepository.findOne(10001);
		assertThat(itemFromDb).isNotNull();
		assertThat(itemFromDb.getImage()).isNotNull();
		itemFromDb.setMaps(null); // It's empty/not null from the db
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
	public void boots() throws IOException {
		String responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"1001\":{\"id\":1001,\"name\":" +
				"\"Boots of Speed\",\"group\":\"BootsNormal\",\"description\":\"<groupLimit>Limited to 1." +
				"</groupLimit><br><br><unique>UNIQUE Passive - Enhanced Movement:</unique> +25 Movement Speed\"," +
				"\"plaintext\":\"Slightly increases Movement Speed\",\"into\":[\"3006\",\"3047\",\"3020\",\"3158\"," +
				"\"3111\",\"3117\",\"3009\"],\"maps\":{\"1\":false,\"8\":true,\"10\":true,\"11\":true,\"12\":true," +
				"\"14\":false},\"image\":{\"full\":\"1001.png\",\"sprite\":\"item0.png\",\"group\":\"item\",\"x\":0," +
				"\"y\":0,\"w\":48,\"h\":48},\"gold\":{\"base\":300,\"total\":300,\"sell\":210,\"purchasable\":true}}}}";
		Item bootsOfSpeed = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("1001");
		itemsRepository.save(bootsOfSpeed);

		responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"3111\":{\"id\":3111,\"name\":" +
				"\"Mercury's Treads\",\"description\":\"<stats>+25 Magic Resist</stats><br><br><unique>UNIQUE Passive" +
				" - Enhanced Movement:</unique> +45 Movement Speed<br><unique>UNIQUE Passive - Tenacity:</unique> " +
				"Reduces the duration of stuns, slows, taunts, fears, silences, blinds, polymorphs, and immobilizes " +
				"by 20%.\",\"plaintext\":\"Increases Movement Speed and reduces duration of disabling effects\"," +
				"\"from\":[\"1001\",\"1033\"],\"into\":[\"1321\",\"1323\",\"1320\",\"1322\",\"1324\"],\"maps\":" +
				"{\"1\":false,\"8\":true,\"10\":true,\"11\":true,\"12\":true,\"14\":false},\"image\":{\"full\":" +
				"\"3111.png\",\"sprite\":\"item1.png\",\"group\":\"item\",\"x\":192,\"y\":48,\"w\":48,\"h\":48}," +
				"\"gold\":{\"base\":350,\"total\":1100,\"sell\":770,\"purchasable\":true}}}}";
		Item mercTreads = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3111");
		itemsRepository.save(mercTreads);

		responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"1319\":{\"id\":1319,\"name\":" +
				"\"Enchantment: Homeguard\",\"group\":\"BootsHomeguard\",\"description\":\"<groupLimit>Limited to 1 " +
				"of each enchantment type.</groupLimit><br>Enchants boots to have Homeguard bonus.<br><br><unique>" +
				"UNIQUE Passive - Homeguard:</unique> Visiting the shop vastly increases Health and Mana Regeneration" +
				" and grants 200% bonus Movement Speed that decays over 8 seconds. Bonus Movement Speed and " +
				"regeneration are disabled for 6 seconds upon dealing or taking damage.<br><br><rules>(Unique " +
				"Passives with the same name don't stack.)</rules>\",\"plaintext\":\"Fully restores health, mana, and" +
				" grants a massive speed boost while visiting the shop\",\"from\":[\"3047\"],\"maps\":{\"1\":false," +
				"\"8\":false,\"10\":false,\"11\":true,\"12\":false,\"14\":false},\"image\":{\"full\":\"1319.png\"," +
				"\"sprite\":\"item2.png\",\"group\":\"item\",\"x\":432,\"y\":384,\"w\":48,\"h\":48},\"gold\":" +
				"{\"base\":450,\"total\":1550,\"sell\":1085,\"purchasable\":true}}}}";
		Item homeguardEnchantment = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("1319");
		itemsRepository.save(homeguardEnchantment);

		responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"3047\":{\"id\":3047,\"name\":" +
				"\"Ninja Tabi\",\"description\":\"<stats>+30 Armor</stats><br><br><unique>UNIQUE Passive:</unique> " +
				"Blocks 10% of the damage from basic attacks.<br><unique>UNIQUE Passive - Enhanced Movement:</unique>" +
				" +45 Movement Speed\",\"plaintext\":\"Enhances Movement Speed and reduces incoming basic attack " +
				"damage\",\"from\":[\"1001\",\"1029\"],\"into\":[\"1316\",\"1318\",\"1315\",\"1317\",\"1319\"]," +
				"\"maps\":{\"1\":false,\"8\":true,\"10\":true,\"11\":true,\"12\":true,\"14\":false},\"image\":" +
				"{\"full\":\"3047.png\",\"sprite\":\"item0.png\",\"group\":\"item\",\"x\":192,\"y\":336,\"w\":48," +
				"\"h\":48},\"gold\":{\"base\":500,\"total\":1100,\"sell\":770,\"purchasable\":true}}}}";
		Item ninjaTabi = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3047");
		itemsRepository.save(ninjaTabi);

		List<Item> boots = itemsRepository.boots(SUMMONERS_RIFT);
		assertThat(boots).isNotEmpty();
		assertThat(boots).flatExtracting(Item::getFrom)
				.contains("1001");
		assertThat(boots).extracting(Item::getName)
				.doesNotHave(new Condition<>(name -> name.contains("Enchantment"), "non enchanted"));
		assertThat(boots).extracting(Item::getDescription)
				.have(new Condition<>(descr -> descr.contains("Enhanced Movement"), "movement"));
		assertThat(boots).extracting(Item::getMaps)
				.extracting(SUMMONERS_RIFT).contains(true);
	}

	@Test
	public void trinkets() throws IOException {
		String responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"3345\":{\"id\":3345,\"name\":" +
				"\"Soul Anchor (Trinket)\",\"group\":\"RelicBase\",\"description\":\"<groupLimit>Limited to 1 " +
				"Trinket.</groupLimit><br><br><active>Active:</active> Consumes a charge to instantly revive at your " +
				"Summoner Platform and grants 125% Movement Speed that decays over 12 seconds.<br><br><rules>" +
				"Additional charges are gained at levels 9 and 14.</rules><br><br><font color='#BBFFFF'>(Max: 2 " +
				"charges)</font></rules><br><br>\",\"plaintext\":\"Consumes charge to revive champion.\",\"maps\":" +
				"{\"1\":false,\"8\":true,\"10\":false,\"11\":false,\"12\":false,\"14\":false},\"image\":{\"full\":" +
				"\"3345.png\",\"sprite\":\"item2.png\",\"group\":\"item\",\"x\":432,\"y\":0,\"w\":48,\"h\":48}," +
				"\"gold\":{\"base\":0,\"total\":0,\"sell\":0,\"purchasable\":false}}}}";
		Item soulAnchor = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3345");
		itemsRepository.save(soulAnchor);

		responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"3340\":{\"id\":3340,\"name\":\"Warding " +
				"Totem (Trinket)\",\"group\":\"RelicBase\",\"description\":\"<groupLimit>Limited to 1 Trinket." +
				"</groupLimit><br><br><active>Active:</active> Consume a charge to place an invisible <font color=" +
				"'#BBFFFF'>Stealth Ward</font> which reveals the surrounding area for <levelScale>60 - 120" +
				"</levelScale> seconds. <br><br>Stores one charge every <levelScale>180 - 120</levelScale> seconds, " +
				"up to 2 maximum charges.<br><br>Ward duration and recharge time gradually improve with level.<br>" +
				"<br><rules>(Limit 3 <font color='#BBFFFF'>Stealth Wards</font> on the map per player. Switching to " +
				"a <font color='#BBFFFF'>Lens</font> type trinket will disable <font color='#BBFFFF'>Trinket</font> " +
				"use for 120 seconds.)</rules><br><br>\",\"plaintext\":\"Periodically place a Stealth Ward\"," +
				"\"maps\":{\"1\":false,\"8\":false,\"10\":false,\"11\":true,\"12\":false,\"14\":false},\"image\":" +
				"{\"full\":\"3340.png\",\"sprite\":\"item2.png\",\"group\":\"item\",\"x\":288,\"y\":0,\"w\":48,\"h\":" +
				"48},\"gold\":{\"base\":0,\"total\":0,\"sell\":0,\"purchasable\":true}}}}";
		Item wardingTotem = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3340");
		itemsRepository.save(wardingTotem);

		responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"3364\":{\"id\":3364,\"name\":" +
				"\"Oracle Alteration\",\"group\":\"RelicBase\",\"description\":\"<levelLimit>* Level 9+ required to " +
				"upgrade.</levelLimit><stats></stats><br><br>Alters the <font color='#FFFFFF'>Sweeping Lens</font> " +
				"Trinket:<br><br><stats><font color='#00FF00'>+</font> Increased detection radius<br><font color=" +
				"'#00FF00'>+</font> Sweeping effect follows you for 10 seconds<br><font color='#FF0000'>-</font> " +
				"<font color='#FF6600'>Cast range reduced to zero</font></stats>\",\"plaintext\":\"Disables nearby " +
				"invisible wards and trap for a duration\",\"maps\":{\"1\":false,\"8\":false,\"10\":false,\"11\":" +
				"true,\"12\":true,\"14\":false},\"image\":{\"full\":\"3364.png\",\"sprite\":\"item2.png\",\"group\":" +
				"\"item\",\"x\":144,\"y\":48,\"w\":48,\"h\":48},\"gold\":{\"base\":0,\"total\":0,\"sell\":0," +
				"\"purchasable\":true}}}}";
		Item oraclesLens = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3364");
		itemsRepository.save(oraclesLens);

		List<Item> trinkets = itemsRepository.trinkets(SUMMONERS_RIFT);
		assertThat(trinkets).isNotEmpty();
		assertThat(trinkets).extracting(Item::getGold).extracting(ItemGold::getTotal)
				.containsOnly(0);
		assertThat(trinkets).extracting(Item::getGold).extracting("purchasable", Boolean.class)
				.containsOnly(true);
		assertThat(trinkets).extracting(Item::getMaps)
				.extracting(SUMMONERS_RIFT).contains(true);
		assertThat(trinkets).extracting(Item::getName)
				.have(new Condition<>(name -> name.contains("Trinket"), "Trinket"));
	}

	@Test
	public void viktorOnly() throws IOException {
		String responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"3196\":{\"id\":3196,\"name\":" +
				"\"The Hex Core mk-1\",\"description\":\"<stats>+3 Ability Power per level<br>+15 Mana per level" +
				"</stats><br><br><unique>UNIQUE Passive - Progress:</unique> Viktor can upgrade one of his basic " +
				"spells.\",\"plaintext\":\"Allows Viktor to improve an ability of his choice\",\"from\":[\"3200\"]," +
				"\"into\":[\"3197\"],\"requiredChampion\":\"Viktor\",\"maps\":{\"1\":false,\"8\":true,\"10\":true," +
				"\"11\":true,\"12\":true,\"14\":false},\"image\":{\"full\":\"3196.png\",\"sprite\":\"item1.png\"," +
				"\"group\":\"item\",\"x\":48,\"y\":288,\"w\":48,\"h\":48},\"gold\":{\"base\":1000,\"total\":1000," +
				"\"sell\":700,\"purchasable\":true}}}}";
		Item hexCoreMk1 = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3196");
		itemsRepository.save(hexCoreMk1);

		responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"3135\":{\"id\":3135,\"name\":" +
				"\"Void Staff\",\"description\":\"<stats>+80 Ability Power</stats><br><br><unique>UNIQUE Passive:" +
				"</unique> +35% <a href='TotalMagicPen'>Magic Penetration</a>.\",\"plaintext\":\"Increases magic " +
				"damage\",\"from\":[\"1026\",\"1052\"],\"maps\":{\"1\":false,\"8\":true,\"10\":true,\"11\":true," +
				"\"12\":true,\"14\":false},\"image\":{\"full\":\"3135.png\",\"sprite\":\"item1.png\",\"group\":" +
				"\"item\",\"x\":192,\"y\":96,\"w\":48,\"h\":48},\"gold\":{\"base\":1365,\"total\":2650,\"sell\":" +
				"1855,\"purchasable\":true}}}}";
		Item voidStaff = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3135");
		itemsRepository.save(voidStaff);

		List<Item> viktorOnlyItems = itemsRepository.viktorOnly();
		assertThat(viktorOnlyItems).isNotEmpty();
		assertThat(viktorOnlyItems).extracting(Item::getDescription)
				.have(new Condition<>(name -> name.contains("Viktor"), "Viktor"));
		assertThat(viktorOnlyItems).extracting(Item::getRequiredChampion)
				.have(new Condition<>(name -> name.equals("Viktor"), "Viktor"));
		assertThat(viktorOnlyItems).extracting(Item::getMaps)
				.extracting(SUMMONERS_RIFT).contains(true);
	}

	@Test
	public void forTrollBuild() throws IOException {
		String responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"1062\":{\"id\":1062,\"name\":" +
				"\"Prospector's Blade\",\"description\":\"<stats>+16 Attack Damage<br>+15% Attack Speed </stats><br>" +
				"<br><unique>UNIQUE Passive - Prospector:</unique> +150 Health<br><br><i>(Unique Passives with the " +
				"same name don't stack.)</i>\",\"plaintext\":\"Good starting item for attackers\",\"maps\":{\"1\":" +
				"false,\"8\":true,\"10\":false,\"11\":false,\"12\":false,\"14\":false},\"image\":{\"full\":" +
				"\"1062.png\",\"sprite\":\"item0.png\",\"group\":\"item\",\"x\":240,\"y\":96,\"w\":48,\"h\":48}," +
				"\"gold\":{\"base\":950,\"total\":950,\"sell\":380,\"purchasable\":true}}}}";
		Item prospectorsBlade = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("1062");
		itemsRepository.save(prospectorsBlade);

		responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"2010\":{\"id\":2010,\"name\":" +
				"\"Total Biscuit of Rejuvenation\",\"group\":\"HealthPotion\",\"description\":\"<consumable>Click to " +
				"Consume:</consumable> Restores 20 health and 10 mana immediately and then 150 Health over 15 " +
				"seconds.\",\"consumed\":true,\"maps\":{\"1\":false,\"8\":true,\"10\":true,\"11\":true,\"12\":true," +
				"\"14\":false},\"image\":{\"full\":\"2010.png\",\"sprite\":\"item0.png\",\"group\":\"item\",\"x\":" +
				"144,\"y\":144,\"w\":48,\"h\":48},\"gold\":{\"base\":35,\"total\":35,\"sell\":14,\"purchasable\":" +
				"false}}}}";
		Item biscuitOfRejuvenation = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("2010");
		itemsRepository.save(biscuitOfRejuvenation);

		responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"3117\":{\"id\":3117,\"name\":" +
				"\"Boots of Mobility\",\"description\":\"<unique>UNIQUE Passive - Enhanced Movement:</unique> +25 " +
				"Movement Speed. Increases to +105 Movement Speed when out of combat for 5 seconds.<br><br><i>(" +
				"Unique Passives with the same name don't stack.)</i>\",\"plaintext\":\"Greatly enhances Movement " +
				"Speed when out of combat\",\"from\":[\"1001\"],\"into\":[\"1326\",\"1328\",\"1325\",\"1327\"," +
				"\"1329\"],\"maps\":{\"1\":false,\"8\":true,\"10\":true,\"11\":true,\"12\":true,\"14\":false}," +
				"\"image\":{\"full\":\"3117.png\",\"sprite\":\"item1.png\",\"group\":\"item\",\"x\":0,\"y\":96,\"w\":" +
				"48,\"h\":48},\"gold\":{\"base\":475,\"total\":800,\"sell\":560,\"purchasable\":true}}}}";
		Item bootsOfMobility = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3117");
		itemsRepository.save(bootsOfMobility);

		responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"3341\":{\"id\":3341,\"name\":" +
				"\"Sweeping Lens (Trinket)\",\"group\":\"RelicBase\",\"description\":\"<groupLimit>Limited to 1 " +
				"Trinket.</groupLimit><br><br><unique>Active:</unique> Reveals and disables nearby invisible traps " +
				"and invisible wards for 6 seconds in a small radius (120 second cooldown).<br><br>At level 9, cast " +
				"range and sweep radius increase by 50% each and the cooldown is reduced to 75 seconds.<br><br><i>" +
				"(Trinkets cannot be used in the first 30 seconds of a game. Selling a Trinket will disable Trinket " +
				"use for 120 seconds).</i>\",\"plaintext\":\"Detects and disables nearby invisible wards and traps\"," +
				"\"into\":[\"3364\"],\"maps\":{\"1\":false,\"8\":false,\"10\":false,\"11\":true,\"12\":true," +
				"\"14\":false},\"image\":{\"full\":\"3341.png\",\"sprite\":\"item2.png\",\"group\":\"item\"," +
				"\"x\":336,\"y\":0,\"w\":48,\"h\":48},\"gold\":{\"base\":0,\"total\":0,\"sell\":0,\"purchasable\":" +
				"true}}}}";
		Item sweepingLens = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3341");
		itemsRepository.save(sweepingLens);

		responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"3198\":{\"id\":3198,\"name\":" +
				"\"Perfect Hex Core\",\"description\":\"<stats>+10 Ability Power per level<br>+25 Mana per level" +
				"</stats><br><br><passive>UNIQUE Passive - Glorious Evolution:</passive> Viktor has reached the " +
				"pinnacle of his power, upgrading Chaos Storm in addition to his basic spells.\",\"plaintext\":" +
				"\"Allows Viktor to improve an ability of his choice\",\"from\":[\"3197\"],\"requiredChampion\":" +
				"\"Viktor\",\"maps\":{\"1\":false,\"8\":true,\"10\":true,\"11\":true,\"12\":true,\"14\":false}," +
				"\"image\":{\"full\":\"3198.png\",\"sprite\":\"item1.png\",\"group\":\"item\",\"x\":144,\"y\":288," +
				"\"w\":48,\"h\":48},\"gold\":{\"base\":1000,\"total\":3000,\"sell\":2100,\"purchasable\":true}}}}";
		Item perfectHexCore = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3198");
		itemsRepository.save(perfectHexCore);

		responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"3242\":{\"id\":3242,\"name\":" +
				"\"Enchantment: Captain\",\"group\":\"BootsCaptain\",\"description\":\"<groupLimit>Limited to 1 of " +
				"each enchantment type.</groupLimit><br>Enchants boots to have Captain bonus.<br><br><unique>UNIQUE " +
				"Passive - Captain:</unique> Grants +10% Movement Speed to nearby approaching allied champions.<br>" +
				"<br><i>(Unique Passives with the same name don't stack.)</i>\",\"maps\":{\"1\":false,\"8\":true," +
				"\"10\":true,\"11\":true,\"12\":true,\"14\":false},\"image\":{\"full\":\"3242.png\",\"sprite\":" +
				"\"item3.png\",\"group\":\"item\",\"x\":336,\"y\":0,\"w\":48,\"h\":48},\"gold\":{\"base\":600," +
				"\"total\":600,\"sell\":420,\"purchasable\":true}}}}";
		Item captainEnchantment = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3242");
		itemsRepository.save(captainEnchantment);

		responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"1056\":{\"id\":1056,\"name\":" +
				"\"Doran's Ring\",\"description\":\"<stats>+60 Health<br>+15 Ability Power</stats><br><br><passive>" +
				"Passive:</passive> <mana>+3 Mana Regen per 5 seconds.<br><passive>Passive:</passive> Restores 4 " +
				"Mana upon killing a unit.</mana>\",\"plaintext\":\"Good starting item for casters\",\"maps\":{\"1\":" +
				"false,\"8\":false,\"10\":true,\"11\":true,\"12\":true,\"14\":false},\"image\":{\"full\":" +
				"\"1056.png\",\"sprite\":\"item0.png\",\"group\":\"item\",\"x\":96,\"y\":96,\"w\":48,\"h\":48}," +
				"\"gold\":{\"base\":400,\"total\":400,\"sell\":160,\"purchasable\":true}}}}";
		Item doransRing = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("1056");
		itemsRepository.save(doransRing);

		responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"2032\":{\"id\":2032,\"name\":" +
				"\"Hunter's Potion\",\"group\":\"FlaskGroup\",\"description\":\"<groupLimit>Limited to one type of " +
				"Healing Potion.</groupLimit><br><br><active>UNIQUE Active:</active> Consumes a charge to restore 60 " +
				"Health and 35 Mana over 8 seconds.  Holds up to 5 charges and refills upon visiting the shop.<br>" +
				"<br>Killing a Large Monster grants one charge.<br><br><rules>(Killing a Large Monster at full " +
				"charges will automatically consume the newest charge.)</rules>\",\"plaintext\":\"Restores health " +
				"and mana over time - Refills at shop and has increased capacity\",\"from\":[\"2031\"],\"maps\":" +
				"{\"1\":false,\"8\":false,\"10\":true,\"11\":true,\"12\":false,\"14\":false},\"image\":{\"full\":" +
				"\"2032.png\",\"sprite\":\"item3.png\",\"group\":\"item\",\"x\":288,\"y\":336,\"w\":48,\"h\":48}," +
				"\"gold\":{\"base\":250,\"total\":400,\"sell\":160,\"purchasable\":true}}}}";
		Item huntersPotion = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("2032");
		itemsRepository.save(huntersPotion);

		responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"2052\":{\"id\":2052,\"name\":" +
				"\"Poro-Snax\",\"group\":\"RelicBase\",\"description\":\"This savory blend of free-range, grass-fed " +
				"Avarosan game hens and organic, non-ZMO Freljordian herbs contains the essential nutrients " +
				"necessary to keep your Poro purring with pleasure.<br><br><i>All proceeds will be donated towards " +
				"fighting Noxian animal cruelty.</i>\",\"consumed\":true,\"maps\":{\"1\":false,\"8\":true,\"10\":" +
				"true,\"11\":true,\"12\":true,\"14\":false},\"image\":{\"full\":\"2052.png\",\"sprite\":" +
				"\"item0.png\",\"group\":\"item\",\"x\":96,\"y\":192,\"w\":48,\"h\":48},\"gold\":{\"base\":0," +
				"\"total\":0,\"sell\":0,\"purchasable\":false}}}}";
		Item poroSnax = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("2052");
		itemsRepository.save(poroSnax);

		responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"3072\":{\"id\":3072,\"name\":" +
				"\"The Bloodthirster\",\"description\":\"<stats>+80 Attack Damage</stats><br><br><passive>UNIQUE " +
				"Passive:</passive> +20% Life Steal<br><passive>UNIQUE Passive:</passive> Your basic attacks can now " +
				"overheal you. Excess life is stored as a shield that can block 50-350 damage, based on champion " +
				"level.<br><br>This shield decays slowly if you haven't dealt or taken damage in the last 25 seconds." +
				"\",\"plaintext\":\"Grants Attack Damage, Life Steal and Life Steal now overheals\",\"from\":" +
				"[\"1053\",\"1038\"],\"maps\":{\"1\":false,\"8\":false,\"10\":false,\"11\":true,\"12\":true,\"14\":" +
				"false},\"image\":{\"full\":\"3072.png\",\"sprite\":\"item0.png\",\"group\":\"item\",\"x\":288," +
				"\"y\":384,\"w\":48,\"h\":48},\"gold\":{\"base\":1150,\"total\":3500,\"sell\":2450,\"purchasable\":" +
				"true}}}}";
		Item bloodThirster = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3072");
		itemsRepository.save(bloodThirster);

		List<Item> forTrollBuild = itemsRepository.forTrollBuild("0");
		assertThat(forTrollBuild).isNotEmpty();
		assertThat(forTrollBuild).extracting(Item::getMaps)
				.extracting(SUMMONERS_RIFT).contains(true);
		assertThat(forTrollBuild).extracting(Item::getGold).extracting("purchasable", Boolean.class)
				.containsOnly(true);
		assertThat(forTrollBuild).extracting(Item::getConsumed)
				.containsNull();
		assertThat(forTrollBuild).flatExtracting(Item::getInto)
				.isEmpty();
		assertThat(forTrollBuild).extracting(Item::getId)
				.doesNotContain(1001);
		assertThat(forTrollBuild).extracting(Item::getName)
				.doesNotHave(new Condition<>(name -> name.contains("Enchants boots"), "Enchants boots"))
				.doesNotHave(new Condition<>(name -> name.contains("Trinket"), "Trinket"))
				.doesNotHave(new Condition<>(name -> name.contains("Viktor"), "Viktor"))
				.doesNotHave(new Condition<>(name -> name.contains("Crystalline Flask"), "Crystalline Flask"))
				.doesNotHave(new Condition<>(name -> name.contains("Enchantment"), "Enchantment"))
				.doesNotHave(new Condition<>(name -> name.contains("Doran"), "Doran"));
		assertThat(forTrollBuild).extracting(Item::getGroup)
				.doesNotHave(new Condition<>(group -> group.contains("FlaskGroup"), "FlaskGroup"));
		assertThat(forTrollBuild).extracting(Item::getGroup)
				.doesNotHave(new Condition<>(group -> group.contains("RelicBase"), "RelicBase"));
	}

	@Test
	public void findBy() throws IOException {
		String responseBody = "{\"type\":\"item\",\"version\":\"5.23.1\",\"data\":{\"3901\":{\"id\":3901,\"name\":" +
				"\"Fire at Will\",\"group\":\"GangplankRUpgrade01\",\"description\":\"Requires 500 Silver Serpents." +
				"<br><br><unique>UNIQUE Passive:</unique> Cannon Barrage fires at an increasing rate over time " +
				"(additional 6 waves over the duration).\",\"plaintext\":\"Cannon Barrage gains extra waves\"," +
				"\"consumed\":true,\"requiredChampion\":\"Gangplank\",\"maps\":{\"1\":false,\"8\":true,\"10\":true," +
				"\"11\":true,\"12\":true,\"14\":false},\"image\":{\"full\":\"3901.png\",\"sprite\":\"item3.png\"," +
				"\"group\":\"item\",\"x\":384,\"y\":192,\"w\":48,\"h\":48},\"gold\":{\"base\":0,\"total\":0," +
				"\"sell\":0,\"purchasable\":true}}}}";
		Item fireAtWill = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3901");
		itemsRepository.save(fireAtWill);

		Page<Item> items = itemsRepository.findBy("fire", new PageRequest(0, 20, ASC, "name"));
		assertThat(items).isNotEmpty().doesNotHaveDuplicates();

		items = itemsRepository.findBy("upgrade", new PageRequest(0, 20, ASC, "name"));
		assertThat(items).isNotEmpty().doesNotHaveDuplicates();

		items = itemsRepository.findBy("GangPLANK", new PageRequest(0, 20, ASC, "name"));
		assertThat(items).isNotEmpty().doesNotHaveDuplicates();
	}

}
