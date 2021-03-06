package com.drumonii.loltrollbuild.batch.maps;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.riot.ImageFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

/**
 * {@link ItemProcessor} for processing {@link GameMap}s from Riot's API.
 */
public class MapsRetrievalItemProcessor implements ItemProcessor<GameMap, GameMap> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MapsRetrievalItemProcessor.class);

	@Autowired
	@Qualifier("mapsImg")
	private UriComponentsBuilder mapsImgUri;

	@Autowired
	private MapsRepository mapsRepository;

	@Autowired
	private ImageFetcher imageFetcher;

	private final Version latestRiotPatch;

	public MapsRetrievalItemProcessor(Version latestVersion) {
		this.latestRiotPatch = latestVersion;
	}

	@Override
	public GameMap process(GameMap map) {
		LOGGER.info("Processing Map: {}", map.getMapName());
		Optional<GameMap> mapFromDb = mapsRepository.findById(map.getMapId());
		if (mapFromDb.isPresent() && mapFromDb.get().equals(map)) {
			return null;
		}
		imageFetcher.setImgSrc(map.getImage(), mapsImgUri, latestRiotPatch);
		return map;
	}

}
