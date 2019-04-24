package com.drumonii.loltrollbuild.batch.maps;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.riot.service.MapsService;
import org.apache.commons.collections4.ListUtils;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * {@link ItemReader} for reading {@link GameMap}s from Riot's API.
 */
public class MapsRetrievalItemReader extends AbstractItemStreamItemReader<GameMap> {

	@Autowired
	private MapsRepository mapsRepository;

	@Autowired
	private MapsService mapsService;

	private final Version latestVersion;
	private List<GameMap> maps;

	public MapsRetrievalItemReader(Version latestVersion) {
		this.latestVersion = latestVersion;
	}

	@Override
	public GameMap read() {
		if (maps == null) {
			getMaps();
		}
		if (!maps.isEmpty()) {
			return maps.remove(0);
		}
		return null;
	}

	private void getMaps() {
		maps = mapsService.getMaps(latestVersion);
		if (maps.isEmpty()) {
			maps = null;
			throw new MapsItemRetrievalException();
		} else {
			List<GameMap> deletedMaps = ListUtils.subtract(mapsRepository.findAll(), maps);
			mapsRepository.deleteAll(deletedMaps);
		}
	}

}
