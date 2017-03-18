package com.drumonii.loltrollbuild.util;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.GameMap;
import org.junit.Test;

import static com.drumonii.loltrollbuild.model.SummonerSpell.GameMode.ARAM;
import static com.drumonii.loltrollbuild.model.SummonerSpell.GameMode.CLASSIC;
import static com.drumonii.loltrollbuild.util.GameMapUtil.eligibleMaps;
import static com.drumonii.loltrollbuild.util.GameMapUtil.getModeFromMap;
import static org.assertj.core.api.Assertions.assertThat;

public class GameMapUtilTest extends BaseSpringTestRunner {

	@Test
	public void getsEligibleMaps() throws Exception {
		GameMap crystalScar = mapsResponse.getMaps().get(CRYSTAL_SCAR);
		mapsRepository.save(crystalScar);

		GameMap summonersRiftNew = mapsResponse.getMaps().get(SUMMONERS_RIFT);
		mapsRepository.save(summonersRiftNew);

		GameMap provingGrounds = mapsResponse.getMaps().get(HOWLING_ABYSS);
		mapsRepository.save(provingGrounds);

		GameMap twistedTreeline = mapsResponse.getMaps().get(TWISTED_TREELINE);
		mapsRepository.save(twistedTreeline);

		assertThat(eligibleMaps(mapsRepository.findAll())).extracting("mapId")
				.containsExactly(provingGrounds.getMapId(), summonersRiftNew.getMapId(), twistedTreeline.getMapId());
	}

	@Test
	public void getsModeFromMap() throws Exception {
		GameMap crystalScar = mapsResponse.getMaps().get(CRYSTAL_SCAR);
		mapsRepository.save(crystalScar);

		assertThat(getModeFromMap(mapsRepository.findOne(crystalScar.getMapId()))).isEqualTo(CLASSIC);

		GameMap summonersRift = mapsResponse.getMaps().get(SUMMONERS_RIFT);
		mapsRepository.save(summonersRift);

		assertThat(getModeFromMap(mapsRepository.findOne(summonersRift.getMapId()))).isEqualTo(CLASSIC);

		GameMap provingGrounds = mapsResponse.getMaps().get(HOWLING_ABYSS);
		mapsRepository.save(provingGrounds);

		assertThat(getModeFromMap(mapsRepository.findOne(provingGrounds.getMapId()))).isEqualTo(ARAM);
	}

}