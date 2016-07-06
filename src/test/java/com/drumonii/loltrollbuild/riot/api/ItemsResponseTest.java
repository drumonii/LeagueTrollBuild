package com.drumonii.loltrollbuild.riot.api;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.ItemGold;
import com.drumonii.loltrollbuild.model.image.ItemImage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

public class ItemsResponseTest extends BaseSpringTestRunner {

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void itemsResponseFromRiotIsMapped() throws IOException {
		String responseBody = "{\"type\":\"item\",\"version\":\"5.22.3\",\"data\":{\"3711\":{\"id\":3711,\"name\":" +
				"\"Tracker's Knife\",\"group\":\"JungleItems\",\"description\":\"<groupLimit>Limited to 1 Jungle item" +
				"</groupLimit><br><br><stats>+10% Life Steal vs. Monsters<br>+150% Mana Regeneration while in Jungle" +
				"</stats><br><br><unique>UNIQUE Passive - Tooth / Nail:</unique> Basic attacks deal 20 bonus damage " +
				"vs. monsters. Damaging a monster steals 30 Health over 5 seconds. Killing a Large Monster grants +30" +
				" bonus experience.<br><active>UNIQUE Active - Warding (Minor):</active> Consumes a charge to place a" +
				" <font color='#BBFFFF'>Stealth Ward</font> that reveals the surrounding area for 150 seconds.  Holds" +
				" up to 2 charges which refill upon visiting the shop. <br><br><rules>(A player may only have 3 " +
				"<font color='#BBFFFF'>Stealth Wards</font> on the map at one time. Unique Passives with the same " +
				"name don't stack.)</rules>\",\"plaintext\":\"Makes your Smite give extra gold from the enemy " +
				"jungle\",\"from\":[\"1039\",\"1041\"],\"into\":[\"1408\",\"1409\",\"1410\",\"1411\"],\"maps\":" +
				"{\"1\":false,\"8\":false,\"10\":false,\"11\":true,\"12\":false,\"14\":false},\"image\":{\"full\":" +
				"\"3711.png\",\"sprite\":\"item2.png\",\"group\":\"item\",\"x\":432,\"y\":192,\"w\":48,\"h\":48}," +
				"\"gold\":{\"base\":350,\"total\":1050,\"sell\":735,\"purchasable\":true}}}}";
		ItemsResponse itemsResponse = null;
		try {
			itemsResponse = objectMapper.readValue(responseBody, ItemsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Items response.", e);
		}

		assertThat(itemsResponse).isNotNull();
		assertThat(itemsResponse).isEqualToComparingOnlyGivenFields(new ItemsResponse("item", "5.22.3"),
				"type", "version");
		assertThat(itemsResponse.getItems()).hasSize(1);
		Item item = new Item(3711, "Tracker's Knife", "JungleItems", null, "<groupLimit>Limited to 1 Jungle item" +
				"</groupLimit><br><br><stats>+10% Life Steal vs. Monsters<br>+150% Mana Regeneration while in Jungle" +
				"</stats><br><br><unique>UNIQUE Passive - Tooth / Nail:</unique> Basic attacks deal 20 bonus damage " +
				"vs. monsters. Damaging a monster steals 30 Health over 5 seconds. Killing a Large Monster grants +30" +
				" bonus experience.<br><active>UNIQUE Active - Warding (Minor):</active> Consumes a charge to place a" +
				" <font color='#BBFFFF'>Stealth Ward</font> that reveals the surrounding area for 150 seconds.  Holds" +
				" up to 2 charges which refill upon visiting the shop. <br><br><rules>(A player may only have 3 <font" +
				" color='#BBFFFF'>Stealth Wards</font> on the map at one time. Unique Passives with the same name " +
				"don't stack.)</rules>", Arrays.asList("1039", "1041"), new HashSet<>(Arrays.asList("1411", "1410",
				"1409", "1408")), null, null, new HashMap<String, Boolean>() {{ put("1", false); put("8", false);
				put("10", false);put("11", true); put("12", false); put("14", false); }}, new ItemImage("3711.png",
				"item2.png", "item", new byte[0], 432, 192, 48, 48), new ItemGold(0, 350, 1050, 735, true, null));
		assertThat(itemsResponse.getItems().get("3711")).isEqualToIgnoringGivenFields(item, "gold");
	}

}
