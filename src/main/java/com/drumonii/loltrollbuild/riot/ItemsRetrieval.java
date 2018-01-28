package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.riot.api.ImageFetcher;
import com.drumonii.loltrollbuild.riot.service.ItemsService;
import com.drumonii.loltrollbuild.riot.service.VersionsService;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A {@link RestController} which retrieves the list of {@link Item} from Riot's {@code lol-static-data-v3} or
 * {@code Data Dragon} API with the {@code /riot/items} URL mapping.
 */
@RestController
@RequestMapping("/riot/items")
public class ItemsRetrieval {

	@Autowired
	@Qualifier("itemsImg")
	private UriComponentsBuilder itemsImgUri;

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private ImageFetcher imageFetcher;

	@Autowired
	private VersionsService versionsService;

	@Autowired
	private ItemsService itemsService;

	/**
	 * Returns the {@link List} of {@link Item} from Riot for the most current patch.
	 *
	 * @return the {@link List} of {@link Item} from Riot
	 */
	@GetMapping
	public Collection<Item> items() {
		return itemsService.getItems();
	}

	/**
	 * Persists the {@link List} of {@link Item} from Riot for the most current patch and saves their images. If Items
	 * already exist in the database, then only the difference (list from Riot not in the database) is persisted. And
	 * if Items not found in Riot exist in the database, then those Items are deleted along with their images.
	 * <p></p>
	 * If the {@code truncate} request parameter is set to {@code true}, then all previous Items and their images are
	 * deleted and all the ones from Riot are persisted along with their images saved.
	 *
	 * @param truncate (optional) if {@code true}, all previous {@link Item}s and their images are deleted and all the
	 * ones from Riot are persisted along with their images saved
	 * @return the {@link List} of {@link Item} that are persisted to the database
	 */
	@PostMapping
	public List<Item> saveItems(@RequestParam(required = false) boolean truncate) {
		List<Item> items = itemsService.getItems();

		if (truncate) {
			itemsRepository.deleteAll();
		} else {
			List<Item> itemsFromDb = itemsRepository.findAll();
			List<Item> deletedItems = ListUtils.subtract(itemsFromDb, items);
			itemsRepository.delete(deletedItems);
			items = ListUtils.subtract(items, itemsFromDb);
		}

		Version latestVersion = versionsService.getLatestVersion();

		imageFetcher.setImgsSrcs(items.stream().map(Item::getImage).collect(Collectors.toList()), itemsImgUri,
				latestVersion);
		return itemsRepository.save(items);
	}

	/**
	 * Returns a {@link Item} from Riot by its ID for the most current patch.
	 *
	 * @param id the ID to lookup the {@link Item} from Riot
	 * @return the {@link Item} from Riot
	 */
	@GetMapping(value = "/{id}")
	public Item item(@PathVariable int id) {
		Item item = itemsService.getItem(id);
		if (item == null) {
			throw new ResourceNotFoundException("Could not find Item with ID: " + id);
		}
		return item;
	}

	/**
	 * Persists a {@link Item} from Riot by its ID for the most current patch and saves its image. If the Item already
	 * exists in the database, then the previous Item and its image are deleted and the one retrieved from Riot is
	 * persisted along with its image saved.
	 *
	 * @param id the ID to lookup the {@link Item} from Riot
	 * @return the persisted {@link Item}
	 */
	@PostMapping(value = "/{id}")
	public Item saveItem(@PathVariable int id) {
		Item item = itemsService.getItem(id);
		if (item == null) {
			throw new ResourceNotFoundException("Could not find Item with ID: " + id);
		}

		Version latestVersion = versionsService.getLatestVersion();

		imageFetcher.setImgSrc(item.getImage(), itemsImgUri, latestVersion);
		return itemsRepository.save(item);
	}

}
