package com.drumonii.loltrollbuild.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.drumonii.loltrollbuild.util.RandomizeUtil.getRandom;
import static com.drumonii.loltrollbuild.util.RandomizeUtil.getRandoms;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class RandomizeUtilTest {

	@Test
	public void getsRandomFromList() {
		List<String> strings = List.of("string 1", "string 2");
		assertThat(getRandom(strings)).isIn(strings);

		try {
			getRandom(new ArrayList<>());
		} catch (Exception e) {
			assertThat(e).isInstanceOf(IllegalArgumentException.class);
		}
	}

	@Test
	public void getsRandomFromSet() {
		Set<Integer> integers = Set.of(1, 2);
		assertThat(getRandom(integers)).isIn(integers);

		try {
			getRandom(new HashSet<>());
		} catch (Exception e) {
			assertThat(e).isInstanceOf(IllegalArgumentException.class);
		}
	}

	@Test
	public void getsRandomFromNull() {
		try {
			getRandom(null);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(IllegalArgumentException.class);
		}
	}

	@Test
	public void getsRandomsFromList() {
		List<String> strings = List.of("string 1", "string 2");
		assertThat(getRandoms(strings, 1)).containsAnyElementsOf(strings)
				.hasSize(1);
		assertThat(getRandoms(strings, strings.size() + 1)).containsAnyElementsOf(strings)
				.hasSize(2);
	}

	@Test
	public void getsRandomsFromSet() {
		Set<Integer> integers = Set.of(1, 2);
		assertThat(getRandoms(integers, 1)).containsAnyElementsOf(integers)
				.hasSize(1);
		assertThat(getRandoms(integers, integers.size() + 1)).containsAnyElementsOf(integers)
				.hasSize(2);
	}

	@Test
	public void getsRandomsFromNull() {
		try {
			getRandoms(null, 1);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(IllegalArgumentException.class);
		}
	}

}