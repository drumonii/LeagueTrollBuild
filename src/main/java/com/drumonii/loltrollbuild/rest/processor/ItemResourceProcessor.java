package com.drumonii.loltrollbuild.rest.processor;

import com.drumonii.loltrollbuild.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

/**
 * {@link ResourceProcessor} to add only a single self {@link Link} to a {@link Item} {@link Resource}.
 */
@Component
public class ItemResourceProcessor implements ResourceProcessor<Resource<Item>> {

	@Autowired
	private RepositoryEntityLinks repositoryEntityLinks;

	@Override
	public Resource<Item> process(Resource<Item> resource) {
		return new Resource<>(resource.getContent(),
				repositoryEntityLinks.linkToSingleResource(Item.class, resource.getContent().getId())
						.withSelfRel());
	}

}
