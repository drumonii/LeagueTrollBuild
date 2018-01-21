package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.config.JpaConfig;
import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest(includeFilters = @Filter(JsonComponent.class))
@ImportAutoConfiguration(JacksonAutoConfiguration.class)
@Import(JpaConfig.class)
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
		championsRepository.save(championsResponse.getChampions().values());
	}

	@Test
	public void findByName() {
		Champion velKoz = championsRepository.findByName("VelKoz");
		assertThat(velKoz).isNotNull();

		velKoz = championsRepository.findByName("vEl'kOz");
		assertThat(velKoz).isNotNull();
	}

	@Test
	public void getTags() {
		assertThat(championsRepository.getTags())
				.containsOnly("Mage", "Tank", "Marksman", "Fighter", "Assassin", "Support");
	}

}
