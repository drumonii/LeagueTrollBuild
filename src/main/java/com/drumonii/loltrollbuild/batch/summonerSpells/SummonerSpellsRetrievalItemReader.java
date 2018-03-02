package com.drumonii.loltrollbuild.batch.summonerSpells;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.riot.service.SummonerSpellsService;
import org.apache.commons.collections4.ListUtils;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * {@link ItemReader} for reading {@link SummonerSpell}s from Riot's API.
 */
public class SummonerSpellsRetrievalItemReader extends AbstractItemStreamItemReader<SummonerSpell> {

	@Autowired
	private SummonerSpellsService summonerSpellsService;

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	private List<SummonerSpell> summonerSpells;
	private int nextSummonerSpell;

	@Override
	public SummonerSpell read() {
		if (nextSummonerSpell < summonerSpells.size()) {
			return summonerSpells.get(nextSummonerSpell++);
		}
		return null;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		summonerSpells = summonerSpellsService.getSummonerSpells();
		List<SummonerSpell> deletedSummonerSpells = ListUtils.subtract(summonerSpellsRepository.findAll(),
				summonerSpells);
		summonerSpellsRepository.deleteAll(deletedSummonerSpells);
	}

}
