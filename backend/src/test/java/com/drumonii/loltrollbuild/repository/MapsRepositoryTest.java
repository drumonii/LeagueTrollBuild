package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.drumonii.loltrollbuild.test.repository.RepositoryTest;
import com.drumonii.loltrollbuild.util.GameMapUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
abstract class MapsRepositoryTest {

	@Autowired
	private MapsRepository mapsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	private MapsResponse mapsResponse;

	protected abstract MapsResponse getMapsResponse();

	@BeforeEach
	void beforeEach() {
		mapsResponse = getMapsResponse();
		mapsRepository.saveAll(mapsResponse.getMaps().values());
	}

	@Test
	void forTrollBuild() {
		GameMap provingGrounds = mapsResponse.getMaps().get(GameMapUtil.HOWLING_ABYSS_SID);
		GameMap summonersRift = mapsResponse.getMaps().get(GameMapUtil.SUMMONERS_RIFT_SID);

		List<GameMap> forTrollBuild = mapsRepository.forTrollBuild();

		assertThat(forTrollBuild).doesNotHaveDuplicates();
		assertThat(forTrollBuild)
				.containsExactly(provingGrounds, summonersRift);
	}

}
