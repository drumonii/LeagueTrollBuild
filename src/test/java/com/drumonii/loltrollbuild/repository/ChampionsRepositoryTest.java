package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Champion;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ChampionsRepositoryTest extends BaseSpringTestRunner {

	@Autowired
	private ChampionsRepository championsRepository;

	@After
	public void after() {
		championsRepository.deleteAll();
	}

	@Test
	public void crudOperations() throws IOException {
		Champion sejuani = championsResponse.getChampions().get("Sejuani");

		// Create
		assertThat(championsRepository.save(sejuani)).isNotNull();

		// Select
		Champion championFromDb = championsRepository.findOne(sejuani.getId());
		assertThat(championFromDb).isNotNull();
		assertThat(championFromDb.getKey()).isEqualTo(sejuani.getKey());
		assertThat(championFromDb.getImage()).isNotNull();
		assertThat(championFromDb.getTitle()).isEqualTo(sejuani.getTitle());
		assertThat(championFromDb).isEqualToIgnoringNullFields(sejuani);

		// Update
		championFromDb.setPartype("Mana");
		championsRepository.save(championFromDb);
		championFromDb = championsRepository.findOne(sejuani.getId());
		assertThat(championFromDb.getPartype()).isEqualTo("Mana");

		// Delete
		championsRepository.delete(championFromDb.getId());
		assertThat(championsRepository.findOne(sejuani.getId())).isNull();
	}

	@Test
	public void findByName() throws IOException {
		Champion velKoz = championsResponse.getChampions().get("Velkoz");
		championsRepository.save(velKoz);

		velKoz = championsRepository.findByName("VelKoz");
		assertThat(velKoz).isNotNull();

		velKoz = championsRepository.findByName("vEl'kOz");
		assertThat(velKoz).isNotNull();
	}

	@Test
	public void getTags() throws IOException {
		championsRepository.save(championsResponse.getChampions().get("Zac"));
		championsRepository.save(championsResponse.getChampions().get("Leona"));

		assertThat(championsRepository.getTags()).containsOnly("Tank", "Fighter", "Support");
	}

}
