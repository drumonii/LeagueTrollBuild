package com.drumonii.loltrollbuild.riot.api;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.image.GameMapImage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.assertj.core.data.MapEntry.entry;

public class MapsResponseTest extends BaseSpringTestRunner {

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void mapsResponseFromRiotIsMapped() {
		String responseBody = "{\"type\":\"map\",\"version\":\"5.24.2\",\"data\":{\"11\":{\"mapName\":" +
				"\"SummonersRiftNew\",\"mapId\":11,\"image\":{\"full\":\"map11.png\",\"sprite\":\"map0.png\"," +
				"\"group\":\"map\",\"x\":144,\"y\":0,\"w\":48,\"h\":48}}}}";
		MapsResponse mapsResponse = null;
		try {
			mapsResponse = objectMapper.readValue(responseBody, MapsResponse.class);
		} catch (IOException e) {
			fail("Unable to unmarshal the Maps response.", e);
		}

		assertThat(mapsResponse).isNotNull();
		assertThat(mapsResponse).isEqualToComparingOnlyGivenFields(new MapsResponse("map", "5.24.2"),
				"type", "version");
		assertThat(mapsResponse.getMaps()).hasSize(1);
		GameMap map = new GameMap(11, "SummonersRiftNew", null, new GameMapImage("map11.png", "map0.png", "map",
				new byte[0], 144, 0, 48, 48));
		assertThat(mapsResponse.getMaps()).containsExactly(entry("11", map));
	}

}