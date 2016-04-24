package com.drumonii.loltrollbuild.rest.processor;

import com.drumonii.loltrollbuild.model.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

/**
 * {@link ResourceProcessor} to add only a single self {@link Link} to a {@link Version} {@link Resource}.
 */
@Component
public class VersionResourceProcessor implements ResourceProcessor<Resource<Version>> {

	@Autowired
	private RepositoryEntityLinks repositoryEntityLinks;

	@Override
	public Resource<Version> process(Resource<Version> resource) {
		return new Resource<>(resource.getContent(),
				repositoryEntityLinks.linkToSingleResource(Version.class, resource.getContent().getPatch())
						.withSelfRel());
	}

}
