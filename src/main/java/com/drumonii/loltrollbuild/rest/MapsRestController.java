package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.rest.specification.MapsSpecification;
import com.drumonii.loltrollbuild.rest.status.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * Repository REST controller for {@link GameMap}s.
 */
@RestController
@RequestMapping("/${api.base-path}/maps")
public class MapsRestController {

	@Autowired
	private MapsRepository mapsRepository;

	/**
	 * Gets a {@link List} of {@link GameMap}s from the sort and search parameters.
	 *
	 * @param sort the {@link Sort}
	 * @param gameMap the search {@link GameMap} to define as the QBE
	 * @return the {@link List} of {@link GameMap}s
	 */
	@GetMapping
	public List<GameMap> getGameMaps(
			@PageableDefault(sort = "mapName", direction = Direction.ASC) Sort sort, GameMap gameMap) {
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher("mapName", GenericPropertyMatcher::contains)
				.withIgnoreCase()
				.withIgnorePaths("mapId", "version")
				.withIgnoreNullValues();
		Example<GameMap> example = Example.of(gameMap, exampleMatcher);
		return mapsRepository.findAll(new MapsSpecification(example), sort);
	}

	/**
	 * Gets a {@link GameMap} by its ID. If not found, returns a 404, otherwise a 200.
	 *
	 * @param id the ID to lookup the {@link GameMap}
	 * @return the {@link GameMap}
	 */
	@GetMapping(path = "/{id}")
	public GameMap getGameMap(@PathVariable int id) {
		Optional<GameMap> map = mapsRepository.findById(id);
		return map.orElseThrow(() -> new ResourceNotFoundException("Unable to find a Map with Id: " + id));
	}

	/**
	 * Gets a {@link List} of {@link GameMap}s for the troll build.
	 * See {@link MapsRepository#forTrollBuild()} for details on data retrieved.
	 *
	 * @return the {@link List} of {@link GameMap}s
	 */
	@GetMapping(path = "/for-troll-build")
	public List<GameMap> getForTrollBuild() {
		return mapsRepository.forTrollBuild();
	}

}
