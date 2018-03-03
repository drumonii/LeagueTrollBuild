package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Repository REST controller for {@link GameMap}s.
 */
@RestController
@RequestMapping("/${spring.data.rest.base-path}/maps")
@RepositoryRestController
public class MapsRestController {

	static final int PAGE_SIZE = 20;

	@Autowired
	private MapsRepository mapsRepository;

	@Autowired
	private PagedResourcesAssembler<GameMap> pagedAssembler;

	/**
	 * Gets a {@link PagedResources} of {@link GameMap} {@link Resource} from the pagination and search parameters.
	 *
	 * @param pageable the {@link Pageable}
	 * @param gameMap the search {@link GameMap} to define as the QBE
	 * @return the {@link PagedResources} of {@link GameMap} {@link Resource}
	 */
	@GetMapping
	public PagedResources<Resource<GameMap>> getGameMaps(
			@PageableDefault(size = PAGE_SIZE, sort = "mapName", direction = Direction.ASC) Pageable pageable,
			GameMap gameMap) {
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher("mapName", GenericPropertyMatcher::contains)
				.withIgnoreCase()
				.withIgnorePaths("mapId", "version")
				.withIgnoreNullValues();
		Example<GameMap> example = Example.of(gameMap, exampleMatcher);
		return pagedAssembler.toResource(mapsRepository.findAll(example, pageable));
	}

}
