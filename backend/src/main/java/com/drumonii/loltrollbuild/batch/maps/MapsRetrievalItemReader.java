package com.drumonii.loltrollbuild.batch.maps;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import org.apache.commons.collections4.ListUtils;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * {@link ItemReader} for reading {@link GameMap}s from Riot's API.
 */
public class MapsRetrievalItemReader extends AbstractItemStreamItemReader<GameMap> {

	@Autowired
	private MapsRepository mapsRepository;

	private List<GameMap> maps;

	public MapsRetrievalItemReader(List<GameMap> maps) {
		this.maps = maps;
	}

	@Override
	public GameMap read() {
		if (!maps.isEmpty()) {
			return maps.remove(0);
		}
		return null;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		if (!maps.isEmpty()) {
			List<GameMap> deletedMaps = ListUtils.subtract(mapsRepository.findAll(), maps);
			mapsRepository.deleteAll(deletedMaps);
		}
	}

}
