package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.builder.*;
import com.drumonii.loltrollbuild.model.image.ChampionImage;
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
public class ChampionDdragonTest {

	@Autowired
	private JacksonTester<Champion> jacksonTester;

	@Test
	public void serializesIntoJson() {
		ChampionInfo info = new ChampionInfoBuilder()
				.withAttack(5)
				.withDefense(2)
				.withMagic(8)
				.withDifficulty(4)
				.build();

		ChampionImage image = new ChampionImageBuilder()
				.withFull("Sona.png")
				.withSprite("champion3.png")
				.withGroup("champion")
				.withX(96)
				.withY(48)
				.withW(48)
				.withH(48)
				.build();

		ChampionPassive passive = new ChampionPassiveBuilder()
				.withName("Power Chord")
				.withDescription("After casting 3 spells, Sona's next attack deals bonus magic damage in addition to a bonus effect depending on what song Sona last activated.")
				.withImage(new ChampionPassiveImageBuilder()
						.withFull("Sona_Passive_Charged.png")
						.withSprite("passive3.png")
						.withGroup("passive")
						.withX(96)
						.withY(48)
						.withX(48)
						.withH(48)
						.build())
				.build();

		ChampionSpell spell = new ChampionSpellBuilder()
				.withName("Hymn of Valor")
				.withDescription("Sona plays the Hymn of Valor, sends out bolts of sound, dealing magic damage to two nearby enemies, prioritizing champions and monsters. Sona gains a temporary aura that grants allies tagged by the zone bonus damage on their next attack against enemies.")
				.withTooltip("<span class=\\\"colorFF9900\\\">Active:</span> Deals {{ e1 }} <span class=\\\"color99FF99\\\">(+{{ a1 }})</span> magic damage to the nearest two enemies (prioritizes champions) and changes her <span class=\\\"colorEEEEEE\\\">Power Chord</span> bonus to <span class=\\\"color4477FF\\\">Staccato</span>.<br /><br /><span class=\\\"colorFF9900\\\">Melody:</span> Sona gains an aura for {{ e3 }} seconds. Allied champions that enter the aura will gain an additional {{ e4 }} <span class=\\\"color99FF99\\\">(+{{ a2 }})</span> magic damage on their next attack.")
				.withKey("SonaQ")
				.withImage(new ChampionSpellImageBuilder()
						.withFull("SonaQ.png")
						.withSprite("spell10.png")
						.withGroup("spell")
						.withX(240)
						.withY(96)
						.withW(48)
						.withH(48)
						.build())
				.build();

		Champion sona = new ChampionBuilder()
				.withId(37)
				.withKey("Sona")
				.withName("Sona")
				.withTitle("Maven of the Strings")
				.withPartype("Mana")
				.withTags("Support", "Mage")
				.withInfo(info)
				.withImage(image)
				.withPassive(passive)
				.withSpells(spell)
				.build();

		JsonContent<Champion> jsonContent = null;
		try {
			jsonContent = jacksonTester.write(sona);
		} catch (IOException e) {
			fail("Unable to serialize Champion into JSON", e);
		}

		assertThat(jsonContent).hasJsonPathNumberValue("$.id");
		assertThat(jsonContent).hasJsonPathStringValue("$.key");
		assertThat(jsonContent).hasJsonPathStringValue("$.name");
		assertThat(jsonContent).hasJsonPathStringValue("$.title");
		assertThat(jsonContent).hasJsonPathStringValue("$.partype");
		assertThat(jsonContent).hasJsonPathArrayValue("$.tags");
		assertThat(jsonContent).hasJsonPathMapValue("$.info");
		assertThat(jsonContent).hasJsonPathMapValue("$.image");
		assertThat(jsonContent).hasJsonPathMapValue("$.passive");
		assertThat(jsonContent).hasJsonPathMapValue("$.passive.image");
		assertThat(jsonContent).hasJsonPathArrayValue("$.spells");
	}

