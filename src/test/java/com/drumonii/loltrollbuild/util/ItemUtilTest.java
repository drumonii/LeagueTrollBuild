package com.drumonii.loltrollbuild.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class ItemUtilTest {

	@Test
	public void determinesIfRequiresAllyOrnn() {
		String colloq = "ie;Ornn;forge;Masterwork";

		assertThat(ItemUtil.requiresAllyOrnn(colloq)).isTrue();

		colloq = "colloq;triforce;tons of damage";
		assertThat(ItemUtil.requiresAllyOrnn(colloq)).isFalse();
	}

}
