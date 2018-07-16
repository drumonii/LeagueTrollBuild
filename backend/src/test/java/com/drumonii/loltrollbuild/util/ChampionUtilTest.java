package com.drumonii.loltrollbuild.util;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.builder.ChampionBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class ChampionUtilTest {

	@Test
	public void determinesIfChampionIsViktor() {
		Champion viktor = new ChampionBuilder()
				.build();
		assertThat(ChampionUtil.isViktor(viktor)).isFalse();

		viktor.setName("Viktor");
		assertThat(ChampionUtil.isViktor(viktor)).isTrue();

		viktor.setName("viktor");
		assertThat(ChampionUtil.isViktor(viktor)).isTrue();

		Champion rammus = new ChampionBuilder()
				.withName("Rammus")
				.build();
		assertThat(ChampionUtil.isViktor(rammus)).isFalse();
	}

}