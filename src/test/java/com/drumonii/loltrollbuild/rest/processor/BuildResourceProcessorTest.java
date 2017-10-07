package com.drumonii.loltrollbuild.rest.processor;

import com.drumonii.loltrollbuild.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class BuildResourceProcessorTest {

	@InjectMocks
	private BuildResourceProcessor resourceProcessor;

	@Mock
	private RepositoryEntityLinks repositoryEntityLinks;

	@Test
	public void processesBuildResource() {
		Build build = new Build();
		build.setId(1);

		Resource<Build> resourceToProcess = new Resource<>(build);

		given(repositoryEntityLinks.linkToSingleResource(eq(Champion.class), eq(build.getChampionId())))
				.willReturn(new Link("http://localhost:8080/api/champions/" + build.getChampionId()));

		given(repositoryEntityLinks.linkToSingleResource(eq(Item.class), eq(build.getItem1Id())))
				.willReturn(new Link("http://localhost:8080/api/items/" + build.getItem1Id()));

		given(repositoryEntityLinks.linkToSingleResource(eq(Item.class), eq(build.getItem2Id())))
				.willReturn(new Link("http://localhost:8080/api/items/" + build.getItem2Id()));
		given(repositoryEntityLinks.linkToSingleResource(eq(Item.class), eq(build.getItem3Id())))
				.willReturn(new Link("http://localhost:8080/api/items/" + build.getItem3Id()));
		given(repositoryEntityLinks.linkToSingleResource(eq(Item.class), eq(build.getItem4Id())))
				.willReturn(new Link("http://localhost:8080/api/items/" + build.getItem4Id()));
		given(repositoryEntityLinks.linkToSingleResource(eq(Item.class), eq(build.getItem5Id())))
				.willReturn(new Link("http://localhost:8080/api/items/" + build.getItem5Id()));
		given(repositoryEntityLinks.linkToSingleResource(eq(Item.class), eq(build.getItem6Id())))
				.willReturn(new Link("http://localhost:8080/api/items/" + build.getItem6Id()));

		given(repositoryEntityLinks.linkToSingleResource(eq(SummonerSpell.class), eq(build.getSummonerSpell1Id())))
				.willReturn(new Link("http://localhost:8080/api/summoner-spells/" + build.getSummonerSpell1Id()));
		given(repositoryEntityLinks.linkToSingleResource(eq(SummonerSpell.class), eq(build.getSummonerSpell2Id())))
				.willReturn(new Link("http://localhost:8080/api/summoner-spells/" + build.getSummonerSpell2Id()));

		given(repositoryEntityLinks.linkToSingleResource(eq(Item.class), eq(build.getTrinketId())))
				.willReturn(new Link("http://localhost:8080/api/items/" + build.getTrinketId()));

		given(repositoryEntityLinks.linkToSingleResource(eq(GameMap.class), eq(build.getMapId())))
				.willReturn(new Link("http://localhost:8080/api/maps/" + build.getMapId()));

		given(repositoryEntityLinks.linkToSingleResource(eq(Build.class), eq(build.getId())))
				.willReturn(new Link("http://localhost:8080/api/builds/" + build.getId()));

		Resource<Build> processedResource = resourceProcessor.process(resourceToProcess);
		assertThat(processedResource.getLinks()).hasSize(12);
		assertThat(processedResource.getLinks()).extracting(Link::getRel)
				.containsOnly("champion", "item1", "item2", "item3", "item4", "item5", "item6", "summonerSpell1",
						"summonerSpell2", "trinket", "map", "self");
		assertThat(processedResource.getLinks()).extracting(Link::getHref)
				.containsOnly("http://localhost:8080/api/champions/" + build.getChampionId(),
						"http://localhost:8080/api/items/" + build.getItem1Id(),
						"http://localhost:8080/api/items/" + build.getItem2Id(),
						"http://localhost:8080/api/items/" + build.getItem3Id(),
						"http://localhost:8080/api/items/" + build.getItem4Id(),
						"http://localhost:8080/api/items/" + build.getItem5Id(),
						"http://localhost:8080/api/items/" + build.getItem6Id(),
						"http://localhost:8080/api/summoner-spells/" + build.getSummonerSpell1Id(),
						"http://localhost:8080/api/summoner-spells/" + build.getSummonerSpell2Id(),
						"http://localhost:8080/api/items/" + build.getTrinketId(),
						"http://localhost:8080/api/maps/" + build.getMapId(),
						"http://localhost:8080/api/builds/" + build.getId());
	}

}