package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.ChampionSpell;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.riot.api.ImageFetcher;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
	@Qualifier("champion")
	private UriComponentsBuilder championUri;

	@Autowired
	@Qualifier("championsImg")
	private UriComponentsBuilder championsImgUri;

	@Autowired
	@Qualifier("championsSpellImg")
	private UriComponentsBuilder championsSpellImgUri;

	@Autowired
	@Qualifier("championsPassiveImgUri")
	private UriComponentsBuilder championsPassiveImgUri;

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	private ImageFetcher imageFetcher;

	@Value("${riot.api.static-data.region}")
	private String region;

	/**
	 * Returns the {@link List} of {@link Champion} from Riot for the most current patch.
	 *
	 * @return the {@link List} of {@link Champion} from Riot
	 */
	@GetMapping
	public Collection<Champion> champions() {
		ChampionsResponse response = restTemplate.getForObject(championsUri.toString(), ChampionsResponse.class);
		return response.getChampions().values();
	}

	/**
	 * Persists the {@link List} of {@link Champion} from Riot for the most current patch and saves their images. If
	 * Champions already exist in the database, then only the difference (list from Riot not in the database) is
	 * persisted. And if Champions not found in Riot exist in the database, then those Champions are deleted along with
	 * their images.
	 * <p></p>
	 * If the {@code truncate} request parameter is set to {@code true}, then all previous Champions and their images
	 * are deleted and all the ones from Riot are persisted along with their images saved.
	 *
	 * @param truncate (optional) if {@code true}, all previous {@link Champion}s and their images are deleted and all
	 * the ones from Riot are persisted along with their images saved
	 * @return the {@link List} of {@link Champion} that are persisted to the database
	 */
	@PostMapping
	public List<Champion> saveChampions(@RequestParam(required = false) boolean truncate) {
		ChampionsResponse response = restTemplate.getForObject(championsUri.toString(), ChampionsResponse.class);
		List<Champion> champions = new ArrayList<>(response.getChampions().values());

		if (truncate) {
			championsRepository.deleteAll();
		} else {
			List<Champion> championsFromDb = championsRepository.findAll();
			List<Champion> deletedChampions = ListUtils.subtract(championsFromDb, champions);
			championsRepository.delete(deletedChampions);
			champions = ListUtils.subtract(champions, championsFromDb);
		}

		imageFetcher.setImgsSrcs(champions.stream().map(Champion::getImage).collect(Collectors.toList()),
				championsImgUri);
		imageFetcher.setImgsSrcs(champions.stream()
				.flatMap(champion -> champion.getSpells().stream()
						.map(ChampionSpell::getImage)).collect(Collectors.toList()), championsSpellImgUri);
		imageFetcher.setImgsSrcs(champions.stream().map(champion -> champion.getPassive().getImage())
						.collect(Collectors.toList()), championsPassiveImgUri);
		return championsRepository.save(champions);
	}

	/**
	 * Returns a {@link Champion} from Riot by its ID for the most current patch.
	 *
	 * @param id the ID to lookup the {@link Champion} from Riot
	 * @return the {@link Champion} from Riot
	 */
	@GetMapping(value = "/{id}")
	public Champion champion(@PathVariable int id) {
		UriComponents uriComponents = championUri.buildAndExpand(region, id);
		Champion champion;
		try {
			champion = restTemplate.getForObject(uriComponents.toString(), Champion.class);
		} catch (RestClientException e) {
			throw new ResourceNotFoundException("Could not find Champion with ID: " + id);
		}
		return champion;
	}

	/**
	 * Persists a {@link Champion} from Riot by its ID for the most current patch and saves its image. If the Champion
	 * already exists in the database, then the previous Champion and its image are deleted and the one retrieved from
	 * Riot is persisted along with its image saved.
	 *
	 * @param id the ID to lookup the {@link Champion} from Riot
	 * @return the persisted {@link Champion}
	 */
	@PostMapping(value = "/{id}")
	public Champion saveChampion(@PathVariable int id) {
		UriComponents uriComponents = championUri.buildAndExpand(region, id);
		Champion champion;
		try {
			champion = restTemplate.getForObject(uriComponents.toString(), Champion.class);
		} catch (RestClientException e) {
			throw new ResourceNotFoundException("Could not find Champion with ID: " + id);
		}

		Champion existing = championsRepository.findOne(id);
		if (existing != null) {
			championsRepository.delete(id);
		}

		imageFetcher.setImgSrc(champion.getImage(), championsImgUri);
		imageFetcher.setImgsSrcs(champion.getSpells().stream().map(ChampionSpell::getImage).collect(Collectors.toList()),
				championsSpellImgUri);
		imageFetcher.setImgSrc(champion.getPassive().getImage(), championsPassiveImgUri);
		return championsRepository.save(champion);
	}

}
