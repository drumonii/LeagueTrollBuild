package com.drumonii.loltrollbuild.batch.champions;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.ChampionSpell;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.riot.api.ImageFetcher;
import org.hibernate.collection.internal.PersistentBag;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
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
	private ChampionsRepository championsRepository;

	@Autowired
	private ImageFetcher imageFetcher;

	@Override
	public Champion process(Champion champion) throws Exception {
		Champion championFromDb = championsRepository.findOne(champion.getId());
		if (championFromDb != null) {
			// PostLoad fails to trigger here
			if (championFromDb.getSpells() instanceof PersistentBag) {
				championFromDb.setSpells(new ArrayList<>(championFromDb.getSpells()));
				for (ChampionSpell spell : championFromDb.getSpells()) {
					spell.setCosts(new ArrayList<>(spell.getCosts()));
					spell.setCooldowns(new ArrayList<>(spell.getCooldowns()));
					spell.setRange(new ArrayList<>(spell.getRange()));
				}
			}
			if (championFromDb.equals(champion)) {
				return null;
			}
		}
		imageFetcher.setImgSrc(champion.getImage(), championsImgUri);
		imageFetcher.setImgsSrcs(champion.getSpells().stream().map(ChampionSpell::getImage).collect(Collectors.toList()),
				championsSpellImgUri);
		return champion;
	}

}
