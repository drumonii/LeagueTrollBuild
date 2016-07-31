package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import org.assertj.core.api.Condition;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static com.drumonii.loltrollbuild.model.SummonerSpell.GameMode.ARAM;
import static com.drumonii.loltrollbuild.model.SummonerSpell.GameMode.CLASSIC;
import static org.assertj.core.api.Assertions.assertThat;

public class SummonerSpellsRepositoryTest extends BaseSpringTestRunner {

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@After
	public void after() {
		summonerSpellsRepository.deleteAll();
	}

	@Test
	public void crudOperations() throws IOException {
		SummonerSpell clarity = summonerSpellsResponse.getSummonerSpells().get("SummonerMana");

		// Create
		assertThat(summonerSpellsRepository.save(clarity)).isNotNull();

		// Select
		SummonerSpell summonerSpellFromDb = summonerSpellsRepository.findOne(clarity.getId());
		assertThat(summonerSpellFromDb).isNotNull();
		assertThat(summonerSpellFromDb.getImage()).isNotNull();
		assertThat(summonerSpellFromDb).isEqualToIgnoringNullFields(clarity);

		// Update
		summonerSpellFromDb.setModes(new HashSet<>(Arrays.asList(CLASSIC)));
		summonerSpellsRepository.save(summonerSpellFromDb);
		summonerSpellFromDb = summonerSpellsRepository.findOne(clarity.getId());
		assertThat(summonerSpellFromDb.getModes()).isEqualTo(new HashSet<>(Arrays.asList(CLASSIC)));

		// Delete
		summonerSpellsRepository.delete(summonerSpellFromDb);
		assertThat(summonerSpellsRepository.findOne(clarity.getId())).isNull();
	}

	@Test
	public void forTrollBuild() throws IOException {
		SummonerSpell mark = summonerSpellsResponse.getSummonerSpells().get("SummonerSnowball");
		summonerSpellsRepository.save(mark);

		SummonerSpell smite = summonerSpellsResponse.getSummonerSpells().get("SummonerSmite");
		summonerSpellsRepository.save(smite);

		SummonerSpell exhaust = summonerSpellsResponse.getSummonerSpells().get("SummonerExhaust");
		summonerSpellsRepository.save(exhaust);

		List<SummonerSpell> forTrollBuild = summonerSpellsRepository.forTrollBuild(CLASSIC);
		assertThat(forTrollBuild).doesNotHaveDuplicates();
		assertThat(forTrollBuild).extracting(SummonerSpell::getModes)
				.have(new Condition<>(mode -> mode.contains(CLASSIC), "CLASSIC"));
		assertThat(forTrollBuild).containsOnly(smite, exhaust);

		forTrollBuild = summonerSpellsRepository.forTrollBuild(ARAM);
		assertThat(forTrollBuild).doesNotHaveDuplicates();
		assertThat(forTrollBuild).extracting(SummonerSpell::getModes)
				.have(new Condition<>(mode -> mode.contains(ARAM), "ARAM"));
		assertThat(forTrollBuild).containsOnly(mark, exhaust);
	}

}
