package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.SummonerSpell.GameMode;
import com.drumonii.loltrollbuild.model.builder.SummonerSpellBuilder;
import com.drumonii.loltrollbuild.model.builder.SummonerSpellImageBuilder;
import com.drumonii.loltrollbuild.model.image.SummonerSpellImage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

@RunWith(SpringRunner.class)
@JsonTest
@ActiveProfiles({ DDRAGON })
public class SummonerSpellDdragonTest {

	@Autowired
	private JacksonTester<SummonerSpell> jacksonTester;

	@Test
	public void serializesIntoJson() {
		SummonerSpellImage image = new SummonerSpellImageBuilder()
				.withFull("SummonerBoost.png")
				.withSprite("spell0.png")
				.withGroup("spell")
				.withX(48)
				.withY(0)
				.withW(48)
				.withH(48)
				.build();

		SummonerSpell cleanse = new SummonerSpellBuilder()
				.withId(1)
				.withKey("SummonerBoost")
				.withName("Cleanse")
				.withDescription("Removes all disables (excluding suppression and airborne) and summoner spell debuffs affecting your champion and lowers the duration of incoming disables by 65% for 3 seconds.")
				.withCooldown(210)
				.withModes(GameMode.ARAM, GameMode.CLASSIC, GameMode.TUTORIAL)
				.withImage(image)
				.build();

		JsonContent<SummonerSpell> jsonContent = null;
		try {
			jsonContent = jacksonTester.write(cleanse);
		} catch (IOException e) {
			fail("Unable to serialize Summoner Spell into JSON", e);
		}

		assertThat(jsonContent).hasJsonPathNumberValue("$.id");
		assertThat(jsonContent).hasJsonPathStringValue("$.key");
		assertThat(jsonContent).hasJsonPathStringValue("$.name");
		assertThat(jsonContent).hasJsonPathStringValue("$.description");
		assertThat(jsonContent).hasJsonPathMapValue("$.image");
		assertThat(jsonContent).hasJsonPathArrayValue("$.cooldown");
		assertThat(jsonContent).hasJsonPathArrayValue("$.modes");
	}

	@Test
	public void deserializesFromJson() {
		String json =
				"{" +
				"  \"id\": \"SummonerBarrier\"," +
				"  \"name\": \"Barrier\"," +
				"  \"description\": \"Shields your champion from 115-455 damage (depending on champion level) for 2 seconds.\"," +
				"  \"tooltip\": \"Temporarily shields {{ f1 }} damage from your champion for 2 seconds.\"," +
				"  \"maxrank\": 1," +
				"  \"cooldown\": [" +
				"    180" +
				"  ]," +
				"  \"cooldownBurn\": \"180\"," +
				"  \"cost\": [" +
				"    0" +
				"  ]," +
				"  \"costBurn\": \"0\"," +
				"  \"effect\": [" +
				"    null," +
				"    [" +
				"      95" +
				"    ]," +
				"    [" +
				"      20" +
				"    ]" +
				"  ]," +
				"  \"effectBurn\": [" +
				"    null," +
				"    \"95\"," +
				"    \"20\"" +
				"  ]," +
				"  \"vars\": []," +
				"  \"key\": \"21\"," +
				"  \"summonerLevel\": 4," +
				"  \"modes\": [" +
				"    \"ARAM\"," +
				"    \"CLASSIC\"," +
				"    \"TUTORIAL\"," +
				"    \"ODIN\"," +
				"    \"ASCENSION\"," +
				"    \"FIRSTBLOOD\"" +
				"  ]," +
				"  \"costType\": \"NoCost\"," +
				"  \"maxammo\": \"-1\"," +
				"  \"range\": [" +
				"    1200" +
				"  ]," +
				"  \"rangeBurn\": \"1200\"," +
				"  \"image\": {" +
				"    \"full\": \"SummonerBarrier.png\"," +
				"    \"sprite\": \"spell0.png\"," +
				"    \"group\": \"spell\"," +
				"    \"x\": 0," +
				"    \"y\": 0," +
				"    \"w\": 48," +
				"    \"h\": 48" +
				"  }," +
				"  \"resource\": \"No Cost\"" +
				"}";

		ObjectContent<SummonerSpell> summonerSpell = null;
		try {
			summonerSpell = jacksonTester.parse(json);
		} catch (IOException e) {
			fail("Unable to deserialize Summoner Spell from JSON", e);
		}

		assertThat(summonerSpell.getObject()).isNotNull();
		assertThat(summonerSpell.getObject().getId()).isEqualTo(21);
		assertThat(summonerSpell.getObject().getKey()).isEqualTo("SummonerBarrier");
		assertThat(summonerSpell.getObject().getDescription()).isEqualTo("Shields your champion from 115-455 damage (depending on champion level) for 2 seconds.");
		assertThat(summonerSpell.getObject().getImage()).isNotNull();
		assertThat(summonerSpell.getObject().getImage().getId()).isEqualTo(21);
		assertThat(summonerSpell.getObject().getImage().getFull()).isEqualTo("SummonerBarrier.png");
		assertThat(summonerSpell.getObject().getImage().getSprite()).isEqualTo("spell0.png");
		assertThat(summonerSpell.getObject().getImage().getGroup()).isEqualTo("spell");
		assertThat(summonerSpell.getObject().getImage().getX()).isEqualTo(0);
		assertThat(summonerSpell.getObject().getImage().getY()).isEqualTo(0);
		assertThat(summonerSpell.getObject().getImage().getW()).isEqualTo(48);
		assertThat(summonerSpell.getObject().getImage().getH()).isEqualTo(48);
		assertThat(summonerSpell.getObject().getCooldown()).containsOnly(180);
		assertThat(summonerSpell.getObject().getModes())
				.containsExactly(GameMode.ARAM, GameMode.CLASSIC, GameMode.TUTORIAL);
	}

}
