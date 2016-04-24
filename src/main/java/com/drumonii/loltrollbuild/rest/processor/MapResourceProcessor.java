package com.drumonii.loltrollbuild.rest.processor;

import com.drumonii.loltrollbuild.model.GameMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

/**
 * {@link ResourceProcessor} to add only a single self {@link Link} to a {@link GameMap} {@link Resource}.
 */
@Component
public class MapResourceProcessor implements ResourceProcessor<Resource<GameMap>> {

	@Autowired
	private RepositoryEntityLinks repositoryEntityLinks;

	@Override
	public Resource<GameMap> process(Resource<GameMap> resource) {
		return new Resource<>(resource.getContent(),
				repositoryEntityLinks.linkToSingleResource(GameMap.class, resource.getContent().getMapId())
						.withSelfRel());
	}

}
