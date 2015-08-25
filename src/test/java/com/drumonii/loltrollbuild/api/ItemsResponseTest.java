package com.drumonii.loltrollbuild.api;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Image;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.ItemGold;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.StrictAssertions.entry;

public class ItemsResponseTest extends BaseSpringTestRunner {

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void itemsResponseFromRiotIsMapped() throws IOException {
		String responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"3711\":{\"id\":3711,\"name\":" +
				"\"Poacher's Knife\",\"group\":\"JungleItems\",\"description\":\"<stats>+30 Bonus Gold per Large " +
				"Monster Kill</stats><br><passive>Passive - Scavenging Smite:</passive> When you Smite a large " +
				"monster in the enemy jungle, you gain half a charge of Smite. If you kill that monster, you gain " +
				"+20 bonus Gold, and you gain 175% increased Movement Speed decaying over 2 seconds.<br><br><passive>" +
				"Passive - Jungler:</passive> Deal 45 additional magic damage to monsters over 2 seconds and gain 10 " +
				"Health Regen and 5 Mana Regen per second while under attack from neutral monsters.<br><br>" +
				"<groupLimit>Limited to 1 Jungle item</groupLimit>\",\"plaintext\":\"Makes your Smite give extra " +
				"gold from the enemy jungle\",\"from\":[\"1039\"],\"into\":[\"3719\",\"3720\",\"3721\",\"3722\"]," +
				"\"image\":{\"full\":\"3711.png\",\"sprite\":\"item2.png\",\"group\":\"item\",\"x\":432,\"y\":192," +
				"\"w\":48,\"h\":48},\"gold\":{\"base\":450,\"total\":850,\"sell\":595,\"purchasable\":true}}}}";

		ItemsResponse itemsResponse = objectMapper.readValue(responseBody, ItemsResponse.class);
		assertThat(itemsResponse).isNotNull();
		assertThat(itemsResponse).isEqualToComparingOnlyGivenFields(new ItemsResponse("item", "5.16.1"),
				"type", "version");
		assertThat(itemsResponse.getItems()).hasSize(1);
		Item item = new Item(3711, "Poacher's Knife", "JungleItems", null, "<stats>+30 Bonus Gold per Large Monster " +
				"Kill</stats><br><passive>Passive - Scavenging Smite:</passive> When you Smite a large monster in " +
				"the enemy jungle, you gain half a charge of Smite. If you kill that monster, you gain +20 bonus " +
				"Gold, and you gain 175% increased Movement Speed decaying over 2 seconds.<br><br><passive>Passive - " +
				"Jungler:</passive> Deal 45 additional magic damage to monsters over 2 seconds and gain 10 Health " +
				"Regen and 5 Mana Regen per second while under attack from neutral monsters.<br><br><groupLimit>" +
				"Limited to 1 Jungle item</groupLimit>", Arrays.asList("1039"), Arrays.asList("3719", "3720", "3721",
				"3722"), null, new Image("3711.png", "item2.png", "item", 432, 192, 48, 48), new ItemGold(450, 850, 595,
				true));
		assertThat(itemsResponse.getItems()).containsExactly(entry("3711", item));
	}

}
