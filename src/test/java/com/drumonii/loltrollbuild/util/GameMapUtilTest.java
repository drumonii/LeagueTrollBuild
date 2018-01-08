package com.drumonii.loltrollbuild.util;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.builder.GameMapBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

import static com.drumonii.loltrollbuild.model.SummonerSpell.GameMode.ARAM;
import static com.drumonii.loltrollbuild.model.SummonerSpell.GameMode.CLASSIC;
import static com.drumonii.loltrollbuild.util.GameMapUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class GameMapUtilTest {

	@Test
	public void getsEligibleMaps() {
		GameMap crystalScar = new GameMapBuilder()
				.withMapId(8)
				.withMapName("The Crystal Scar")
				.build();
		GameMap summonersRiftNew = new GameMapBuilder()
				.withMapId(11)
				.withMapName("Summoner's Rift")
				.build();
		GameMap provingGrounds = new GameMapBuilder()
				.withMapId(12)
				.withMapName("Howling Abyss")
				.build();
		GameMap twistedTreeline = new GameMapBuilder()
				.withMapId(10)
				.withMapName("The Twisted Treeline")
				.build();

		assertThat(eligibleMaps(Arrays.asList(crystalScar, summonersRiftNew, provingGrounds, twistedTreeline)))
				.extracting("mapId")
				.containsExactly(provingGrounds.getMapId(), summonersRiftNew.getMapId(), twistedTreeline.getMapId());
	}

	@Test
	public void getsModeFromMap() {
		GameMap crystalScar = new GameMapBuilder()
				.withMapId(8)
				.withMapName("The Crystal Scar")
				.build();
		assertThat(getModeFromMap(crystalScar)).isEqualTo(CLASSIC);

		GameMap summonersRift = new GameMapBuilder()
				.withMapId(11)
				.withMapName("Summoner's Rift")
				.build();
		assertThat(getModeFromMap(summonersRift)).isEqualTo(CLASSIC);

		GameMap provingGrounds = new GameMapBuilder()
				.withMapId(12)
				.withMapName("Howling Abyss")
				.build();
		assertThat(getModeFromMap(provingGrounds)).isEqualTo(ARAM);

		assertThat(getModeFromMap(null)).isEqualTo(CLASSIC);
	}

	@Test
	public void getsNameFromId() {
		assertThat(getNameFromId(CRYSTAL_SCAR_ID)).isEqualTo(CRYSTAL_SCAR);
		assertThat(getNameFromId(TWISTED_TREELINE_ID)).isEqualTo(TWISTED_TREELINE);
		assertThat(getNameFromId(SUMMONERS_RIFT_ID)).isEqualTo(SUMMONERS_RIFT);
		assertThat(getNameFromId(HOWLING_ABYSS_ID)).isEqualTo(HOWLING_ABYSS);
	}

}