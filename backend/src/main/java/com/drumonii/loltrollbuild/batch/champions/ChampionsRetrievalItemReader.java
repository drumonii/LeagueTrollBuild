package com.drumonii.loltrollbuild.batch.champions;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.riot.service.ChampionsService;
import org.apache.commons.collections4.ListUtils;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * {@link ItemReader} for reading {@link Champion}s from Riot's API.
 */
public class ChampionsRetrievalItemReader implements ItemReader<Champion> {

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	private ChampionsService championsService;

	private final Version latestVersion;
	private List<Champion> champions;

	public ChampionsRetrievalItemReader(Version latestVersion) {
		this.latestVersion = latestVersion;
	}

	@Override
	public Champion read() {
		if (champions == null) {
			getChampions();
		}
		if (!champions.isEmpty()) {
			return champions.remove(0);
		}
		return null;
	}

	private void getChampions() {
		champions = championsService.getChampions(latestVersion);
		if (champions.isEmpty()) {
			champions = null;
			throw new ChampionsRetrievalException();
		} else {
			List<Champion> deletedChampions = ListUtils.subtract(championsRepository.findAll(), champions);
			championsRepository.deleteAll(deletedChampions);
		}
	}

}
