package com.drumonii.loltrollbuild.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for randomizing.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RandomizeUtil {

	/**
	 * Gets a random element {@code E} from the passed {@link List}.
	 *
	 * @param list the {@link List} to get the random element
	 * @param <E> the element type
	 * @return a random element from the passed {@link List}
	 */
	public static <E> E getRandom(List<E> list) {
		return list.get(RandomUtils.nextInt(0, list.size()));
	}

	/**
	 * Gets a {@link List} of random elements {@code E} from the passed list to choose from.
	 *
	 * @param list the {@link List} to choose the random elements
	 * @param maxSize the maximum size of the {@link List} to generate
	 * @return a {@link List} of random elements from the passed {@link List}
	 */
	public static <E> List<E> getRandoms(List<E> list, int maxSize) {
		List<E> randoms = new ArrayList<>();
		while (randoms.size() < maxSize) {
			E randomElement = getRandom(list);
			if (!randoms.contains(randomElement)) {
				randoms.add(randomElement);
			}
		}
		return randoms;
	}

}
