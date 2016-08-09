package com.drumonii.loltrollbuild.model;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.image.ChampionImage;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import org.apache.commons.collections4.ListUtils;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;

public class ChampionTest extends BaseSpringTestRunner {

	@Autowired
	private ChampionsRepository championsRepository;

	@After
	public void after() {
		championsRepository.deleteAll();
	}

	@Test
	public void isViktor() throws IOException {
		Champion viktor = championsResponse.getChampions().get("Viktor");
		assertThat(viktor.isViktor()).isTrue();

		Champion rammus = championsResponse.getChampions().get("Rammus");
		assertThat(rammus.isViktor()).isFalse();
	}

	@Test
	public void equals() throws IOException {
		Champion nasusFromRiot = championsResponse.getChampions().get("Nasus");
		championsRepository.save(nasusFromRiot);

		Champion nasusFromDb = championsRepository.findByName(nasusFromRiot.getName());
		assertThat(nasusFromRiot).isEqualTo(nasusFromDb);

		ChampionImage image = nasusFromRiot.getImage();
		image.setFull("NewNasus.png");
		nasusFromRiot.setImage(image);
		assertThat(nasusFromRiot).isNotEqualTo(nasusFromDb);
	}

	@Test
	public void cardinality() throws IOException {
		Champion gnarFromRiot = championsResponse.getChampions().get("Gnar");
		Champion gnarFromDb = championsRepository.save(gnarFromRiot);
		gnarFromRiot.setTags(new TreeSet<>(Arrays.asList("NEW_TAG")));

		Champion gravesFromRiot = championsResponse.getChampions().get("Graves");
		Champion gravesFromDb = championsRepository.save(gravesFromRiot);

		Champion evelynnFromRiot = championsResponse.getChampions().get("Evelynn");
		Champion evelynnFromDb = championsRepository.save(evelynnFromRiot);

		// Gnar, Illaoi, and Graves
		List<Champion> championsFromDb = Arrays.asList(gnarFromDb, gravesFromDb, evelynnFromDb);

		Champion illaoiFromRiot = championsResponse.getChampions().get("Illaoi");

		// Updated Gnar, same Graves, "new" Illaoi, and no Evelynn
		List<Champion> championsFromRiot = Arrays.asList(gnarFromRiot, gravesFromRiot, illaoiFromRiot);

		List<Champion> deletedChampions = ListUtils.subtract(championsFromDb, championsFromRiot);
		assertThat(deletedChampions).hasSize(2);
		assertThat(deletedChampions).containsOnly(gnarFromDb, evelynnFromDb);

		List<Champion> unmodifiedChampions = ListUtils.intersection(championsFromDb, championsFromRiot);
		assertThat(unmodifiedChampions).hasSize(1);
		assertThat(unmodifiedChampions).containsOnly(gravesFromDb);

		List<Champion> championsToUpdate = ListUtils.subtract(championsFromRiot, championsFromDb);
		assertThat(championsToUpdate).hasSize(2);
		assertThat(championsToUpdate).containsOnly(gnarFromRiot, illaoiFromRiot);
	}

}