package com.drumonii.loltrollbuild.util;

import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Repeat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.drumonii.loltrollbuild.util.RandomizeUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RandomizeUtilTest {

	private static final List<String> LIST = List.of("string 1", "string 2", "string 3", "string 4", "string 5");
	private static final Set<Integer> SET = Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

	@Repeat(100)
	@Test
	void getsRandomFromList() {
		assertThat(getRandom(LIST)).isIn(LIST);
	}

	@Repeat(100)
	@Test
	void getsRandomFromSet() {
		assertThat(getRandom(SET)).isIn(SET);
	}

	@Test
	void getsRandomFromEmptyCollection() {
		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() -> getRandom(new ArrayList<>()))
				.withMessage("Unable to get a random element from an empty Collection");

		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() -> getRandom(new HashSet<>()))
				.withMessage("Unable to get a random element from an empty Collection");
	}

	@Test
	void getsRandomFromNull() {
		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() -> getRandom(null))
				.withMessage("Unable to get a random element from an empty Collection");
	}

	@Repeat(100)
	@Test
	void getsRandomsFromList() {
		assertThat(getRandoms(LIST, 1)).containsAnyElementsOf(LIST)
				.hasSize(1);
		assertThat(getRandoms(LIST, LIST.size() + 1)).containsAnyElementsOf(LIST)
				.hasSize(LIST.size());
	}

	@Repeat(100)
	@Test
	void getsRandomsFromSet() {
		assertThat(getRandoms(SET, 1)).containsAnyElementsOf(SET)
				.hasSize(1);
		assertThat(getRandoms(SET, SET.size() + 1)).containsAnyElementsOf(SET)
				.hasSize(SET.size());
	}

	@Test
	void getsRandomsFromNull() {
		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() -> getRandoms(null, 1))
				.withMessage("Unable to get random elements from an null Collection");
	}

	@Repeat(100)
	@Test
	void getsRandomLong() {
		assertThat(getRandomLong()).isNotZero();
	}

	@Repeat(100)
	@Test
	void getsRandomInt() {
		assertThat(getRandomInt()).isNotZero();
	}

}
