package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.model.builder.GameMapBuilder;
import org.apache.commons.collections4.ListUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GameMapTest {

	@Test
	void cardinality() {
		GameMap summonersRiftFromRiot = new GameMapBuilder()
				.withMapId(11)
				.withMapName("Summoner's Rift New")
				.build();
		GameMap summonersRiftNewFromDb = new GameMapBuilder()
				.withMapId(11)
				.withMapName("Summoner's Rift")
				.build();

		GameMap howlingAbyssFromRiot = new GameMapBuilder()
				.withMapId(12)
				.withMapName("Howling Abyss")
				.build();
		GameMap howlingAbyssFromDb = new GameMapBuilder()
				.withMapId(12)
				.withMapName("Howling Abyss")
				.build();

		GameMap crystalScarFromDb = new GameMapBuilder()
				.withMapId(8)
				.withMapName("The Crystal Scar")
				.build();

		// Summoner's Rift, Howling Abyss, and Crystal Scar
		List<GameMap> mapsFromDb = Arrays.asList(summonersRiftNewFromDb, howlingAbyssFromDb, crystalScarFromDb);

		GameMap butchersBridgeFromRiot = new GameMapBuilder()
				.withMapId(14)
				.withMapName("Butcher's Bridge")
				.build();

		// Updated Summoner's Rift, same Proving Grounds, "new" Butcher's Bridge, and no Crystal Scar
		List<GameMap> mapsFromRiot = Arrays.asList(summonersRiftFromRiot, howlingAbyssFromRiot, butchersBridgeFromRiot);

		List<GameMap> deletedMaps = ListUtils.subtract(mapsFromDb, mapsFromRiot);
		assertThat(deletedMaps).hasSize(2);
		assertThat(deletedMaps).containsOnly(summonersRiftNewFromDb, crystalScarFromDb);

		List<GameMap> unmodifiedMaps = ListUtils.intersection(mapsFromDb, mapsFromRiot);
		assertThat(unmodifiedMaps).hasSize(1);
		assertThat(unmodifiedMaps).containsOnly(howlingAbyssFromDb);

		List<GameMap> mapsToUpdate = ListUtils.subtract(mapsFromRiot, mapsFromDb);
		assertThat(mapsToUpdate).hasSize(2);
		assertThat(mapsToUpdate).containsOnly(summonersRiftFromRiot, butchersBridgeFromRiot);
	}

}
