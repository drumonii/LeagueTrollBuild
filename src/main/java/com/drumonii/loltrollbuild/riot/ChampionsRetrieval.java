package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
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
 * A {@link RestController} which retrieves the list of {@link Champion} from Riot's {@code lol-static-data-v1.2} API
 * with the {@code /riot/champions} URL mapping.
 */
@RestController
@RequestMapping("/riot/champions")
public class ChampionsRetrieval {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("champions")
	private UriComponents championsUri;

	@RequestMapping(method = RequestMethod.GET)
	public List<Champion> champions() {
		ChampionsResponse response = restTemplate.getForObject(championsUri.toString(), ChampionsResponse.class);
		return getElementsFromMap(response.getChampions());
	}

}
