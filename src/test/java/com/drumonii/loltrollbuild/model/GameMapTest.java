package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.image.GameMapImage;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

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

		twistedTreeline.setImage(new GameMapImage("NewTwistedTreeline.png", "map0.png", "map", 0, 0, 48, 48));
		assertThat(twistedTreeline).isNotEqualTo(twistedTreelineFromDb);
	}

}