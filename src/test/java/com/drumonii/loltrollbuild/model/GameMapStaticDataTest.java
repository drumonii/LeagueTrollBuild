package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.builder.GameMapBuilder;
import com.drumonii.loltrollbuild.model.builder.GameMapImageBuilder;
import com.drumonii.loltrollbuild.model.image.GameMapImage;
import com.drumonii.loltrollbuild.util.GameMapUtil;
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

import static com.drumonii.loltrollbuild.config.Profiles.STATIC_DATA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

@RunWith(SpringRunner.class)
@JsonTest
@TestPropertySource(properties = "riot.static-data.apiKey=API_KEY")
@ActiveProfiles({ STATIC_DATA })
public class GameMapStaticDataTest {

	@Autowired
	private JacksonTester<GameMap> jacksonTester;

	@Test
	public void serializesIntoJson() {
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
	public void deserializesFromJson() {
		String json =
				"{" +
				"  \"mapName\": \"Summoner's Rift\"," +
				"  \"mapId\": 11," +
				"  \"image\": {" +
				"    \"full\": \"map11.png\"," +
				"    \"sprite\": \"map0.png\"," +
				"    \"group\": \"map\"," +
				"    \"x\": 96," +
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
		assertThat(map.getObject().getImage().getX()).isEqualTo(96);
		assertThat(map.getObject().getImage().getY()).isEqualTo(0);
		assertThat(map.getObject().getImage().getW()).isEqualTo(48);
		assertThat(map.getObject().getImage().getH()).isEqualTo(48);
	}

}