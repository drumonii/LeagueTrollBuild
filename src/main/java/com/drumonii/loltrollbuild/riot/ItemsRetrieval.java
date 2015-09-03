package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static com.drumonii.loltrollbuild.util.MapUtil.getElementsFromMap;

/**
 * A {@link RestController} which retrieves the list of {@link Item} from Riot's {@code lol-static-data-v1.2} API with
 * the {@code /riot/items} URL mapping.
 */
@RestController
@RequestMapping("/riot/items")
@Slf4j
public class ItemsRetrieval {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("items")
	private UriComponents itemsUri;

	@Autowired
	@Qualifier("item")
	private UriComponentsBuilder itemUri;

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private RiotApiProperties riotProperties;

	/**
	 * Returns the {@link List} of {@link Item} from Riot.
	 *
	 * @return the {@link List} of {@link Item} from Riot
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<Item> items() {
		ItemsResponse response = restTemplate.getForObject(itemsUri.toString(), ItemsResponse.class);
		return getElementsFromMap(response.getItems());
	}

	/**
	 * Persists the {@link List} of {@link Item} from Riot. If Items already exist in the database, then only the
	 * difference (list from Riot not in the database) is persisted. If the {@code truncate} request parameter is set to
	 * {@code true}, then all previous Items are deleted and all the ones from Riot are persisted.
	 *
	 * @param truncate (optional) if {@code true}, all previous {@link Item}s are deleted and all the ones from Riot are
	 * persisted
	 * @return the {@link List} of {@link Item} that are persisted to the database
	 */
	@RequestMapping(method = RequestMethod.POST)
	public List<Item> saveItems(@RequestParam(required = false) boolean truncate) {
		ItemsResponse response = restTemplate.getForObject(itemsUri.toString(), ItemsResponse.class);
		List<Item> items = getElementsFromMap(response.getItems());
		if (truncate) {
			itemsRepository.deleteAll();
		} else {
			items = (List<Item>) CollectionUtils.subtract(items, itemsRepository.findAll());
		}
		return (List<Item>) itemsRepository.save(items);
	}

	/**
	 * Returns a {@link Item} from Riot by its ID.
	 *
	 * @param id the ID to lookup the {@link Item} from Riot
	 * @return the {@link Item} from Riot
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Item item(@PathVariable int id) {
		UriComponents uriComponents = itemUri.buildAndExpand(riotProperties.getApi().getStaticData().getRegion(), id);
		Item item;
		try {
			item = restTemplate.getForObject(uriComponents.toString(), Item.class);
		} catch (RestClientException e) {
			log.warn("Could not find Item with ID: " + id);
			throw new ResourceNotFoundException();
		}
		return item;
	}

	/**
	 * Persists a {@link Item} from Riot. If the Item already exists in the database, then the existing Item is
	 * returned. Otherwise, the persisted Item is returned. If the {@code overwrite} request parameter is set to {@code
	 * true}, then the previous Item, obtained from the ID, is deleted (if one exists) and the new Item is persisted and
	 * returned.
	 *
	 * @param overwrite (optional) if {@code true}, the previous {@link Item}, obtained from the ID, is deleted (if one
	 * exists)
	 * @param id the ID to lookup the {@link Item} from Riot
	 * @return the persisted {@link Item} or existing the existing one in the database
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public Item saveItem(@PathVariable int id, @RequestParam(required = false) boolean overwrite) {
		UriComponents uriComponents = itemUri.buildAndExpand(riotProperties.getApi().getStaticData().getRegion(), id);
		Item item;
		try {
			item = restTemplate.getForObject(uriComponents.toString(), Item.class);
		} catch (RestClientException e) {
			log.warn("Could not find Item with ID: " + id);
			throw new ResourceNotFoundException();
		}
		Item existing = itemsRepository.findOne(id);
		if (overwrite && existing != null) {
			itemsRepository.delete(id);
			return itemsRepository.save(item);
		}
		return existing == null ? itemsRepository.save(item) : existing;
	}

}
