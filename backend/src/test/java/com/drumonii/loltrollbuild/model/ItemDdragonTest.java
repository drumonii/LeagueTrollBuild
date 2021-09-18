package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.builder.ItemBuilder;
import com.drumonii.loltrollbuild.model.builder.ItemGoldBuilder;
import com.drumonii.loltrollbuild.model.builder.ItemImageBuilder;
import com.drumonii.loltrollbuild.model.image.ItemImage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.Fail.fail;

@JsonTest
class ItemDdragonTest {

	@Autowired
	private JacksonTester<Item> jacksonTester;

	@Test
	void serializesIntoJson() {
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
				.withTags("Health")
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

		assertThat(jsonContent).hasJsonPathNumberValue("$.id")
				.extractingJsonPathNumberValue("$.id")
				.isEqualTo(1011);
		assertThat(jsonContent).hasJsonPathStringValue("name")
				.extractingJsonPathStringValue("$.name")
				.isEqualTo("Giant's Belt");
		assertThat(jsonContent).hasJsonPathStringValue("description");
		assertThat(jsonContent).hasJsonPathMapValue("$.image");
		assertThat(jsonContent).hasJsonPathMapValue("$.gold");
		assertThat(jsonContent).hasJsonPathArrayValue("$.tags");
		assertThat(jsonContent).hasJsonPathArrayValue("$.from");
		assertThat(jsonContent).hasJsonPathArrayValue("$.into");
		assertThat(jsonContent).hasJsonPathMapValue("$.maps");
	}

	@Test
	void deserializesFromJson() {
		String json =
				"{" +
				"  \"name\": \"Boots of Speed\"," +
				"  \"description\": \"<groupLimit>Limited to 1.</groupLimit><br><br><unique>UNIQUE Passive - Enhanced Movement:</unique> +25 Movement Speed\"," +
				"  \"into\": [" +
				"    \"3006\"," +
				"    \"3047\"," +
				"    \"3020\"," +
				"    \"3158\"," +
				"    \"3111\"," +
				"    \"3117\"," +
				"    \"3009\"" +
				"  ]," +
				"  \"image\": {" +
				"    \"full\": \"1001.png\"," +
				"    \"sprite\": \"item0.png\"," +
				"    \"group\": \"item\"," +
				"    \"x\": 0," +
				"    \"y\": 0," +
				"    \"w\": 48," +
				"    \"h\": 48" +
				"  }," +
				"  \"gold\": {" +
				"    \"base\": 300," +
				"    \"purchasable\": true," +
				"    \"total\": 300," +
				"    \"sell\": 210" +
				"  }," +
				"  \"tags\": [" +
				"    \"Boots\"" +
				"  ]," +
				"  \"maps\": {" +
				"    \"1\": false," +
				"    \"8\": true," +
				"    \"10\": true," +
				"    \"11\": true," +
				"    \"12\": true," +
				"    \"14\": false" +
				"  }" +
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
		assertThat(item.getObject().getDescription()).isEqualTo("<groupLimit>Limited to 1.</groupLimit><br><br><unique>UNIQUE Passive - Enhanced Movement:</unique> +25 Movement Speed");
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
				.containsExactly(entry(1, false), entry(8, true), entry(10, true), entry(11, true), entry(12, true),
						entry(14, false));
		assertThat(item.getObject().getTags()).containsOnly("Boots");
		assertThat(item.getObject().getGold()).isNotNull();
		assertThat(item.getObject().getGold().getId()).isEqualTo(1001);
		assertThat(item.getObject().getGold().getBase()).isEqualTo(300);
		assertThat(item.getObject().getGold().getTotal()).isEqualTo(300);
		assertThat(item.getObject().getGold().getSell()).isEqualTo(210);
		assertThat(item.getObject().getGold().isPurchasable()).isTrue();
	}

}
