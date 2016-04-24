package com.drumonii.loltrollbuild.rest.processor;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

/**
 * {@link ResourceProcessor} to add only a single self {@link Link} to a {@link SummonerSpell} {@link Resource}.
 */
@Component
public class SummonerSpellResourceProcessor implements ResourceProcessor<Resource<SummonerSpell>> {

	@Autowired
	private RepositoryEntityLinks repositoryEntityLinks;

	@Override
	public Resource<SummonerSpell> process(Resource<SummonerSpell> resource) {
		return new Resource<>(resource.getContent(),
				repositoryEntityLinks.linkToSingleResource(SummonerSpell.class, resource.getContent().getId())
						.withSelfRel());
	}

}
