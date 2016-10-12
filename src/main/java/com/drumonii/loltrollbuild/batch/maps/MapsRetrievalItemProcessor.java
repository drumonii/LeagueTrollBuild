package com.drumonii.loltrollbuild.batch.maps;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.riot.api.ImageFetcher;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * {@link ItemProcessor} for processing {@link GameMap}s from Riot's API.
 */
public class MapsRetrievalItemProcessor implements ItemProcessor<GameMap, GameMap> {

	@Autowired
	@Qualifier("mapsImg")
	private UriComponentsBuilder mapsImgUri;

	@Autowired
	private MapsRepository mapsRepository;

	@Autowired
	private ImageFetcher imageFetcher;

	private Version latestVersion;

	public MapsRetrievalItemProcessor(Version latestVersion) {
		this.latestVersion = latestVersion;
	}

	@Override
	public GameMap process(GameMap map) throws Exception {
		GameMap mapFromDb = mapsRepository.findOne(map.getMapId());
		if (mapFromDb != null && mapFromDb.equals(map)) {
			return null;
		}
		imageFetcher.setImgSrc(map.getImage(), mapsImgUri, latestVersion);
		return map;
	}

}
