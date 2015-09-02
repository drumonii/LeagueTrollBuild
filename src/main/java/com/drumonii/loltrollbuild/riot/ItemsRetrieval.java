package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.util.List;

import static com.drumonii.loltrollbuild.util.MapUtil.getElementsFromMap;

/**
 * A {@link RestController} which retrieves the list of {@link Item} from Riot's {@code lol-static-data-v1.2} API with
 * the {@code /riot/items} URL mapping.
 */
@RestController
@RequestMapping("/riot/items")
public class ItemsRetrieval {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("items")
	private UriComponents itemsUri;

	@Autowired
	private ItemsRepository itemsRepository;

	/**
	 * Creates a {@link ModelAttribute} of a {@link List} of {@link Item} from Riot.
	 *
	 * @return a {@link List} of {@link Item} from Riot
	 */
	@ModelAttribute
	public List<Item> itemsFromResponse() {
		ItemsResponse response = restTemplate.getForObject(itemsUri.toString(), ItemsResponse.class);
		return getElementsFromMap(response.getItems());
	}

	/**
	 * Returns the {@link List} of {@link Item} from Riot.
	 *
	 * @param items the {@link ModelAttribute} of {@link List} of {@link Item} from Riot
	 * @return the {@link List} of {@link Item} from Riot
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<Item> items(@ModelAttribute List<Item> items) {
		return items;
	}

	/**
	 * Persists the {@link List} of {@link Item} from Riot. If Items already exist in the database, then only the
	 * difference (list from Riot not in the database) is persisted. If the {@code truncate} request parameter is set to
	 * {@code true}, then all previous Items are deleted and all the ones from Riot are persisted.
	 *
	 * @param items the {@link ModelAttribute} of {@link List} of {@link Item} from Riot
	 * @param truncate (optional) if {@code true}, all previous {@link Item}s are deleted and all the ones from Riot are
	 * persisted
	 * @return the {@link List} of {@link Item} that are persisted to the database
	 */
	@RequestMapping(method = RequestMethod.POST)
	public List<Item> saveItems(@ModelAttribute List<Item> items,
			@RequestParam(required = false) boolean truncate) {
		if (truncate) {
			itemsRepository.deleteAll();
		} else {
			items = (List<Item>) CollectionUtils.subtract(items, itemsRepository.findAll());
		}
		itemsRepository.save(items);
		return items;
	}

}
