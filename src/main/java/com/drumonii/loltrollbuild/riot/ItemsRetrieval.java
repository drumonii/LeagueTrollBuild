package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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

	@RequestMapping(method = RequestMethod.GET)
	public List<Item> items() {
		ItemsResponse response = restTemplate.getForObject(itemsUri.toString(), ItemsResponse.class);
		return getElementsFromMap(response.getItems());
	}

}
