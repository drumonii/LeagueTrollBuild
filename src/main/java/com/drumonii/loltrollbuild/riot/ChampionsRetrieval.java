package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
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

	@Autowired
	private ChampionsRepository championsRepository;

	/**
	 * Creates a {@link ModelAttribute} of a {@link List} of {@link Champion} from Riot.
	 *
	 * @return a {@link List} of {@link Champion} from Riot
	 */
	@ModelAttribute
	public List<Champion> championsFromResponse() {
		ChampionsResponse response = restTemplate.getForObject(championsUri.toString(), ChampionsResponse.class);
		return getElementsFromMap(response.getChampions());
	}

	/**
	 * Returns the {@link List} of {@link Champion} from Riot.
	 *
	 * @param champions the {@link ModelAttribute} of {@link List} of {@link Champion} from Riot
	 * @return the {@link List} of {@link Champion} from Riot
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<Champion> champions(@ModelAttribute List<Champion> champions) {
		return champions;
	}

	/**
	 * Persists the {@link List} of {@link Champion} from Riot. If Champions already exist in the database, then only
	 * the difference (list from Riot not in the database) is persisted. If the {@code truncate} request parameter is
	 * set to {@code true}, then all previous Champions are deleted and all the ones from Riot are persisted.
	 *
	 * @param champions the {@link ModelAttribute} of {@link List} of {@link Champion} from Riot
	 * @param truncate (optional) if {@code true}, all previous {@link Champion}s are deleted and all the ones from Riot
	 * are persisted
	 * @return the {@link List} of {@link Champion} that are persisted to the database
	 */
	@RequestMapping(method = RequestMethod.POST)
	public List<Champion> saveChampions(@ModelAttribute List<Champion> champions,
			@RequestParam(required = false) boolean truncate) {
		if (truncate) {
			championsRepository.deleteAll();
		} else {
			champions = (List<Champion>) CollectionUtils.subtract(champions, championsRepository.findAll());
		}
		championsRepository.save(champions);
		return champions;
	}

}
