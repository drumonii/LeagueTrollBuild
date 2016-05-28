package com.drumonii.loltrollbuild.util;

import com.drumonii.loltrollbuild.BaseUnitTestRunner;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.drumonii.loltrollbuild.util.RandomizeUtil.getRandom;
import static com.drumonii.loltrollbuild.util.RandomizeUtil.getRandoms;
import static org.assertj.core.api.Assertions.assertThat;

public class RandomizeUtilTest extends BaseUnitTestRunner {

	@Test
	public void getsRandom() throws Exception {
		List<String> strings = new ArrayList<>();
		strings.add("string 1");
		strings.add("string 2");
		assertThat(getRandom(strings))
				.isNotNull()
				.isNotEmpty();
	}

	@Test
	public void getsRandoms() throws Exception {
		List<String> strings = new ArrayList<>();
		strings.add("string 1");
		strings.add("string 2");
		assertThat(getRandoms(strings, 1))
				.isNotNull()
				.hasSize(1);
	}

}