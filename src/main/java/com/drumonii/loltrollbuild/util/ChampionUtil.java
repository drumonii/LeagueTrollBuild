package com.drumonii.loltrollbuild.util;

import com.drumonii.loltrollbuild.model.Champion;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility methods relating to {@link Champion}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChampionUtil {

	/**
	 * Determines if the Champion is Viktor.
	 *
	 * @param champion the champion to inspect
	 * @return {@code true} if Viktor, otherwise {@code false}
	 */
	public static boolean isViktor(Champion champion) {
		return champion.getName() != null && "Viktor" .equalsIgnoreCase(champion.getName());
	}

}
