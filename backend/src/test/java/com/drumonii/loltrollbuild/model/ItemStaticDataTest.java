package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.builder.ItemBuilder;
import com.drumonii.loltrollbuild.model.builder.ItemGoldBuilder;
import com.drumonii.loltrollbuild.model.builder.ItemImageBuilder;
import com.drumonii.loltrollbuild.model.image.ItemImage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;

import static com.drumonii.loltrollbuild.config.Profiles.STATIC_DATA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.Fail.fail;

@RunWith(SpringRunner.class)
@JsonTest
@TestPropertySource(properties = "riot.static-data.apiKey=API_KEY")
@ActiveProfiles({ STATIC_DATA })
public class ItemStaticDataTest {

	@Autowired
	private JacksonTester<Item> jacksonTester;

	@Test
	public void serializesIntoJson() {
		ItemImage image = new ItemImageBuilder()
				.withFull("1011.png")
				.withSprite("item0.png")
				.withGroup("item")
				.withX(144)
				.withY(0)
				.withW(48)
				.withH(48)
				.build();

		ItemGold gold = new ItemGoldBuilder()
				.withBase(600)
				.withTotal(1000)
				.withSell(700)
				.withPurchasable(true)
				.build();

		Item giantsBelt = new ItemBuilder()
				.withId(1011)
				.withName("Giant's Belt")
				.withDescription("<stats>+380 Health</stats>")
				.withImage(image)
				.withGold(gold)
				.withFrom(1028)
				.withInto(3083, 3084, 3022, 3143, 3742)
				.withMapEntries(new SimpleEntry<>(8, true), new SimpleEntry<>(10, true), new SimpleEntry<>(11, true),
						new SimpleEntry<>(12, true))
				.build();

		JsonContent<Item> jsonContent = null;
		try {
			jsonContent = jacksonTester.write(giantsBelt);
		} catch (IOException e) {
			fail("Unable to serialize Item into JSON", e);
		}

		assertThat(jsonContent).hasJsonPathNumberValue("$.id");
		assertThat(jsonContent).hasJsonPathStringValue("name");
		assertThat(jsonContent).hasJsonPathStringValue("description");
		assertThat(jsonContent).hasJsonPathMapValue("$.image");
		assertThat(jsonContent).hasJsonPathMapValue("$.gold");
		assertThat(jsonContent).hasJsonPathArrayValue("$.from");
		assertThat(jsonContent).hasJsonPathArrayValue("$.into");
		assertThat(jsonContent).hasJsonPathMapValue("$.maps");
	}

	@Test
	public void deserializesFromJson() {
		String json =
				"{" +
				"  \"gold\": {" +
				"    \"base\": 300," +
				"    \"total\": 300," +
				"    \"sell\": 210," +
				"    \"purchasable\": true" +
				"  }," +
				"  \"image\": {" +
				"    \"full\": \"1001.png\"," +
				"    \"sprite\": \"item0.png\"," +
				"    \"group\": \"item\"," +
				"    \"x\": 0," +
				"    \"y\": 0," +
				"    \"w\": 48," +
				"    \"h\": 48" +
				"  }," +
				"  \"into\": [" +
				"    \"3006\"," +
				"    \"3047\"," +
				"    \"3020\"," +
				"    \"3158\"," +
				"    \"3111\"," +
				"    \"3117\"," +
				"    \"3009\"" +
				"  ]," +
				"  \"maps\": {" +
				"    \"8\": true," +
				"    \"10\": true," +
				"    \"11\": true," +
				"    \"12\": true," +
				"    \"14\": false," +
				"    \"16\": false," +
				"    \"18\": true," +
				"    \"19\": true" +
				"  }," +
				"  \"name\": \"Boots of Speed\"," +
				"  \"description\": \"<groupLimit>Limited to 1 pair of boots.</groupLimit><br><br><unique>UNIQUE Passive - Enhanced Movement:</unique> +25 Movement Speed\"," +
				"  \"plaintext\": \"Slightly increases Movement Speed\"," +
				"  \"id\": 1001" +
				"}";

		ObjectContent<Item> item = null;
		try {
			item = jacksonTester.parse(json);
		} catch (IOException e) {
			fail("Unable to deserialize Item from JSON", e);
		}

		assertThat(item.getObject()).isNotNull();
		assertThat(item.getObject().getId()).isEqualTo(1001);
		assertThat(item.getObject().getName()).isEqualTo("Boots of Speed");
		assertThat(item.getObject().getGroup()).isNull();
		assertThat(item.getObject().getConsumed()).isNull();
		assertThat(item.getObject().getDescription()).isEqualTo("<groupLimit>Limited to 1 pair of boots.</groupLimit><br><br><unique>UNIQUE Passive - Enhanced Movement:</unique> +25 Movement Speed");
		assertThat(item.getObject().getFrom()).isEmpty();
		assertThat(item.getObject().getInto()).containsExactly(3006, 3009, 3020, 3047, 3111, 3117, 3158);
		assertThat(item.getObject().getImage()).isNotNull();
		assertThat(item.getObject().getImage().getId()).isEqualTo(1001);
		assertThat(item.getObject().getImage().getFull()).isEqualTo("1001.png");
		assertThat(item.getObject().getImage().getSprite()).isEqualTo("item0.png");
		assertThat(item.getObject().getImage().getGroup()).isEqualTo("item");
		assertThat(item.getObject().getImage().getX()).isEqualTo(0);
		assertThat(item.getObject().getImage().getY()).isEqualTo(0);
		assertThat(item.getObject().getImage().getW()).isEqualTo(48);
		assertThat(item.getObject().getImage().getH()).isEqualTo(48);
		assertThat(item.getObject().getRequiredChampion()).isNull();
		assertThat(item.getObject().getRequiredAlly()).isNull();
		assertThat(item.getObject().getMaps())
				.containsExactly(entry(8, true), entry(10, true), entry(11, true), entry(12, true), entry(14, false),
						entry(16, false), entry(18, true), entry(19, true));
		assertThat(item.getObject().getGold()).isNotNull();
		assertThat(item.getObject().getGold().getId()).isEqualTo(1001);
		assertThat(item.getObject().getGold().getBase()).isEqualTo(300);
		assertThat(item.getObject().getGold().getTotal()).isEqualTo(300);
		assertThat(item.getObject().getGold().getSell()).isEqualTo(210);
		assertThat(item.getObject().getGold().isPurchasable()).isTrue();
	}

}
