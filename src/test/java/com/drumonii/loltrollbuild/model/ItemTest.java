package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.ListUtils;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemTest extends BaseSpringTestRunner {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ItemsRepository itemsRepository;

	@After
	public void after() {
		itemsRepository.deleteAll();
	}

	@Test
	public void equals() throws IOException {
		String responseBody = "{\"type\":\"item\",\"version\":\"5.23.1\",\"data\":{\"3112\":{\"id\":3112,\"name\":" +
				"\"Orb of Winter\",\"description\":\"<stats>+70 Magic Resist<br>+100% Base Health Regeneration " +
				"</stats><br><br><unique>UNIQUE Passive:</unique> Grants a shield that absorbs up to 30 " +
				"(+10 per level) damage. The shield will refresh after 9 seconds without receiving damage.\"," +
				"\"plaintext\":\"Grants a shield when out of combat\",\"from\":[\"1006\",\"1006\",\"1057\"]," +
				"\"maps\":{\"1\":false,\"8\":false,\"10\":false,\"11\":false,\"12\":true,\"14\":false},\"image\":{" +
				"\"full\":\"3112.png\",\"sprite\":\"item1.png\",\"group\":\"item\",\"x\":240,\"y\":48,\"w\":48," +
				"\"h\":48},\"gold\":{\"base\":990,\"total\":2010,\"sell\":1407,\"purchasable\":true}}}}";
		Item orbOfWinterFromRiot = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3112");
		itemsRepository.save(orbOfWinterFromRiot);

		Item orbOfWinterFromDb = itemsRepository.findOne(orbOfWinterFromRiot.getId());
		assertThat(orbOfWinterFromRiot).isEqualTo(orbOfWinterFromDb);

		responseBody = "{\"type\":\"item\",\"version\":\"5.23.1\",\"data\":{\"1004\":{\"id\":1004,\"name\":" +
				"\"Faerie Charm\",\"description\":\"<stats><mana>+25% Base Mana Regen </mana></stats>\"," +
				"\"plaintext\":\"Slightly increases Mana Regen\",\"into\":[\"3028\",\"3070\",\"3073\",\"3098\"," +
				"\"3096\",\"3114\"],\"maps\":{\"1\":false,\"8\":true,\"10\":true,\"11\":true,\"12\":true," +
				"\"14\":false},\"image\":{\"full\":\"1004.png\",\"sprite\":\"item0.png\",\"group\":\"item\",\"x\":48," +
				"\"y\":0,\"w\":48,\"h\":48},\"gold\":{\"base\":125,\"total\":125,\"sell\":88,\"purchasable\":true}}}}";
		Item faerieCharmFromRiot = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("1004");
		itemsRepository.save(faerieCharmFromRiot);

		Item faerieCharmFromDb = itemsRepository.findOne(faerieCharmFromRiot.getId());
		assertThat(faerieCharmFromRiot).isEqualTo(faerieCharmFromDb);

		responseBody = "{\"type\":\"item\",\"version\":\"5.23.1\",\"data\":{\"3800\":{\"id\":3800,\"name\":" +
				"\"Righteous Glory\",\"description\":\"<stats>+500 Health<br><mana>+300 Mana</mana><br>+100% Base " +
				"Health Regen </stats><br><br><unique>UNIQUE Active:</unique> Grants +60% Movement Speed to nearby " +
				"allies when moving towards enemies or enemy turrets for 3 seconds. After 3 seconds, a shockwave is " +
				"emitted, slowing nearby enemy champion Movement Speed by 80% for 1 second(s) (90 second cooldown)." +
				"<br><br>This effect may be reactivated early to instantly release the shockwave.\",\"plaintext\":" +
				"\"Grants Health, Mana. Activate to speed towards enemies and slow them.\",\"from\":[\"3010\"," +
				"\"3801\"],\"maps\":{\"1\":false,\"8\":true,\"10\":true,\"11\":true,\"12\":true,\"14\":false}," +
				"\"image\":{\"full\":\"3800.png\",\"sprite\":\"item2.png\",\"group\":\"item\",\"x\":192,\"y\":288," +
				"\"w\":48,\"h\":48},\"gold\":{\"base\":750,\"total\":2600,\"sell\":1820,\"purchasable\":true}}}}";
		Item righteousGloryFromRiot = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3800");
		itemsRepository.save(righteousGloryFromRiot);

		Item righteousGloryFromDb = itemsRepository.findOne(righteousGloryFromRiot.getId());
		assertThat(righteousGloryFromRiot).isEqualTo(righteousGloryFromDb);

		righteousGloryFromRiot.setGold(new ItemGold(0, 700, 2500, 1750, true, null));
		assertThat(righteousGloryFromRiot).isNotEqualTo(righteousGloryFromDb);
	}

	@Test
	public void cardinality() throws IOException {
		String responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"3124\":{\"id\":3124,\"name\":" +
				"\"Guinsoo's Rageblade\",\"description\":\"<stats>+30 Attack Damage<br>+40 Ability Power</stats><br>" +
				"<br><unique>UNIQUE Passive:</unique> Basic attacks grant +8% Attack Speed, +3 Attack Damage, and +4 " +
				"Ability Power for 5 seconds (stacks up to 8 times, melee attacks grant 2 stacks). While you have 8 " +
				"stacks, gain <unlockedPassive>Guinsoo's Rage</unlockedPassive>.<br><br><unlockedPassive>Guinsoo's " +
				"Rage:</unlockedPassive> Basic attacks deal bonus magic damage on hit equal to 20 + 15% of bonus " +
				"Attack Damage and 7.5% of Ability Power to the target and nearby enemy units.\",\"plaintext\":" +
				"\"Increases Ability Power and Attack Damage\",\"from\":[\"1026\",\"1037\"],\"maps\":{\"1\":false," +
				"\"8\":true,\"10\":true,\"11\":true,\"12\":true,\"14\":false},\"image\":{\"full\":\"3124.png\"," +
				"\"sprite\":\"item1.png\",\"group\":\"item\",\"x\":96,\"y\":96,\"w\":48,\"h\":48},\"gold\":{\"base\":" +
				"775,\"total\":2500,\"sell\":1750,\"purchasable\":true}}}}";
		Item guinsoosRagebladeFromRiot = objectMapper.readValue(responseBody, ItemsResponse.class).getItems()
				.get("3124");
		itemsRepository.save(guinsoosRagebladeFromRiot);
		Item guinsoosRagebladeFromDb = itemsRepository.findOne(guinsoosRagebladeFromRiot.getId());

		responseBody = "{\"type\":\"item\",\"version\":\"5.23.1\",\"data\":{\"3136\":{\"id\":3136,\"name\":" +
				"\"Haunting Guise\",\"description\":\"<stats>+25 Ability Power<br>+200 Health</stats><br><br><unique>" +
				"UNIQUE Passive - Eyes of Pain:</unique> +15 <a href='FlatMagicPen'>Magic Penetration</a>\"," +
				"\"plaintext\":\"Increases magic damage\",\"from\":[\"1028\",\"1052\"],\"into\":[\"3151\"],\"maps\":{" +
				"\"1\":false,\"8\":true,\"10\":true,\"11\":true,\"12\":true,\"14\":false},\"image\":{\"full\":" +
				"\"3136.png\",\"sprite\":\"item1.png\",\"group\":\"item\",\"x\":240,\"y\":96,\"w\":48,\"h\":48}," +
				"\"gold\":{\"base\":765,\"total\":1600,\"sell\":1120,\"purchasable\":true}}}}";
		Item hauntingGuiseFromRiot = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3136");
		itemsRepository.save(hauntingGuiseFromRiot);
		Item hauntingGuiseFromDb = itemsRepository.findOne(hauntingGuiseFromRiot.getId());

		responseBody = "{\"type\":\"item\",\"version\":\"5.23.1\",\"data\":{\"3170\":{\"id\":3170,\"name\":" +
				"\"Moonflair Spellblade\",\"description\":\"<stats>+50 Ability Power<br>+50 Armor<br>+50 Magic Resist" +
				"</stats><br><br><unique>UNIQUE Passive - Tenacity:</unique> Reduces the duration of stuns, slows, " +
				"taunts, fears, silences, blinds, polymorphs, and immobilizes by 35%.\",\"plaintext\":\"Improves " +
				"defense and reduces duration of disabling effects\",\"from\":[\"3191\",\"1057\"],\"maps\":{\"1\":" +
				"false,\"8\":true,\"10\":true,\"11\":false,\"12\":false,\"14\":false},\"image\":{\"full\":" +
				"\"3170.png\",\"sprite\":\"item1.png\",\"group\":\"item\",\"x\":432,\"y\":192,\"w\":48,\"h\":48}," +
				"\"gold\":{\"base\":580,\"total\":2500,\"sell\":1750,\"purchasable\":true}}}}";
		Item moonflairSpellbladeFromRiot = objectMapper.readValue(responseBody, ItemsResponse.class).getItems()
				.get("3170");
		itemsRepository.save(moonflairSpellbladeFromRiot);
		Item moonflairSpellbladeFromDb = itemsRepository.findOne(moonflairSpellbladeFromRiot.getId());

		// Rageblade, Haunting Guise, and Moonflair
		List<Item> itemsFromDb = Arrays.asList(guinsoosRagebladeFromDb, hauntingGuiseFromDb, moonflairSpellbladeFromDb);

		responseBody = "{\"type\":\"item\",\"version\":\"5.23.1\",\"data\":{\"3124\":{\"id\":3124,\"name\":" +
				"\"Guinsoo's Rageblade\",\"description\":\"<stats>+30 Attack Damage<br>+40 Ability Power</stats><br>" +
				"<br><unique>UNIQUE Passive:</unique> Basic attacks grant +8% Attack Speed, +3 Attack Damage, and +4 " +
				"Ability Power for 5 seconds (stacks up to 8 times, melee attacks grant 2 stacks). While you have 8 " +
				"stacks, gain <unlockedPassive>Guinsoo's Rage</unlockedPassive>.<br><br><unlockedPassive>Guinsoo's " +
				"Rage:</unlockedPassive> Basic attacks deal bonus magic damage on hit equal to 20 + 15% of bonus " +
				"Attack Damage and 7.5% of Ability Power to the target and nearby enemy units.\",\"plaintext\":" +
				"\"Increases Ability Power and Attack Damage\",\"from\":[\"1026\",\"1037\"],\"maps\":{\"1\":false," +
				"\"8\":true,\"10\":true,\"11\":true,\"12\":true,\"14\":false},\"image\":{\"full\":\"3124.png\"," +
				"\"sprite\":\"item1.png\",\"group\":\"item\",\"x\":96,\"y\":96,\"w\":48,\"h\":48},\"gold\":{\"base\":" +
				"1075,\"total\":2800,\"sell\":1960,\"purchasable\":true}}}}";
		guinsoosRagebladeFromRiot = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3124");

		responseBody = "{\"type\":\"item\",\"version\":\"5.23.1\",\"data\":{\"1011\":{\"id\":1011,\"name\":" +
				"\"Giant's Belt\",\"description\":\"<stats>+380 Health</stats>\",\"plaintext\":\"Greatly increases " +
				"Health\",\"from\":[\"1028\"],\"into\":[\"3083\",\"3143\",\"3116\",\"3084\",\"3742\"],\"maps\":" +
				"{\"1\":false,\"8\":true,\"10\":true,\"11\":true,\"12\":true,\"14\":false},\"image\":{\"full\":" +
				"\"1011.png\",\"sprite\":\"item0.png\",\"group\":\"item\",\"x\":144,\"y\":0,\"w\":48,\"h\":48}," +
				"\"gold\":{\"base\":600,\"total\":1000,\"sell\":700,\"purchasable\":true}}}}";
		Item giantsBeltFromRiot = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("1011");

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