	@Test
	public void deserializesFromJson() {
		String json =
				"{" +
				"  \"id\": \"Jax\"," +
				"  \"key\": \"24\"," +
				"  \"name\": \"Jax\"," +
				"  \"title\": \"Grandmaster at Arms\"," +
				"  \"image\": {" +
				"    \"full\": \"Jax.png\"," +
				"    \"sprite\": \"champion1.png\"," +
				"    \"group\": \"champion\"," +
				"    \"x\": 48," +
				"    \"y\": 48," +
				"    \"w\": 48," +
				"    \"h\": 48" +
				"  }," +
				"  \"tags\": [" +
				"    \"Fighter\"," +
				"    \"Assassin\"" +
				"  ]," +
				"  \"partype\": \"MP\"," +
				"  \"info\": {" +
				"    \"attack\": 7," +
				"    \"defense\": 5," +
				"    \"magic\": 7," +
				"    \"difficulty\": 5" +
				"  }," +
				"  \"stats\": {}," +
				"  \"spells\": [" +
				"    {" +
				"      \"id\": \"JaxLeapStrike\"," +
				"      \"name\": \"Leap Strike\"," +
				"      \"description\": \"Jax leaps toward a unit. If they are an enemy, he strikes them with his weapon.\"," +
				"      \"tooltip\": \"Jax leaps to target unit, dealing {{ e1 }} <span class=\\\"colorFF8C00\\\">(+{{ f1 }})</span> <span class=\\\"color99FF99\\\">(+{{ a1 }})</span> physical damage if it is an enemy.\"," +
				"      \"image\": {" +
				"        \"full\": \"JaxLeapStrike.png\"," +
				"        \"sprite\": \"spell4.png\"," +
				"        \"group\": \"spell\"," +
				"        \"x\": 384," +
				"        \"y\": 48," +
				"        \"w\": 48," +
				"        \"h\": 48" +
				"      }" +
				"    }," +
				"    {" +
				"      \"id\": \"JaxEmpowerTwo\"," +
				"      \"name\": \"Empower\"," +
				"      \"description\": \"Jax charges his weapon with energy, causing his next attack to deal additional damage.\"," +
				"      \"tooltip\": \"Jax charges his weapon with energy, causing his next basic attack or Leap Strike to deal an additional {{ e1 }} <span class=\\\"color99FF99\\\">(+{{ a1 }}) </span>Magic Damage.\"," +
				"      \"image\": {" +
				"        \"full\": \"JaxEmpowerTwo.png\"," +
				"        \"sprite\": \"spell4.png\"," +
				"        \"group\": \"spell\"," +
				"        \"x\": 432," +
				"        \"y\": 48," +
				"        \"w\": 48," +
				"        \"h\": 48" +
				"      }" +
				"    }," +
				"    {" +
				"      \"id\": \"JaxCounterStrike\"," +
				"      \"name\": \"Counter Strike\"," +
				"      \"description\": \"Jax's combat prowess allows him to dodge all incoming attacks for a short duration and then quickly counterattack, stunning all surrounding enemies.\"," +
				"      \"tooltip\": \"Jax enters a defensive stance for up to {{ e6 }} seconds, dodging all incoming basic attacks and taking {{ e3 }}% less damage from area of effect abilities.<br><br>After 2 seconds or if activated again, Jax stuns surrounding enemies for {{ e2 }} second and deals {{ e1 }} <span class=\\\"colorFF8C00\\\">(+{{ f2 }})</span> physical damage to them.<br><br><span class=\\\"color99FF99\\\">Counter Strike deals {{ e5 }}% more damage for each attack Jax dodged (max: {{ e4 }}% increased damage).</span>\"," +
				"      \"image\": {" +
				"        \"full\": \"JaxCounterStrike.png\"," +
				"        \"sprite\": \"spell4.png\"," +
				"        \"group\": \"spell\"," +
				"        \"x\": 0," +
				"        \"y\": 96," +
				"        \"w\": 48," +
				"        \"h\": 48" +
				"      }" +
				"    }," +
				"    {" +
				"      \"id\": \"JaxRelentlessAssault\"," +
				"      \"name\": \"Grandmaster's Might\"," +
				"      \"description\": \"Every third consecutive attack deals additional Magic Damage. Additionally, Jax can activate this ability to strengthen his resolve, increasing his Armor and Magic Resist for a short duration.\"," +
				"      \"tooltip\": \"<span class=\\\"colorFF9900\\\">Passive:</span> Every third consecutive strike Jax deals {{ e1 }}<span class=\\\"color99FF99\\\"> (+{{ a1 }}) </span>additional Magic Damage.<br><br><span class=\\\"colorFF9900\\\">Active:</span> Jax strengthens his resolve, granting him <span class=\\\"colorFF8C00\\\">{{ f2 }}</span> Armor and <span class=\\\"color99FF99\\\">{{ f1 }}</span> Magic Resist for {{ e5 }} seconds.<span class=\\\"colorFF8C00\\\"><br><br>Armor bonus is equal to {{ e3 }} + {{ e6 }}% bonus Attack Damage.</span><span class=\\\"color99FF99\\\"><br>Magic Resist bonus is equal to {{ e3 }} + {{ e7 }}% Ability Power.</span>\"," +
				"      \"image\": {" +
				"        \"full\": \"JaxRelentlessAssault.png\"," +
				"        \"sprite\": \"spell4.png\"," +
				"        \"group\": \"spell\"," +
				"        \"x\": 48," +
				"        \"y\": 96," +
				"        \"w\": 48," +
				"        \"h\": 48" +
				"      }" +
				"    }" +
				"  ]," +
				"  \"passive\": {" +
				"    \"name\": \"Relentless Assault\"," +
				"    \"description\": \"Jax's consecutive basic attacks continuously increase his Attack Speed.\"," +
				"    \"image\": {" +
				"      \"full\": \"Armsmaster_MasterOfArms.png\"," +
				"      \"sprite\": \"passive1.png\"," +
				"      \"group\": \"passive\"," +
				"      \"x\": 144," +
				"      \"y\": 48," +
				"      \"w\": 48," +
				"      \"h\": 48" +
				"    }" +
				"  }" +
				"}";

		ObjectContent<Champion> champion = null;
		try {
			champion = jacksonTester.parse(json);
		} catch (IOException e) {
			fail("Unable to deserialize Champion from JSON", e);
		}

		assertThat(champion.getObject()).isNotNull();
		assertThat(champion.getObject().getId()).isEqualTo(24);
		assertThat(champion.getObject().getKey()).isEqualTo("Jax");
		assertThat(champion.getObject().getName()).isEqualTo("Jax");
		assertThat(champion.getObject().getTitle()).isEqualTo("Grandmaster at Arms");
		assertThat(champion.getObject().getPartype()).isEqualTo("MP");
		assertThat(champion.getObject().getTags())
				.containsExactly("Assassin", "Fighter");
		assertThat(champion.getObject().getInfo()).isNotNull();
		assertThat(champion.getObject().getInfo().getAttack()).isEqualTo(7);
		assertThat(champion.getObject().getInfo().getDefense()).isEqualTo(5);
		assertThat(champion.getObject().getInfo().getMagic()).isEqualTo(7);
		assertThat(champion.getObject().getInfo().getDifficulty()).isEqualTo(5);
		assertThat(champion.getObject().getImage()).isNotNull();
		assertThat(champion.getObject().getImage().getId()).isEqualTo(24);
		assertThat(champion.getObject().getImage().getFull()).isEqualTo("Jax.png");
		assertThat(champion.getObject().getImage().getSprite()).isEqualTo("champion1.png");
		assertThat(champion.getObject().getImage().getGroup()).isEqualTo("champion");
		assertThat(champion.getObject().getImage().getX()).isEqualTo(48);
		assertThat(champion.getObject().getImage().getY()).isEqualTo(48);
		assertThat(champion.getObject().getImage().getW()).isEqualTo(48);
		assertThat(champion.getObject().getImage().getH()).isEqualTo(48);
		assertThat(champion.getObject().getPassive()).isNotNull();
		assertThat(champion.getObject().getPassive().getName()).isEqualTo("Relentless Assault");
		assertThat(champion.getObject().getPassive().getDescription()).isEqualTo("Jax's consecutive basic attacks continuously increase his Attack Speed.");
		assertThat(champion.getObject().getPassive().getImage()).isNotNull();
		assertThat(champion.getObject().getPassive().getImage().getFull()).isEqualTo("Armsmaster_MasterOfArms.png");
		assertThat(champion.getObject().getPassive().getImage().getSprite()).isEqualTo("passive1.png");
		assertThat(champion.getObject().getPassive().getImage().getGroup()).isEqualTo("passive");
		assertThat(champion.getObject().getPassive().getImage().getX()).isEqualTo(144);
		assertThat(champion.getObject().getPassive().getImage().getY()).isEqualTo(48);
		assertThat(champion.getObject().getPassive().getImage().getW()).isEqualTo(48);
		assertThat(champion.getObject().getPassive().getImage().getH()).isEqualTo(48);
		assertThat(champion.getObject().getSpells()).hasSize(4);
		assertThat(champion.getObject().getSpells()).extracting(ChampionSpell::getImage).hasSize(4);
	}

}