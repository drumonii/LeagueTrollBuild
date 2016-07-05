package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.riot.api.ImageFetcher;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.drumonii.loltrollbuild.util.MapUtil;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

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
	@Qualifier("summonerSpell")
	private UriComponentsBuilder summonerSpellUri;

	@Autowired
	@Qualifier("summonerSpellsImg")
	private UriComponentsBuilder summonerSpellsImgUri;

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	private ImageFetcher imageFetcher;

	@Value("${riot.api.static-data.region}")
	private String region;

	/**
	 * Returns the {@link List} of {@link SummonerSpell} from Riot for the most current patch.
	 *
	 * @return the {@link List} of {@link SummonerSpell} from Riot
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<SummonerSpell> summonerSpells() {
		SummonerSpellsResponse response = restTemplate.getForObject(summonerSpellsUri.toString(),
				SummonerSpellsResponse.class);
		return MapUtil.getElementsFromMap(response.getSummonerSpells());
	}

	/**
	 * Persists the {@link List} of {@link SummonerSpell} from Riot for the most current patch and saves their images.
	 * If Summoner Spells already exist in the database, then only the difference (list from Riot not in the database)
	 * is persisted. And if Summoner Spells not found in Riot exist in the database, then those Summoner Spells are
	 * deleted along with their images.
	 * <p></p>
	 * If the {@code truncate} request parameter is set to {@code true}, then all previous Summoner Spells and their
	 * images are deleted and all the ones from Riot are persisted along with their images saved.
	 *
	 * @param truncate (optional) if {@code true}, all previous {@link SummonerSpell}s and their images are deleted and
	 * all the ones from Riot are persisted along with their images saved
	 * @return the {@link List} of {@link SummonerSpell} that are persisted to the database
	 */
	@RequestMapping(method = RequestMethod.POST)
	public List<SummonerSpell> saveSummonerSpells(@RequestParam(required = false) boolean truncate) {
		SummonerSpellsResponse response = restTemplate.getForObject(summonerSpellsUri.toString(),
				SummonerSpellsResponse.class);
		List<SummonerSpell> summonerSpells = MapUtil.getElementsFromMap(response.getSummonerSpells());

		if (truncate) {
			summonerSpellsRepository.deleteAll();
		} else {
			List<SummonerSpell> summonerSpellsFromDb = summonerSpellsRepository.findAll();
			List<SummonerSpell> deletedSummonerSpells = ListUtils.subtract(summonerSpellsFromDb, summonerSpells);
			summonerSpellsRepository.delete(deletedSummonerSpells);
			summonerSpells = ListUtils.subtract(summonerSpells, summonerSpellsFromDb);
		}

		imageFetcher.setImgsSrcs(summonerSpells.stream().map(SummonerSpell::getImage).collect(Collectors.toList()),
				summonerSpellsImgUri);
		return summonerSpellsRepository.save(summonerSpells);
	}

	/**
	 * Returns a {@link SummonerSpell} from Riot by its ID for the most current patch.
	 *
	 * @param id the ID to lookup the {@link SummonerSpell} from Riot
	 * @return the {@link SummonerSpell} from Riot
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public SummonerSpell summonerSpell(@PathVariable int id) {
		UriComponents uriComponents = summonerSpellUri.buildAndExpand(region, id);
		SummonerSpell summonerSpell;
		try {
			summonerSpell = restTemplate.getForObject(uriComponents.toString(), SummonerSpell.class);
		} catch (RestClientException e) {
			throw new ResourceNotFoundException("Could not find Summoner Spell with ID: " + id);
		}
		return summonerSpell;
	}

	/**
	 * Persists a {@link SummonerSpell} from Riot by its ID for the most current patch and saves its image. If the
	 * Summoner Spell already exists in the database, then the previous Summoner Spell and its image are deleted and
	 * the one retrieved from Riot is persisted along with its image saved.
	 *
	 * @param id the ID to lookup the {@link SummonerSpell} from Riot
	 * @return the persisted {@link SummonerSpell}
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public SummonerSpell saveSummonerSpell(@PathVariable int id) {
		UriComponents uriComponents = summonerSpellUri.buildAndExpand(region, id);
		SummonerSpell summonerSpell;
		try {
			summonerSpell = restTemplate.getForObject(uriComponents.toString(), SummonerSpell.class);
		} catch (RestClientException e) {
			throw new ResourceNotFoundException("Could not find Summoner Spell with ID: " + id);
		}

		SummonerSpell existing = summonerSpellsRepository.findOne(id);
		if (existing != null) {
			summonerSpellsRepository.delete(id);
		}

		imageFetcher.setImgSrc(summonerSpell.getImage(), summonerSpellsImgUri);
		return summonerSpellsRepository.save(summonerSpell);
	}

}
