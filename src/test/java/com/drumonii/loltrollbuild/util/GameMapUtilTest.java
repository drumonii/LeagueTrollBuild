package com.drumonii.loltrollbuild.util;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static com.drumonii.loltrollbuild.model.SummonerSpell.GameMode.ARAM;
import static com.drumonii.loltrollbuild.model.SummonerSpell.GameMode.CLASSIC;
import static com.drumonii.loltrollbuild.util.GameMapUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

public class GameMapUtilTest extends BaseSpringTestRunner {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MapsRepository mapsRepository;

	@After
	public void after() {
		mapsRepository.deleteAll();
	}

	@Test
	public void getsActualMapNameFromGameMap() throws Exception {
		String responseBody = "{\"type\":\"map\",\"version\":\"5.24.2\",\"data\":{\"11\":{\"mapName\":" +
				"\"SummonersRiftNew\",\"mapId\":11,\"image\":{\"full\":\"map11.png\",\"sprite\":\"map0.png\"," +
				"\"group\":\"map\",\"x\":288,\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap summonersRiftNewFromRiot = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("11");
		assertThat(getActualMapName("11")).isEqualTo("Summoner's Rift");
		assertThat(getActualMapName(summonersRiftNewFromRiot.getMapName())).isEqualTo("Summoner's Rift");

		responseBody = "{\"type\":\"map\",\"version\":\"5.24.2\",\"data\":{\"8\":{\"mapName\":\"CrystalScar\"," +
				"\"mapId\":8,\"image\":{\"full\":\"map8.png\",\"sprite\":\"map0.png\",\"group\":\"map\",\"x\":192," +
				"\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap crystalScar = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("8");
		assertThat(getActualMapName("8")).isEqualTo("Crystal Scar");
		assertThat(getActualMapName(crystalScar.getMapName())).isEqualTo("Crystal Scar");

		responseBody = "{\"type\":\"map\",\"version\":\"5.24.2\",\"data\":{\"12\":{\"mapName\":\"ProvingGroundsNew\"," +
				"\"mapId\":12,\"image\":{\"full\":\"map12.png\",\"sprite\":\"map0.png\",\"group\":\"map\",\"x\":48," +
				"\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap provingGroundsFromRiot = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("12");
		assertThat(getActualMapName("12")).isEqualTo("Howling Abyss");
		assertThat(getActualMapName(provingGroundsFromRiot.getMapName())).isEqualTo("Howling Abyss");

		responseBody = "{\"type\":\"map\",\"version\":\"5.1.1\",\"data\":{\"1\":{\"mapName\":\"SummonersRift\"," +
				"\"mapId\":1,\"image\":{\"full\":\"map1.png\",\"sprite\":\"map0.png\",\"group\":\"map\",\"x\":240," +
				"\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap summonersRiftOldFromRiot = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("1");
		assertThat(getActualMapName("1")).isEqualTo("Summoner's Rift");
		assertThat(getActualMapName(summonersRiftOldFromRiot.getMapName())).isEqualTo("Summoner's Rift");

		responseBody = "{\"type\":\"map\",\"version\":\"5.24.2\",\"data\":{\"10\":{\"mapName\":" +
				"\"NewTwistedTreeline\",\"mapId\":10,\"image\":{\"full\":\"map10.png\",\"sprite\":\"map0.png\"," +
				"\"group\":\"map\",\"x\":0,\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap twistedTreeline = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("10");
		assertThat(getActualMapName("10")).isEqualTo("Twisted Treeline");
		assertThat(getActualMapName(twistedTreeline.getMapName())).isEqualTo("Twisted Treeline");
	}

