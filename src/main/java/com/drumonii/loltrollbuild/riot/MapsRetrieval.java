package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.riot.api.ImageFetcher;
import com.drumonii.loltrollbuild.riot.service.MapsService;
import com.drumonii.loltrollbuild.riot.service.VersionsService;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A {@link RestController} which retrieves the list of {@link GameMap} from Riot's {@code lol-static-data-v3} or
 * {@code Data Dragon} API with the {@code /riot/maps} URL mapping.
 */
@RestController
@RequestMapping("/riot/maps")
public class MapsRetrieval {

	@Autowired
	private MapsService mapsService;

	@Autowired
	@Qualifier("mapsImg")
	private UriComponentsBuilder mapsImgUri;

	@Autowired
	private MapsRepository mapsRepository;

	@Autowired
	private ImageFetcher imageFetcher;

	@Autowired
	private VersionsService versionsService;

	/**
	 * Returns the {@link List} of {@link GameMap} from Riot for the most current patch.
	 *
	 * @return the {@link List} of {@link GameMap} from Riot
	 */
	@GetMapping
	public Collection<GameMap> maps() {
		return mapsService.getMaps();
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
	@PostMapping
	public List<GameMap> saveMaps(@RequestParam(required = false) boolean truncate) {
		List<GameMap> maps = mapsService.getMaps();

		if (truncate) {
			mapsRepository.deleteAll();
		} else {
			List<GameMap> mapsFromDb = mapsRepository.findAll();
			List<GameMap> deletedMaps = ListUtils.subtract(mapsFromDb, maps);
			mapsRepository.deleteAll(deletedMaps);
			maps = ListUtils.subtract(maps, mapsFromDb);
		}

		Version latestVersion = versionsService.getLatestVersion();

		imageFetcher.setImgsSrcs(maps.stream().map(GameMap::getImage).collect(Collectors.toList()), mapsImgUri,
				latestVersion);
		return mapsRepository.saveAll(maps);
	}

	/**
	 * Returns a {@link GameMap} from Riot by its ID for the most current patch.
	 *
	 * @param id the ID to lookup the {@link GameMap} from Riot
	 * @return the {@link GameMap} from Riot
	 */
	@GetMapping(path = "/{id}")
	public GameMap map(@PathVariable int id) {
		GameMap map = mapsService.getMap(id);
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
	@PostMapping(value = "/{id}")
	public GameMap saveMap(@PathVariable int id) {
		GameMap map = mapsService.getMap(id);
		if (map == null) {
			throw new ResourceNotFoundException("Could not find Map with ID: " + id);
		}

		Version latestVersion = versionsService.getLatestVersion();

		imageFetcher.setImgSrc(map.getImage(), mapsImgUri, latestVersion);
		return mapsRepository.save(map);
	}

}
