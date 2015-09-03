package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
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
 * A {@link RestController} which retrieves the list of {@link SummonerSpell} from Riot's {@code lol-static-data-v1.2}
 * API with the {@code /riot/summoner-spells} URL mapping.
 */
@RestController
@RequestMapping("/riot/summoner-spells")
@Slf4j
public class SummonerSpellsRetrieval {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("summonerSpells")
	private UriComponents summonerSpellsUri;

	@Autowired
	@Qualifier("summonerSpell")
	private UriComponentsBuilder summonerSpellUri;

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	private RiotApiProperties riotProperties;

	/**
	 * Returns the {@link List} of {@link SummonerSpell} from Riot.
	 *
	 * @return the {@link List} of {@link SummonerSpell} from Riot
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<SummonerSpell> summonerSpells() {
		SummonerSpellsResponse response = restTemplate.getForObject(summonerSpellsUri.toString(),
				SummonerSpellsResponse.class);
		return getElementsFromMap(response.getSummonerSpells());
	}

	/**
	 * Persists the {@link List} of {@link SummonerSpell} from Riot. If Summoner Spells already exist in the database,
	 * then only the difference (list from Riot not in the database) is persisted. If the {@code truncate} request
	 * parameter is set to {@code true}, then all previous Summoner Spells are deleted and all the ones from Riot are
	 * persisted.
	 *
	 * @param truncate (optional) If {@code true}, all previous {@link SummonerSpell}s are deleted and all the ones from
	 * Riot are persisted
	 * @return the {@link List} of {@link SummonerSpell} that are persisted to the database
	 */
	@RequestMapping(method = RequestMethod.POST)
	public List<SummonerSpell> saveSummonerSpells(@RequestParam(required = false) boolean truncate) {
		SummonerSpellsResponse response = restTemplate.getForObject(summonerSpellsUri.toString(),
				SummonerSpellsResponse.class);
		List<SummonerSpell> summonerSpells = getElementsFromMap(response.getSummonerSpells());
		if (truncate) {
			summonerSpellsRepository.deleteAll();
		} else {
			summonerSpells = (List<SummonerSpell>) CollectionUtils.subtract(summonerSpells,
					summonerSpellsRepository.findAll());
		}
		return (List<SummonerSpell>) summonerSpellsRepository.save(summonerSpells);
	}

	/**
	 * Returns a {@link SummonerSpell} from Riot by its ID.
	 *
	 * @param id the ID to lookup the {@link SummonerSpell} from Riot
	 * @return the {@link SummonerSpell} from Riot
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public SummonerSpell summonerSpell(@PathVariable int id) {
		UriComponents uriComponents =
				summonerSpellUri.buildAndExpand(riotProperties.getApi().getStaticData().getRegion(), id);
		SummonerSpell summonerSpell;
		try {
			summonerSpell = restTemplate.getForObject(uriComponents.toString(), SummonerSpell.class);
		} catch (RestClientException e) {
			log.warn("Could not find Summoner Spell with ID: " + id);
			throw new ResourceNotFoundException();
		}
		return summonerSpell;
	}

	/**
	 * Persists a {@link SummonerSpell} from Riot. If the Summoner Spell already exists in the database, then the
	 * existing Summoner Spell is returned. Otherwise, the persisted Summoner Spell is returned. If the {@code
	 * overwrite} request parameter is set to {@code true}, then the previous Summoner Spell, obtained from the ID, is
	 * deleted (if one exists) and the new Summoner Spell is persisted and returned.
	 *
	 * @param overwrite (optional) if {@code true}, the previous {@link SummonerSpell}, obtained from the ID, is deleted
	 * (if one exists)
	 * @param id the ID to lookup the {@link SummonerSpell} from Riot
	 * @return the persisted {@link SummonerSpell} or existing the existing one in the database
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public SummonerSpell saveSummonerSpell(@PathVariable int id, @RequestParam(required = false) boolean overwrite) {
		UriComponents uriComponents =
				summonerSpellUri.buildAndExpand(riotProperties.getApi().getStaticData().getRegion(), id);
		SummonerSpell summonerSpell;
		try {
			summonerSpell = restTemplate.getForObject(uriComponents.toString(), SummonerSpell.class);
		} catch (RestClientException e) {
			log.warn("Could not find Summoner Spell with ID: " + id);
			throw new ResourceNotFoundException();
		}
		SummonerSpell existing = summonerSpellsRepository.findOne(id);
		if (overwrite && existing != null) {
			summonerSpellsRepository.delete(id);
			return summonerSpellsRepository.save(summonerSpell);
		}
		return existing == null ? summonerSpellsRepository.save(summonerSpell) : existing;
	}

}
