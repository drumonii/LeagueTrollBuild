package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ChampionsRepositoryTest extends BaseSpringTestRunner {

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void crudOperations() throws IOException {
		String responseBody = "{\"type\":\"champion\",\"version\":\"5.16.1\",\"data\":{\"Test\":{\"id\":10001,\"" +
				"key\":\"Test\",\"name\":\"Test\",\"title\":\"much Test Champion\",\"image\":{\"full\":" +
				"\"Test.png\",\"sprite\":\"champion0.png\",\"group\":\"champion\",\"x\":336,\"y\":0,\"w\":48," +
				"\"h\":48},\"tags\":[\"Testing1\",\"Testing2\"],\"partype\":\"TestParType\"}}}";
		ChampionsResponse championsResponse = objectMapper.readValue(responseBody, ChampionsResponse.class);

		Champion unmarshalChampion = championsResponse.getChampions().get("Test");

		// Create
		championsRepository.save(unmarshalChampion);

		// Select
		Champion championFromDb = championsRepository.findOne(10001);
		assertThat(championFromDb).isNotNull();
		assertThat(championFromDb.getTitle()).isEqualTo(StringUtils.capitalize(unmarshalChampion.getTitle()));
		unmarshalChampion.setTitle(championFromDb.getTitle()); // title from Riot is uncapitalized, set from the db's
		assertThat(championFromDb).isEqualToIgnoringNullFields(unmarshalChampion);

		// Update
		championFromDb.setPartype("Mana");
		championsRepository.save(championFromDb);
		championFromDb = championsRepository.findOne(10001);
		assertThat(championFromDb.getPartype()).isEqualTo("Mana");

		// Delete
		championsRepository.delete(championFromDb);
		assertThat(championsRepository.findOne(10001)).isNull();
	}

}
