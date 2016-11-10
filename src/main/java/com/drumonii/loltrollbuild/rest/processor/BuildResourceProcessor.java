package com.drumonii.loltrollbuild.rest.processor;

import com.drumonii.loltrollbuild.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ResourceProcessor} to add only a single self {@link Link} to a {@link Build} {@link Resource}.
 */
@Component
public class BuildResourceProcessor implements ResourceProcessor<Resource<Build>> {

	@Autowired
	private RepositoryEntityLinks repositoryEntityLinks;

	@Override
	public Resource<Build> process(Resource<Build> resource) {
		List<Link> links = new ArrayList<>();
		links.add(repositoryEntityLinks.linkToSingleResource(Champion.class, resource.getContent().getChampionId())
				.withRel("champion"));

		links.add(repositoryEntityLinks.linkToSingleResource(Item.class, resource.getContent().getItem1Id())
				.withRel("item1"));

		links.add(repositoryEntityLinks.linkToSingleResource(Item.class, resource.getContent().getItem2Id())
				.withRel("item2"));
		links.add(repositoryEntityLinks.linkToSingleResource(Item.class, resource.getContent().getItem3Id())
				.withRel("item3"));
		links.add(repositoryEntityLinks.linkToSingleResource(Item.class, resource.getContent().getItem4Id())
				.withRel("item4"));
		links.add(repositoryEntityLinks.linkToSingleResource(Item.class, resource.getContent().getItem5Id())
				.withRel("item5"));
		links.add(repositoryEntityLinks.linkToSingleResource(Item.class, resource.getContent().getItem6Id())
				.withRel("item6"));

		links.add(repositoryEntityLinks.linkToSingleResource(SummonerSpell.class, resource.getContent()
				.getSummonerSpell1Id()).withRel("summonerSpell1"));
		links.add(repositoryEntityLinks.linkToSingleResource(SummonerSpell.class, resource.getContent()
				.getSummonerSpell2Id()).withRel("summonerSpell2"));

		links.add(repositoryEntityLinks.linkToSingleResource(Item.class, resource.getContent().getTrinketId())
				.withRel("trinket"));

		links.add(repositoryEntityLinks.linkToSingleResource(GameMap.class, resource.getContent().getMapId())
				.withRel("map"));

		links.add(repositoryEntityLinks.linkToSingleResource(Build.class, resource.getContent().getId())
				.withSelfRel());
		return new Resource<>(resource.getContent(), new Links(links));
	}

}
