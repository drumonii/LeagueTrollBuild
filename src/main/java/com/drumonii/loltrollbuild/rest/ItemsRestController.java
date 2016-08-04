package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.StringMatcher.CONTAINING;
import static org.springframework.hateoas.mvc.BasicLinkBuilder.linkToCurrentMapping;

/**
 * Repository REST controller for {@link Item}s.
 */
@RestController
@RequestMapping("/${spring.data.rest.base-path}/items")
@RepositoryRestController
public class ItemsRestController {

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
	public PagedResources<Resource<Item>> getItems(Pageable pageable, Item item) {
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher("name", matcher -> matcher.stringMatcher(CONTAINING))
				.withMatcher("group", matcher -> matcher.stringMatcher(CONTAINING))
				.withMatcher("requiredChampion", matcher -> matcher.stringMatcher(CONTAINING))
				.withIgnoreCase()
				.withIgnorePaths("id")
				.withIgnoreNullValues();
		Example<Item> example = Example.of(item, exampleMatcher);
		return pagedAssembler.toResource(itemsRepository.findAll(example, pageable));
	}

	/**
	 * Gets a {@link Resources} of boots {@link Item} {@link Resource} based on the specified map ID.
	 * See {@link ItemsRepository#boots(String mapId)} for details on data retrieved.
	 *
	 * @param mapId the map ID to get eligible {@link Item}s
	 * @return the {@link Resources} of {@link Item} {@link Resource}
	 */
	@GetMapping(value = "/boots")
	public Resources<Resource<Item>> getBoots(@RequestParam String mapId) {
		return new Resources<>(itemsRepository.boots(mapId).stream()
				.map(item -> new Resource<>(item))
				.collect(Collectors.toList()), linkToCurrentMapping().withSelfRel());
	}

	/**
	 * Gets a {@link Resources} of trinkets {@link Item} {@link Resource} based on the specified map ID.
	 * See {@link ItemsRepository#trinkets(String mapId)} for details on data retrieved.
	 *
	 * @param mapId the map ID to get eligible trinkets
	 * @return the {@link Resources} of {@link Item} {@link Resource}
	 */
	@GetMapping(value = "/trinkets")
	public Resources<Resource<Item>> getTrinkets(@RequestParam String mapId) {
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
	 * See {@link ItemsRepository#forTrollBuild(String mapId)} for details on data retrieved.
	 *
	 * @param mapId the map ID to get eligible {@link Item}s for the troll build
	 * @return the {@link Resources} of {@link Item} {@link Resource}
	 */
	@GetMapping(value = "/for-troll-build")
	public Resources<Resource<Item>> getForTrollBuild(@RequestParam String mapId) {
		return new Resources<>(itemsRepository.forTrollBuild(mapId).stream()
				.map(item -> new Resource<>(item))
				.collect(Collectors.toList()), linkToCurrentMapping().withSelfRel());
	}

}
