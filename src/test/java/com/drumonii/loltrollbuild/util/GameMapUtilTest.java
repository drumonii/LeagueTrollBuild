package com.drumonii.loltrollbuild.util;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.repository.MapsRepository;
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

	private static final String SUMMONERS_RIFT_OLD = String.valueOf(GameMapUtil.SUMMONERS_RIFT_ID);

	@Autowired
	private MapsRepository mapsRepository;

	@After
	public void after() {
		mapsRepository.deleteAll();
	}

	@Test
	public void getsActualMapNameFromGameMap() throws Exception {
		GameMap summonersRiftNewFromRiot = mapsResponse.getMaps().get(SUMMONERS_RIFT);
		assertThat(getActualMapName(SUMMONERS_RIFT)).isEqualTo("Summoner's Rift");
		assertThat(getActualMapName(summonersRiftNewFromRiot.getMapName())).isEqualTo("Summoner's Rift");

		GameMap crystalScar = mapsResponse.getMaps().get(CRYSTAL_SCAR);
		assertThat(getActualMapName(CRYSTAL_SCAR)).isEqualTo("Crystal Scar");
		assertThat(getActualMapName(crystalScar.getMapName())).isEqualTo("Crystal Scar");

		GameMap provingGroundsFromRiot = mapsResponse.getMaps().get(HOWLING_ABYSS);
		assertThat(getActualMapName(HOWLING_ABYSS)).isEqualTo("Howling Abyss");
		assertThat(getActualMapName(provingGroundsFromRiot.getMapName())).isEqualTo("Howling Abyss");

		GameMap summonersRiftOldFromRiot = mapsResponse.getMaps().get(SUMMONERS_RIFT_OLD);
		assertThat(getActualMapName(SUMMONERS_RIFT_OLD)).isEqualTo("Summoner's Rift");
		assertThat(getActualMapName(summonersRiftOldFromRiot.getMapName())).isEqualTo("Summoner's Rift");

		GameMap twistedTreeline = mapsResponse.getMaps().get(TWISTED_TREELINE);
		assertThat(getActualMapName(TWISTED_TREELINE)).isEqualTo("Twisted Treeline");
		assertThat(getActualMapName(twistedTreeline.getMapName())).isEqualTo("Twisted Treeline");
	}

	@Test
	public void getsEligibleMaps() throws Exception {
		GameMap crystalScar = mapsResponse.getMaps().get(CRYSTAL_SCAR);
		mapsRepository.save(crystalScar);

		GameMap summonersRift = mapsResponse.getMaps().get(SUMMONERS_RIFT_OLD);
		mapsRepository.save(summonersRift);

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

	@Test
	public void getsAvailableMaps() {
		Map<String, Boolean> maps = new HashMap<>();
		maps.put("1", false); // Summoner's Rift
		maps.put("10", true); // Twisted Treeline

		assertThat(getAvailableMaps(maps)).containsExactly("Twisted Treeline");
	}

}