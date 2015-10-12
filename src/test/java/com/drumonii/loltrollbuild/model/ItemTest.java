package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

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
		String responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"3112\":{\"id\":3112,\"name\":" +
				"\"Orb of Winter\",\"description\":\"<stats>+70 Magic Resist<br>+100% Base Health Regeneration " +
				"</stats><br><br><unique>UNIQUE Passive:</unique> Grants a shield that absorbs up to 30 (+10 per " +
				"level) damage. The shield will refresh after 9 seconds without receiving damage.\",\"plaintext\":" +
				"\"Grants a shield when out of combat\",\"from\":[\"1006\",\"1006\",\"1057\"],\"maps\":{\"1\":false," +
				"\"8\":false,\"10\":false,\"11\":false,\"12\":true,\"14\":false},\"image\":{\"full\":\"3112.png\"," +
				"\"sprite\":\"item1.png\",\"group\":\"item\",\"x\":240,\"y\":48,\"w\":48,\"h\":48},\"gold\":" +
				"{\"base\":990,\"total\":2150,\"sell\":1505,\"purchasable\":true}}}}";
		Item orbOfWinterFromRiot = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3112");
		itemsRepository.save(orbOfWinterFromRiot);

		Item orbOfWinterFromDb = itemsRepository.findOne(orbOfWinterFromRiot.getId());
		assertThat(orbOfWinterFromRiot).isEqualTo(orbOfWinterFromDb);

		responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"1004\":{\"id\":1004,\"name\":" +
				"\"Faerie Charm\",\"description\":\"<stats><mana>+25% Base Mana Regen </mana></stats>\"," +
				"\"plaintext\":\"Slightly increases Mana Regen\",\"into\":[\"3028\",\"3070\",\"3073\",\"3114\"]," +
				"\"maps\":{\"1\":false,\"8\":true,\"10\":true,\"11\":true,\"12\":true,\"14\":false},\"image\":" +
				"{\"full\":\"1004.png\",\"sprite\":\"item0.png\",\"group\":\"item\",\"x\":48,\"y\":0,\"w\":48," +
				"\"h\":48},\"gold\":{\"base\":180,\"total\":180,\"sell\":126,\"purchasable\":true}}}}";
		Item faerieCharmFromRiot = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("1004");
		itemsRepository.save(faerieCharmFromRiot);

		Item faerieCharmFromDb = itemsRepository.findOne(faerieCharmFromRiot.getId());
		assertThat(faerieCharmFromRiot).isEqualTo(faerieCharmFromDb);

		responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"3800\":{\"id\":3800,\"name\":" +
				"\"Righteous Glory\",\"description\":\"<stats>+500 Health<br><mana>+300 Mana</mana><br>+100% Base " +
				"Health Regen </stats><br><br><unique>UNIQUE Active:</unique> Grants +60% Movement Speed to nearby " +
				"allies when moving towards enemies or enemy turrets for 3 seconds. After 3 seconds, a shockwave is " +
				"emitted, slowing nearby enemy champion Movement Speed by 80% for 1 second(s) (90 second cooldown)." +
				"<br><br>This effect may be reactivated early to instantly release the shockwave.\",\"plaintext\":" +
				"\"Grants Health, Mana. Activate to speed towards enemies and slow them.\",\"from\":[\"3010\"," +
				"\"3801\"],\"maps\":{\"1\":false,\"8\":true,\"10\":true,\"11\":true,\"12\":true,\"14\":false}," +
				"\"image\":{\"full\":\"3800.png\",\"sprite\":\"item2.png\",\"group\":\"item\",\"x\":192,\"y\":288," +
				"\"w\":48,\"h\":48},\"gold\":{\"base\":600,\"total\":2400,\"sell\":1680,\"purchasable\":true}}}}}";
		Item righteousGloryFromRiot = objectMapper.readValue(responseBody, ItemsResponse.class).getItems().get("3800");
		itemsRepository.save(righteousGloryFromRiot);

		Item righteousGloryFromDb = itemsRepository.findOne(righteousGloryFromRiot.getId());
		assertThat(righteousGloryFromRiot).isEqualTo(righteousGloryFromDb);

		righteousGloryFromRiot.setGold(new ItemGold(0, 700, 2500, 1750, true, null));
		assertThat(righteousGloryFromRiot).isNotEqualTo(righteousGloryFromDb);
	}

}