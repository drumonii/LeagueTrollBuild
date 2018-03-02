package com.drumonii.loltrollbuild.batch.summonerSpells;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.riot.api.ImageFetcher;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

/**
 * {@link ItemProcessor} for processing {@link SummonerSpell}s from Riot's API.
 */
public class SummonerSpellsRetrievalItemProcessor implements ItemProcessor<SummonerSpell, SummonerSpell> {

	@Autowired
	@Qualifier("summonerSpellsImg")
	private UriComponentsBuilder summonerSpellsImgUri;

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	private ImageFetcher imageFetcher;

	private final Version latestVersion;

	public SummonerSpellsRetrievalItemProcessor(Version latestVersion) {
		this.latestVersion = latestVersion;
	}

	@Override
	public SummonerSpell process(SummonerSpell summonerSpell) {
		Optional<SummonerSpell> summonerSpellFromDb = summonerSpellsRepository.findById(summonerSpell.getId());
		if (summonerSpellFromDb.isPresent() && summonerSpellFromDb.get().equals(summonerSpell)) {
			return null;
		}
		imageFetcher.setImgSrc(summonerSpell.getImage(), summonerSpellsImgUri, latestVersion);
		return summonerSpell;
	}

}
