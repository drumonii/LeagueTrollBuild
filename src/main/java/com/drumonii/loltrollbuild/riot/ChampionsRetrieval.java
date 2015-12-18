package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.riot.api.ImageSaver;
import com.drumonii.loltrollbuild.riot.api.RiotApiProperties;
import com.drumonii.loltrollbuild.util.MapUtil;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

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
	private ChampionsRepository championsRepository;

	@Autowired
	private ImageSaver imageSaver;

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
		return MapUtil.getElementsFromMap(response.getChampions());
	}

	/**
	 * Persists the {@link List} of {@link Champion} from Riot and saves their images. If Champions already exist in
	 * the database, then only the difference (list from Riot not in the database) is persisted. And if Champions not
	 * found in Riot exist in the database, then those Champions are deleted along with their images.
	 * <p></p>
	 * If the {@code truncate} request parameter is set to {@code true}, then all previous Champions and their images
	 * are deleted and all the ones from Riot are persisted along with their images saved.
	 *
	 * @param truncate (optional) if {@code true}, all previous {@link Champion}s and their images are deleted and all
	 * the ones from Riot are persisted along with their images saved
	 * @return the {@link List} of {@link Champion} that are persisted to the database
	 */
	@RequestMapping(method = RequestMethod.POST)
	public List<Champion> saveChampions(@RequestParam(required = false) boolean truncate) {
		ChampionsResponse response = restTemplate.getForObject(championsUri.toString(), ChampionsResponse.class);
		List<Champion> champions = MapUtil.getElementsFromMap(response.getChampions());

		if (truncate) {
			championsRepository.deleteAll();
		} else {
			List<Champion> championsFromDb = (List<Champion>) championsRepository.findAll();
			List<Champion> deletedChampions = ListUtils.subtract(championsFromDb, champions);
			championsRepository.delete(deletedChampions);
			imageSaver.deleteImages(deletedChampions.stream().map(Champion::getImage).collect(Collectors.toList()));
			champions = ListUtils.subtract(champions, championsFromDb);
		}

		imageSaver.copyImagesFromURLs(champions.stream().map(Champion::getImage).collect(Collectors.toList()), truncate,
				championsImgUri);

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
			throw new ResourceNotFoundException("Could not find Champion with ID: " + id);
		}
		return champion;
	}

	/**
	 * Persists a {@link Champion} from Riot by its ID and saves its image. If the Champion already exists in the
	 * database, then the previous Champion and its image are deleted and the one retrieved from Riot is persisted
	 * along with its image saved.
	 *
	 * @param id the ID to lookup the {@link Champion} from Riot
	 * @return the persisted {@link Champion}
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public Champion saveChampion(@PathVariable int id) {
		UriComponents uriComponents =
				championUri.buildAndExpand(riotProperties.getApi().getStaticData().getRegion(), id);
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

		imageSaver.copyImageFromURL(champion.getImage(), championsImgUri);
		return championsRepository.save(champion);
	}

}
