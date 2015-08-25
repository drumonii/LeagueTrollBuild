package com.drumonii.loltrollbuild.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utility methods relating to {@link Map}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapUtil {

	/**
	 * Obtains a {@link List} of element {@code <E>} from a given {@link Map} with values of element {@code <E>}.
	 *
	 * @param elementsMap the {@link Map} to extract the element values
	 * @param <E> the element values type
	 * @return a {@link List} of element {@code <E>} from a given {@link Map}'s values
	 */
	public static <E> List<E> getElementsFromMap(Map<String, E> elementsMap) {
		List<E> items = new ArrayList<>();
		elementsMap.forEach((k, v) -> items.add(v));
		return items;
	}

}
