package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import org.assertj.core.api.Condition;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

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
	}

}
