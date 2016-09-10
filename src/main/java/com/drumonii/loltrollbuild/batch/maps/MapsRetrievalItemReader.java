package com.drumonii.loltrollbuild.batch.maps;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import org.apache.commons.collections4.ListUtils;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ItemReader} for reading {@link GameMap}s from Riot's API.
 */
public class MapsRetrievalItemReader implements ItemReader<GameMap> {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("maps")
	private UriComponents mapsUri;

	@Autowired
	private MapsRepository mapsRepository;

	private List<GameMap> maps;
	private int nextMap;

	@Override
	public GameMap read() throws Exception {
		if (isNotInitialized()) {
			maps = new ArrayList<>(restTemplate.getForObject(mapsUri.toString(), MapsResponse.class).getMaps()
					.values());
			List<GameMap> deletedMaps = ListUtils.subtract(mapsRepository.findAll(), maps);
			mapsRepository.delete(deletedMaps);
		}

		GameMap map = null;
		if (nextMap < maps.size()) {
			map = maps.get(nextMap);
			nextMap++;
		}

		return map;
	}

	private boolean isNotInitialized() {
		return maps == null;
	}

}
