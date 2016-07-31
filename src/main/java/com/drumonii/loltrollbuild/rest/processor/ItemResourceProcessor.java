package com.drumonii.loltrollbuild.rest.processor;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * {@link ResourceProcessor} to add only a single self {@link Link} to a {@link Item} {@link Resource}.
 */
@Component
public class ItemResourceProcessor implements ResourceProcessor<Resource<Item>> {

	@Autowired
	private RepositoryEntityLinks repositoryEntityLinks;

	@Override
	public Resource<Item> process(Resource<Item> resource) {
		List<Link> links = new ArrayList<>();
		if (!resource.getContent().getFrom().isEmpty()) {
			links.addAll(resource.getContent().getFrom().stream()
					.map(from -> repositoryEntityLinks.linkToSingleResource(Item.class, from)
							.withRel("from"))
					.collect(Collectors.toList()));
		}
		if (!resource.getContent().getInto().isEmpty()) {
			links.addAll(resource.getContent().getInto().stream()
					.map(into -> repositoryEntityLinks.linkToSingleResource(Item.class, into)
							.withRel("into"))
					.collect(Collectors.toList()));
		}
		links.addAll(resource.getContent().getMaps().entrySet().stream()
				.filter(Map.Entry::getValue)
				.map(entry -> repositoryEntityLinks.linkToSingleResource(GameMap.class, entry.getKey()).withRel("maps"))
				.collect(Collectors.toList()));
		links.add(repositoryEntityLinks.linkToSingleResource(Item.class, resource.getContent().getId())
				.withSelfRel());
		return new Resource<>(resource.getContent(), new Links(links));
	}

}
