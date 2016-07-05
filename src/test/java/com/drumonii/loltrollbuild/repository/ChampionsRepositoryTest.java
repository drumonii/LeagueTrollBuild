package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ChampionsRepositoryTest extends BaseSpringTestRunner {

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@After
	public void after() {
		championsRepository.deleteAll();
	}

	@Test
	public void crudOperations() throws IOException {
		String responseBody = "{\"type\":\"champion\",\"version\":\"5.16.1\",\"data\":{\"Test\":{\"id\":10001,\"" +
				"key\":\"Test\",\"name\":\"Test\",\"title\":\"much Test Champion\",\"image\":{\"full\":" +
				"\"Test.png\",\"sprite\":\"champion0.png\",\"group\":\"champion\",\"x\":336,\"y\":0,\"w\":48," +
				"\"h\":48},\"tags\":[\"Testing1\",\"Testing2\"],\"partype\":\"TestParType\"}}}";
		ChampionsResponse championsResponse = objectMapper.readValue(responseBody, ChampionsResponse.class);

		Champion unmarshalledChampion = championsResponse.getChampions().get("Test");

		// Create
		assertThat(championsRepository.save(unmarshalledChampion)).isNotNull();

		// Select
		Champion championFromDb = championsRepository.findOne(10001);
		assertThat(championFromDb).isNotNull();
		assertThat(championFromDb.getKey()).isEqualTo(unmarshalledChampion.getKey());
		assertThat(championFromDb.getImage()).isNotNull();
		assertThat(championFromDb.getTitle()).isEqualTo(unmarshalledChampion.getTitle());
		assertThat(championFromDb).isEqualToIgnoringNullFields(unmarshalledChampion);

		// Update
		championFromDb.setPartype("Mana");
		championsRepository.save(championFromDb);
		championFromDb = championsRepository.findOne(10001);
		assertThat(championFromDb.getPartype()).isEqualTo("Mana");

		// Delete
		championsRepository.delete(championFromDb);
		assertThat(championsRepository.findOne(10001)).isNull();
	}

	@Test
	public void findByName() throws IOException {
		String responseBody = "{\"type\":\"champion\",\"version\":\"5.16.1\",\"data\":{\"Velkoz\":{\"id\":161," +
				"\"key\":\"Velkoz\",\"name\":\"Vel'Koz\",\"title\":\"the Eye of the Void\",\"image\":{\"full\":" +
				"\"Velkoz.png\",\"sprite\":\"champion3.png\",\"group\":\"champion\",\"x\":384,\"y\":48,\"w\":48," +
				"\"h\":48},\"tags\":[\"Mage\"],\"partype\":\"Mana\"}}}";
		Champion velKoz = objectMapper.readValue(responseBody, ChampionsResponse.class).getChampions().get("Velkoz");
		championsRepository.save(velKoz);

		velKoz = championsRepository.findByName("VelKoz");
		assertThat(velKoz).isNotNull();

		velKoz = championsRepository.findByName("vEl'kOz");
		assertThat(velKoz).isNotNull();
	}

	@Test
	public void getTags() throws IOException {
		String responseBody = "{\"type\":\"champion\",\"version\":\"5.23.1\",\"data\":{\"Zac\":{\"id\":154,\"key\":" +
				"\"Zac\",\"name\":\"Zac\",\"title\":\"the Secret Weapon\",\"image\":{\"full\":\"Zac.png\",\"sprite\":" +
				"\"champion4.png\",\"group\":\"champion\",\"x\":288,\"y\":0,\"w\":48,\"h\":48},\"tags\":[\"Tank\"," +
				"\"Fighter\"],\"partype\":\"None\"}}}";
		championsRepository.save(objectMapper.readValue(responseBody, ChampionsResponse.class).getChampions()
				.get("Zac"));
		responseBody = "{\"type\":\"champion\",\"version\":\"5.23.1\",\"data\":{\"Leona\":{\"id\":89,\"key\":" +
				"\"Leona\",\"name\":\"Leona\",\"title\":\"the Radiant Dawn\",\"image\":{\"full\":\"Leona.png\"," +
				"\"sprite\":\"champion1.png\",\"group\":\"champion\",\"x\":336,\"y\":96,\"w\":48,\"h\":48},\"tags\":" +
				"[\"Tank\",\"Support\"],\"partype\":\"MP\"}}}";
		championsRepository.save(objectMapper.readValue(responseBody, ChampionsResponse.class).getChampions()
				.get("Leona"));

		assertThat(championsRepository.getTags()).containsOnly("Tank", "Fighter", "Support");
	}

}
