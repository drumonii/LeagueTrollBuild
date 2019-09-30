package com.drumonii.loltrollbuild.util;

import org.apache.commons.collections4.IterableUtils;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Utility methods for randomizing.
 */
public class RandomizeUtil {

	private RandomizeUtil() {}

	private static final SecureRandom RANDOM = new SecureRandom();

	/**
	 * Gets a random element {@code E} from the passed {@link Collection}.
	 *
	 * @param collection the {@link Collection} to get the random element
	 * @param <E> the element type
	 * @return a random element from the passed {@link Collection}
	 */
	public static <E> E getRandom(Collection<E> collection) {
		if (collection == null || collection.isEmpty()) {
			throw new IllegalArgumentException("Unable to get a random element from an empty Collection");
		}
		int start = 0;
		int end = collection.size();
		return IterableUtils.get(collection, start + RANDOM.nextInt(end - start));
	}

	/**
	 * Gets a {@link Collection} of random elements {@code E} from the passed {@link Collection} to choose from.
	 *
	 * @param collection the {@link Collection} to choose the random elements
	 * @param size the size of the {@link Collection} to generate
	 * @return a {@link Collection} of random elements from the passed {@link Collection}
	 */
	public static <E> Collection<E> getRandoms(Collection<E> collection, int size) {
		Set<E> randoms = new LinkedHashSet<>();
		if (collection == null) {
			throw new IllegalArgumentException("Unable to get random elements from an null Collection");
		}
		if (size > collection.size()) {
			size = collection.size();
		}
		while (randoms.size() < size) {
			E randomElement = getRandom(collection);
			randoms.add(randomElement);
		}
		return randoms;
	}

	/**
	 * Gets a random integer as a long.
	 *
	 * @return the random integer as a long
	 */
	public static long getRandomLong() {
		return getRandomInt();
	}

	/**
	 * Gets a random integer.
	 *
	 * @return the random integer
	 */
	public static int getRandomInt() {
		return RANDOM.nextInt();
	}

}
