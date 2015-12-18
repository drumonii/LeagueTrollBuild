package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.image.ChampionImage;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.ListUtils;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

	@Test
	public void cardinality() throws IOException {
		String responseBody = "{\"type\":\"champion\",\"version\":\"5.8.1\",\"data\":{\"Gnar\":{\"id\":150,\"key\":" +
				"\"Gnar\",\"name\":\"Gnar\",\"title\":\"the Missing Link\",\"image\":{\"full\":\"Gnar.png\"," +
				"\"sprite\":\"champion0.png\",\"group\":\"champion\",\"x\":432,\"y\":96,\"w\":48,\"h\":48},\"tags\":" +
				"[\"Fighter\",\"Marksman\"],\"partype\":\"Gnarfury\"}}}";
		Champion gnarFromRiot = objectMapper.readValue(responseBody, ChampionsResponse.class).getChampions()
				.get("Gnar");
		championsRepository.save(gnarFromRiot);
		Champion gnarFromDb = championsRepository.findOne(gnarFromRiot.getId());


		responseBody = "{\"type\":\"champion\",\"version\":\"5.24.2\",\"data\":{\"Graves\":{\"id\":104,\"key\":" +
				"\"Graves\",\"name\":\"Graves\",\"title\":\"the Outlaw\",\"image\":{\"full\":\"Graves.png\"," +
				"\"sprite\":\"champion1.png\",\"group\":\"champion\",\"x\":48,\"y\":0,\"w\":48,\"h\":48},\"tags\":" +
				"[\"Marksman\"],\"partype\":\"MP\"}}}";
		Champion gravesFromRiot = objectMapper.readValue(responseBody, ChampionsResponse.class).getChampions()
				.get("Graves");
		championsRepository.save(gravesFromRiot);
		Champion gravesFromDb = championsRepository.findOne(gravesFromRiot.getId());
		
		responseBody = "{\"type\":\"champion\",\"version\":\"5.22.3\",\"data\":{\"Evelynn\":{\"id\":28,\"key\":" +
				"\"Evelynn\",\"name\":\"Evelynn\",\"title\":\"the Widowmaker\",\"image\":{\"full\":\"Evelynn.png\"," +
				"\"sprite\":\"champion0.png\",\"group\":\"champion\",\"x\":48,\"y\":96,\"w\":48,\"h\":48},\"tags\":" +
				"[\"Assassin\",\"Mage\"],\"partype\":\"Mana\"}}}";
		Champion evelynnFromRiot = objectMapper.readValue(responseBody, ChampionsResponse.class).getChampions()
				.get("Evelynn");
		championsRepository.save(evelynnFromRiot);
		Champion evelynnFromDb = championsRepository.findOne(evelynnFromRiot.getId());

		// Gnar, Illaoi, and Graves
		List<Champion> championsFromDb = Arrays.asList(gnarFromDb, gravesFromDb, evelynnFromDb);
		
		responseBody = "{\"type\":\"champion\",\"version\":\"5.24.2\",\"data\":{\"Gnar\":{\"id\":150,\"key\":" +
				"\"Gnar\",\"name\":\"Gnar\",\"title\":\"the Missing Link\",\"image\":{\"full\":\"Gnar.png\"," +
				"\"sprite\":\"champion0.png\",\"group\":\"champion\",\"x\":432,\"y\":96,\"w\":48,\"h\":48},\"tags\":" +
				"[\"Fighter\",\"Tank\"],\"partype\":\"Gnarfury\"}}}";
		gnarFromRiot = objectMapper.readValue(responseBody, ChampionsResponse.class).getChampions().get("Gnar");

		responseBody = "{\"type\":\"champion\",\"version\":\"5.22.3\",\"data\":{\"Illaoi\":{\"id\":420," +
				"\"key\":\"Illaoi\",\"name\":\"Illaoi\",\"title\":\"the Kraken Priestess\",\"image\":{\"full\":" +
				"\"Illaoi.png\",\"sprite\":\"champion4.png\",\"group\":\"champion\",\"x\":336,\"y\":0,\"w\":48,\"h\":" +
				"48},\"tags\":[\"Fighter\",\"Tank\"],\"partype\":\"Mana\"}}}";
		Champion illaoiFromRiot = objectMapper.readValue(responseBody, ChampionsResponse.class).getChampions()
				.get("Illaoi");

		// Updated Gnar, same Graves, "new" Illaoi, and no Evelynn
		List<Champion> championsFromRiot = Arrays.asList(gnarFromRiot, gravesFromRiot, illaoiFromRiot);

		List<Champion> deletedChampions = ListUtils.subtract(championsFromDb, championsFromRiot);
		assertThat(deletedChampions).hasSize(2);
		assertThat(deletedChampions).containsOnly(gnarFromDb, evelynnFromDb);

		List<Champion> unmodifiedChampions = ListUtils.intersection(championsFromDb, championsFromRiot);
		assertThat(unmodifiedChampions).hasSize(1);
		assertThat(unmodifiedChampions).containsOnly(gravesFromDb);

		List<Champion> championsToUpdate = ListUtils.subtract(championsFromRiot, championsFromDb);
		assertThat(championsToUpdate).hasSize(2);
		assertThat(championsToUpdate).containsOnly(gnarFromRiot, illaoiFromRiot);
	}

}