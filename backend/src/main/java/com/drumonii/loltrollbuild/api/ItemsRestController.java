package com.drumonii.loltrollbuild.api;

import com.drumonii.loltrollbuild.api.service.ItemsApiService;
import com.drumonii.loltrollbuild.api.status.ResourceNotFoundException;
import com.drumonii.loltrollbuild.api.view.ApiViews;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.repository.specification.ExampleSpecification;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.drumonii.loltrollbuild.util.GameMapUtil.SUMMONERS_RIFT_SID;

/**
 * Repository REST controller for {@link Item}s.
 */
@RestController
@RequestMapping("${api.base-path}/items")
public class ItemsRestController {

	@Autowired
	private ItemsApiService itemsApiService;

	/**
	 * Gets a {@link List} of {@link Item}s from the sort and search parameters.
	 *
	 * @param sort the {@link Sort}
	 * @param item the search {@link Item} to define as the QBE
	 * @return the {@link List} of {@link Item}s
	 */
	@JsonView(ApiViews.LtbApi.class)
	@GetMapping
	public List<Item> getItems(
			@PageableDefault(sort = "name", direction = Direction.ASC) Sort sort, Item item) {
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher("name", GenericPropertyMatcher::contains)
				.withMatcher("group", GenericPropertyMatcher::contains)
				.withMatcher("requiredChampion", GenericPropertyMatcher::contains)
				.withIgnoreCase()
				.withIgnorePaths("id", "version")
				.withIgnoreNullValues();
		Example<Item> example = Example.of(item, exampleMatcher);
		return itemsApiService.qbe(new ExampleSpecification<>(example), sort);
	}

	/**
	 * Gets a {@link Item} by its ID. If not found, returns a 404, otherwise a 200.
	 *
	 * @param id the ID to lookup the {@link Item}
	 * @return the {@link Item}
	 */
	@JsonView(ApiViews.LtbApi.class)
	@GetMapping(path = "/{id}")
	public Item getItem(@PathVariable int id) {
		return itemsApiService.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Unable to find an Item with Id: " + id));
	}

	/**
	 * Gets a {@link List} of boots {@link Item}s based on the specified map ID.
	 * See {@link ItemsRepository#boots(int mapId)} for details on data retrieved.
	 *
	 * @param mapId the map ID to get eligible {@link Item}s
	 * @return the {@link List} of {@link Item}s
	 */
	@JsonView(ApiViews.LtbApi.class)
	@GetMapping(path = "/boots")
	public List<Item> getBoots(@RequestParam(required = false, defaultValue = SUMMONERS_RIFT_SID) int mapId) {
		return itemsApiService.boots(mapId);
	}

	/**
	 * Gets a {@link List} of trinkets {@link Item}s based on the specified map ID.
	 * See {@link ItemsRepository#trinkets(int mapId)} for details on data retrieved.
	 *
	 * @param mapId the map ID to get eligible trinkets
	 * @return the {@link List} of {@link Item}s
	 */
	@JsonView(ApiViews.LtbApi.class)
	@GetMapping(path = "/trinkets")
	public List<Item> getTrinkets(@RequestParam(required = false, defaultValue = SUMMONERS_RIFT_SID) int mapId) {
		return itemsApiService.trinkets(mapId);
	}

	/**
	 * Gets a {@link List} of {@link Item}s for Viktor only.
	 * See {@link ItemsRepository#viktorOnly} for details on data retrieved.
	 *
	 * @return the {@link List} of {@link Item}s
	 */
	@JsonView(ApiViews.LtbApi.class)
	@GetMapping(path = "/viktor-only")
	public List<Item> getViktorOnly() {
		return itemsApiService.viktorOnly();
	}

	/**
	 * Gets a {@link List} of {@link Item}s for the troll build based on the specified map ID.
	 * See {@link ItemsRepository#forTrollBuild(int mapId)} for details on data retrieved.
	 *
	 * @param mapId the map ID to get eligible {@link Item}s for the troll build
	 * @return the {@link List} of {@link Item}s
	 */
	@JsonView(ApiViews.LtbApi.class)
	@GetMapping(value = "/for-troll-build")
	public List<Item> getForTrollBuild(@RequestParam(required = false, defaultValue = SUMMONERS_RIFT_SID) int mapId) {
		return itemsApiService.forTrollBuild(mapId);
	}

}
