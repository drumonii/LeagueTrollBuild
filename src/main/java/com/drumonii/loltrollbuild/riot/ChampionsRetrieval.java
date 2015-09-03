package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
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
 * A {@link RestController} which retrieves the list of {@link Champion} from Riot's {@code lol-static-data-v1.2} API
 * with the {@code /riot/champions} URL mapping.
 */
@RestController
@RequestMapping("/riot/champions")
@Slf4j
public class ChampionsRetrieval {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("champions")
	private UriComponents championsUri;

	@Autowired
	@Qualifier("champion")
	private UriComponentsBuilder championUri;

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	private RiotApiProperties riotProperties;

	/**
	 * Returns the {@link List} of {@link Champion} from Riot.
	 *
	 * @return the {@link List} of {@link Champion} from Riot
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<Champion> champions() {
		ChampionsResponse response = restTemplate.getForObject(championsUri.toString(), ChampionsResponse.class);
		return getElementsFromMap(response.getChampions());
	}

	/**
	 * Persists the {@link List} of {@link Champion} from Riot. If Champions already exist in the database, then only
	 * the difference (list from Riot not in the database) is persisted. If the {@code truncate} request parameter is
	 * set to {@code true}, then all previous Champions are deleted and all the ones from Riot are persisted.
	 *
	 * @param truncate (optional) if {@code true}, all previous {@link Champion}s are deleted and all the ones from Riot
	 * are persisted
	 * @return the {@link List} of {@link Champion} that are persisted to the database
	 */
	@RequestMapping(method = RequestMethod.POST)
	public List<Champion> saveChampions(@RequestParam(required = false) boolean truncate) {
		ChampionsResponse response = restTemplate.getForObject(championsUri.toString(), ChampionsResponse.class);
		List<Champion> champions = getElementsFromMap(response.getChampions());
		if (truncate) {
			championsRepository.deleteAll();
		} else {
			champions = (List<Champion>) CollectionUtils.subtract(champions, championsRepository.findAll());
		}
		return (List<Champion>) championsRepository.save(champions);
	}

	/**
	 * Returns a {@link Champion} from Riot by its ID.
	 *
	 * @param id the ID to lookup the {@link Champion} from Riot
	 * @return the {@link Champion} from Riot
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Champion champion(@PathVariable int id) {
		UriComponents uriComponents =
				championUri.buildAndExpand(riotProperties.getApi().getStaticData().getRegion(), id);
		Champion champion;
		try {
			champion = restTemplate.getForObject(uriComponents.toString(), Champion.class);
		} catch (RestClientException e) {
			log.warn("Could not find Champion with ID: " + id);
			throw new ResourceNotFoundException();
		}
		return champion;
	}

	/**
	 * Persists a {@link Champion} from Riot. If the Champion already exists in the database, then the existing Champion
	 * is returned. Otherwise, the persisted Champion is returned. If the {@code overwrite} request parameter is set to
	 * {@code true}, then the previous Champion, obtained from the ID, is deleted (if one exists) and the new Champion
	 * is persisted and returned.
	 *
	 * @param overwrite (optional) if {@code true}, the previous {@link Champion}, obtained from the ID, is deleted (if
	 * one exists)
	 * @param id the ID to lookup the {@link Champion} from Riot
	 * @return the persisted {@link Champion} or existing the existing one in the database
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public Champion saveChampion(@PathVariable int id, @RequestParam(required = false) boolean overwrite) {
		UriComponents uriComponents =
				championUri.buildAndExpand(riotProperties.getApi().getStaticData().getRegion(), id);
		Champion champion;
		try {
			champion = restTemplate.getForObject(uriComponents.toString(), Champion.class);
		} catch (RestClientException e) {
			log.warn("Could not find Champion with ID: " + id);
			throw new ResourceNotFoundException();
		}
		Champion existing = championsRepository.findOne(id);
		if (overwrite && existing != null) {
			championsRepository.delete(id);
			return championsRepository.save(champion);
		}
		return existing == null ? championsRepository.save(champion) : existing;
	}

}