	@Test
	public void getsEligibleMaps() throws Exception {
		String responseBody = "{\"type\":\"map\",\"version\":\"6.3.1\",\"data\":{\"8\":{\"mapName\":\"CrystalScar\"," +
				"\"mapId\":8,\"image\":{\"full\":\"map8.png\",\"sprite\":\"map0.png\",\"group\":\"map\",\"x\":192," +
				"\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap crystalScar = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("8");
		mapsRepository.save(crystalScar);

		responseBody = "{\"type\":\"map\",\"version\":\"6.3.1\",\"data\":{\"1\":{\"mapName\":\"SummonersRift\"," +
				"\"mapId\":1,\"image\":{\"full\":\"map1.png\",\"sprite\":\"map0.png\",\"group\":\"map\",\"x\":144," +
				"\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap summonersRift = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("1");
		mapsRepository.save(summonersRift);

		responseBody = "{\"type\":\"map\",\"version\":\"6.3.1\",\"data\":{\"11\":{\"mapName\":\"SummonersRiftNew\"," +
				"\"mapId\":11,\"image\":{\"full\":\"map11.png\",\"sprite\":\"map0.png\",\"group\":\"map\",\"x\":144," +
				"\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap summonersRiftNew = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("11");
		mapsRepository.save(summonersRiftNew);

		responseBody = "{\"type\":\"map\",\"version\":\"6.3.1\",\"data\":{\"12\":{\"mapName\":\"ProvingGroundsNew\"," +
				"\"mapId\":12,\"image\":{\"full\":\"map12.png\",\"sprite\":\"map0.png\",\"group\":\"map\",\"x\":48," +
				"\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap provingGrounds = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("12");
		mapsRepository.save(provingGrounds);

		responseBody = "{\"type\":\"map\",\"version\":\"6.3.1\",\"data\":{\"10\":{\"mapName\":" +
				"\"NewTwistedTreeline\",\"mapId\":10,\"image\":{\"full\":\"map10.png\",\"sprite\":\"map0.png\"," +
				"\"group\":\"map\",\"x\":0,\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap twistedTreeline = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("10");
		mapsRepository.save(twistedTreeline);

		assertThat(eligibleMaps(mapsRepository.findAll())).extracting("mapId")
				.containsExactly(provingGrounds.getMapId(), summonersRiftNew.getMapId(), twistedTreeline.getMapId());
	}

	@Test
	public void getsModeFromMap() throws Exception {
		String responseBody = "{\"type\":\"map\",\"version\":\"6.3.1\",\"data\":{\"8\":{\"mapName\":\"CrystalScar\"," +
				"\"mapId\":8,\"image\":{\"full\":\"map8.png\",\"sprite\":\"map0.png\",\"group\":\"map\",\"x\":192," +
				"\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap crystalScar = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("8");
		mapsRepository.save(crystalScar);

		assertThat(getModeFromMap(mapsRepository.findOne(crystalScar.getMapId()))).isEqualTo(CLASSIC);

		responseBody = "{\"type\":\"map\",\"version\":\"6.3.1\",\"data\":{\"11\":{\"mapName\":\"SummonersRiftNew\"," +
				"\"mapId\":11,\"image\":{\"full\":\"map11.png\",\"sprite\":\"map0.png\",\"group\":\"map\",\"x\":144," +
				"\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap summonersRift = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("11");
		mapsRepository.save(summonersRift);

		assertThat(getModeFromMap(mapsRepository.findOne(summonersRift.getMapId()))).isEqualTo(CLASSIC);

		responseBody = "{\"type\":\"map\",\"version\":\"6.3.1\",\"data\":{\"12\":{\"mapName\":\"ProvingGroundsNew\"," +
				"\"mapId\":12,\"image\":{\"full\":\"map12.png\",\"sprite\":\"map0.png\",\"group\":\"map\",\"x\":48," +
				"\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap provingGrounds = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("12");
		mapsRepository.save(provingGrounds);

		assertThat(getModeFromMap(mapsRepository.findOne(provingGrounds.getMapId()))).isEqualTo(ARAM);
	}

	@Test
	public void getsAvailableMaps() {
		Map<String, Boolean> maps = new HashMap<>();
		maps.put("1", false); // Summoner's Rift
		maps.put("10", true); // Twisted Treeline

		assertThat(getAvailableMaps(maps)).containsExactly("Twisted Treeline");
	}

}