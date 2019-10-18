package com.drumonii.loltrollbuild.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ItemUtilTest {

	@Test
	void determinesIfRequiresAllyOrnn() {
		assertThat(ItemUtil.requiresAllyOrnn(null)).isFalse();

		String colloq = "ie;Ornn;forge;Masterwork";

		assertThat(ItemUtil.requiresAllyOrnn(colloq)).isTrue();

		colloq = "colloq;triforce;tons of damage";
		assertThat(ItemUtil.requiresAllyOrnn(colloq)).isFalse();
	}

}
