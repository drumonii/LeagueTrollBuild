package com.drumonii.loltrollbuild.batch.champions;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.riot.service.ChampionsService;
import org.apache.commons.collections4.ListUtils;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * {@link ItemReader} for reading {@link Champion}s from Riot's API.
 */
public class ChampionsRetrievalItemReader extends AbstractItemStreamItemReader<Champion> {

	@Autowired
	private ChampionsService championsService;

	@Autowired
	private ChampionsRepository championsRepository;

	private List<Champion> champions;
	private int nextChampion;

	@Override
	public Champion read() {
		if (nextChampion < champions.size()) {
			return champions.get(nextChampion++);
		}
		return null;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		champions = championsService.getChampions();
		List<Champion> deletedChampions = ListUtils.subtract(championsRepository.findAll(), champions);
		championsRepository.delete(deletedChampions);
	}

}
