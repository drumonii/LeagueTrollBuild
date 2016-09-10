package com.drumonii.loltrollbuild.batch.champions;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.riot.api.ImageFetcher;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * {@link ItemProcessor} for processing {@link Champion}s from Riot's API.
 */
public class ChampionsRetrievalItemProcessor implements ItemProcessor<Champion, Champion> {

	@Autowired
	@Qualifier("championsImg")
	private UriComponentsBuilder championsImgUri;

	@Autowired
	private ChampionsRepository championsRepository;

	@Autowired
	private ImageFetcher imageFetcher;

	@Override
	public Champion process(Champion champion) throws Exception {
		Champion championFromDb = championsRepository.findOne(champion.getId());
		if (championFromDb != null && championFromDb.equals(champion)) {
			return null;
		}
		imageFetcher.setImgSrc(champion.getImage(), championsImgUri);
		return champion;
	}

}
