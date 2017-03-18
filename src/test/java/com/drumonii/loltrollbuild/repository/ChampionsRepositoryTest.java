package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Champion;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ChampionsRepositoryTest extends BaseSpringTestRunner {

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
