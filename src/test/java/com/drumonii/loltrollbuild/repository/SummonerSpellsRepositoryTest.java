package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.SummonerSpell.GameMode;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.SortedSet;
import java.util.function.Consumer;

import static com.drumonii.loltrollbuild.model.SummonerSpell.GameMode.CLASSIC;
import static org.assertj.core.api.Assertions.assertThat;

public class SummonerSpellsRepositoryTest extends BaseSpringTestRunner {

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
		assertThat(forTrollBuild).extracting(SummonerSpell::getModes).allSatisfy(
				(Consumer<SortedSet<GameMode>>) gameModes -> assertThat(gameModes).contains(CLASSIC));
		assertThat(forTrollBuild).containsOnly(smite, exhaust);
	}

}
