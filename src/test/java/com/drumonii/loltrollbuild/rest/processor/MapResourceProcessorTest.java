package com.drumonii.loltrollbuild.rest.processor;

import com.drumonii.loltrollbuild.model.GameMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class MapResourceProcessorTest {

	@InjectMocks
	private MapResourceProcessor resourceProcessor;

	@Mock
	private RepositoryEntityLinks repositoryEntityLinks;

	@Test
	public void processesMapResource() {
		GameMap gameMap = new GameMap();
		gameMap.setMapId(1);

		Resource<GameMap> resourceToProcess = new Resource<>(gameMap);

		given(repositoryEntityLinks.linkToSingleResource(eq(GameMap.class), eq(gameMap.getMapId())))
				.willReturn(new Link("http://localhost:8080/api/maps/" + gameMap.getMapId()));

		Resource<GameMap> processedResource = resourceProcessor.process(resourceToProcess);
		assertThat(processedResource.getLinks()).hasSize(1);
		assertThat(processedResource.getLinks()).extracting(Link::getRel)
				.containsOnly("self");
		assertThat(processedResource.getLinks()).extracting(Link::getHref)
				.containsOnly("http://localhost:8080/api/maps/" + gameMap.getMapId());
	}

}