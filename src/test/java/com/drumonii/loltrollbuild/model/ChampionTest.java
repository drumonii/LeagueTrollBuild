package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.image.ChampionImage;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ChampionTest extends BaseSpringTestRunner {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ChampionsRepository championsRepository;

	@After
	public void after() {
		championsRepository.deleteAll();
	}

	@Test
	public void isViktor() throws IOException {
		String responseBody = "{\"type\":\"champion\",\"version\":\"5.16.1\",\"data\":{\"Viktor\":{\"id\":112," +
				"\"key\":\"Viktor\",\"name\":\"Viktor\",\"title\":\"the Machine Herald\",\"image\":{\"full\":" +
				"\"Viktor.png\",\"sprite\":\"champion3.png\",\"group\":\"champion\",\"x\":0,\"y\":96,\"w\":48," +
				"\"h\":48},\"tags\":[\"Mage\"],\"partype\":\"Mana\"}}}";
		Champion viktor = objectMapper.readValue(responseBody, ChampionsResponse.class).getChampions().get("Viktor");
		assertThat(viktor.isViktor()).isTrue();

		responseBody = "{\"type\":\"champion\",\"version\":\"5.16.1\",\"data\":{\"Rammus\":{\"id\":33,\"key\":" +
				"\"Rammus\",\"name\":\"Rammus\",\"title\":\"the Armordillo\",\"image\":{\"full\":\"Rammus.png\"," +
				"\"sprite\":\"champion2.png\",\"group\":\"champion\",\"x\":240,\"y\":48,\"w\":48,\"h\":48},\"tags\":" +
				"[\"Tank\",\"Fighter\"],\"partype\":\"Mana\"}}}";
		Champion rammus = objectMapper.readValue(responseBody, ChampionsResponse.class).getChampions().get("Rammus");
		assertThat(rammus.isViktor()).isFalse();
	}

	@Test
	public void equals() throws IOException {
		String responseBody = "{\"type\":\"champion\",\"version\":\"5.16.1\",\"data\":{\"Nasus\":{\"id\":75,\"key\":" +
				"\"Nasus\",\"name\":\"Nasus\",\"title\":\"the Curator of the Sands\",\"image\":{\"full\":" +
				"\"Nasus.png\",\"sprite\":\"champion2.png\",\"group\":\"champion\",\"x\":240,\"y\":0,\"w\":48," +
				"\"h\":48},\"tags\":[\"Fighter\",\"Tank\"],\"partype\":\"Mana\"}}}";
		Champion nasusFromRiot = objectMapper.readValue(responseBody, ChampionsResponse.class).getChampions()
				.get("Nasus");
		championsRepository.save(nasusFromRiot);

		Champion nasusFromDb = championsRepository.findByName(nasusFromRiot.getName());
		assertThat(nasusFromRiot).isEqualTo(nasusFromDb);

		nasusFromRiot.setImage(new ChampionImage("Nasus.png", "champion1.png", "champion", 336, 48, 48, 48));
		assertThat(nasusFromRiot).isNotEqualTo(nasusFromDb);
	}

}