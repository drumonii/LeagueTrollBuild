package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.image.GameMapImage;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.ListUtils;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GameMapTest extends BaseSpringTestRunner {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MapsRepository mapsRepository;

	@After
	public void after() {
		mapsRepository.deleteAll();
	}

	@Test
	public void equals() throws IOException {
		String responseBody = "{\"type\":\"map\",\"version\":\"5.24.2\",\"data\":{\"10\":{\"mapName\":" +
				"\"NewTwistedTreeline\",\"mapId\":10,\"image\":{\"full\":\"map10.png\",\"sprite\":\"map0.png\"," +
				"\"group\":\"map\",\"x\":0,\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap twistedTreeline = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("10");
		mapsRepository.save(twistedTreeline);

		GameMap twistedTreelineFromDb = mapsRepository.findOne(twistedTreeline.getMapId());
		assertThat(twistedTreeline).isEqualTo(twistedTreelineFromDb);

		twistedTreeline.setImage(new GameMapImage("NewTwistedTreeline.png", "map0.png", "map", new byte[0], 0, 0, 48,
				48));
		assertThat(twistedTreeline).isNotEqualTo(twistedTreelineFromDb);
	}

	@Test
	public void cardinality() throws IOException {
		String responseBody = "{\"type\":\"map\",\"version\":\"5.24.2\",\"data\":{\"11\":{\"mapName\":" +
				"\"SummonersRiftNew\",\"mapId\":11,\"image\":{\"full\":\"map11.png\",\"sprite\":\"map0.png\"," +
				"\"group\":\"map\",\"x\":288,\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap summonersRiftNewFromRiot = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("11");
		mapsRepository.save(summonersRiftNewFromRiot);
		GameMap summonersRiftNewFromDb = mapsRepository.findOne(summonersRiftNewFromRiot.getMapId());

		responseBody = "{\"type\":\"map\",\"version\":\"5.24.2\",\"data\":{\"12\":{\"mapName\":\"ProvingGroundsNew\"," +
				"\"mapId\":12,\"image\":{\"full\":\"map12.png\",\"sprite\":\"map0.png\",\"group\":\"map\",\"x\":48," +
				"\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap provingGroundsFromRiot = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("12");
		mapsRepository.save(provingGroundsFromRiot);
		GameMap provingGroundsFromDb = mapsRepository.findOne(provingGroundsFromRiot.getMapId());

		responseBody = "{\"type\":\"map\",\"version\":\"5.1.1\",\"data\":{\"1\":{\"mapName\":\"SummonersRift\"," +
				"\"mapId\":1,\"image\":{\"full\":\"map1.png\",\"sprite\":\"map0.png\",\"group\":\"map\",\"x\":240," +
				"\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap summonersRiftOldFromRiot = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("1");
		mapsRepository.save(summonersRiftOldFromRiot);
		GameMap summonersRiftOldFromDb = mapsRepository.findOne(summonersRiftOldFromRiot.getMapId());

		// Summoner's Rift (New), Proving Grounds, and Summoner's Rift (Old)
		List<GameMap> mapsFromDb = Arrays.asList(summonersRiftNewFromDb, provingGroundsFromDb, summonersRiftOldFromDb);

		responseBody = "{\"type\":\"map\",\"version\":\"5.24.2\",\"data\":{\"11\":{\"mapName\":\"SummonersRiftNew\"," +
				"\"mapId\":11,\"image\":{\"full\":\"map11.png\",\"sprite\":\"map0.png\",\"group\":\"map\",\"x\":144," +
				"\"y\":0,\"w\":48,\"h\":48}}}}";
		summonersRiftNewFromRiot = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("1");

		responseBody = "{\"type\":\"map\",\"version\":\"5.24.2\",\"data\":{\"8\":{\"mapName\":\"CrystalScar\"," +
				"\"mapId\":8,\"image\":{\"full\":\"map8.png\",\"sprite\":\"map0.png\",\"group\":\"map\",\"x\":192," +
				"\"y\":0,\"w\":48,\"h\":48}}}}";
		GameMap crystalScar = objectMapper.readValue(responseBody, MapsResponse.class).getMaps().get("8");

		// Updated Summoner's Rift (New), same Proving Grounds, "new" Crystal Scar, and no Summoner's Rift (Old)
		List<GameMap> mapsFromRiot = Arrays.asList(summonersRiftNewFromRiot, provingGroundsFromDb, crystalScar);

		List<GameMap> deletedMaps = ListUtils.subtract(mapsFromDb, mapsFromRiot);
		assertThat(deletedMaps).hasSize(2);
		assertThat(deletedMaps).containsOnly(summonersRiftNewFromDb, summonersRiftOldFromDb);

		List<GameMap> unmodifiedMaps = ListUtils.intersection(mapsFromDb, mapsFromRiot);
		assertThat(unmodifiedMaps).hasSize(1);
		assertThat(unmodifiedMaps).containsOnly(provingGroundsFromDb);

		List<GameMap> mapsToUpdate = ListUtils.subtract(mapsFromRiot, mapsFromDb);
		assertThat(mapsToUpdate).hasSize(2);
		assertThat(mapsToUpdate).containsOnly(summonersRiftNewFromRiot, crystalScar);
	}

}