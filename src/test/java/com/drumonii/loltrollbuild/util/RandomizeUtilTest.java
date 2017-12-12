package com.drumonii.loltrollbuild.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static com.drumonii.loltrollbuild.util.RandomizeUtil.getRandom;
import static com.drumonii.loltrollbuild.util.RandomizeUtil.getRandoms;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class RandomizeUtilTest {

	@Test
	public void getsRandom() {
		List<String> strings = new ArrayList<>();
		strings.add("string 1");
		strings.add("string 2");
		assertThat(getRandom(strings))
				.isNotNull()
				.isNotEmpty();
		assertThat(getRandom(new ArrayList<Integer>())).isNull();
	}

	@Test
	public void getsRandoms() {
		List<String> strings = new ArrayList<>();
		strings.add("string 1");
		strings.add("string 2");
		assertThat(getRandoms(strings, 1))
				.isNotNull()
				.hasSize(1);
		assertThat(getRandoms(strings, strings.size() + 1))
				.isNotNull()
				.hasSize(2);
	}

}