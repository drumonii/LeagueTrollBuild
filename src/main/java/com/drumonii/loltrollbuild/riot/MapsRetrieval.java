package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.riot.api.ImageFetcher;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A {@link RestController} which retrieves the list of {@link GameMap} from Riot's {@code lol-static-data-v1.2} API
 * with the {@code /riot/maps} URL mapping.
 */
@RestController
@RequestMapping("/riot/maps")
public class MapsRetrieval {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("maps")
	private UriComponents mapsUri;

	@Autowired
	@Qualifier("mapsImg")
	private UriComponentsBuilder mapsImgUri;

	@Autowired
	private MapsRepository mapsRepository;

	@Autowired
	private ImageFetcher imageFetcher;

	/**
	 * Returns the {@link List} of {@link GameMap} from Riot for the most current patch.
	 *
	 * @return the {@link List} of {@link GameMap} from Riot
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Collection<GameMap> maps() {
		MapsResponse response = restTemplate.getForObject(mapsUri.toString(), MapsResponse.class);
		return response.getMaps().values();
	}

	/**
	 * Persists the {@link List} of {@link GameMap} from Riot for the most current patch and saves their images. If
	 * Maps already exist in the database, then only the difference (list from Riot not in the database) is persisted.
	 * And if Maps not found in Riot exist in the database, then those Maps are deleted along with their images.
	 * <p></p>
	 * If the {@code truncate} request parameter is set to {@code true}, then all previous Maps and their images are
	 * deleted and all the ones from Riot are persisted along with their images saved.
	 *
	 * @param truncate (optional) if {@code true}, all previous {@link GameMap}s and their images are deleted and all
	 * the ones from Riot are persisted along with their images saved
	 * @return the {@link List} of {@link GameMap} that are persisted to the database
	 */
	@RequestMapping(method = RequestMethod.POST)
	public List<GameMap> saveMaps(@RequestParam(required = false) boolean truncate) {
		MapsResponse response = restTemplate.getForObject(mapsUri.toString(), MapsResponse.class);
		List<GameMap> maps = new ArrayList<>(response.getMaps().values());

		if (truncate) {
			mapsRepository.deleteAll();
		} else {
			List<GameMap> mapsFromDb = mapsRepository.findAll();
			List<GameMap> deletedMaps = ListUtils.subtract(mapsFromDb, maps);
			mapsRepository.delete(deletedMaps);
			maps = ListUtils.subtract(maps, mapsFromDb);
		}

		imageFetcher.setImgsSrcs(maps.stream().map(GameMap::getImage).collect(Collectors.toList()), mapsImgUri);
		return mapsRepository.save(maps);
	}

	/**
	 * Returns a {@link GameMap} from Riot by its ID for the most current patch.
	 *
	 * @param id the ID to lookup the {@link GameMap} from Riot
	 * @return the {@link GameMap} from Riot
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public GameMap map(@PathVariable int id) {
		MapsResponse response = restTemplate.getForObject(mapsUri.toString(), MapsResponse.class);
		GameMap map = response.getMaps().get(String.valueOf(id));
		if (map == null) {
			throw new ResourceNotFoundException("Could not find Map with ID: " + id);
		}
		return map;
	}

	/**
	 * Persists a {@link GameMap} from Riot by its ID for the most current patch and saves its image. If the Map
	 * already exists in the database, then the previous Map and its image are deleted and the one retrieved from Riot
	 * is persisted along with its image saved.
	 *
	 * @param id the ID to lookup the {@link GameMap} from Riot
	 * @return the persisted {@link GameMap}
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public GameMap saveMap(@PathVariable int id) {
		MapsResponse response = restTemplate.getForObject(mapsUri.toString(), MapsResponse.class);
		GameMap map = response.getMaps().get(String.valueOf(id));
		if (map == null) {
			throw new ResourceNotFoundException("Could not find Map with ID: " + id);
		}

		GameMap existing = mapsRepository.findOne(id);
		if (existing != null) {
			mapsRepository.delete(id);
		}

		imageFetcher.setImgSrc(map.getImage(), mapsImgUri);
		return mapsRepository.save(map);
	}

}
