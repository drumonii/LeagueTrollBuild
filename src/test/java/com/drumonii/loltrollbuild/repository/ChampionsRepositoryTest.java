package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.test.repository.RepositoryTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@RepositoryTest
public abstract class ChampionsRepositoryTest {

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	private ChampionsResponse championsResponse;

	protected abstract ChampionsResponse getChampionsResponse();

	@Before
	public void before() {
		championsResponse = getChampionsResponse();
		championsRepository.saveAll(championsResponse.getChampions().values());
	}

	@Test
	public void findByName() {
		Optional<Champion> velKoz = championsRepository.findByName("VelKoz");
		assertThat(velKoz).isPresent();

		velKoz = championsRepository.findByName("vEl'kOz");
		assertThat(velKoz).isPresent();
	}

	@Test
	public void getTags() {
		assertThat(championsRepository.getTags())
				.containsOnly("Mage", "Tank", "Marksman", "Fighter", "Assassin", "Support");
	}

}
