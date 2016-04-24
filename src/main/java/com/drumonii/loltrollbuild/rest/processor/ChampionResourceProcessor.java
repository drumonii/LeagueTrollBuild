package com.drumonii.loltrollbuild.rest.processor;

import com.drumonii.loltrollbuild.model.Champion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

/**
 * {@link ResourceProcessor} to add only a single self {@link Link} to a {@link Champion} {@link Resource}.
 */
@Component
public class ChampionResourceProcessor implements ResourceProcessor<Resource<Champion>> {

	@Autowired
	private RepositoryEntityLinks repositoryEntityLinks;

	@Override
	public Resource<Champion> process(Resource<Champion> resource) {
		return new Resource<>(resource.getContent(),
				repositoryEntityLinks.linkToSingleResource(Champion.class, resource.getContent().getId())
						.withSelfRel());
	}

}
