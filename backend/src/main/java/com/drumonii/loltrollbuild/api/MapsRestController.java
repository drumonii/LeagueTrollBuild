package com.drumonii.loltrollbuild.api;

import com.drumonii.loltrollbuild.api.service.MapsApiService;
import com.drumonii.loltrollbuild.api.status.ResourceNotFoundException;
import com.drumonii.loltrollbuild.api.view.ApiViews;
import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.repository.specification.ExampleSpecification;
import com.fasterxml.jackson.annotation.JsonView;
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

/**
 * Repository REST controller for {@link GameMap}s.
 */
@RestController
@RequestMapping("/maps")
public class MapsRestController {

	@Autowired
	private MapsApiService mapsApiService;

	/**
	 * Gets a {@link List} of {@link GameMap}s from the sort and search parameters.
	 *
	 * @param sort the {@link Sort}
	 * @param gameMap the search {@link GameMap} to define as the QBE
	 * @return the {@link List} of {@link GameMap}s
	 */
	@JsonView(ApiViews.LtbApi.class)
	@GetMapping
	public List<GameMap> getGameMaps(
			@PageableDefault(sort = "mapName", direction = Direction.ASC) Sort sort, GameMap gameMap) {
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher("mapName", GenericPropertyMatcher::contains)
				.withIgnoreCase()
				.withIgnorePaths("mapId", "version")
				.withIgnoreNullValues();
		Example<GameMap> example = Example.of(gameMap, exampleMatcher);
		return mapsApiService.qbe(ExampleSpecification.of(example), sort);
	}

	/**
	 * Gets a {@link GameMap} by its ID. If not found, returns a 404, otherwise a 200.
	 *
	 * @param id the ID to lookup the {@link GameMap}
	 * @return the {@link GameMap}
	 */
	@JsonView(ApiViews.LtbApi.class)
	@GetMapping("/{id}")
	public GameMap getGameMap(@PathVariable int id) {
		return mapsApiService.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Unable to find a Map with Id: " + id));
	}

	/**
	 * Gets a {@link List} of {@link GameMap}s for the troll build.
	 * See {@link MapsRepository#forTrollBuild()} for details on data retrieved.
	 *
	 * @return the {@link List} of {@link GameMap}s
	 */
	@JsonView(ApiViews.LtbApi.class)
	@GetMapping("/for-troll-build")
	public List<GameMap> getForTrollBuild() {
		return mapsApiService.forTrollBuild();
	}

}
