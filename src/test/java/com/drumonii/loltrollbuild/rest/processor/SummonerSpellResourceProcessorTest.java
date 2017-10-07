package com.drumonii.loltrollbuild.rest.processor;

import com.drumonii.loltrollbuild.model.SummonerSpell;
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
public class SummonerSpellResourceProcessorTest {

	@InjectMocks
	private SummonerSpellResourceProcessor resourceProcessor;

	@Mock
	private RepositoryEntityLinks repositoryEntityLinks;

	@Test
	public void processesSummonerSpellResource() {
		SummonerSpell summonerSpell = new SummonerSpell();
		summonerSpell.setId(1);

		Resource<SummonerSpell> resourceToProcess = new Resource<>(summonerSpell);

		given(repositoryEntityLinks.linkToSingleResource(eq(SummonerSpell.class), eq(summonerSpell.getId())))
				.willReturn(new Link("http://localhost:8080/api/summoner-spells/" + summonerSpell.getId()));

		Resource<SummonerSpell> processedResource = resourceProcessor.process(resourceToProcess);
		assertThat(processedResource.getLinks()).hasSize(1);
		assertThat(processedResource.getLinks()).extracting(Link::getRel)
				.containsOnly("self");
		assertThat(processedResource.getLinks()).extracting(Link::getHref)
				.containsOnly("http://localhost:8080/api/summoner-spells/" + summonerSpell.getId());
	}

}