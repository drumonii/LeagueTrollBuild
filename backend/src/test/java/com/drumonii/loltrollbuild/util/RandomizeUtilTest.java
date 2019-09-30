package com.drumonii.loltrollbuild.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.drumonii.loltrollbuild.util.RandomizeUtil.*;
import static org.assertj.core.api.Assertions.*;

@RunWith(JUnit4.class)
public class RandomizeUtilTest {

	@Test
	public void getsRandomFromList() {
		List<String> strings = List.of("string 1", "string 2");
		assertThat(getRandom(strings)).isIn(strings);
	}

	@Test
	public void getsRandomFromSet() {
		Set<Integer> integers = Set.of(1, 2);
		assertThat(getRandom(integers)).isIn(integers);
	}

	@Test
	public void getsRandomFromEmptyCollection() {
		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() -> getRandom(new ArrayList<>()))
				.withMessage("Unable to get a random element from an empty Collection");

		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() -> getRandom(new HashSet<>()))
				.withMessage("Unable to get a random element from an empty Collection");
	}

	@Test
	public void getsRandomFromNull() {
		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() -> getRandom(null))
				.withMessage("Unable to get a random element from an empty Collection");
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
		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() -> getRandoms(null, 1))
				.withMessage("Unable to get random elements from an null Collection");
	}

	@Test
	public void getsRandomLong() {
		assertThat(getRandomLong()).isNotZero();
	}

	@Test
	public void getsRandomInt() {
		assertThat(getRandomInt()).isNotZero();
	}

}
