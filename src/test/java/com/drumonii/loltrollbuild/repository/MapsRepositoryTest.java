package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.GameMap;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class MapsRepositoryTest extends BaseSpringTestRunner {

	@Autowired
	private MapsRepository mapsRepository;

	@After
	public void after() {
		mapsRepository.deleteAll();
	}

	@Test
	public void crudOperations() throws IOException {
		GameMap crystalScar = mapsResponse.getMaps().get(CRYSTAL_SCAR);

		// Create
		assertThat(mapsRepository.save(crystalScar)).isNotNull();

		// Select
		GameMap mapFromDb = mapsRepository.findOne(crystalScar.getMapId());
		assertThat(mapFromDb).isNotNull();
		assertThat(mapFromDb.getImage()).isNotNull();
		assertThat(mapFromDb).isEqualTo(crystalScar);

		// Update
		mapFromDb.setMapName("NewMapName");
		mapsRepository.save(mapFromDb);
		mapFromDb = mapsRepository.findOne(crystalScar.getMapId());
		assertThat(mapFromDb.getMapName()).isEqualTo("NewMapName");

		// Delete
		mapsRepository.delete(mapFromDb.getMapId());
		assertThat(mapsRepository.findOne(crystalScar.getMapId())).isNull();
	}

}