package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.drumonii.loltrollbuild.test.repository.RepositoryTest;
import com.drumonii.loltrollbuild.util.GameMapUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@RepositoryTest
public abstract class MapsRepositoryTest {

	@Autowired
	private MapsRepository mapsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	private MapsResponse mapsResponse;

	protected abstract MapsResponse getMapsResponse();

	@Before
	public void before() {
		mapsResponse = getMapsResponse();
		mapsRepository.saveAll(mapsResponse.getMaps().values());
	}

	@Test
	public void forTrollBuild() {
		GameMap provingGrounds = mapsResponse.getMaps().get(GameMapUtil.HOWLING_ABYSS_SID);
		GameMap summonersRift = mapsResponse.getMaps().get(GameMapUtil.SUMMONERS_RIFT_SID);
		GameMap twistedTreeline = mapsResponse.getMaps().get(GameMapUtil.TWISTED_TREELINE_SID);

		List<GameMap> forTrollBuild = mapsRepository.forTrollBuild();

		assertThat(forTrollBuild).doesNotHaveDuplicates();
		assertThat(forTrollBuild)
				.containsExactly(provingGrounds, summonersRift, twistedTreeline);
	}

}