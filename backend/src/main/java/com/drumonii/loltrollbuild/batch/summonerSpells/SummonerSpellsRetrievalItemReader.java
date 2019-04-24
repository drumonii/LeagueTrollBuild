package com.drumonii.loltrollbuild.batch.summonerSpells;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.riot.service.SummonerSpellsService;
import org.apache.commons.collections4.ListUtils;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * {@link ItemReader} for reading {@link SummonerSpell}s from Riot's API.
 */
public class SummonerSpellsRetrievalItemReader extends AbstractItemStreamItemReader<SummonerSpell> {

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	private SummonerSpellsService summonerSpellsService;

	private final Version latestVersion;
	private List<SummonerSpell> summonerSpells;

	public SummonerSpellsRetrievalItemReader(Version latestVersion) {
		this.latestVersion = latestVersion;
	}

	@Override
	public SummonerSpell read() {
		if (summonerSpells == null) {
			getSummonerSpells();
		}
		if (!summonerSpells.isEmpty()) {
			return summonerSpells.remove(0);
		}
		return null;
	}

	private void getSummonerSpells() {
		summonerSpells = summonerSpellsService.getSummonerSpells(latestVersion);
		if (summonerSpells.isEmpty()) {
			summonerSpells = null;
			throw new SummonerSpellsRetrievalException();
		} else {
			List<SummonerSpell> deletedSummonerSpells = ListUtils.subtract(summonerSpellsRepository.findAll(),
					summonerSpells);
			summonerSpellsRepository.deleteAll(deletedSummonerSpells);
		}
	}

}
