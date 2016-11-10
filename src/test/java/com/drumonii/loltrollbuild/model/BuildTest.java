package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.repository.BuildsRepository;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class BuildTest extends BaseSpringTestRunner {

	@Autowired
	private BuildsRepository buildsRepository;

	@After
	public void after() {
		buildsRepository.deleteAll();
	}

	@Test
	public void equals() {
		Build build1 = new Build();
		build1.setChampionId(championsResponse.getChampions().get("Braum").getId());
		build1.setItem1Id(itemsResponse.getItems().get("3009").getId());
		build1.setItem2Id(itemsResponse.getItems().get("3151").getId());
		build1.setItem3Id(itemsResponse.getItems().get("3142").getId());
		build1.setItem4Id(itemsResponse.getItems().get("3147").getId());
		build1.setItem5Id(itemsResponse.getItems().get("3157").getId());
		build1.setItem6Id(itemsResponse.getItems().get("3001").getId());
		build1.setSummonerSpell1Id(summonerSpellsResponse.getSummonerSpells().get("SummonerHeal").getId());
		build1.setSummonerSpell2Id(summonerSpellsResponse.getSummonerSpells().get("SummonerHaste").getId());
		build1.setTrinketId(itemsResponse.getItems().get("3341").getId());
		build1.setMapId(mapsResponse.getMaps().get(SUMMONERS_RIFT).getMapId());
		build1 = buildsRepository.save(build1);

		Build build2 = new Build();
		build2.setChampion(build1.getChampion());
		build2.setItem1Id(build1.getItem1Id());
		build2.setItem2Id(build1.getItem2Id());
		build2.setItem3Id(build1.getItem3Id());
		build2.setItem4Id(build1.getItem4Id());
		build2.setItem5Id(build1.getItem5Id());
		build2.setItem6Id(build1.getItem6Id());
		build2.setSummonerSpell1Id(build1.getSummonerSpell1Id());
		build2.setSummonerSpell2Id(build1.getSummonerSpell2Id());
		build2.setTrinketId(build1.getTrinketId());
		build2.setMapId(build1.getMapId());
		build2 = buildsRepository.save(build1);

		assertThat(build1).isEqualTo(build2);

		build2.setItem2Id(itemsResponse.getItems().get("3083").getId());
		assertThat(build1).isNotEqualTo(build2);
	}

}