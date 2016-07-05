package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class MapsRepositoryTest extends BaseSpringTestRunner {

	@Autowired
	private MapsRepository mapsRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@After
	public void after() {
		mapsRepository.deleteAll();
	}

	@Test
	public void crudOperations() throws IOException {
		String responseBody = "{\"type\":\"map\",\"version\":\"5.24.2\",\"data\":{\"10001\":{\"mapName\":" +
				"\"Cool Map\",\"mapId\":10001,\"image\":{\"full\":\"map10001.png\",\"sprite\":\"map0.png\",\"group\":" +
				"\"map\",\"x\":192,\"y\":0,\"w\":48,\"h\":48}}}}";
		MapsResponse mapsResponse = objectMapper.readValue(responseBody, MapsResponse.class);

		GameMap unmarshalledMap = mapsResponse.getMaps().get("10001");

		// Create
		assertThat(mapsRepository.save(unmarshalledMap)).isNotNull();

		// Select
		GameMap mapFromDb = mapsRepository.findOne(10001);
		assertThat(mapFromDb).isNotNull();
		assertThat(mapFromDb.getImage()).isNotNull();
		assertThat(mapFromDb).isEqualTo(unmarshalledMap);

		// Update
		mapFromDb.setMapName("NewMapName");
		mapsRepository.save(mapFromDb);
		mapFromDb = mapsRepository.findOne(10001);
		assertThat(mapFromDb.getMapName()).isEqualTo("NewMapName");

		// Delete
		mapsRepository.delete(mapFromDb);
		assertThat(mapsRepository.findOne(10001)).isNull();
	}

}