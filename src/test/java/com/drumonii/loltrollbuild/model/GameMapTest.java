package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.image.GameMapImage;
import com.drumonii.loltrollbuild.repository.MapsRepository;
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
	private MapsRepository mapsRepository;

	@After
	public void after() {
		mapsRepository.deleteAll();
	}

	@Test
	public void equals() throws IOException {
		GameMap crystalScar = mapsResponse.getMaps().get(CRYSTAL_SCAR);
		mapsRepository.save(crystalScar);

		GameMap twistedTreelineFromDb = mapsRepository.findOne(crystalScar.getMapId());
		assertThat(crystalScar).isEqualTo(twistedTreelineFromDb);

		crystalScar.setImage(new GameMapImage("NewCrystalScar.png", "map0.png", "map", new byte[0], 0, 0, 48, 48));
		assertThat(crystalScar).isNotEqualTo(twistedTreelineFromDb);
	}

	@Test
	public void cardinality() throws IOException {
		GameMap summonersRiftFromRiot = mapsResponse.getMaps().get(SUMMONERS_RIFT);
		GameMap summonersRiftNewFromDb =  mapsRepository.save(summonersRiftFromRiot);
		summonersRiftFromRiot.setMapName("NEW_MAP_NAME");

		GameMap provingGroundsFromRiot = mapsResponse.getMaps().get(HOWLING_ABYSS);
		GameMap provingGroundsFromDb = mapsRepository.save(provingGroundsFromRiot);

		GameMap twistedTreeline = mapsResponse.getMaps().get(TWISTED_TREELINE);
		GameMap summonersRiftOldFromDb = mapsRepository.save(twistedTreeline);

		// Summoner's Rift, Proving Grounds, and Twisted Treeline
		List<GameMap> mapsFromDb = Arrays.asList(summonersRiftNewFromDb, provingGroundsFromDb, summonersRiftOldFromDb);

		GameMap crystalScar = mapsResponse.getMaps().get(CRYSTAL_SCAR);

		// Updated Summoner's Rift, same Proving Grounds, "new" Crystal Scar, and no Twisted Treeline
		List<GameMap> mapsFromRiot = Arrays.asList(summonersRiftFromRiot, provingGroundsFromDb, crystalScar);

		List<GameMap> deletedMaps = ListUtils.subtract(mapsFromDb, mapsFromRiot);
		assertThat(deletedMaps).hasSize(2);
		assertThat(deletedMaps).containsOnly(summonersRiftNewFromDb, summonersRiftOldFromDb);

		List<GameMap> unmodifiedMaps = ListUtils.intersection(mapsFromDb, mapsFromRiot);
		assertThat(unmodifiedMaps).hasSize(1);
		assertThat(unmodifiedMaps).containsOnly(provingGroundsFromDb);

		List<GameMap> mapsToUpdate = ListUtils.subtract(mapsFromRiot, mapsFromDb);
		assertThat(mapsToUpdate).hasSize(2);
		assertThat(mapsToUpdate).containsOnly(summonersRiftFromRiot, crystalScar);
	}

}