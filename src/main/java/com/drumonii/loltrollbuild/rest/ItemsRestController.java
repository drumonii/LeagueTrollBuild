package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.rest.specification.ItemSpecification;
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
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.BasicLinkBuilder.linkToCurrentMapping;

/**
 * Repository REST controller for {@link Item}s.
 */
@RestController
@RequestMapping("/${spring.data.rest.base-path}/items")
@RepositoryRestController
public class ItemsRestController {

	static final int PAGE_SIZE = 20;

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private PagedResourcesAssembler<Item> pagedAssembler;

	/**
	 * Gets a {@link PagedResources} of {@link Item} {@link Resource} from the pagination and search parameters.
	 *
	 * @param pageable the {@link Pageable}
	 * @param item the search {@link Item} to define as the QBE
	 * @return the {@link PagedResources} of {@link Item} {@link Resource}
	 */
	@GetMapping
	public PagedResources<Resource<Item>> getItems(
			@PageableDefault(size = PAGE_SIZE, sort = "name", direction = Direction.ASC) Pageable pageable,
			Item item) {
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher("name", GenericPropertyMatcher::contains)
				.withMatcher("group", GenericPropertyMatcher::contains)
				.withMatcher("requiredChampion", GenericPropertyMatcher::contains)
				.withIgnoreCase()
				.withIgnorePaths("id", "version")
				.withIgnoreNullValues();
		Example<Item> example = Example.of(item, exampleMatcher);
		return pagedAssembler.toResource(itemsRepository.findAll(new ItemSpecification(example), pageable));
	}

	/**
	 * Gets a {@link Resources} of boots {@link Item} {@link Resource} based on the specified map ID.
	 * See {@link ItemsRepository#boots(int mapId)} for details on data retrieved.
	 *
	 * @param mapId the map ID to get eligible {@link Item}s
	 * @return the {@link Resources} of {@link Item} {@link Resource}
	 */
	@GetMapping(path = "/boots")
	public Resources<Resource<Item>> getBoots(@RequestParam(required = false, defaultValue = "11") int mapId) {
		return new Resources<>(itemsRepository.boots(mapId).stream()
				.map(item -> new Resource<>(item))
				.collect(Collectors.toList()), linkToCurrentMapping().withSelfRel());
	}

	/**
	 * Gets a {@link Resources} of trinkets {@link Item} {@link Resource} based on the specified map ID.
	 * See {@link ItemsRepository#trinkets(int mapId)} for details on data retrieved.
	 *
	 * @param mapId the map ID to get eligible trinkets
	 * @return the {@link Resources} of {@link Item} {@link Resource}
	 */
	@GetMapping(path = "/trinkets")
	public Resources<Resource<Item>> getTrinkets(@RequestParam(required = false, defaultValue = "11") int mapId) {
		return new Resources<>(itemsRepository.trinkets(mapId).stream()
				.map(item -> new Resource<>(item))
				.collect(Collectors.toList()), linkToCurrentMapping().withSelfRel());
	}

	/**
	 * Gets a {@link Resources} of {@link Item} {@link Resource} for Viktor only.
	 * See {@link ItemsRepository#viktorOnly} for details on data retrieved.
	 *
	 * @return the {@link Resources} of {@link Item} {@link Resource}
	 */
	@GetMapping(value = "/viktor-only")
	public Resources<Resource<Item>> getViktorOnly() {
		return new Resources<>(itemsRepository.viktorOnly().stream()
				.map(item -> new Resource<>(item))
				.collect(Collectors.toList()), linkToCurrentMapping().withSelfRel());
	}

	/**
	 * Gets a {@link Resources} of {@link Item} {@link Resource} for the troll build based on the specified map ID.
	 * See {@link ItemsRepository#forTrollBuild(int mapId)} for details on data retrieved.
	 *
	 * @param mapId the map ID to get eligible {@link Item}s for the troll build
	 * @return the {@link Resources} of {@link Item} {@link Resource}
	 */
	@GetMapping(value = "/for-troll-build")
	public Resources<Resource<Item>> getForTrollBuild(@RequestParam(required = false, defaultValue = "11") int mapId) {
		return new Resources<>(itemsRepository.forTrollBuild(mapId).stream()
				.map(item -> new Resource<>(item))
				.collect(Collectors.toList()), linkToCurrentMapping().withSelfRel());
	}

}
