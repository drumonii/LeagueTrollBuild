package com.drumonii.loltrollbuild.batch.champions;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.ChampionSpell;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.riot.ImageFetcher;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * {@link ItemProcessor} for processing {@link Champion}s from Riot's API.
 */
public class ChampionsRetrievalItemProcessor implements ItemProcessor<Champion, Champion> {

	@Autowired
	@Qualifier("championsImg")
	private UriComponentsBuilder championsImgUri;

	@Autowired
	@Qualifier("championsSpellImg")
	private UriComponentsBuilder championsSpellImgUri;

	@Autowired
	@Qualifier("championsPassiveImgUri")
	private UriComponentsBuilder championsPassiveImgUri;

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	private ImageFetcher imageFetcher;

	private final Version latestRiotPatch;

	public ChampionsRetrievalItemProcessor(Version latestRiotPatch) {
		this.latestRiotPatch = latestRiotPatch;
	}

	@Override
	public Champion process(Champion champion) {
		Optional<Champion> championFromDb = championsRepository.findById(champion.getId());
		if (championFromDb.isPresent() && championFromDb.get().equals(champion)) {
			return null;
		}
		imageFetcher.setImgSrc(champion.getImage(), championsImgUri, latestRiotPatch);
		imageFetcher.setImgsSrcs(champion.getSpells().stream().map(ChampionSpell::getImage).collect(Collectors.toList()),
				championsSpellImgUri, latestRiotPatch);
		imageFetcher.setImgSrc(champion.getPassive().getImage(), championsPassiveImgUri, latestRiotPatch);
		return champion;
	}

}
