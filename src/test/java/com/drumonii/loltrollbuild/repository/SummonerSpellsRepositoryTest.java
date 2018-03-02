package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.config.JpaConfig;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.drumonii.loltrollbuild.model.SummonerSpell.GameMode.CLASSIC;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest(includeFilters = @Filter(JsonComponent.class))
@ImportAutoConfiguration(JacksonAutoConfiguration.class)
@Import(JpaConfig.class)
public abstract class SummonerSpellsRepositoryTest {

	@Autowired
	private SummonerSpellsRepository summonerSpellsRepository;

	@Autowired
	protected ObjectMapper objectMapper;

	private SummonerSpellsResponse summonerSpellsResponse;

	protected abstract SummonerSpellsResponse getSummonerSpellsResponse();

	@Before
	public void before() {
		summonerSpellsResponse = getSummonerSpellsResponse();
		summonerSpellsRepository.saveAll(summonerSpellsResponse.getSummonerSpells().values());
	}

	@Test
	public void forTrollBuild() {
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
