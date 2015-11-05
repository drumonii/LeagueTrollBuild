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
		Item homeguardEnchantment = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("1319");
		itemsRepository.save(homeguardEnchantment);

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
				"\"into\":[\"3197\"],\"requiredChampion\":\"Viktor\",\"image\":{\"full\":\"3196.png\",\"sprite\":" +
				"\"item1.png\",\"group\":\"item\",\"x\":48,\"y\":288,\"w\":48,\"h\":48},\"gold\":{\"base\":1000," +
				"\"total\":1000,\"sell\":700,\"purchasable\":true}}}}";
		Item hexCoreMk1 = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3196");
		itemsRepository.save(hexCoreMk1);

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
		assertThat(viktorOnlyItems).extracting(Item::getDescription)
				.have(new Condition<>(name -> name.contains("Viktor"), "Viktor"));
		assertThat(viktorOnlyItems).extracting(Item::getRequiredChampion)
				.have(new Condition<>(name -> name.equals("Viktor"), "Viktor"));
	}

	@Test
	public void forTrollBuild() throws IOException {
		String responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"1062\":{\"id\":1062,\"name\":" +
				"\"Prospector's Blade\",\"description\":\"<stats>+16 Attack Damage<br>+15% Attack Speed </stats><br>" +
				"<br><unique>UNIQUE Passive - Prospector:</unique> +150 Health<br><br><i>(Unique Passives with the " +
				"same name don't stack.)</i>\",\"plaintext\":\"Good starting item for attackers\",\"maps\":{\"1\":" +
				"false,\"8\":true,\"10\":false,\"11\":false,\"12\":false,\"14\":false},\"image\":{\"full\":" +
				"\"1062.png\",\"sprite\":\"item0.png\",\"group\":\"item\",\"x\":240,\"y\":96,\"w\":48,\"h\":48}," +
				"\"gold\":{\"base\":950,\"total\":950,\"sell\":380,\"purchasable\":true}}}}";
		Item prospectorsBlade = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("1062");
		itemsRepository.save(prospectorsBlade);

		responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"2010\":{\"id\":2010,\"name\":" +
				"\"Total Biscuit of Rejuvenation\",\"group\":\"HealthPotion\",\"description\":\"<consumable>Click to " +
				"Consume:</consumable> Restores 20 health and 10 mana immediately and then 150 Health over 15 " +
				"seconds.\",\"consumed\":true,\"maps\":{\"1\":false,\"8\":true,\"10\":true,\"11\":true,\"12\":true," +
				"\"14\":false},\"image\":{\"full\":\"2010.png\",\"sprite\":\"item0.png\",\"group\":\"item\",\"x\":" +
				"144,\"y\":144,\"w\":48,\"h\":48},\"gold\":{\"base\":35,\"total\":35,\"sell\":14,\"purchasable\":" +
				"false}}}}";
		Item biscuitOfRejuvenation = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("2010");
		itemsRepository.save(biscuitOfRejuvenation);

		responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"3117\":{\"id\":3117,\"name\":" +
				"\"Boots of Mobility\",\"description\":\"<unique>UNIQUE Passive - Enhanced Movement:</unique> +25 " +
				"Movement Speed. Increases to +105 Movement Speed when out of combat for 5 seconds.<br><br><i>(" +
				"Unique Passives with the same name don't stack.)</i>\",\"plaintext\":\"Greatly enhances Movement " +
				"Speed when out of combat\",\"from\":[\"1001\"],\"into\":[\"1326\",\"1328\",\"1325\",\"1327\"," +
				"\"1329\"],\"maps\":{\"1\":false,\"8\":true,\"10\":true,\"11\":true,\"12\":true,\"14\":false}," +
				"\"image\":{\"full\":\"3117.png\",\"sprite\":\"item1.png\",\"group\":\"item\",\"x\":0,\"y\":96,\"w\":" +
				"48,\"h\":48},\"gold\":{\"base\":475,\"total\":800,\"sell\":560,\"purchasable\":true}}}}";
		Item bootsOfMobility = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3117");
		itemsRepository.save(bootsOfMobility);

		responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"3341\":{\"id\":3341,\"name\":" +
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

		responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"3198\":{\"id\":3198,\"name\":" +
				"\"Perfect Hex Core\",\"description\":\"<stats>+10 Ability Power per level<br>+25 Mana per level" +
				"</stats><br><br><passive>UNIQUE Passive - Glorious Evolution:</passive> Viktor has reached the " +
				"pinnacle of his power, upgrading Chaos Storm in addition to his basic spells.\",\"plaintext\":" +
				"\"Allows Viktor to improve an ability of his choice\",\"from\":[\"3197\"],\"requiredChampion\":" +
				"\"Viktor\",\"maps\":{\"1\":false,\"8\":true,\"10\":true,\"11\":true,\"12\":true,\"14\":false}," +
				"\"image\":{\"full\":\"3198.png\",\"sprite\":\"item1.png\",\"group\":\"item\",\"x\":144,\"y\":288," +
				"\"w\":48,\"h\":48},\"gold\":{\"base\":1000,\"total\":3000,\"sell\":2100,\"purchasable\":true}}}}";
		Item perfectHexCore = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3198");
		itemsRepository.save(perfectHexCore);

		responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"2041\":{\"id\":2041,\"name\":" +
				"\"Crystalline Flask\",\"description\":\"<unique>UNIQUE Passive:</unique> Holds 3 charges and " +
				"refills upon visiting the shop.<br><active>UNIQUE Active:</active> Consumes a charge to restore 120 " +
				"Health and 60 Mana over 12 seconds.\",\"plaintext\":\"Restores Health and Mana over time, refills " +
				"at shop\",\"maps\":{\"1\":false,\"8\":true,\"10\":true,\"11\":true,\"12\":false,\"14\":false}," +
				"\"image\":{\"full\":\"2041.png\",\"sprite\":\"item0.png\",\"group\":\"item\",\"x\":192,\"y\":144," +
				"\"w\":48,\"h\":48},\"gold\":{\"base\":345,\"total\":345,\"sell\":138,\"purchasable\":true}}}}";
		Item crystallineFlask = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("2041");
		itemsRepository.save(crystallineFlask);

		responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"3242\":{\"id\":3242,\"name\":" +
				"\"Enchantment: Captain\",\"group\":\"BootsCaptain\",\"description\":\"<groupLimit>Limited to 1 of " +
				"each enchantment type.</groupLimit><br>Enchants boots to have Captain bonus.<br><br><unique>UNIQUE " +
				"Passive - Captain:</unique> Grants +10% Movement Speed to nearby approaching allied champions.<br>" +
				"<br><i>(Unique Passives with the same name don't stack.)</i>\",\"maps\":{\"1\":false,\"8\":true," +
				"\"10\":true,\"11\":true,\"12\":true,\"14\":false},\"image\":{\"full\":\"3242.png\",\"sprite\":" +
				"\"item3.png\",\"group\":\"item\",\"x\":336,\"y\":0,\"w\":48,\"h\":48},\"gold\":{\"base\":600," +
				"\"total\":600,\"sell\":420,\"purchasable\":true}}}}";
		Item captainEnchantment = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3242");
		itemsRepository.save(captainEnchantment);

		responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"1056\":{\"id\":1056,\"name\":" +
				"\"Doran's Ring\",\"description\":\"<stats>+60 Health<br>+15 Ability Power</stats><br><br><passive>" +
				"Passive:</passive> <mana>+3 Mana Regen per 5 seconds.<br><passive>Passive:</passive> Restores 4 " +
				"Mana upon killing a unit.</mana>\",\"plaintext\":\"Good starting item for casters\",\"maps\":{\"1\":" +
				"false,\"8\":false,\"10\":true,\"11\":true,\"12\":true,\"14\":false},\"image\":{\"full\":" +
				"\"1056.png\",\"sprite\":\"item0.png\",\"group\":\"item\",\"x\":96,\"y\":96,\"w\":48,\"h\":48}," +
				"\"gold\":{\"base\":400,\"total\":400,\"sell\":160,\"purchasable\":true}}}}";
		Item doransRing = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("1056");
		itemsRepository.save(doransRing);

		responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"3041\":{\"id\":3041,\"name\":\"Mejai's " +
				"Soulstealer\",\"description\":\"<stats>+20 Ability Power  </stats><br><br><unique>UNIQUE Passive:" +
				"</unique> Grants +8 Ability Power per stack and 5 stacks upon first purchase. Grants 2 stacks for a " +
				"kill or 1 stack for an assist (max 20 stacks). Half of the stacks are lost upon death. At 20 stacks," +
				" grants +15% Cooldown Reduction.\",\"plaintext\":\"Grants Ability Power for kills and assists\"," +
				"\"from\":[\"1052\"],\"maps\":{\"1\":false,\"8\":false,\"10\":false,\"11\":true,\"12\":false,\"14\":" +
				"false},\"image\":{\"full\":\"3041.png\",\"sprite\":\"item0.png\",\"group\":\"item\",\"x\":432,\"y\":" +
				"288,\"w\":48,\"h\":48},\"gold\":{\"base\":965,\"total\":1400,\"sell\":980,\"purchasable\":true}}}}";
		Item mejaisSoulstealer = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3041");
		itemsRepository.save(mejaisSoulstealer);

		responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"3072\":{\"id\":3072,\"name\":" +
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

		List<Item> forTrollBuild = itemsRepository.forTrollBuild();
		assertThat(forTrollBuild).isNotEmpty();
		assertThat(forTrollBuild).extracting(Item::getMaps)
				.extracting("11").contains(true);
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
		assertThat(forTrollBuild).extracting(Item::getDescription)
				.doesNotHave(new Condition<>(descr -> descr.contains("At 20 stacks"), "At 20 stacks"));
	}

}
