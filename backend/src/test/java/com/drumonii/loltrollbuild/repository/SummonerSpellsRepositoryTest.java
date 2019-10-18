package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.drumonii.loltrollbuild.test.repository.RepositoryTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.drumonii.loltrollbuild.model.SummonerSpell.GameMode.CLASSIC;
import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
abstract class SummonerSpellsRepositoryTest {

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	private SummonerSpellsResponse summonerSpellsResponse;

	protected abstract SummonerSpellsResponse getSummonerSpellsResponse();

	@BeforeEach
	void beforeEach() {
		summonerSpellsResponse = getSummonerSpellsResponse();
		summonerSpellsRepository.saveAll(summonerSpellsResponse.getSummonerSpells().values());
	}

	@Test
	void forTrollBuild() {
		SummonerSpell cleanse = summonerSpellsResponse.getSummonerSpells().get("SummonerBoost");
		SummonerSpell exhaust = summonerSpellsResponse.getSummonerSpells().get("SummonerExhaust");
		SummonerSpell flash = summonerSpellsResponse.getSummonerSpells().get("SummonerFlash");
		SummonerSpell ghost = summonerSpellsResponse.getSummonerSpells().get("SummonerHaste");
		SummonerSpell heal = summonerSpellsResponse.getSummonerSpells().get("SummonerHeal");
		SummonerSpell smite = summonerSpellsResponse.getSummonerSpells().get("SummonerSmite");
		SummonerSpell teleport = summonerSpellsResponse.getSummonerSpells().get("SummonerTeleport");
		SummonerSpell ignite = summonerSpellsResponse.getSummonerSpells().get("SummonerDot");
		SummonerSpell barrier = summonerSpellsResponse.getSummonerSpells().get("SummonerBarrier");

		List<SummonerSpell> forTrollBuild = summonerSpellsRepository.forTrollBuild(CLASSIC);
		assertThat(forTrollBuild).doesNotHaveDuplicates();
		assertThat(forTrollBuild).extracting(SummonerSpell::getModes)
				.allSatisfy(gameModes -> assertThat(gameModes).contains(CLASSIC));
		assertThat(forTrollBuild)
				.containsOnly(cleanse, exhaust, flash, ghost, heal, smite, teleport, ignite, barrier);
	}

}
