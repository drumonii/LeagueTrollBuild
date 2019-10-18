package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.test.repository.RepositoryTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
abstract class ChampionsRepositoryTest {

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	private ChampionsResponse championsResponse;

	protected abstract ChampionsResponse getChampionsResponse();

	@BeforeEach
	void beforeEach() {
		championsResponse = getChampionsResponse();
		championsRepository.saveAll(championsResponse.getChampions().values());
	}

	@Test
	void findByName() {
		Optional<Champion> velKoz = championsRepository.findByName("VelKoz");
		assertThat(velKoz).isPresent();

		velKoz = championsRepository.findByName("vEl'kOz");
		assertThat(velKoz).isPresent();
	}

	@Test
	void getTags() {
		assertThat(championsRepository.getTags())
				.containsOnly("Assassin", "Fighter", "Mage", "Marksman", "Support", "Tank");
	}

}
