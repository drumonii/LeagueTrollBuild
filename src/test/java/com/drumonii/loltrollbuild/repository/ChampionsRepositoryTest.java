package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

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
		championsRepository.save(unmarshalledChampion);

		// Select
		Champion championFromDb = championsRepository.findOne(10001);
		assertThat(championFromDb).isNotNull();
		assertThat(championFromDb.getTitle()).isEqualTo(StringUtils.capitalize(unmarshalledChampion.getTitle()));
		unmarshalledChampion.setTitle(championFromDb.getTitle()); // title from Riot is uncapitalized, set from the db's
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
	public void findByLikeName() throws IOException {
		String responseBody = "{\"type\":\"champion\",\"version\":\"5.16.1\",\"data\":{\"FiddleSticks\":{\"id\":9," +
				"\"key\":\"FiddleSticks\",\"name\":\"Fiddlesticks\",\"title\":\"the Harbinger of Doom\",\"image\":" +
				"{\"full\":\"FiddleSticks.png\",\"sprite\":\"champion0.png\",\"group\":\"champion\",\"x\":144," +
				"\"y\":96,\"w\":48,\"h\":48},\"tags\":[\"Mage\",\"Support\"],\"partype\":\"Mana\"}}}";
		Champion fiddleSticks = objectMapper.readValue(responseBody, ChampionsResponse.class).getChampions()
				.get("FiddleSticks");
		championsRepository.save(fiddleSticks);
		responseBody = "{\"type\":\"champion\",\"version\":\"5.16.1\",\"data\":{\"Fizz\":{\"id\":105,\"key\":" +
				"\"Fizz\",\"name\":\"Fizz\",\"title\":\"the Tidal Trickster\",\"image\":{\"full\":\"Fizz.png\"," +
				"\"sprite\":\"champion0.png\",\"group\":\"champion\",\"x\":240,\"y\":96,\"w\":48,\"h\":48},\"tags\":" +
				"[\"Assassin\",\"Fighter\"],\"partype\":\"Mana\"}}}";
		Champion fizz = objectMapper.readValue(responseBody, ChampionsResponse.class).getChampions().get("Fizz");
		championsRepository.save(fizz);

		List<Champion> champions = championsRepository.findByLikeName("fi");
		assertThat(champions).isNotEmpty();

		champions = championsRepository.findByLikeName("FI");
		assertThat(champions).isNotEmpty();
	}

}
