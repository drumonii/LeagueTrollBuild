package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.util.List;

import static com.drumonii.loltrollbuild.util.MapUtil.getElementsFromMap;

/**
 * A {@link RestController} which retrieves the list of {@link SummonerSpell} from Riot's {@code lol-static-data-v1.2}
 * API with the {@code /riot/summoner-spells} URL mapping.
 */
@RestController
@RequestMapping("/riot/summoner-spells")
public class SummonerSpellsRetrieval {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("summonerSpells")
	private UriComponents summonerSpellsUri;

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	/**
	 * Creates a {@link ModelAttribute} of a {@link List} of {@link SummonerSpell} from Riot.
	 *
	 * @return a {@link List} of {@link SummonerSpell} from Riot
	 */
	@ModelAttribute
	public List<SummonerSpell> summonerSpellsFromResponse() {
		SummonerSpellsResponse response = restTemplate.getForObject(summonerSpellsUri.toString(),
				SummonerSpellsResponse.class);
		return getElementsFromMap(response.getSummonerSpells());
	}

	/**
	 * Returns the {@link List} of {@link SummonerSpell} from Riot.
	 *
	 * @param summonerSpells the {@link ModelAttribute} of {@link List} of {@link SummonerSpell} from Riot
	 * @return the {@link List} of {@link SummonerSpell} from Riot
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<SummonerSpell> summonerSpells(@ModelAttribute List<SummonerSpell> summonerSpells) {
		return summonerSpells;
	}

	/**
	 * Persists the {@link List} of {@link SummonerSpell} from Riot. If Summoner Spells already exist in the database,
	 * then only the difference (list from Riot not in the database) is persisted. If the {@code truncate} request
	 * parameter is set to {@code true}, then all previous Summoner Spells are deleted and all the ones from Riot are
	 * persisted.
	 *
	 * @param summonerSpells the {@link ModelAttribute} of {@link List} of {@link SummonerSpell} from Riot
	 * @param truncate (optional) If {@code true}, all previous {@link SummonerSpell}s are deleted and all the ones from
	 * Riot are persisted
	 * @return the {@link List} of {@link SummonerSpell} that are persisted to the database
	 */
	@RequestMapping(method = RequestMethod.POST)
	public List<SummonerSpell> saveSummonerSpells(@ModelAttribute List<SummonerSpell> summonerSpells,
			@RequestParam(required = false) boolean truncate) {
		if (truncate) {
			summonerSpellsRepository.deleteAll();
		} else {
			summonerSpells = (List<SummonerSpell>) CollectionUtils.subtract(summonerSpells,
					summonerSpellsRepository.findAll());
		}
		summonerSpellsRepository.save(summonerSpells);
		return summonerSpells;
	}

}
