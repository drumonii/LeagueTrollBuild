package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.domain.ExampleMatcher.StringMatcher.CONTAINING;

/**
 * Repository REST controller for {@link Champion}s.
 */
@RestController
@RequestMapping("/${spring.data.rest.base-path}/champions")
@RepositoryRestController
public class ChampionsRestController {

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	private PagedResourcesAssembler<Champion> pagedAssembler;

	/**
	 * Gets a {@link PagedResources} of {@link Champion} {@link Resource} from the pagination and search parameters.
	 *
	 * @param pageable the {@link Pageable}
	 * @param champion the search {@link Champion} to define as the QBE
	 * @return the {@link PagedResources} of {@link Champion} {@link Resource}
	 */
	@GetMapping
	public PagedResources<Resource<Champion>> getChampions(
			@PageableDefault(size = 20, sort = "name", direction = Direction.ASC) Pageable pageable,
			Champion champion) {
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher("name", matcher -> matcher.stringMatcher(CONTAINING))
				.withMatcher("title", matcher -> matcher.stringMatcher(CONTAINING))
				.withMatcher("partype", matcher -> matcher.stringMatcher(CONTAINING))
				.withIgnoreCase()
				.withIgnorePaths("id")
				.withIgnoreNullValues();
		Example<Champion> example = Example.of(champion, exampleMatcher);
		return pagedAssembler.toResource(championsRepository.findAll(example, pageable));
	}

}
