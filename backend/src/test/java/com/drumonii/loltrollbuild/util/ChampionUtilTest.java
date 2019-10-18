package com.drumonii.loltrollbuild.util;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.builder.ChampionBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChampionUtilTest {

	@Test
	void determinesIfChampionIsViktor() {
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
