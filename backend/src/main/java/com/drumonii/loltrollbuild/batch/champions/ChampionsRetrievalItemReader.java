package com.drumonii.loltrollbuild.batch.champions;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
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
	private ChampionsRepository championsRepository;

	private List<Champion> champions;

	public ChampionsRetrievalItemReader(List<Champion> champions) {
		this.champions = champions;
	}

	@Override
	public Champion read() {
		if (!champions.isEmpty()) {
			return champions.remove(0);
		}
		return null;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		if (!champions.isEmpty()) {
			List<Champion> deletedChampions = ListUtils.subtract(championsRepository.findAll(), champions);
			championsRepository.deleteAll(deletedChampions);
		}
	}

}
