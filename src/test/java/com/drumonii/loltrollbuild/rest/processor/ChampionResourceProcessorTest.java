package com.drumonii.loltrollbuild.rest.processor;

import com.drumonii.loltrollbuild.model.Champion;
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
public class ChampionResourceProcessorTest {

	@InjectMocks
	private ChampionResourceProcessor resourceProcessor;

	@Mock
	private RepositoryEntityLinks repositoryEntityLinks;

	@Test
	public void processesChampionResource() {
		Champion champion = new Champion();
		champion.setId(1);

		Resource<Champion> resourceToProcess = new Resource<>(champion);

		given(repositoryEntityLinks.linkToSingleResource(eq(Champion.class), eq(champion.getId())))
				.willReturn(new Link("http://localhost:8080/api/champions/" + champion.getId()));

		Resource<Champion> processedResource = resourceProcessor.process(resourceToProcess);
		assertThat(processedResource.getLinks()).hasSize(1);
		assertThat(processedResource.getLinks()).extracting(Link::getRel)
				.containsOnly("self");
		assertThat(processedResource.getLinks()).extracting(Link::getHref)
				.containsOnly("http://localhost:8080/api/champions/" + champion.getId());
	}

}