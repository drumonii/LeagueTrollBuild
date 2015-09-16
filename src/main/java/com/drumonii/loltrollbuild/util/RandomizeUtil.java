package com.drumonii.loltrollbuild.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomUtils;

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
	 * @return a random element from the {@link List}
	 */
	public static <E> E getRandom(List<E> list) {
		return list.get(RandomUtils.nextInt(0, list.size()));
	}

	/**
	 * Gets a random element {@code E} from the passed {@link List} and removes the chosen element from the list.
	 *
	 * @param list the {@link List} to get and remove the random element
	 * @param <E> the element type
	 * @return a random element from the {@link List}
	 */
	public static <E> E getRandomAndRemove(List<E> list) {
		int randomInt = RandomUtils.nextInt(0, list.size());
		E element = list.get(randomInt);
		list.remove(randomInt);
		return element;
	}

}
