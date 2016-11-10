package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Build;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class BuildsRepositoryTest extends BaseSpringTestRunner {

	@Autowired
	private BuildsRepository buildsRepository;

	@After
	public void after() {
		buildsRepository.deleteAll();
	}

	@Test
	public void crudOperations() {
		Build build = new Build();
		build.setChampionId(championsResponse.getChampions().get("Jax").getId());
		build.setItem1Id(itemsResponse.getItems().get("3006").getId());
		build.setItem2Id(itemsResponse.getItems().get("3075").getId());
		build.setItem3Id(itemsResponse.getItems().get("3748").getId());
		build.setItem4Id(itemsResponse.getItems().get("3812").getId());
		build.setItem5Id(itemsResponse.getItems().get("3085").getId());
		build.setItem6Id(itemsResponse.getItems().get("3152").getId());
		build.setSummonerSpell1Id(summonerSpellsResponse.getSummonerSpells().get("SummonerSmite").getId());
		build.setSummonerSpell2Id(summonerSpellsResponse.getSummonerSpells().get("SummonerTeleport").getId());
		build.setTrinketId(itemsResponse.getItems().get("3340").getId());
		build.setMapId(mapsResponse.getMaps().get(SUMMONERS_RIFT).getMapId());

		// Create
		build = buildsRepository.save(build);
		assertThat(build).isNotNull();

		// Select
		Build buildFromDb = buildsRepository.findOne(build.getId());
		assertThat(buildFromDb).isNotNull();
		assertThat(buildFromDb).isEqualTo(build);

		// Update
		buildFromDb.setItem2Id(itemsResponse.getItems().get("3190").getId());
		buildFromDb = buildsRepository.findOne(build.getId());
		assertThat(buildFromDb.getItem2Id()).isEqualTo(itemsResponse.getItems().get("3190").getId());

		// Delete
		buildsRepository.delete(buildFromDb.getId());
		assertThat(buildsRepository.findOne(build.getId())).isNull();
	}

}