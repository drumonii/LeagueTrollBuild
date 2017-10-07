package com.drumonii.loltrollbuild.rest.processor;

import com.drumonii.loltrollbuild.model.Version;
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
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class VersionResourceProcessorTest {

	@InjectMocks
	private VersionResourceProcessor resourceProcessor;

	@Mock
	private RepositoryEntityLinks repositoryEntityLinks;

	@Test
	public void processesVersionResource() {
		Version version = new Version("6.21.1");
		Resource<Version> resourceToProcess = new Resource<>(version);

		given(repositoryEntityLinks.linkToSingleResource(eq(Version.class), anyString()))
				.willReturn(new Link("http://localhost/api/versions/" + version.getPatch()));

		Resource<Version> processedResource = resourceProcessor.process(resourceToProcess);
		assertThat(processedResource.getLinks()).hasSize(1);
		assertThat(processedResource.getLinks()).extracting(Link::getRel)
				.containsOnly("self");
		assertThat(processedResource.getLinks()).extracting(Link::getHref)
				.containsOnly("http://localhost/api/versions/" + version.getPatch());
	}

}