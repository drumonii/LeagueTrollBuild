package com.drumonii.loltrollbuild.batch.champions;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import org.apache.commons.collections4.ListUtils;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ItemReader} for reading {@link Champion}s from Riot's API.
 */
public class ChampionsRetrievalItemReader extends AbstractItemStreamItemReader<Champion> {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("champions")
	private UriComponents championsUri;

	@Autowired
	private ChampionsRepository championsRepository;

	private List<Champion> champions;
	private int nextChampion;

	@Override
	public Champion read() throws Exception {
		if (nextChampion < champions.size()) {
			return champions.get(nextChampion++);
		}
		return null;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		champions = new ArrayList<>(restTemplate.getForObject(championsUri.toString(), ChampionsResponse.class)
				.getChampions().values());
		List<Champion> deletedChampions = ListUtils.subtract(championsRepository.findAll(), champions);
		championsRepository.delete(deletedChampions);
	}

}
