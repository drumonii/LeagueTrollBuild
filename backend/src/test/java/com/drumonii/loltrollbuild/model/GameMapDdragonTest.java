package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.builder.GameMapBuilder;
import com.drumonii.loltrollbuild.model.builder.GameMapImageBuilder;
import com.drumonii.loltrollbuild.model.image.GameMapImage;
import com.drumonii.loltrollbuild.util.GameMapUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

@JsonTest
@ActiveProfiles({ DDRAGON })
class GameMapDdragonTest {

	@Autowired
	private JacksonTester<GameMap> jacksonTester;

	@Test
	void serializesIntoJson() {
		GameMapImage twistedTreeLineImage = new GameMapImageBuilder()
				.withFull("map10.png")
				.withSprite("map0.png")
				.withGroup("map")
				.withX(48)
				.withY(0)
				.withW(48)
				.withH(48)
				.build();

		GameMap twistedTreeLine = new GameMapBuilder()
				.withMapId(10)
				.withMapName("The Twisted Treeline")
				.withImage(twistedTreeLineImage)
				.build();

		JsonContent<GameMap> jsonContent = null;
		try {
			jsonContent = jacksonTester.write(twistedTreeLine);
		} catch (IOException e) {
			fail("Unable to serialize Game Map into JSON", e);
		}

		assertThat(jsonContent).hasJsonPathNumberValue("$.mapId");
		assertThat(jsonContent).hasJsonPathStringValue("$.mapName");
		assertThat(jsonContent).hasJsonPathMapValue("$.image");
	}

	@Test
	void deserializesFromJson() {
		String json =
				"{" +
				"  \"MapName\": \"SummonersRiftNew\"," +
				"  \"MapId\": \"11\"," +
				"  \"image\": {" +
				"    \"full\": \"map11.png\"," +
				"    \"sprite\": \"map0.png\"," +
				"    \"group\": \"map\"," +
				"    \"x\": 192," +
				"    \"y\": 0," +
				"    \"w\": 48," +
				"    \"h\": 48" +
				"  }" +
				"}";

		ObjectContent<GameMap> map = null;
		try {
			map = jacksonTester.parse(json);
		} catch (IOException e) {
			fail("Unable to deserialize Game Map from JSON", e);
		}

		assertThat(map.getObject()).isNotNull();
		assertThat(map.getObject().getMapName()).isEqualTo(GameMapUtil.SUMMONERS_RIFT);
		assertThat(map.getObject().getMapId()).isEqualTo(11);
		assertThat(map.getObject().getImage()).isNotNull();
		assertThat(map.getObject().getImage().getId()).isEqualTo(11);
		assertThat(map.getObject().getImage().getFull()).isEqualTo("map11.png");
		assertThat(map.getObject().getImage().getSprite()).isEqualTo("map0.png");
		assertThat(map.getObject().getImage().getGroup()).isEqualTo("map");
		assertThat(map.getObject().getImage().getX()).isEqualTo(192);
		assertThat(map.getObject().getImage().getY()).isEqualTo(0);
		assertThat(map.getObject().getImage().getW()).isEqualTo(48);
		assertThat(map.getObject().getImage().getH()).isEqualTo(48);
	}

}
