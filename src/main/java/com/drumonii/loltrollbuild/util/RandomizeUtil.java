package com.drumonii.loltrollbuild.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.IterableUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Utility methods for randomizing.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RandomizeUtil {

	private static final Random random = new Random();

	/**
	 * Gets a random element {@code E} from the passed {@link Collection}.
	 *
	 * @param collection the {@link Collection} to get the random element
	 * @param <E> the element type
	 * @return a random element from the passed {@link Collection}
	 */
	public static <E> E getRandom(Collection<E> collection) {
		int start = 0;
		int end = collection.size();
		return IterableUtils.get(collection, start == end ? start : start + random.nextInt(end - start));
	}

	/**
	 * Gets a {@link List} of random elements {@code E} from the passed {@link Collection} to choose from.
	 *
	 * @param list the {@link Collection} to choose the random elements
	 * @param size the size of the {@link List} to generate
	 * @return a {@link List} of random elements from the passed {@link List}
	 */
	public static <E> List<E> getRandoms(Collection<E> list, int size) {
		List<E> randoms = new ArrayList<>();
		if (size > list.size()) {
			size = list.size();
		}
		while (randoms.size() < size) {
			E randomElement = getRandom(list);
			if (!randoms.contains(randomElement)) {
				randoms.add(randomElement);
			}
		}
		return randoms;
	}

}
